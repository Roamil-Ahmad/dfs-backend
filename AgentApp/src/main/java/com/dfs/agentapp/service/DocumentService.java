package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.UploadDocumentRequest;
import com.dfs.agentapp.dto.common.Request;

import java.math.BigDecimal;
import java.util.HashMap;

public interface DocumentService {
    HashMap<String, Object> uploadDocument(UploadDocumentRequest uploadDocumentRequest, Request request, BigDecimal userId);

    HashMap<String, Object> uploadDocument(UploadDocumentRequest uploadDocumentRequest, Request request, BigDecimal userId, boolean isAgent);
}
