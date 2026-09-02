package com.barq.nadra.service.nadra;

import com.barq.nadra.controller.HelperClass;
import com.barq.nadra.dto.*;
import com.barq.nadra.model.TblNidData;
import com.barq.nadra.model.TblNidHits;
import com.barq.nadra.model.TblNidMock;
import com.barq.nadra.repo.LkpCityRepo;
import com.barq.nadra.repo.TblGlobalConfigRepo;
import com.barq.nadra.repo.TblNidDataRepo;
import com.barq.nadra.repo.TblNidHitsRepo;
import com.barq.nadra.repo.TblNidMockRepo;
import com.barq.nadra.utils.Constants;
import com.barq.nadra.utils.Utills;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@SuppressWarnings("java:S1192")
public class NadraApisImpl extends HelperClass implements NadraApis {
    @Value("${nadra-finger-verification-url}")
    private String nadraFingerVerificationUrl;
    @Value("${nadra-api-url}")
    private String nadraApiurl;
    @Value("${nadra-bvs-header-name}")
    private String nadraBvsHeaderName;
    @Value("${nadra-bvs-header-value}")
    private String nadraBvsHeaderValue;
    @Autowired
    private TblNidDataRepo tblNidDataRepo;
    @Autowired
    private TblNidHitsRepo tblNidHitsRepo;
    @Autowired
    private TblNidMockRepo tblNidMockRepo;
    @Value("${nadraBvs.mock}")
    private String nadraBvsMock;
    @Value("${nadraBio.mock}")
    private String nadraBioMock;
    @Autowired
    private TblGlobalConfigRepo tblGlobalConfigRepo;
    @Autowired
    private LkpCityRepo lkpCityRepo;
    @Value("${nadra.bvs.exemption.mock}")
    private String nadraBvsExemptionMock;

    @Override
    public NadraVerificationResponse checkNadraExistance(NadraVerificationRequest nadraVerificationRequest) {

        if (isNullOrEmpty(nadraVerificationRequest.getCnicIssuanceDate())) {
            // Callers that verify a CNIC without an issuance date (e.g. mobileRegistration) cannot
            // use this issuance-date-qualified lookup; they fall through to a full verification.
            return null;
        }
        String issuenceDate = new SimpleDateFormat("dd-MM-yyyy").format(nadraVerificationRequest.getCnicIssuanceDate());
        TblNidData tblNidData = tblNidDataRepo.findByNidNoAndIssuanceDate(nadraVerificationRequest.getCnic(), issuenceDate);
        NadraVerificationResponse nadraVerificationResponse = new NadraVerificationResponse();
        if (tblNidData != null) {
            nadraVerificationResponse.setExpiryDate(tblNidData.getExpiryDate());
            nadraVerificationResponse.setMotherName(tblNidData.getMotherName());
            nadraVerificationResponse.setBirthPlace(tblNidData.getBirthPlace());
            nadraVerificationResponse.setGender(tblNidData.getGender());
            nadraVerificationResponse.setDateOfBirth(tblNidData.getDateOfBirth());
            nadraVerificationResponse.setPresentAddress(tblNidData.getPresentAddress());
            nadraVerificationResponse.setCitizenNumber(String.valueOf(tblNidData.getNidNo()));
            nadraVerificationResponse.setMessage(tblNidData.getStatusMessage());
            nadraVerificationResponse.setCode(tblNidData.getStatusCode());
            nadraVerificationResponse.setName(tblNidData.getName());
            nadraVerificationResponse.setSessionId(tblNidData.getSessionId());
            nadraVerificationResponse.setMobileNo(nadraVerificationRequest.getMobileNumber());

        } else {
            nadraVerificationResponse = null;
        }
        return nadraVerificationResponse;
    }

