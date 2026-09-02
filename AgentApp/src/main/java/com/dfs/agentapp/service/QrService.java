package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.DecodeQrRequest;
import com.dfs.agentapp.dto.QrRequest;
import com.dfs.agentapp.dto.common.Request;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;

public interface QrService {
    void generateStaticQrForP2P(String mobileNumber, String accountLevelCode);

    HashMap<String, Object> generateQr(QrRequest qrRequest, BigDecimal userId, Request request) throws ParseException;

    HashMap<String, Object> decodeQr(DecodeQrRequest decodeQrRequest, BigDecimal userId, Request request) throws ParseException;

    void generateCustomerStaticQrForP2P(String accountNo, String accountLevelCode);
}
