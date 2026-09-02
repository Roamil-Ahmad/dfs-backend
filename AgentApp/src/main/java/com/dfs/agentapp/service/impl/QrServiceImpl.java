package com.dfs.agentapp.service.impl;

import com.dfs.agentapp.dto.DecodeQrRequest;
import com.dfs.agentapp.dto.QrDecodeResponse;
import com.dfs.agentapp.dto.QrRequest;
import com.dfs.agentapp.dto.QrResponse;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.model.TblAccount;
import com.dfs.agentapp.repo.TblAccountRepo;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.QrService;
import com.dfs.agentapp.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

@Service
public class QrServiceImpl implements QrService {
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AESencryption aeSencryption;


    @Override
    public void generateStaticQrForP2P(String mobileNumber, String accountLevelCode) {

        TblAccount tblAccount = tblAccountRepo.findByMobileNoOrCnicAndAccountLevelCode(aeSencryption.encryptwith256(mobileNumber), Constants.EMPTY,accountLevelCode);
        if(tblAccount==null){
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }


        generateQr(tblAccount);

    }

    @Override
    public HashMap<String, Object> generateQr(QrRequest qrRequest, BigDecimal userId, Request request) throws ParseException {
        TblAccount tblAccount = tblAccountRepo.findByMobileNoOrCnicAndAccountLevelCode(aeSencryption.encryptwith256(qrRequest.getMobileNumber()), Constants.EMPTY,qrRequest.getAccountLevelCode());
        if(tblAccount==null){
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
        QrResponse qrResponse = new QrResponse();
        qrRequest.setAmount(qrRequest.getAmount());


        if (qrRequest.getType().equalsIgnoreCase("P")) {
            String f0 = QrStandards.PAYLOAD_FORMAT_INDICATOR_PTP.getCode() + QrStandards.PAYLOAD_FORMAT_INDICATOR_PTP.getLength() + QrStandards.PAYLOAD_FORMAT_INDICATOR_PTP.getValue();
            String f1 = "";
            if (qrRequest.getAmount() == null || qrRequest.getAmount().isEmpty()) {
                f1 = QrStandards.POINT_OF_INITIATION_DYNAMIC.getCode() + QrStandards.POINT_OF_INITIATION_DYNAMIC.getLength() + QrStandards.POINT_OF_INITIATION_DYNAMIC.getValue();

            } else {
                f1 = QrStandards.POINT_OF_INITIATION_STATIC.getCode() + QrStandards.POINT_OF_INITIATION_STATIC.getLength() + QrStandards.POINT_OF_INITIATION_STATIC.getValue();

            }
            String f2 = QrStandards.PERSON_02.getCode() + QrStandards.PERSON_02.getLength() + QrStandards.PERSON_02.getValue();
            String f4 = QrStandards.PERSON_IBAN.getCode() + QrStandards.PERSON_IBAN.getLength() + tblAccount.getIban();
            String f5 = "";
            String f6 = "";
            String f7="";
            if (qrRequest.getAmount() != null && !qrRequest.getAmount().isEmpty()) {
//                if(Long.valueOf(qrRequest.getAmount())<10){
//                    qrRequest.setAmount("0"+qrRequest.getAmount());
//                }
                f5 = QrStandards.PERSON_AMOUNT.getCode() + (qrRequest.getAmount().length() < 10 ? "0" + qrRequest.getAmount().length() : qrRequest.getAmount().length()) + qrRequest.getAmount();
                String dueDate = "";
                if (qrRequest.getDueDate()==null || qrRequest.getDueDate().isEmpty()) {
                    LocalDate newDate1 = LocalDate.now().plusDays(7);
                    dueDate = DateTimeFormatter.ofPattern("ddMMyyyy").format(newDate1) + "2359";
                } else {

                    Date date=new SimpleDateFormat("yyyy-MM-dd").parse(qrRequest.getDueDate());
                    dueDate = new SimpleDateFormat("ddMMyyyy").format(date) + "2359";

                }
                f6 = QrStandards.PERSON_DUE_DATE_TIME.getCode() + QrStandards.PERSON_DUE_DATE_TIME.getLength() + dueDate;
                if (qrRequest.getAmountAfterDueDate()==null || qrRequest.getAmountAfterDueDate().isEmpty()) {
                   qrRequest.setAmountAfterDueDate(qrRequest.getAmount());
                }
//                else {
//                    if(Long.valueOf(qrRequest.getAmountAfterDueDate())<10){
//                        qrRequest.setAmountAfterDueDate("0"+qrRequest.getAmountAfterDueDate());
//                    }
//                }
                f7 = QrStandards.AMOUNT_AFTER_DUE_DATE.getCode() + (qrRequest.getAmountAfterDueDate().length() < 10 ? "0" + qrRequest.getAmountAfterDueDate().length() : qrRequest.getAmountAfterDueDate().length()) + qrRequest.getAmountAfterDueDate();
            }
            String mainString = f0 + f1 + f2 + f4 + f5 + f6 +f7+ QrStandards.PERSON_CRC.getCode() + QrStandards.PERSON_CRC.getLength();

            String crc = CRCGenerator.generateCRC(0, mainString);
            mainString = mainString + crc;
            qrResponse.setQr(mainString);
            qrResponse.setType("p2p");


        } else {
            throw new CustomException(GenericResponseCode.INVALID_QR_TYPE.getResponseCode());
        }
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), qrResponse);


    }
    @Override
    public HashMap<String, Object> decodeQr(DecodeQrRequest decodeQrRequest, BigDecimal userId, Request request) throws ParseException {
        QrDecodeResponse qrDecodeResponse = new QrDecodeResponse();
        String qr = decodeQrRequest.getQr();
        String f0 = qr.substring(4, 6);
        if (f0.equals(QrStandards.PAYLOAD_FORMAT_INDICATOR_PTP.getValue())) {
            String f1 = qr.substring(10, 12);
            String iban = qr.substring(22, 46);
            qrDecodeResponse.setIban(iban);
            TblAccount tblAccount = tblAccountRepo.findByIban(iban);
            if (tblAccount == null) {
                throw new CustomDataNotFoundException(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
            }
            qrDecodeResponse.setTitle(aeSencryption.decrypt(tblAccount.getAccountTitle()));
            if (f1.equalsIgnoreCase(QrStandards.POINT_OF_INITIATION_STATIC.getValue())) {

                String amountLength = qr.substring(48, 50);
                int dueDateIndex=Integer.valueOf(amountLength)+50;

                String amount = qr.substring(50, dueDateIndex);
//                amount=setDeciaml(amount);
                qrDecodeResponse.setAmount(amount);
                String dueDateLength=qr.substring(dueDateIndex+2,dueDateIndex+4);
                int dueAmountIndex=dueDateIndex+4+Integer.valueOf(dueDateLength);
                String dueDate = qr.substring(dueDateIndex+4, dueAmountIndex);
                qrDecodeResponse.setDueDate(new SimpleDateFormat("ddMMyyyyHHmm").parse(dueDate));
                String dueAmountLength = qr.substring(dueAmountIndex+2, dueAmountIndex+4);
                String dueAmount = qr.substring(dueAmountIndex+4, 4+dueAmountIndex+Integer.valueOf(dueAmountLength));
//                dueAmount=setDeciaml(dueAmount);
                qrDecodeResponse.setAmountAfterDueDate(dueAmount);

            }
        } else {
            // this is merchant Qr
        }
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), qrDecodeResponse);
    }

    @Override
    public void generateCustomerStaticQrForP2P(String mobileNumber, String accountLevelCode) {
        TblAccount tblAccount = tblAccountRepo.findByAccountNoOrCnicAndAccountLevelCode(mobileNumber, Constants.EMPTY, accountLevelCode);
        if(tblAccount==null){
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }


        generateQr(tblAccount);
    }

    private void generateQr(TblAccount tblAccount) {
        String f0 = QrStandards.PAYLOAD_FORMAT_INDICATOR_PTP.getCode() + QrStandards.PAYLOAD_FORMAT_INDICATOR_PTP.getLength() + QrStandards.PAYLOAD_FORMAT_INDICATOR_PTP.getValue();
        String f1 = QrStandards.POINT_OF_INITIATION_DYNAMIC.getCode() + QrStandards.POINT_OF_INITIATION_DYNAMIC.getLength() + QrStandards.POINT_OF_INITIATION_DYNAMIC.getValue();


        String f2 = QrStandards.PERSON_02.getCode() + QrStandards.PERSON_02.getLength() + QrStandards.PERSON_02.getValue();
        String f4 = QrStandards.PERSON_IBAN.getCode() + QrStandards.PERSON_IBAN.getLength() + tblAccount.getIban();
        String f5 = "";
        String f6 = "";

        String mainString = f0 + f1 + f2 + f4 + f5 + f6 + QrStandards.PERSON_CRC.getCode() + QrStandards.PERSON_CRC.getLength();

        String crc = CRCGenerator.generateCRC(0, mainString);
        mainString = mainString + crc;


        tblAccount.setQrCode(mainString);
        tblAccountRepo.saveAndFlush(tblAccount);
    }
}
