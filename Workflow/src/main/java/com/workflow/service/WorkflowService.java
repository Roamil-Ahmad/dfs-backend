package com.workflow.service;

import com.workflow.dto.*;
import com.workflow.modal.LkpStatus;
import com.workflow.modal.TblMcConfig;
import com.workflow.modal.TblMcRequest;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface WorkflowService {

    ProcedureResponse checkMcApplicability(String tableName, String formName, String requestType);

    McResponse parkRequestToChecker(String formName, String makerId, String makerComments, String ftFlag, String tableName, String requestType, String updateType, String refTableId, String updatejson, BigDecimal userId, String oldJson);

    McActionResponse mcAction(McActionRequest request);

    List<McRequestResponse> getAllMcRequestAgainstUserId(String userId);

    List<McPendingRequestResponse> getPendigMcRequestAgainstUserId(String userId);

    List<McApprovedRequestResponse> getApprovedMcRequestAgainstUserId(String userId);

    List<McRejectRequestResponse> getRejectedMcRequestAgainstUserId(String userId);

    List<CheckerSearchResponse> getallmyauthorizationsrequests(CheckerSearch checkerSearch, BigDecimal userId) throws SQLException;

    List<AuthorizationHistoryResponse> getActionAuthorizationHistory(String refTableId, String tableName);

    TblMcRequest getMcRequestByMcRequestId(String userId);

    List<TblMcConfig> getAllRequestTypes();

    List<LkpStatus> getAllStatus();
}
