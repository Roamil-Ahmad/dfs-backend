package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.dto.QrRequest;
import com.dfs.backoffice.dto.QrResponse;
import com.dfs.backoffice.model.TblAccount;
import com.dfs.backoffice.repo.TblAccountRepo;
import com.dfs.backoffice.service.CommonService;
import com.dfs.backoffice.service.QrService;
import com.dfs.backoffice.utils.*;
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


    @Override
    public void generateStaticQrForP2P(TblAccount tblAccount) {

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
