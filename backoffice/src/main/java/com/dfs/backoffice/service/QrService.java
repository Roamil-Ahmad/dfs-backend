package com.dfs.backoffice.service;


import com.dfs.backoffice.dto.QrRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.TblAccount;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;

public interface QrService {
    void generateStaticQrForP2P(TblAccount tblAccount);
}