    @Override
    public NadraVerificationResponse checkNadraRecord(NadraVerificationRequest nadraVerificationRequest, BigDecimal userId, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        NadraVerificationResponse nadraVerificationResponse = new NadraVerificationResponse();
        TblNidData tblNidData = tblNidDataRepo.findByNidNo(nadraVerificationRequest.getCnic());
        if (tblNidData == null) {
            tblNidData = new TblNidData();

        }
        JSONObject jsonObject = null;
        String value = tblGlobalConfigRepo.findByKeyName(Constants.NADRA_MOCK);
        nadraBioMock = value != null ? value : nadraBioMock;

        boolean servedFromMock = false;
        if (nadraBioMock.equalsIgnoreCase(Constants.NOT_VERIFIED)) {
            jsonObject = nadraVarification(nadraVerificationRequest, userId);
        }
        if (jsonObject != null) {

            settingNadraResponseToTblNadra(userId, tblNidData, jsonObject);

        } else {

            // Mock mode: the citizen record is read from TBL_NID_MOCK and from nowhere else.
            // No row for this CNIC means the verification failed.
            TblNidMock tblNidMock = tblNidMockRepo.findByNidNo(nadraVerificationRequest.getCnic());
            if (tblNidMock == null) {
                recordNidHit(nadraVerificationRequest.getCnic(), userId, Constants.BIO_SERVICE_NAME,
                        null, Constants.RESPONSE_CODE_FAILURE, Constants.NID_VERIFICATION_FAILED);
                nadraVerificationResponse.setCode(Constants.RESPONSE_CODE_FAILURE);
                nadraVerificationResponse.setMessage(Constants.NID_VERIFICATION_FAILED);
                nadraVerificationResponse.setCitizenNumber(nadraVerificationRequest.getCnic());
                nadraVerificationResponse.setMobileNo(nadraVerificationRequest.getMobileNumber());
                return nadraVerificationResponse;
            }
            applyMockRecord(tblNidData, tblNidMock, nadraVerificationRequest.getCnic());
            servedFromMock = true;
        }

        handleNadraResponse(nadraVerificationRequest, userId, httpServletRequest, nadraVerificationResponse, tblNidData);
        if (servedFromMock) {
            recordNidHit(nadraVerificationRequest.getCnic(), userId, Constants.BIO_SERVICE_NAME,
                    convertObjecttoJson(nadraVerificationResponse),
                    nadraVerificationResponse.getCode(), nadraVerificationResponse.getMessage());
        }
        return nadraVerificationResponse;
    }


    private void handleNadraResponse(NadraVerificationRequest nadraVerificationRequest, BigDecimal userId, HttpServletRequest httpServletRequest, NadraVerificationResponse nadraVerificationResponse, TblNidData tblNidData) throws JsonProcessingException {
        if (Constants.NADRA_SUCESS_CODE.equals(tblNidData.getStatusCode())) {

            tblNidData.setIssuanceDate(!isNullOrEmpty(nadraVerificationRequest.getCnicIssuanceDate()) ? nadraVerificationRequest.getCnicIssuanceDate() : tblNidData.getIssuanceDate());
            tblNidData.setCreateuser(userId);
            // The live NADRA payload carries only the *_EN variants, so fall back to those when the
            // source record has no localised value. A record that already carries both (every
            // TBL_NID_MOCK row does) is left untouched.
            tblNidData.setName(!isNullOrEmpty(tblNidData.getName()) ? tblNidData.getName() : tblNidData.getNameEn());
            tblNidData.setMotherName(!isNullOrEmpty(tblNidData.getMotherName()) ? tblNidData.getMotherName() : tblNidData.getMotherNameEn());
            tblNidData.setPresentAddress(!isNullOrEmpty(tblNidData.getPresentAddress()) ? tblNidData.getPresentAddress() : tblNidData.getPresentAddressEn());
            tblNidData.setBirthPlaceEn(!isNullOrEmpty(tblNidData.getBirthPlaceEn()) ? tblNidData.getBirthPlaceEn() : tblNidData.getBirthPlace());
            tblNidData.setCreatedate(new Date());
            tblNidData = tblNidDataRepo.saveAndFlush(tblNidData);
            nadraVerificationResponse.setSessionId(tblNidData.getSessionId());
            nadraVerificationResponse.setExpiryDate(tblNidData.getExpiryDate());
            nadraVerificationResponse.setMotherName(tblNidData.getMotherName());
            nadraVerificationResponse.setBirthPlace(tblNidData.getBirthPlace());
            nadraVerificationResponse.setGender(tblNidData.getGender());
            nadraVerificationResponse.setDateOfBirth(tblNidData.getDateOfBirth());
            nadraVerificationResponse.setPresentAddress(tblNidData.getPresentAddress());
            nadraVerificationResponse.setCitizenNumber(String.valueOf(tblNidData.getNidNo()));
            nadraVerificationResponse.setMessage(tblNidData.getStatusMessage());
            nadraVerificationResponse.setCode(tblNidData.getStatusCode());
            nadraVerificationResponse.setName(tblNidData.getName());
            nadraVerificationResponse.setMobileNo(nadraVerificationRequest.getMobileNumber());


        } else {
            nadraVerificationResponse.setCode(tblNidData.getStatusCode());
            nadraVerificationResponse.setMessage(tblNidData.getStatusMessage());
        }
    }

