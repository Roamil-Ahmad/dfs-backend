package com.dfs.app.service.impl;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.UpdateCardRequest;
import com.dfs.app.dto.card.*;
import com.dfs.app.dto.common.Request;
import com.dfs.app.model.LkpDebitCardType;
import com.dfs.app.model.TblAccount;
import com.dfs.app.model.TblDebitCardRequest;
import com.dfs.app.model.TblDebitCardResponse;
import com.dfs.app.repo.LkpDebitCardTypeRepo;
import com.dfs.app.repo.TblAccountRepo;
import com.dfs.app.repo.TblDebitCardRequestRepo;
import com.dfs.app.repo.TblDebitCardResponseRepo;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.DebitCardService;
import com.dfs.app.util.AESencryption;
import com.dfs.app.util.CustomDataNotFoundException;
import com.dfs.app.util.CustomException;
import com.dfs.app.util.GenericResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class DebitCardServiceImpl extends HelperClass implements DebitCardService {
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TblDebitCardRequestRepo tblDebitCardRequestRepo;
    @Autowired
    private TblDebitCardResponseRepo tblDebitCardResponseRepo;
    @Autowired
    private AESencryption aeSencryption;
    @Value("${card.api.base.url}")
    private String baseUrl;
    @Autowired
    private LkpDebitCardTypeRepo lkpDebitCardTypeRepo;

    private static final Logger log = LoggerFactory.getLogger(DebitCardServiceImpl.class);

    @Override
    @Transactional
    public HashMap<String, Object> newCardRequest(NewRequest newRequest, Request request, BigDecimal userId)
            throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo.findByAccountNo(newRequest.getAccountNumber());
        if (tblAccount != null) {
            TblDebitCardRequest tblDebitCardRequest = tblDebitCardRequestRepo
                    .findByTblAccountAccountId(tblAccount.getAccountId());
            if (tblDebitCardRequest != null) {
                throw new CustomDataNotFoundException(GenericResponseCode.DEBIT_CARD_EXIST.getResponseCode());
            } else {
                TblDebitCardRequest tblDebitCardRequest1 = new TblDebitCardRequest();
                tblDebitCardRequest1.setTblAccount(tblAccount);
                LkpDebitCardType lkpDebitCardType = lkpDebitCardTypeRepo
                        .findByDebitCardTypeCode(String.valueOf(newRequest.getCardType()));
                tblDebitCardRequest1.setLkpDebitCardType(lkpDebitCardType);
                tblDebitCardRequest1.setNameoncard(tblAccount.getAccountTitle());
                tblDebitCardRequest1.setProductcode(newRequest.getProductCode());
                tblDebitCardRequest1.setCreatedate(new Date());
                tblDebitCardRequest1.setCreateuser(userId);
                tblDebitCardRequestRepo.save(tblDebitCardRequest1);
                TblDebitCardResponse tblDebitCardResponse = new TblDebitCardResponse();
                tblDebitCardResponse.setTblDebitCardRequest(tblDebitCardRequest1);
                tblDebitCardResponse.setNameoncard(tblDebitCardRequest1.getNameoncard());
                tblDebitCardResponse.setCardProductCode(tblDebitCardRequest1.getProductcode());
                tblDebitCardResponse.setCardType(tblDebitCardRequest1.getLkpDebitCardType().getDebitCardTypeDescr());
                tblDebitCardResponse.setCustomerId(String.valueOf(tblAccount.getTblCustomer().getCustomerId()));
                tblDebitCardResponse.setCreatedate(new Date());
                tblDebitCardResponse.setCreateuser(userId);
                tblDebitCardResponse.setUpdateindex(tblDebitCardResponse.getUpdateindex() == null ? BigDecimal.ONE
                        : tblDebitCardResponse.getUpdateindex().add(BigDecimal.ONE));
                tblDebitCardResponseRepo.save(tblDebitCardResponse);
                String response = getResponseFromPostAPI(new HashMap<>(), newRequest, baseUrl + "new-request");
                GenericResponse genericResponse = fromJson(response, GenericResponse.class);
                if (genericResponse != null && genericResponse.getResponseCode() == 0) {
                    return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
                } else {
                    throw new CustomException((genericResponse != null && genericResponse.getResponseMessage() != null)
                            ? genericResponse.getResponseMessage()
                            : "Failed to create card request");
                }
            }
        } else {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
    }

    @Override
    public HashMap<String, Object> changeCardPin(ChangePinRequest changePinRequest, Request request)
            throws JsonProcessingException {
        List<TblAccount> tblAccount = tblAccountRepo.findTblAccountsByMobileOrCnic(null,
                aeSencryption.encryptwith256(changePinRequest.getRelationshipNum()));
        if (!tblAccount.isEmpty()) {
            changePinRequest.setRelationshipNum(aeSencryption.decrypt(tblAccount.get(0).getTblCustomer().getNidNo()));
            String response = getResponseFromPostAPI(new HashMap<>(), changePinRequest, baseUrl + "change-pin");
            GenericResponse genericResponse = fromJson(response, GenericResponse.class);
            if (genericResponse != null && genericResponse.getResponseCode() == 0) {
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
            } else {
                throw new CustomException((genericResponse != null && genericResponse.getResponseMessage() != null)
                        ? genericResponse.getResponseMessage()
                        : "Failed to change card pin");
            }
        } else {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
    }

    @Override
    public HashMap<String, Object> forgotCardPin(GeneratePinRequest forgotPinRequest, Request request)
            throws JsonProcessingException {
        List<TblAccount> tblAccount = tblAccountRepo.findTblAccountsByMobileOrCnic(null,
                aeSencryption.encryptwith256(forgotPinRequest.getRelationshipNum()));
        if (!tblAccount.isEmpty()) {
            String decryptedRelationshipNum = aeSencryption.decrypt(tblAccount.get(0).getTblCustomer().getNidNo());
            GeneratePinRequest apiRequest = new GeneratePinRequest(
                    forgotPinRequest.getPan(),
                    decryptedRelationshipNum,
                    forgotPinRequest.getPin(),
                    forgotPinRequest.getConfirmPin(),
                    forgotPinRequest.getFlag());
            String response = getResponseFromPostAPI(new HashMap<>(), apiRequest, baseUrl + "generate-pin");
            GenericResponse genericResponse = fromJson(response, GenericResponse.class);
            if (genericResponse != null && genericResponse.getResponseCode() == 0) {
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
            } else {
                throw new CustomException((genericResponse != null && genericResponse.getResponseMessage() != null)
                        ? genericResponse.getResponseMessage()
                        : "Failed to update card pin");
            }
        } else {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
    }

    @Override
    public HashMap<String, Object> updateCardStatus(UpdateStatusRequest updateStatusRequest, Request request)
            throws JsonProcessingException {
        UpdateStatusApiRequest apiRequest = new UpdateStatusApiRequest(updateStatusRequest.getPan(),
                updateStatusRequest.getStatusCode());
        String response = getResponseFromPostAPI(new HashMap<>(), apiRequest, baseUrl + "update-status");
        GenericResponse genericResponse = fromJson(response, GenericResponse.class);
        if (genericResponse != null && genericResponse.getResponseCode() == 0) {
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
        } else {
            throw new CustomException((genericResponse != null && genericResponse.getResponseMessage() != null)
                    ? genericResponse.getResponseMessage()
                    : "Failed to update card status");
        }

    }

    @Override
    public HashMap<String, Object> cardInquiry(InquiryRequest inquiryRequest, Request request)
            throws JsonProcessingException {
        List<TblAccount> tblAccount = tblAccountRepo.findTblAccountsByMobileOrCnic(null,
                aeSencryption.encryptwith256(inquiryRequest.getRelationshipNum()));
        if (!tblAccount.isEmpty()) {
            String response = getResponseFromPostAPI(new HashMap<>(), inquiryRequest, baseUrl + "inquiry");
            InquiryResponse genericResponse = fromJson(response, InquiryResponse.class);
            if (genericResponse != null && genericResponse.getResponseCode() == 0) {
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(),
                        genericResponse.getResponseBody());
            } else {
                throw new CustomException((genericResponse != null && genericResponse.getResponseMessage() != null)
                        ? genericResponse.getResponseMessage()
                        : "Inquiry Failed");
            }
        } else {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
    }

    @Override
    public HashMap<String, Object> cardLov() throws JsonProcessingException {
        Object response = getResponseFromGetAPI(baseUrl + "lov/all");
        String json = objectMapper.writeValueAsString(response);
        CardResponse cardResponse = objectMapper.readValue(json, CardResponse.class);
        if (cardResponse != null && cardResponse.getResponseCode() == 0) {
            Map<String, List<LovEntry>> transformed = new LinkedHashMap<>();
            for (CardTypeResponse typeBlock : cardResponse.getResponseBody()) {
                transformed.put(typeBlock.getType(), typeBlock.getLov());
            }
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), transformed);
        } else {
            return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
        }
    }

    @Override
    public HashMap<String, Object> updateCard(UpdateCardRequest updateCardRequest) {
        TblAccount tblAccount = tblAccountRepo.findByAccountNo(updateCardRequest.getAccountNumber());
        if (tblAccount != null) {
            TblDebitCardRequest tblDebitCardRequest = tblDebitCardRequestRepo
                    .findByTblAccountAccountId(tblAccount.getAccountId());
            if (tblDebitCardRequest != null) {
                tblDebitCardRequest.setDebitCardRequestId(Long.parseLong(updateCardRequest.getRequestId()));
                tblDebitCardRequest.setLastupdatedate(new Date());
                tblDebitCardRequest.setLastupdateuser(BigDecimal.ONE);
                tblDebitCardRequest.setUpdateindex(tblDebitCardRequest.getUpdateindex() == null ? BigDecimal.ONE
                        : tblDebitCardRequest.getUpdateindex().add(BigDecimal.ONE));
                tblDebitCardRequest = tblDebitCardRequestRepo.save(tblDebitCardRequest);
                TblDebitCardResponse tblDebitCardResponse = tblDebitCardResponseRepo
                        .findByTblDebitCardRequestDebitCardRequestId(tblDebitCardRequest.getDebitCardRequestId());
                tblDebitCardResponse.setCardNumber(updateCardRequest.getPan());
                tblDebitCardResponse.setExpirydate(updateCardRequest.getCardExpiryDate());
                tblDebitCardResponse.setLastupdatedate(new Date());
                tblDebitCardResponse.setLastupdateuser(BigDecimal.ONE);
                tblDebitCardResponse.setUpdateindex(tblDebitCardResponse.getUpdateindex() == null ? BigDecimal.ONE
                        : tblDebitCardResponse.getUpdateindex().add(BigDecimal.ONE));
                tblDebitCardResponseRepo.save(tblDebitCardResponse);
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
            } else {
                throw new CustomDataNotFoundException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
            }

        } else {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
    }

    @Override
    public HashMap<String, Object> checkCardStatus(String accountId) {
        TblDebitCardRequest tblDebitCardRequest = tblDebitCardRequestRepo
                .findByTblAccountAccountId(Long.parseLong(accountId));
        if (tblDebitCardRequest == null) {
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
        } else {
            TblDebitCardResponse tblDebitCardResponse = tblDebitCardResponseRepo
                    .findByTblDebitCardRequestDebitCardRequestId(tblDebitCardRequest.getDebitCardRequestId());
            return commonService.getResponse(GenericResponseCode.DEBIT_CARD_EXIST.getResponseCode(),
                    tblDebitCardResponse != null ? tblDebitCardResponse.getCardNumber() : "5267720000001009");
        }
    }

    @Override
    public HashMap<String, Object> fetchLimits(FetchLimitRequest fetchLimitRequest, Request request)
            throws JsonProcessingException {
        try {
            // Card limits now come from the card-management wrapper (which reads the CMS DB),
            // instead of a native query against CMS.* tables from App. fetchLimitRequest carries
            // only `pan`, which the wrapper matches against the card PAN.
            String result = getResponseFromPostAPI(new HashMap<>(), fetchLimitRequest, baseUrl + "limit/fetch");
            LimitFetchApiResponse apiResponse = fromJson(result, LimitFetchApiResponse.class);

            if (apiResponse != null && apiResponse.getResponseCode() == 0 && apiResponse.getResponseBody() != null) {
                return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), apiResponse.getResponseBody());
            } else {
                return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
            }
        } catch (Exception e) {
            log.error("Error in fetchLimits: ", e);
            throw new CustomException("Failed to fetch card limits");
        }
    }
}
