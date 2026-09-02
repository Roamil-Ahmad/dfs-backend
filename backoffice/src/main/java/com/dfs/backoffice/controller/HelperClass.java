package com.dfs.backoffice.controller;


import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.TblMessage;
import com.dfs.backoffice.repo.TblMessageRepo;
import com.dfs.backoffice.utils.AESencryption;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.CustomException;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.*;

public class HelperClass {
    @Value("${workflow.url}")
    private String workflowurl;
    private SecureRandom rand = new SecureRandom();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private AESencryption aeSencryption;
    @Autowired
    private Environment env;
    @Autowired
    private TblMessageRepo tblMessageRepo;
    @Autowired
    private WebClient.Builder webClientBuilder;

    public static <T> T fromJson(String json, Class<T> tClass) throws JsonProcessingException {
        if (json == null || tClass == null) {
            throw new CustomException(GenericResponseCode.JSON_PARSE_EXCEPTION.getResponseCode());
        }
        return objectMapper.readValue(json, tClass);
    }

    public static String convertObjecttoJson(Object object) {
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Response sendRequestAndGetResponse(Request request, String token, String endpoint) throws JsonProcessingException {
        String result = this.getResponseFromPostAPI(this.createHeaderMapBackOffice(token), request, endpoint);
        return fromJson(result, Response.class);
    }

    public String getResponseFromPostAPI(Map<String, String> headerMap, Object requestBody, String url) {
        WebClient webClient = webClientBuilder.clone().baseUrl(url).build();
        WebClient.RequestBodySpec bodySpec = (WebClient.RequestBodySpec) webClient.post().uri((rec$) -> {
            return ((UriBuilder) rec$).build(new Object[0]);
        });
        Iterator var6 = headerMap.entrySet().iterator();

        while (var6.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) var6.next();
            bodySpec.header((String) entry.getKey(), new String[]{(String) entry.getValue()});
        }

        WebClient.ResponseSpec responseSpec = bodySpec.body(BodyInserters.fromValue(requestBody)).retrieve();

        ResponseEntity response;
        try {
            response = (ResponseEntity) responseSpec.toEntity(String.class).block();
        } catch (HttpClientErrorException.BadRequest var9) {
            response = new ResponseEntity(var9.getResponseBodyAsString(), var9.getStatusCode());
        } catch (Exception var10) {
            throw new CustomException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }

        if (response != null && response.getBody() != null) {
            return (String) response.getBody();
        } else {
            throw new CustomException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
    }

    public Map<String, String> createHeaderMapBackOffice(String authorization) {
        // Create a new HashMap object to store the headers
        Map<String, String> headerMap = new HashMap<>();
        // Set the "content-type" header to "application/json"
        headerMap.put(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
        // Set the "accept" header to "application/json"
        headerMap.put(Constants.ACCEPT, Constants.APPLICATION_JSON);
        //Set the "Authorization" Token
        headerMap.put(Constants.AUTHORIZATION, authorization);

        // Return the populated headerMap object
        return headerMap;
    }

    public static <T> boolean isNullOrEmpty(T input) {
        return input == null || (input instanceof String && ((String) input).isEmpty()) ||
                (input instanceof List && ((List<?>) input).isEmpty()) ||
                (input instanceof Map && ((Map<?, ?>) input).isEmpty()) ||
                (input instanceof BigDecimal && ((BigDecimal) input).compareTo(BigDecimal.ZERO) <= 0);
    }

    public String autoGenerate(String type, int length) {
        byte var5 = -1;
        switch (type.hashCode()) {
            case -2009771127:
                if (type.equals("LongTime")) {
                    var5 = 12;
                }
                break;
            case -1597066262:
                if (type.equals("RequestId")) {
                    var5 = 7;
                }
                break;
            case -532667892:
                if (type.equals("RandomNumber")) {
                    var5 = 2;
                }
                break;
            case -335760659:
                if (type.equals("Numeric")) {
                    var5 = 3;
                }
                break;
            case 81422:
                if (type.equals("RRN")) {
                    var5 = 6;
                }
                break;
            case 2122702:
                if (type.equals("Date")) {
                    var5 = 11;
                }
                break;
            case 75450319:
                if (type.equals("Nonce")) {
                    var5 = 5;
                }
                break;
            case 81068325:
                if (type.equals("Trace")) {
                    var5 = 0;
                }
                break;
            case 684961775:
                if (type.equals("AlphaNumeric")) {
                    var5 = 4;
                }
                break;
            case 1247202393:
                if (type.equals("TransactionID")) {
                    var5 = 1;
                }
                break;
            case 1297628770:
                if (type.equals("TelcoDateTime")) {
                    var5 = 8;
                }
                break;
            case 1332768889:
                if (type.equals("TransactionDateTime")) {
                    var5 = 10;
                }
                break;
            case 1857393595:
                if (type.equals("DateTime")) {
                    var5 = 9;
                }
        }

        String response;
        switch (var5) {
            case 0:
                response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                response = response.substring(response.length() - 6);
                response = new String(this.shuffleArray(response.toCharArray()));
                break;
            case 1:
                response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                response = response.substring(response.length() - 15);
                response = new String(this.shuffleArray(response.toCharArray()));
                break;
            case 2:
                String ticks = String.valueOf(System.currentTimeMillis());
                String extractedNumbers = ticks.substring(ticks.length() - 4);
                String ranNumber = String.valueOf(this.rand.nextInt()).substring(1, length - 4);
                return ranNumber + extractedNumbers;
            case 3:
                String var10000 = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                response = var10000 + convertDateToStringInFormat("ddMMyyyyHHmmss", new Date());
                response = response.substring(response.length() - length);
                response = new String(this.shuffleArray(response.toCharArray()));
                break;
            case 4:
                response = "A1B2C3D4E5F6G7H8I9J10K11L12M13N1";
                response = response.substring(response.length() - length);
                response = new String(this.shuffleArray(response.toCharArray()));
                break;
            case 5:
                return String.valueOf(UUID.randomUUID());
            case 6:
                if (length == 0) {
                    response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                } else if (length == 13) {
                    response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date()).substring(0, 12);
                } else {
                    response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                }

                response = new String(this.shuffleArray(response.toCharArray()));
                break;
            case 7:
                response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                response = new String(this.shuffleArray(response.toCharArray()));
                break;
            case 8:
                response = convertDateToStringInFormat("yyyy-MM-dd'T'HH:mm:ss", new Date());
                break;
            case 9:
                response = convertDateToStringInFormat("yyyyMMddHHmmss", new Date());
                break;
            case 10:
                response = convertDateToStringInFormat("MMddHHmmss", new Date());
                break;
            case 11:
                response = convertDateToStringInFormat("MMdd", new Date());
                break;
            case 12:
                response = convertDateToStringInFormat("yyyyMMddHHmmssSSSSSS", new Date());
                break;
            default:
                response = convertDateToStringInFormat("HHmmss", new Date());
        }

        return response;
    }

    public static String convertDateToStringInFormat(String format, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    private char[] shuffleArray(char[] array) {
        ArrayList characters = new ArrayList();
        char[] shuffledArray = array;
        int i = array.length;

        for (int var5 = 0; var5 < i; ++var5) {
            char c = shuffledArray[var5];
            characters.add(c);
        }

        Collections.shuffle(characters);
        shuffledArray = new char[array.length];

        for (i = 0; i < array.length; ++i) {
            shuffledArray[i] = (Character) characters.get(i);
        }

        return shuffledArray;
    }


    public ResponseEntity<Response> getResponseFormat(HttpStatus status, String message, Object data, boolean custom) {
        String responsestatus;
        TblMessage tblMessage = null;
        if (status.equals(HttpStatus.OK) || status.value() == 200) {
            responsestatus = "1";
        } else {
            responsestatus = "0";
        }
        if (!custom) {
            tblMessage = tblMessageRepo.findTblMessageByMessageCode(message);
        }
        Response response = new Response();
        response.setResponseCode(responsestatus);
        response.setMessage(tblMessage != null ? tblMessage.getMessageDescr() : message);
        response.setPayload(data);
        return ResponseEntity.status(status).body(response);
    }

    public String checkMcApplicability(String authorization, String tableName, String formName, String requestTypeSave) {

        String url = workflowurl + "/ckeckMcApplicable";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        headerMap.put("Accept", "application/json");
        headerMap.put("Authorization", authorization);
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("tableName", tableName);
        requestParams.put("formName", formName);
        requestParams.put("requestType", requestTypeSave);
        Request request = new Request();
        request.setPayLoad(requestParams);
        return getResponseFromPostAPI(headerMap, request, url);
    }

    public String mcRequest(String authorization, String tableName, String formName, McRequestDetail mcRequestDetail) {
        String urlmcRequest = workflowurl + "/mcRequest";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        headerMap.put("Accept", "application/json");
        headerMap.put("Authorization", authorization);
        Map<String, String> requestParamsMcRequest = new HashMap<>();
        requestParamsMcRequest.put("formName", formName);
        requestParamsMcRequest.put("makerId", String.valueOf(mcRequestDetail.getMakerId()));
        requestParamsMcRequest.put("makerComments", mcRequestDetail.getMakerComments());
        requestParamsMcRequest.put("ftFlag", mcRequestDetail.getFtFlag());
        requestParamsMcRequest.put("tableName", tableName);
        requestParamsMcRequest.put("requestType", mcRequestDetail.getRequestType());
        requestParamsMcRequest.put("updateType", mcRequestDetail.getUpdateType());
        requestParamsMcRequest.put("updateJson", mcRequestDetail.getUpdateJson());
        requestParamsMcRequest.put("refTableId", String.valueOf(mcRequestDetail.getRefTableId()));
        requestParamsMcRequest.put("oldJson", String.valueOf(mcRequestDetail.getOldJson()));
        Request jsonRequest = new Request();
        jsonRequest.setPayLoad(requestParamsMcRequest);
        return getResponseFromPostAPI(headerMap, jsonRequest, urlmcRequest);
    }

    public String mcAction(Request jsonRequest, String authorization, McActionRequest mcActionRequest, BigDecimal userId) {
        String urlmcRequest = workflowurl + "/mcAction";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        headerMap.put("Accept", "application/json");
        headerMap.put("Authorization", authorization);
        Map<String, String> requestParamsMcAction = new HashMap<String, String>();
        requestParamsMcAction.put("mcRequestId", mcActionRequest.getMcRequestId());
        requestParamsMcAction.put("mcPeindingRequestId", mcActionRequest.getMcPeindingRequestId());
        requestParamsMcAction.put("checkerId", String.valueOf(userId));
        requestParamsMcAction.put("checkerComments", mcActionRequest.getCheckerComments());
        requestParamsMcAction.put("action", mcActionRequest.getAction());
        requestParamsMcAction.put("updatedIndex", String.valueOf(mcActionRequest.getUpdatedIndex()));
        jsonRequest.setPayLoad(requestParamsMcAction);
        return getResponseFromPostAPI(headerMap, jsonRequest, urlmcRequest);
    }

    public String checkMakerCheckerApplicability(String authorization, String tableName, String formName, String requestTypeSave) {
        String result = "";
        Gson gson = new Gson();
        String checkMcApplicability = this.checkMcApplicability(authorization, tableName, formName, requestTypeSave);
        if (checkMcApplicability != null) {
            Response response1 = (Response) gson.fromJson(checkMcApplicability, Response.class);
            ProcedureResponse procedureResponse = (ProcedureResponse) gson.fromJson(this.convertObjecttoJson(response1.getPayload()), ProcedureResponse.class);
            if (procedureResponse == null || procedureResponse.getMcApplicability() <= 0) {
                result = "102010201020";
            }
        } else {
            result = GenericResponseCode.RECORD_NOT_FOUND.getResponseCode();
        }

        return result;
    }

    public Response makerCheckerRequest(String authorization, String tableName, String formName, McRequestDetail mcRequestDetail) {
        Response response = new Response();
        Gson gson = new Gson();
        String mcRequest = this.mcRequest(authorization, tableName, formName, mcRequestDetail);
        if (mcRequest != null) {
            Response response2 = (Response) gson.fromJson(mcRequest, Response.class);
            McResponse mcResponse = (McResponse) gson.fromJson(convertObjecttoJson(response2.getPayload()), McResponse.class);
            if (mcResponse != null && mcResponse.getStatus() == 1) {
                setResponse(response, Constants.ONE, mcResponse, GenericResponseCode.SUCCESS.getResponseCode(), mcResponse.getStatusDecsr());
            } else {
                setResponse(response, Constants.ZERO, mcResponse, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), mcResponse != null ? mcResponse.getStatusDecsr() : GenericResponseCode.RECORD_NOT_SAVED.getResponseMessage());
            }
        } else {
            this.getResponseFormat(HttpStatus.BAD_REQUEST, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), null, false);
        }

        return response;
    }

//    public void setResponse(Response response, String type, Object payload, String message) {
//        TblMessage tblMessage = tblMessageRepo.findTblMessageByMessageCode(message);
//        response.setPayload(payload);
//        response.setResponseCode(type);
//        response.setMessage(tblMessage != null ? tblMessage.getMessageDescr() : GenericResponseCode.GENERAL_PROCESSING_ERROR.getResponseMessage());
//    }