    /**
     * Copies a TBL_NID_MOCK row onto the TBL_NID_DATA record that is about to be persisted and
     * returned. Every citizen attribute is taken verbatim from the mock row; nothing is generated.
     * Only STATUS_CODE / STATUS_MESSAGE fall back to "verified", because the presence of the row
     * is itself the verification result.
     */
    private void applyMockRecord(TblNidData tblNidData, TblNidMock tblNidMock, String cnic) {
        tblNidData.setNidNo(Long.parseLong(cnic));
        tblNidData.setName(tblNidMock.getName());
        tblNidData.setNameEn(tblNidMock.getNameEn());
        tblNidData.setFatherHusbandNameEn(tblNidMock.getFatherHusbandNameEn());
        tblNidData.setMotherName(tblNidMock.getMotherName());
        tblNidData.setMotherNameEn(tblNidMock.getMotherNameEn());
        tblNidData.setPresentAddress(tblNidMock.getPresentAddress());
        tblNidData.setPresentAddressEn(tblNidMock.getPresentAddressEn());
        tblNidData.setPermanentAddressEn(tblNidMock.getPermanentAddressEn());
        tblNidData.setBirthPlace(tblNidMock.getBirthPlace());
        tblNidData.setBirthPlaceEn(tblNidMock.getBirthPlaceEn());
        tblNidData.setDateOfBirth(tblNidMock.getDateOfBirth());
        tblNidData.setGender(tblNidMock.getGender());
        tblNidData.setExpiryDate(tblNidMock.getExpiryDate());
        tblNidData.setIssuanceDate(tblNidMock.getIssuanceDate());
        tblNidData.setTranslationId(tblNidMock.getTranslationId());
        tblNidData.setSessionId(tblNidMock.getSessionId());
        tblNidData.setStatusCode(!isNullOrEmpty(tblNidMock.getStatusCode()) ? tblNidMock.getStatusCode() : Constants.NADRA_SUCESS_CODE);
        tblNidData.setStatusMessage(!isNullOrEmpty(tblNidMock.getStatusMessage()) ? tblNidMock.getStatusMessage() : Constants.NID_VERIFICATION_SUCCESS);
    }

