package com.wallet.transaction.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.dto.*;
import com.wallet.transaction.dto.aps.CardAcceptorNameAndLocation;
import com.wallet.transaction.dto.aps.CardTitleFetchRqst;
import com.wallet.transaction.dto.aps.PointOfService;
import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.model.LkpBank;
import com.wallet.transaction.model.LkpCurrency;
import com.wallet.transaction.model.TblAccount;
import com.wallet.transaction.model.TblAccountDebitCard;
import com.wallet.transaction.repo.LkpBankRepo;
import com.wallet.transaction.repo.LkpCurrencyRepo;
import com.wallet.transaction.repo.TblAccountDebitCardRepo;
import com.wallet.transaction.repo.TblAccountRepo;
import com.wallet.transaction.service.CardLinkingService;
import com.wallet.transaction.service.CommonService;
import com.wallet.transaction.util.AESencryption;
import com.wallet.transaction.util.Constants;
import com.wallet.transaction.util.CustomDataNotFoundException;
import com.wallet.transaction.util.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class CardLinkingServiceImpl extends HelperClass implements CardLinkingService {
    @Autowired
    private TblAccountDebitCardRepo tblAccountDebitCardRepo;
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private AESencryption aeSencryption;
    @Value("${account.type.wallet}")
    private String accountTypeWallet;
    @Autowired
    private LkpBankRepo lkpBankRepo;
    @Autowired
    private CommonService commonService;
    @Value("${card.titlefetch.url}")
    private String cardTitleFetchUrl;
    @Autowired
    private LkpCurrencyRepo lkpCurrencyRepo;


    @Override
    public HashMap<String, Object> linkCard(CardLinkingRequest cardLinkingRequest, Request request, String authorization, BigDecimal userId) throws JsonProcessingException {
        TblAccount tblAccount = tblAccountRepo.getAccountByAccountNumberAndAccountType(cardLinkingRequest.getAccountNo(), accountTypeWallet);

        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_ACCOUNT.getResponseCode());
        }
        CardTitleFetchRqst cardTitleFetchRqst = new CardTitleFetchRqst();
        cardTitleFetchRqst.setPan(cardLinkingRequest.getPan());
        cardTitleFetchRqst.setMerchantType("6012");
        cardTitleFetchRqst.setCardAcceptorIdentification("00000001");
        cardTitleFetchRqst.setCardAcceptorCode("123456789101475");
        cardTitleFetchRqst.setCvcPresent("000");
        cardTitleFetchRqst.setTransactionType("1");
        cardTitleFetchRqst.setTerminalType("2");
        cardTitleFetchRqst.setTransactionCurrencyCode("971");

        PointOfService pointOfService = new PointOfService();
        pointOfService.setCardDataInputCapability("5");
        pointOfService.setCardholderAuthenticationCapability("1");
        pointOfService.setCardCaptureCapability("0");
        pointOfService.setOperatingEnvironment("1");
        pointOfService.setCardholderPresenceIndicator("0");
        pointOfService.setCardPresence("1");
        pointOfService.setCardDataInputMode("2");
        pointOfService.setCardholderAuthenticationMethod("5");
        pointOfService.setCardholderAuthenticationEntity("4");
        pointOfService.setCardDataOutputCapability("1");
        pointOfService.setTerminalOutputCapability("4");
        pointOfService.setPinCaptureCapability("6");
        cardTitleFetchRqst.setPointOfService(pointOfService);

        CardAcceptorNameAndLocation cardAcceptor = new CardAcceptorNameAndLocation();
        cardAcceptor.setName("DFS");
        cardAcceptor.setStreet("1234");
        cardAcceptor.setCity("KABUL");
        cardAcceptor.setState("KBL");
        cardAcceptor.setCountry("AFG");
        cardAcceptor.setPostalCode("123456789");
        cardTitleFetchRqst.setCardAcceptorNameAndLocation(cardAcceptor);

        String json = getResponseFromPostAPI(this.createHeaderMapBackOffice(""), cardTitleFetchRqst, cardTitleFetchUrl);
        ObjectMapper mapper = new ObjectMapper();
        if (json == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
        }
        TransactionResponseDto<CardTitleFetchApsResponse> responseDto = mapper.readValue(json, mapper.getTypeFactory()
                .constructParametricType(TransactionResponseDto.class, CardTitleFetchApsResponse.class));

        if (responseDto != null && responseDto.getCode().equals(GenericResponseCode.SUCCESS.getResponseCode())) {
            TblAccountDebitCard tblAccountDebitCard = tblAccountDebitCardRepo.findByAccountIdPanAccountNoIsActive(tblAccount.getAccountId(),cardLinkingRequest.getPan(),tblAccount.getAccountNo(),Constants.YES);
            if(tblAccountDebitCard!=null){
                tblAccountDebitCard.setLastupdatedate(new Date());
                tblAccountDebitCard.setLastupdateuser(userId);
                tblAccountDebitCard.setUpdateindex(tblAccountDebitCard.getUpdateindex()!=null?tblAccountDebitCard.getUpdateindex().add(BigDecimal.ONE):BigDecimal.ONE);
            }else {
                tblAccountDebitCard=new TblAccountDebitCard();
                tblAccountDebitCard.setCreateuser(userId);
                tblAccountDebitCard.setCreatedate(new Date());


            }
            tblAccountDebitCard.setAccountNo(tblAccount.getAccountNo());
            tblAccountDebitCard.setTblAccount(tblAccount);
            tblAccountDebitCard.setLinkedCard(Constants.YES);

            tblAccountDebitCard.setPan(cardLinkingRequest.getPan());
            tblAccountDebitCard.setNameOnCard(cardLinkingRequest.getCardDisplayName());
            tblAccountDebitCard.setExpiryDate(cardLinkingRequest.getExpiryDate());
            tblAccountDebitCard.setCvv(cardLinkingRequest.getCvv());
            tblAccountDebitCard.setIsActive(Constants.YES);
            LkpBank lkpBank = lkpBankRepo.findById(cardLinkingRequest.getBankId()).orElseThrow(() -> new CustomDataNotFoundException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
            LkpCurrency lkpCurrency = lkpCurrencyRepo.findById(cardLinkingRequest.getCurrencyId()).orElseThrow(() -> new CustomDataNotFoundException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
            tblAccountDebitCard.setLkpCurrency(lkpCurrency);
            tblAccountDebitCard.setLkpBank(lkpBank);

            tblAccountDebitCardRepo.saveAndFlush(tblAccountDebitCard);
            CardLinkingResponse dto = new CardLinkingResponse();
            dto.setAccountDebitCardId(tblAccountDebitCard.getAccountDebitCardId());
            dto.setAccountNo(tblAccountDebitCard.getAccountNo());

            // Map bank name (if available)
            if (tblAccountDebitCard.getLkpBank() != null) {
                dto.setBankName(tblAccountDebitCard.getLkpBank().getBankName()); // adjust getter name
            }

            dto.setCreateDate(tblAccountDebitCard.getCreatedate());

            // Map currencyCode (if available in currency lookup)
            if (tblAccountDebitCard.getLkpCurrency() != null) {
                // Assuming you want a direct code, not numeric ID
                dto.setCurrencyName(tblAccountDebitCard.getLkpCurrency().getCurrencyDescr());
            }

            dto.setCvv(tblAccountDebitCard.getCvv());
            dto.setExpiryDate(tblAccountDebitCard.getExpiryDate());
            dto.setIsActive(tblAccountDebitCard.getIsActive());
            dto.setLastUpdateDate(tblAccountDebitCard.getLastupdatedate());
            dto.setLastUpdateUser(tblAccountDebitCard.getLastupdateuser());
            dto.setLinkedCard(tblAccountDebitCard.getLinkedCard());
            dto.setNameOnCard(tblAccountDebitCard.getNameOnCard());
            dto.setPan(tblAccountDebitCard.getPan());
            dto.setRenewalChargesCount(tblAccountDebitCard.getRenewalChargesCount());
            dto.setUpdateIndex(tblAccountDebitCard.getUpdateindex());
            if (tblAccountDebitCard.getTblAccount() != null) {
                dto.setAccountId(tblAccountDebitCard.getTblAccount().getAccountId()); // adjust getter name
            }
            dto.setIsDefault(tblAccountDebitCard.getIsDefault());
            dto.setCardTitle(responseDto.getData().getCardTitle());
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), dto);
        } else {
            return commonService.getResponse(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode(), null);
        }

    }

    @Override
    public HashMap<String, Object> getAllLinkingCards(GetCardLinkingRequest getCardLinkingRequest, Request request, String authorization, BigDecimal userId) {
        TblAccount tblAccount = tblAccountRepo.getAccountByAccountNumberAndAccountType(getCardLinkingRequest.getAccountNo(), accountTypeWallet);
        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_ACCOUNT.getResponseCode());
        }
        List<TblAccountDebitCard> getAllActiveLinkedCards=tblAccountDebitCardRepo.findAllByTblAccountAccountIdAndIsActive(tblAccount.getAccountId(),Constants.YES);
        if(isNullOrEmpty(getAllActiveLinkedCards)){
            return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
        }
        List<CardLinkingResponse> response=new ArrayList<>();
        for(TblAccountDebitCard tblAccountDebitCard:getAllActiveLinkedCards){
            CardLinkingResponse dto = new CardLinkingResponse();
            dto.setAccountDebitCardId(tblAccountDebitCard.getAccountDebitCardId());
            dto.setAccountNo(tblAccountDebitCard.getAccountNo());

            // Map bank name (if available)
            if (tblAccountDebitCard.getLkpBank() != null) {
                dto.setBankName(tblAccountDebitCard.getLkpBank().getBankName()); // adjust getter name
            }

            dto.setCreateDate(tblAccountDebitCard.getCreatedate());

            // Map currencyCode (if available in currency lookup)
            if (tblAccountDebitCard.getLkpCurrency() != null) {
                // Assuming you want a direct code, not numeric ID
                dto.setCurrencyName(tblAccountDebitCard.getLkpCurrency().getCurrencyDescr());
            }
            dto.setBankId(tblAccountDebitCard.getLkpBank().getBankId());
            dto.setCvv(tblAccountDebitCard.getCvv());
            dto.setExpiryDate(tblAccountDebitCard.getExpiryDate());
            dto.setIsActive(tblAccountDebitCard.getIsActive());
            dto.setLastUpdateDate(tblAccountDebitCard.getLastupdatedate());
            dto.setLastUpdateUser(tblAccountDebitCard.getLastupdateuser());
            dto.setLinkedCard(tblAccountDebitCard.getLinkedCard());
            dto.setNameOnCard(tblAccountDebitCard.getNameOnCard());
            dto.setPan(tblAccountDebitCard.getPan());
            dto.setRenewalChargesCount(tblAccountDebitCard.getRenewalChargesCount());
            dto.setUpdateIndex(tblAccountDebitCard.getUpdateindex());
            if (tblAccountDebitCard.getTblAccount() != null) {
                dto.setAccountId(tblAccountDebitCard.getTblAccount().getAccountId()); // adjust getter name
            }
            dto.setIsDefault(tblAccountDebitCard.getIsDefault());
            response.add(dto);
        }
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), response);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public HashMap<String, Object> setCardDefault(SetDefaultRequest setDefaultRequest,
                                                  Request request,
                                                  String authorization,
                                                  BigDecimal userId) {

        // Check account exists
        TblAccount tblAccount = tblAccountRepo.getAccountByAccountNumberAndAccountType(
                setDefaultRequest.getAccountNo(),
                accountTypeWallet
        );

        if (tblAccount == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_ACCOUNT.getResponseCode());
        }

        // Fetch all active linked cards
        List<TblAccountDebitCard> getAllActiveLinkedCards =
                tblAccountDebitCardRepo.findAllByTblAccountAccountIdAndIsActive(
                        tblAccount.getAccountId(),
                        Constants.YES
                );

        if (isNullOrEmpty(getAllActiveLinkedCards)) {
            return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
        }

        // Update all cards in one pass, and find the default card
        TblAccountDebitCard defaultCard = null;
        for (TblAccountDebitCard card : getAllActiveLinkedCards) {
            if (card.getAccountDebitCardId()==setDefaultRequest.getCardId()) {
                card.setIsDefault(setDefaultRequest.getIsDefault());
                defaultCard = card;
            }
            card.setLastupdatedate(new Date());
            card.setLastupdateuser(userId);
            card.setUpdateindex(card.getUpdateindex()==null?BigDecimal.ONE:card.getUpdateindex().add(BigDecimal.ONE));
            tblAccountDebitCardRepo.saveAndFlush(card); // persist changes
        }

        // If no matching card found
        if (defaultCard == null) {
            return commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), null);
        }

        // Map to DTO
        CardLinkingResponse dto = new CardLinkingResponse();
        dto.setAccountDebitCardId(defaultCard.getAccountDebitCardId());
        dto.setAccountNo(defaultCard.getAccountNo());

        if (defaultCard.getLkpBank() != null) {
            dto.setBankName(defaultCard.getLkpBank().getBankName());
        }
        dto.setCreateDate(defaultCard.getCreatedate());

        if (defaultCard.getLkpCurrency() != null) {
            dto.setCurrencyName(defaultCard.getLkpCurrency().getCurrencyDescr());
        }

        dto.setCvv(defaultCard.getCvv());
        dto.setExpiryDate(defaultCard.getExpiryDate());
        dto.setIsActive(defaultCard.getIsActive());
        dto.setLastUpdateDate(defaultCard.getLastupdatedate());
        dto.setLastUpdateUser(defaultCard.getLastupdateuser());
        dto.setLinkedCard(defaultCard.getLinkedCard());
        dto.setNameOnCard(defaultCard.getNameOnCard());
        dto.setPan(defaultCard.getPan());
        dto.setRenewalChargesCount(defaultCard.getRenewalChargesCount());
        dto.setUpdateIndex(defaultCard.getUpdateindex());

        if (defaultCard.getTblAccount() != null) {
            dto.setAccountId(defaultCard.getTblAccount().getAccountId());
        }

        dto.setIsDefault(defaultCard.getIsDefault()); // should be "Y"

        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), dto);
    }

    @Override
    public HashMap<String, Object> deLinkCard(SetDefaultRequest setDefaultRequest, Request request, String authorization, BigDecimal userId) {
        // Check account exists
        TblAccount tblAccount = tblAccountRepo.getAccountByAccountNumberAndAccountType(
                setDefaultRequest.getAccountNo(),
                accountTypeWallet
        );
        if(tblAccount==null){

            throw new CustomDataNotFoundException(GenericResponseCode.INVALID_ACCOUNT.getResponseCode());
        }
        TblAccountDebitCard tblAccountDebitCard=tblAccountDebitCardRepo.findById(setDefaultRequest.getCardId()).orElseThrow(()->new CustomDataNotFoundException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode()));
        tblAccountDebitCard.setIsActive(Constants.NOT);
        tblAccountDebitCard.setIsDefault(Constants.NOT);
        tblAccountDebitCard.setLastupdateuser(userId);
        tblAccountDebitCard.setLastupdatedate(new Date());
        tblAccountDebitCardRepo.saveAndFlush(tblAccountDebitCard);
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), null);
    }
}