    public void setResponse(Response response, String type, Object payload, String code) {
        TblMessage tblMessage = tblMessageRepo.findTblMessageByMessageCode(code);
        response.setPayload(payload);
        response.setResponseCode(type);
        response.setMessage(tblMessage != null ? tblMessage.getMessageDescr() : GenericResponseCode.GENERAL_PROCESSING_ERROR.getResponseCode());
    }

    public void setResponse(Response response, String type, Object payload, String code, String customMessage) {
        TblMessage tblResponseMessage = tblMessageRepo.findTblMessageByMessageCode(code);
        response.setPayload(payload);
        response.setResponseCode(type);
        response.setMessage(customMessage != null ? customMessage : tblResponseMessage.getMessageDescr());
    }

    public ResponseEntity<Response> castResponseToEntity(Response response, String code) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        return !Objects.equals(code, Constants.ZERO)
                ? new ResponseEntity<>(response, headers, HttpStatus.OK)
                : new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }

    public String encryptWithAes(String text) {
        return aeSencryption.encryptwith256(text);
    }

    public String decrypttWithAes(String text) {
        return aeSencryption.decrypt(text);
    }

    public BigDecimal setUpdateIndex(BigDecimal value) {
        return value == null ? BigDecimal.ONE : value.add(BigDecimal.ONE);
    }

    public boolean callPdfJasperReport(String reportName, String reportOutName, HashMap<String, Object> parameterMap) {

        String conString = env.getProperty("spring.datasource.url");
        String conDriver = env.getProperty("spring.datasource.driver-class");
        String userName = env.getProperty("spring.datasource.username");
        String userPassword = env.getProperty("spring.datasource.password");
        try {
            JasperReport jp = JasperCompileManager.compileReport(reportName);
            JasperPrint jasperPrint = null;

            // database connection
            Class.forName(conDriver).newInstance();
            Connection datasource = DriverManager.getConnection(conString, userName, userPassword);

            jasperPrint = JasperFillManager.fillReport(jp, parameterMap, datasource);

            List<JRPrintPage> pages = jasperPrint.getPages();
            if (pages.size() == 0) {
                return false;
            } else {
            }

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportOutName));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            pdfExporter.setConfiguration(configuration);

            pdfExporter.exportReport();

            if (!datasource.isClosed()) {
                datasource.close();
            }
            return true;
        } catch (Exception e) {
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null) {
                    if (e.getCause().getCause().getCause() != null) {
                        String e1 = e.getCause().getCause().getCause().getLocalizedMessage();
                        if (e1.contains("ORA")) {
                            System.out.println(e1);
                        } else {
                            System.out.println(e1);
                        }
                    } else {
                        System.out.println(e.getCause().getCause().getLocalizedMessage());
                    }

                } else {
                    System.out.println(e.getCause().getLocalizedMessage());
                }
            }
            System.out.println(e.getMessage());
            return false;
        }

    }


}