    /** Writes one TBL_NID_HITS audit row, reusing the current session/transaction id when there is one. */
    private void recordNidHit(String cnic, BigDecimal userId, String serviceName,
                              String responsePayload, String responseCode, String responseMessage) {
        TblNidHits tblNidHits = getSessionId(cnic);
        tblNidHits.setNidNo(cnic);
        tblNidHits.setServiceName(serviceName);
        tblNidHits.setCreateuser(userId);
        tblNidHits.setCreatedate(new Date());
        tblNidHits.setResponse(trimToColumn(responsePayload, Constants.NID_HITS_RESPONSE_MAX_LEN));
        tblNidHits.setResponseCode(responseCode);
        tblNidHits.setResponseMessage(trimToColumn(responseMessage, Constants.NID_HITS_RESPONSE_MESSAGE_MAX_LEN));
        if (isNullOrEmpty(tblNidHits.getSessionId())) {
            tblNidHits.setSessionId("29120" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        }
        tblNidHitsRepo.saveAndFlush(tblNidHits);
    }

    /** Keeps a value inside its physical column width so a long payload cannot raise ORA-12899. */
    private String trimToColumn(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }

    private void settingNadraResponseToTblNadra(BigDecimal userId, TblNidData tblNidData, JSONObject jsonObject) {
        if (jsonObject.has("CODE")) {
            tblNidData.setStatusCode(jsonObject.get("CODE").toString());
        }
        if (jsonObject.has("MESSAGE")) {
            tblNidData.setStatusMessage(jsonObject.get("MESSAGE").toString());
        }
        if (jsonObject.has("SESSION_ID")) {
            tblNidData.setSessionId(jsonObject.get("SESSION_ID").toString());
        }
        if (jsonObject.has("CITIZEN_NUMBER")) {


            tblNidData.setNidNo(Long.parseLong(jsonObject.get("CITIZEN_NUMBER").toString()));
        }
        if (jsonObject.has("NAME")) {

            tblNidData.setNameEn(jsonObject.get("NAME").toString());
        }
        if (jsonObject.has("PRESENT_ADDRESS")) {

            tblNidData.setPresentAddressEn(jsonObject.get("PRESENT_ADDRESS").toString());
        }
        if (jsonObject.has("DATE_OF_BIRTH")) {

            tblNidData.setDateOfBirth(jsonObject.get("DATE_OF_BIRTH").toString());
        }
        if (jsonObject.has("GENDER")) {
            tblNidData.setGender(jsonObject.get("GENDER").toString());

        }
        if (jsonObject.has("BIRTH_PLACE")) {
            tblNidData.setBirthPlace(jsonObject.get("BIRTH_PLACE").toString());
        }
        if (jsonObject.has("MOTHER_NAME")) {
            tblNidData.setMotherNameEn(jsonObject.get("MOTHER_NAME").toString());
        }
        if (jsonObject.has("EXPIRY_DATE")) {
            tblNidData.setExpiryDate(jsonObject.get("EXPIRY_DATE").toString());

        }
    }

    //This Method is used for nadra varification through nadra api getCitizenData
    public JSONObject nadraVarification(NadraVerificationRequest nadraVerificationRequest, BigDecimal userId) throws JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder();
        TblNidHits tblNidHits = getSessionId(nadraVerificationRequest.getCnic());
        String transactionId = tblNidHits.getTransactionId();
        String sessionID = tblNidHits.getSessionId();
        stringBuilder.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:jsn=\"http://JSNadraWebService\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <jsn:GetCitizenData>\n" +
                "         <franchizeID>2912</franchizeID>\n" +
                "         <xml_request_data>&lt;BIOMETRIC_VERIFICATION>\n" +
                "    &lt;USER_VERIFICATION>\n" +
                "        &lt;USERNAME>js_blb&lt;/USERNAME>\n" +
                "        &lt;PASSWORD>GcT3@2NdpS#&lt;/PASSWORD>\n" +
                "    &lt;/USER_VERIFICATION>\n" +
                "    &lt;REQUEST_DATA>\n" +
                "        &lt;TRANSACTION_ID>" + transactionId + "&lt;/TRANSACTION_ID>\n" +
                "        &lt;SESSION_ID>" + sessionID + "&lt;/SESSION_ID>\n" +
                "        &lt;CITIZEN_NUMBER>" + nadraVerificationRequest.getCnic() + "&lt;/CITIZEN_NUMBER>\n" +
                "        &lt;CONTACT_NUMBER>" + nadraVerificationRequest.getMobileNumber() + "&lt;/CONTACT_NUMBER>\n" +
                "        &lt;FINGER_INDEX>0&lt;/FINGER_INDEX>\n" +
                "        &lt;AREA_NAME>Punjab&lt;/AREA_NAME>\n" +
                "        &lt;ISSUE_DATE>" + (isNullOrEmpty(nadraVerificationRequest.getCnicIssuanceDate()) ? Constants.EMPTY : new SimpleDateFormat("dd-MM-yyyy").format(nadraVerificationRequest.getCnicIssuanceDate())) + "&lt;/ISSUE_DATE>\n" +
                "    &lt;/REQUEST_DATA>\n" +
                "&lt;/BIOMETRIC_VERIFICATION\n" +
                "></xml_request_data>\n" +
                "      </jsn:GetCitizenData>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>");
        JSONObject jObject = Utills.getSoapResponse(nadraApiurl, stringBuilder.toString());
        tblNidHits.setNidNo(nadraVerificationRequest.getCnic());
        tblNidHits.setCreatedate(new Date());
        tblNidHits.setCreateuser(userId);
        tblNidHits.setServiceName(Constants.BIO_SERVICE_NAME);

