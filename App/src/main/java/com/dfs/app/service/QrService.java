package com.dfs.app.service;

import com.dfs.app.dto.DecodeQrRequest;
import com.dfs.app.dto.QrRequest;
import com.dfs.app.dto.common.Request;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;

public interface QrService {
    void generateStaticQrForP2P(String mobileNumber, String accountLevelCode);

    HashMap<String, Object> generateQr(QrRequest qrRequest, BigDecimal userId, Request request) throws ParseException;

    HashMap<String, Object> decodeQr(DecodeQrRequest decodeQrRequest, BigDecimal userId, Request request) throws ParseException;
}
