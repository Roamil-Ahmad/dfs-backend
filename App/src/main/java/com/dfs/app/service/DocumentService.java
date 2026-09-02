package com.dfs.app.service;

import com.dfs.app.dto.UploadDocumentRequest;
import com.dfs.app.dto.common.Request;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface DocumentService {
    HashMap<String, Object> uploadDocument(UploadDocumentRequest uploadDocumentRequest, Request request, BigDecimal userId);
}