        if (jObject != null) {
            jObject = jObject.getJSONObject("soapenv:Envelope");
            jObject = jObject.getJSONObject("soapenv:Body");
            if (jObject.has("NS1:GetCitizenDataResponse")) {
                jObject = jObject.getJSONObject("NS1:GetCitizenDataResponse");
            }


            Object object = jObject.get("GetCitizenDataResult");
            String xml = XML.toString(object);
            xml = xml.replace("&lt;", "<");
            xml = xml.replace("&gt;", ">");
            xml = xml.replace(";", " ");
            xml = xml.replace("&quot", "\"");
            jObject = XML.toJSONObject(xml);
            if (jObject != null) {
                String r = jObject.toString();
                Root root = fromJson(r, Root.class);
                tblNidHits.setResponse(trimToColumn(r, Constants.NID_HITS_RESPONSE_MAX_LEN));
                String sessionId = String.valueOf(root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getSESSION_ID());
                tblNidHits.setSessionId((sessionId != null && !sessionId.isEmpty() && !sessionId.equalsIgnoreCase("null")) ? sessionId : null);
                jObject = new JSONObject();
                jObject.put("CODE", String.valueOf(root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getResponseStatus().getCODE()));
                tblNidHits.setResponseCode(String.valueOf(root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getResponseStatus().getCODE()));
                jObject.put("MESSAGE", root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getResponseStatus().getMESSAGE());
                tblNidHits.setResponseMessage(trimToColumn(jObject.getString("MESSAGE"), Constants.NID_HITS_RESPONSE_MESSAGE_MAX_LEN));
                jObject.put("SESSION_ID", String.valueOf(root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getSESSION_ID()));
                jObject.put("CITIZEN_NUMBER", String.valueOf(root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getCitizenNumber()));
                tblNidHitsRepo.saveAndFlush(tblNidHits);
                if (root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getResponseStatus().getCODE() == 100) {
                    jObject.put("MOTHER_NAME", String.valueOf(root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getPersonData().getMOTHER_NAME()));
                    jObject.put("PRESENT_ADDRESS", root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getPersonData().getPRESENT_ADDRESS());
                    jObject.put("GENDER", root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getPersonData().getGENDER());
                    jObject.put("BIRTH_PLACE", root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getPersonData().getBIRTH_PLACE());
                    jObject.put("DATE_OF_BIRTH", root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getPersonData().getDATE_OF_BIRTH());
                    jObject.put("EXPIRY_DATE", root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getPersonData().getEXPIRY_DATE());
                    jObject.put("NAME", root.getBIOMETRIC_VERIFICATION().getRESPONSE_DATA().getPersonData().getNAME());
                }
            } else {
                jObject = null;
            }
        } else {
            jObject = null;
        }
        return jObject;

    }

    private TblNidHits getSessionId(String cnic) {
        TblNidHits tblNidHits = new TblNidHits();
        Object object = tblNidHitsRepo.getSessionId(cnic);
        if (object != null) {
            Object[] row = (Object[]) object;
            tblNidHits.setTransactionId(String.valueOf(row[1]));
            tblNidHits.setSessionId(String.valueOf(row[0]));
        } else {
            tblNidHits.setTransactionId("29120" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            tblNidHits.setSessionId("");
        }
        return tblNidHits;
    }

    @Override
    public Response inAppNadraBvs(VerifyFingerPrintRequest verifyFingerPrintRequest, BigDecimal userID, HttpServletRequest request) throws JsonProcessingException {
        Response response = new Response();
        TblNidHits tblNidHits = new TblNidHits();
        TblNidData tblNidData = tblNidDataRepo.findByNidNo(verifyFingerPrintRequest.getCnic());
        String value = tblGlobalConfigRepo.findByKeyName(Constants.NADRA_BVS_MOCK);
        nadraBvsMock = value != null ? value : nadraBvsMock;
        if (tblNidData == null) {
            tblNidData = new TblNidData();
        }

        if (!nadraBvsMock.equalsIgnoreCase(Constants.NOT_VERIFIED)) {
            NadraVerificationRequest nadraVerificationRequest = new NadraVerificationRequest();
            NadraVerificationResponse nadraVerificationResponse = new NadraVerificationResponse();

            // Mock mode: the citizen record is read from TBL_NID_MOCK and from nowhere else.
            // No row for this CNIC means the verification failed.
            TblNidMock tblNidMock = tblNidMockRepo.findByNidNo(verifyFingerPrintRequest.getCnic());
            if (tblNidMock == null) {
                recordNidHit(verifyFingerPrintRequest.getCnic(), userID, Constants.BVS_SERVICE_NAME,
                        null, Constants.RESPONSE_CODE_FAILURE, Constants.NID_VERIFICATION_FAILED);
                response.setData(null);
                response.setMessages(Constants.NID_VERIFICATION_FAILED);
                response.setResponsecode(Constants.RESPONSE_CODE_FAILURE);
                return response;
            }
            applyMockRecord(tblNidData, tblNidMock, verifyFingerPrintRequest.getCnic());
            nadraVerificationResponse.setCode(tblNidData.getStatusCode());

            tblNidHits = getSessionId(verifyFingerPrintRequest.getCnic());
            tblNidHits.setSessionId("29120" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            tblNidHits.setServiceName(Constants.BVS_SERVICE_NAME);
            tblNidHits.setCreateuser(userID);
            tblNidHits.setCreatedate(new Date());
            tblNidHits.setNidNo(verifyFingerPrintRequest.getCnic());
            value = tblGlobalConfigRepo.findByKeyName(Constants.BVS_EXEMPTION_MOCK);
            nadraBvsExemptionMock = value != null ? value : nadraBvsExemptionMock;
            if (nadraBvsExemptionMock.equals(Constants.Y)) {
                tblNidHits.setResponseCode("122");
                tblNidHits.setResponseMessage("Bvs Not Matched");
                response.setData(null);
                response.setMessages("BVS Not Verified");
                response.setResponsecode(Constants.RESPONSE_CODE_FAILURE);
            } else {
                tblNidHits.setResponseCode(tblNidData.getStatusCode());
                tblNidHits.setResponseMessage(trimToColumn(tblNidData.getStatusMessage(), Constants.NID_HITS_RESPONSE_MESSAGE_MAX_LEN));
                response.setData(tblNidData);
                response.setMessages("SUCCESS");
                response.setResponsecode(Constants.RESPONSE_CODE_SUCCESS);
            }

            tblNidHitsRepo.saveAndFlush(tblNidHits);
            nadraVerificationRequest.setCnic(verifyFingerPrintRequest.getCnic());
            nadraVerificationRequest.setMobileNumber(verifyFingerPrintRequest.getMobileNumber());
            handleNadraResponse(nadraVerificationRequest, userID, request, nadraVerificationResponse, tblNidData);
            return response;
        }

        for (CustomerFpData customerFpData : verifyFingerPrintRequest.getCustomerFpData()) {

            NadraBvsRequest nadraBvsRequest = new NadraBvsRequest();
            tblNidHits = getSessionId(verifyFingerPrintRequest.getCnic());
            String transactionId = tblNidHits.getTransactionId();
            String sessionID = tblNidHits.getSessionId();
            nadraBvsRequest.setAreaName(verifyFingerPrintRequest.getAreaName());
            nadraBvsRequest.setCitizenNumber(verifyFingerPrintRequest.getCnic());
            nadraBvsRequest.setCompanyName("NOVA");
            nadraBvsRequest.setContactNumber(verifyFingerPrintRequest.getMobileNumber());
            nadraBvsRequest.setFingerIndex(customerFpData.getFingerIndex());
            nadraBvsRequest.setFingerTemplate(customerFpData.getFingerTemplate());
            nadraBvsRequest.setDateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            nadraBvsRequest.setMerchantType("0088");
            nadraBvsRequest.setProcessingCode("VerifyFingerprints");
            nadraBvsRequest.setSecondaryCitizenNumber(verifyFingerPrintRequest.getCnic());
            nadraBvsRequest.setTemplateType(verifyFingerPrintRequest.getTemplateType());
            nadraBvsRequest.setTransactionId(transactionId);
            nadraBvsRequest.setSessionId(sessionID);
            nadraBvsRequest.setTraceNo("000008");
            Object object = getResponseFromRestApi(nadraFingerVerificationUrl, nadraBvsHeaderName, nadraBvsHeaderValue, nadraBvsRequest);
            tblNidHits.setServiceName(Constants.BVS_SERVICE_NAME);
            tblNidHits.setCreateuser(userID);
            tblNidHits.setCreatedate(new Date());
            tblNidHits.setNidNo(verifyFingerPrintRequest.getCnic());

            if (object != null) {
                NadraBvsResponse nadraBvsResponse = new ObjectMapper().readValue(convertObjecttoJson(object), NadraBvsResponse.class);
                tblNidHits.setResponse(trimToColumn(convertObjecttoJson(object), Constants.NID_HITS_RESPONSE_MAX_LEN));
                tblNidHits.setResponseCode(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getResponsestatus().getCode());
                tblNidHits.setResponseMessage(trimToColumn(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getResponsestatus().getMessage(), Constants.NID_HITS_RESPONSE_MESSAGE_MAX_LEN));
                String sessionId = nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getSessionId();
                tblNidHits.setSessionId((sessionId != null && !sessionId.isEmpty() && !sessionId.equalsIgnoreCase("null")) ? sessionId : null);
                response.setData(nadraBvsResponse);
                if (tblNidHits.getResponseCode().equals(Constants.NADRA_SUCESS_CODE)) {
                    settingNadraResponseToTblNadraBvs(userID, tblNidData, nadraBvsResponse);
                    break;
                }
            }
            tblNidHits = tblNidHitsRepo.saveAndFlush(tblNidHits);
        }
        NadraVerificationRequest nadraVerificationRequest = new NadraVerificationRequest();
        NadraVerificationResponse nadraVerificationResponse = new NadraVerificationResponse();
        nadraVerificationRequest.setCnic(verifyFingerPrintRequest.getCnic());
        nadraVerificationRequest.setMobileNumber(verifyFingerPrintRequest.getMobileNumber());
        if (isNullOrEmpty(tblNidHits.getResponse())) {
            response.setData(null);
            response.setMessages("UNSUCCESSFUL");
            response.setResponsecode(Constants.RESPONSE_CODE_FAILURE);
        } else if (tblNidHits.getResponseCode().equals(Constants.NADRA_SUCESS_CODE)) {
            response.setData(nadraVerificationResponse);
            response.setMessages("SUCCESS");
            response.setResponsecode(Constants.RESPONSE_CODE_SUCCESS);
        } else {
            response.setData(nadraVerificationResponse);
            response.setMessages("RECORD NOT FOUND");
            response.setResponsecode(Constants.RESPONSE_CODE_FAILURE);
        }

        handleNadraResponse(nadraVerificationRequest, userID, request, nadraVerificationResponse, tblNidData);
        return response;
    }

    private String extractCityName(String presentAddressEn) {
        String result = Constants.EMPTY;
        ArrayList<String> cities = lkpCityRepo.getAllCitiesIsActiveYes();

        if (!isNullOrEmpty(cities)) {
            result = cities.parallelStream()
                    .map(city -> findBestMatch(presentAddressEn.toUpperCase(), city))  // Get the best match
                    .filter(match -> !match.equals(Constants.EMPTY))  // Ensure matches exist
                    .findFirst()  // Get the first valid match
                    .orElse(Constants.EMPTY);  // If no match found, return EMPTY
        }

        return result;
    }

    public String findBestMatch(String address, String city) {
        LevenshteinDistance distance = new LevenshteinDistance();

        // Tokenize the address using delimiters: , ; : space
        List<String> tokens = Arrays.asList(address.split("[,;: ]+"));

        String bestMatch = Constants.EMPTY;
        double highestSimilarity = 0.0;

        // Iterate over tokens and compare with the city name
        for (String token : tokens) {
            // Calculate Levenshtein distance between token and city name
            int dist = distance.apply(token.trim(), city.trim());

            // Calculate similarity as a percentage
            double similarity = (1 - (double) dist / Math.max(token.length(), city.length())) * 100;

            // If the similarity is greater than 90, and it is the best match so far
            if (similarity > 90 && similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatch = city;  // Update the best match to the city name
            }
        }

        // Return the best match if the similarity is greater than 90
        return bestMatch;
    }

    private void settingNadraResponseToTblNadraBvs(BigDecimal userId, TblNidData tblNidData, NadraBvsResponse nadraBvsResponse) {
        tblNidData.setStatusCode(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getResponsestatus().getCode());
        tblNidData.setStatusMessage(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getResponsestatus().getMessage());
        tblNidData.setSessionId(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getSessionId());
        tblNidData.setNidNo(Long.parseLong(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getCitizenNumber()));
        tblNidData.setNameEn(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getPersonData().getName());
        tblNidData.setPresentAddressEn(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getPersonData().getPresentAddress());
        tblNidData.setDateOfBirth(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getPersonData().getDateOfBirth());
        tblNidData.setGender(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getPersonData().getGender());
        tblNidData.setBirthPlace(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getPersonData().getBirthPlace());
        tblNidData.setMotherNameEn(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getPersonData().getMotherName());
        tblNidData.setExpiryDate(nadraBvsResponse.getVerifyFingerPrintsResponse().getbIOMETRIC_VERIFICATION().getResponsedata().getPersonData().getExpiryDate());

    }


}
