/*Author Name:muhammad.kashif
Project Name: makerchecker
Package Name:com.workflow.makerchecker.service.impl
Class Name: makerCheckerImpl
Date and Time:2/17/2023 10:53 AM
Version:1.0*/
package com.workflow.service.impl;

import com.workflow.dto.*;
import com.workflow.modal.LkpStatus;
import com.workflow.modal.TblMcConfig;
import com.workflow.modal.TblMcRequest;
import com.workflow.repo.*;
import com.workflow.service.WorkflowService;
import com.workflow.utils.Constants;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class WorkflowImpl implements WorkflowService {

    @PersistenceContext
    EntityManager em;
    @Autowired
    private TblMcRequestRepo tblMcRequestRepo;
    @Autowired
    private TblMcConfigRepo tblMcConfigRepo;
    @Autowired
    private LkpStatusRepo lkpStatusRepo;

    @Override
    public ProcedureResponse checkMcApplicability(String tableName, String formName, String requestType) {
        try {

            Session session = em.unwrap(Session.class);
            final int[] mcApplicability = new int[1];
            final int[] status = new int[1];
            final String[] statusDescr = new String[1];
            ProcedureResponse responce = new ProcedureResponse();
            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection
                            .prepareCall("{call PKG_MKRCKR.CHECK_MC_APPLICABLE(?,?,?,?,?,?) }");
                    call.setString(1, tableName);
                    call.setString(2, formName);
                    call.setString(3, requestType);
                    call.registerOutParameter(4, Types.INTEGER);
                    call.registerOutParameter(5, Types.INTEGER);
                    call.registerOutParameter(6, Types.VARCHAR);
                    call.execute();
                    mcApplicability[0] = call.getInt(4);
                    status[0] = call.getInt(5);
                    statusDescr[0] = call.getString(6);
                    responce.setMcApplicability(mcApplicability[0]);
                    responce.setStatus(status[0]);
                    responce.setStatusDescr(statusDescr[0]);
                }
            });
            if (status != null) {
                return responce;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return Constants.PROC_UNHANDLE_EXCEPTION;
    }

    @Override
    public McResponse parkRequestToChecker(String formName, String makerId, String makerComments, String ftFlag, String tableName, String requestType, String updateType, String refTableId, String updateJson, BigDecimal userId, String oldJson) {
        try {
            final int[] pendingRequestId = new int[1];

            Session session = em.unwrap(Session.class);
            final int[] status = new int[1];
            final String[] statusDescr = new String[1];
            McResponse responce = new McResponse();
            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection
                            .prepareCall("{call PKG_MKRCKR.MC_REQUEST(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                    call.setString(1, formName);
                    call.setBigDecimal(2, userId);
                    call.setString(3, "");
                    call.setString(4, tableName);
                    call.setString(5, requestType);
                    call.setString(6, updateType);
                    call.setString(7, updateJson);
                    call.setBigDecimal(8, new BigDecimal(refTableId));
                    call.setString(9, "");
                    call.setString(10, oldJson);
                    call.registerOutParameter(11, Types.INTEGER);
                    call.registerOutParameter(12, Types.INTEGER);
                    call.registerOutParameter(13, Types.VARCHAR);
                    call.execute();
                    pendingRequestId[0] = call.getInt(11);
                    status[0] = call.getInt(12);
                    statusDescr[0] = call.getString(13);
                    responce.setStatus(status[0]);
                    responce.setStatusDecsr(statusDescr[0]);
                }
            });
            if (status != null) {
                return responce;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return Constants.RES_UNHANDLE_EXCEPTION;
    }

    @Override
    public McActionResponse mcAction(McActionRequest mcActionRequest) {
        try {

            Session session = em.unwrap(Session.class);
            final int[] status = new int[1];
            final String[] requestStatus = new String[1];
            final int[] pendingRequestId = new int[1];
            final String[] statusDescr = new String[1];
            McActionResponse responce = new McActionResponse();
            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection
                            .prepareCall("{call PKG_MKRCKR.MC_ACTION(?,?,?,?,?,?,?,?,?,?)}");
                    call.setInt(1, Integer.parseInt(mcActionRequest.getMcRequestId()));
                    call.setInt(2, Integer.parseInt(mcActionRequest.getMcPeindingRequestId()));
                    call.setInt(3, Integer.parseInt(mcActionRequest.getCheckerId()));
                    call.setString(4, mcActionRequest.getCheckerComments());
                    call.setString(5, mcActionRequest.getAction());
                    call.setBigDecimal(6, mcActionRequest.getUpdatedIndex());
                    call.registerOutParameter(7, Types.NVARCHAR);
                    call.registerOutParameter(8, Types.NVARCHAR);
                    call.registerOutParameter(9, Types.INTEGER);
                    call.registerOutParameter(10, Types.NVARCHAR);
                    call.execute();
                    pendingRequestId[0] = call.getInt(7);
                    requestStatus[0] = call.getString(8);
                    status[0] = call.getInt(9);
                    statusDescr[0] = call.getString(10);
                    responce.setRequestStatus(requestStatus[0]);
                    responce.setStatus(status[0]);
                    responce.setStatusDecsr(statusDescr[0]);
                }
            });
            if (status != null) {
                return responce;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return Constants.ACT_UNHANDLE_EXCEPTION;
    }


    @Override
    public List<McRequestResponse> getAllMcRequestAgainstUserId(String userId) {
        List<Object> objectList = tblMcRequestRepo.getAllMcRequestAgainstAppUserId(userId);
        List<McRequestResponse> mcRequestResponses = new ArrayList<>();
        McRequestResponse mcRequestResponse = null;
        if (objectList != null && objectList.size() > 0) {
            for (Object record : objectList) {
                mcRequestResponse = new McRequestResponse();
                Object[] row = (Object[]) record;
                if (row[0] != null) {
                    mcRequestResponse.setRequestId((BigDecimal) row[0]);
                }
                if ((String) row[1] != null) {
                    mcRequestResponse.setFormName((String) row[1]);
                }
                if ((String) row[2] != null) {
                    mcRequestResponse.setMakeName((String) row[2]);
                }
                if ((String) row[3] != null) {
                    mcRequestResponse.setMakeDate((String) row[3]);
                }
                if ((String) row[4] != null) {
                    mcRequestResponse.setMakerComments((String) row[4]);
                }
                if ((String) row[5] != null) {
                    mcRequestResponse.setStatus((String) row[5]);
                }
                if (row[6] != null) {
                    mcRequestResponse.setUpdateIndex((BigDecimal) row[6]);
                }
                mcRequestResponses.add(mcRequestResponse);
            }
            return mcRequestResponses;
        }
        return mcRequestResponses;
    }

    @Override
    public List<McPendingRequestResponse> getPendigMcRequestAgainstUserId(String userId) {
        List<Object> objectList = tblMcRequestRepo.getPendingMcRequestAgainstAppUserId(userId);
        List<McPendingRequestResponse> mcPendingRequestResponses = new ArrayList<>();
        McPendingRequestResponse mcPendingRequestResponse = null;
        if (objectList != null && objectList.size() > 0) {
            for (Object record : objectList) {
                mcPendingRequestResponse = new McPendingRequestResponse();
                Object[] row = (Object[]) record;
                if (row[0] != null) {
                    mcPendingRequestResponse.setRequestId((BigDecimal) row[0]);
                }
                if (row[1] != null) {
                    mcPendingRequestResponse.setFormName((String) row[1]);
                }
                if (row[2] != null) {
                    mcPendingRequestResponse.setMakeName((String) row[2]);
                }
                if (row[3] != null) {
                    mcPendingRequestResponse.setMakeDate((String) row[3]);
                }
                if (row[4] != null) {
                    mcPendingRequestResponse.setMakerComments((String) row[4]);
                }
                if (row[5] != null) {
                    mcPendingRequestResponse.setStatus((String) row[5]);
                }
                if (row[6] != null) {
                    mcPendingRequestResponse.setUpdateIndex((BigDecimal) row[6]);
                }
                if (row[7] != null) {
                    mcPendingRequestResponse.setMcPendingRequestId((BigDecimal) row[7]);
                }
                if (row[8] != null) {
                    mcPendingRequestResponse.setRefTableId((BigDecimal) row[8]);
                }
                if (row[9] != null) {
                    mcPendingRequestResponse.setUrl((String) row[9]);
                }
                mcPendingRequestResponses.add(mcPendingRequestResponse);
            }
            return mcPendingRequestResponses;
        } else {
            return mcPendingRequestResponses;
        }
    }

    @Override
    public List<McApprovedRequestResponse> getApprovedMcRequestAgainstUserId(String userId) {
        List<Object> objectList = tblMcRequestRepo.getApprovedMcRequestAgainstAppUserId(userId);
        List<McApprovedRequestResponse> mcApprovedRequestResponses = new ArrayList<>();
        McApprovedRequestResponse mcApprovedRequestResponse = null;
        if (objectList != null && objectList.size() > 0) {
            for (Object record : objectList) {
                mcApprovedRequestResponse = new McApprovedRequestResponse();
                Object[] row = (Object[]) record;
                if (row[0] != null) {
                    mcApprovedRequestResponse.setFormName((String) row[0]);
                }
                if (row[1] != null) {
                    mcApprovedRequestResponse.setMakeName((String) row[1]);
                }
                if (row[2] != null) {
                    mcApprovedRequestResponse.setMakeDate((String) row[2]);
                }
                if (row[3] != null) {
                    mcApprovedRequestResponse.setMakerComments((String) row[3]);
                }
                if (row[4] != null) {
                    mcApprovedRequestResponse.setCheckerName((String) row[4]);
                }
                if (row[5] != null) {
                    mcApprovedRequestResponse.setCheckDate((String) row[5]);
                }
                if (row[6] != null) {
                    mcApprovedRequestResponse.setCheckerComments((String) row[6]);
                }
                if (row[7] != null) {
                    mcApprovedRequestResponse.setStatus((String) row[7]);
                }

                mcApprovedRequestResponses.add(mcApprovedRequestResponse);
            }
            return mcApprovedRequestResponses;
        } else {
            return mcApprovedRequestResponses;
        }
    }

    @Override
    public List<McRejectRequestResponse> getRejectedMcRequestAgainstUserId(String userId) {
        List<Object> objectList = tblMcRequestRepo.getRejectedMcRequestAgainstAppUserId(userId);
        List<McRejectRequestResponse> mcRejectRequestResponses = new ArrayList<>();
        McRejectRequestResponse mcRejectRequestResponse = null;
        if (objectList != null && objectList.size() > 0) {
            for (Object record : objectList) {
                mcRejectRequestResponse = new McRejectRequestResponse();
                Object[] row = (Object[]) record;
                if (row[0] != null) {
                    mcRejectRequestResponse.setFormName((String) row[0]);
                }
                if (row[1] != null) {
                    mcRejectRequestResponse.setMakeName((String) row[1]);
                }
                if (row[2] != null) {
                    mcRejectRequestResponse.setMakeDate((String) row[2]);
                }
                if (row[3] != null) {
                    mcRejectRequestResponse.setMakerComments((String) row[3]);
                }
                if (row[4] != null) {
                    mcRejectRequestResponse.setCheckerName((String) row[4]);
                }
                if (row[5] != null) {
                    mcRejectRequestResponse.setCheckDate((String) row[5]);
                }
                if (row[6] != null) {
                    mcRejectRequestResponse.setCheckerComments((String) row[6]);
                }
                if (row[7] != null) {
                    mcRejectRequestResponse.setStatus((String) row[7]);
                }

                mcRejectRequestResponses.add(mcRejectRequestResponse);
            }
            return mcRejectRequestResponses;
        } else {
            return mcRejectRequestResponses;
        }
    }

    @Override
    public List<CheckerSearchResponse> getallmyauthorizationsrequests(CheckerSearch checkerSearch, BigDecimal userId) throws SQLException {
        String dateFromInput = Constants.UNHANDLE_EXCEPTION;
        String dateToInput = Constants.UNHANDLE_EXCEPTION;
        if (checkerSearch.getFromDate() != null && !(checkerSearch.getFromDate().equals(Constants.EMPTY))) {
            dateFromInput = checkerSearch.getFromDate() + " 00:00:00";
        }
        if (checkerSearch.getToDate() != null && !(checkerSearch.getToDate().equals(Constants.EMPTY))) {
            dateToInput = checkerSearch.getToDate() + " 23:59:59";
        }
        List<CheckerSearchResponse> checkerSearchResponseArrayList = new ArrayList<CheckerSearchResponse>();
        CheckerSearchResponse checkerSearchResponse = null;
        String sql = "SELECT R.MC_REQUEST_ID, R.REF_TABLE_ID, R.ACTION_ID, R.FORM_NAME, U.EMPLOYEE_NAME, P.CREATEDATE MAKE_DATE, NVL(S.STATUS_DESCR,SP.STATUS_DESCR) STATUS_DESCR, R.TABLE_NAME, R.REQUEST_TYPE,\n" +
                "       DECODE(NVL(R.UPDATE_TYPE,R.REQUEST_TYPE),'I','Create Request','U','Update Request','A','Enable/Disable Request') REQUEST_TYPE_DESCR, \n" +
                "       P.MC_PENDING_REQUEST_ID,A.CHECKER_COMMENTS , G.VIEW_DETAIL_URL, G.EDIT_DETAIL_URL, P.ACTION_TAKEN, P.SEQ\n" +
                "  FROM TBL_MC_REQUEST R\n" +
                " INNER JOIN TBL_MC_PENDING_REQUEST P ON R.MC_REQUEST_ID = P.MC_REQUEST_ID\n" +
                " INNER JOIN TBL_APP_USER AU ON P.USER_ID = AU.USER_ID\n" +
                " INNER JOIN LKP_STATUS SP ON R.STATUS_ID = SP.STATUS_ID \n" +
                " INNER JOIN TBL_MC_CONFIG G ON R.MC_CONFIG_ID = G.MC_CONFIG_ID\n" +
                " INNER JOIN TBL_USER U ON R.MAKER_ID = U.USER_ID\n" +
                "  LEFT OUTER JOIN TBL_MC_REQUEST_ACTION A ON P.MC_PENDING_REQUEST_ID = A.MC_PENDING_REQUEST_ID   --R.MC_REQUEST_ID = A.MC_REQUEST_ID\n" +
                "  LEFT  JOIN LKP_STATUS S ON A.STATUS_ID = S.STATUS_ID\n" +
                " WHERE R.ACTION_ID = NVL(?,R.ACTION_ID)\n" +
                "   AND R.FORM_NAME = NVL(?,R.FORM_NAME)\n" +
                "   AND R.REF_CREATEDATE BETWEEN NVL(TO_DATE(?,'YYYY-MM-DD HH24:MI:SS'),R.REF_CREATEDATE) AND NVL(TO_DATE(?,'YYYY-MM-DD HH24:MI:SS'),R.REF_CREATEDATE)\n" +
                "   AND NVL(A.STATUS_ID,R.STATUS_ID) = NVL(?,NVL(A.STATUS_ID,R.STATUS_ID))\n" +
                "   AND R.REF_CREATEUSER = NVL(?,R.REF_CREATEUSER)\n" +
                "   AND NVL(R.REF_UPDATEUSER,0) = NVL(?,NVL(R.REF_UPDATEUSER,0))\n" +
                "   AND (AU.APP_USER_ID = ?\n" +
                "       OR P.ROLE_ID IN (SELECT ROLE_ID   \n" +
                "                          FROM TBL_USER_ROLE R\n" +
                "                         INNER JOIN TBL_APP_USER AU ON R.USER_ID = AU.USER_ID    \n" +
                "                         WHERE AU.APP_USER_ID = ?))\n" +
                "ORDER BY P.MC_PENDING_REQUEST_ID";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, checkerSearch.getActionId());
        query.setParameter(2, checkerSearch.getFormName());
        query.setParameter(3, dateFromInput);
        query.setParameter(4, dateToInput);
        query.setParameter(5, checkerSearch.getStatusId());
        query.setParameter(6, checkerSearch.getCreatedBy());
        query.setParameter(7, checkerSearch.getUpdatedBy());
        query.setParameter(8, userId);
        query.setParameter(9, userId);
        List<Object> resultList = (List<Object>) query.getResultList();
        int count = 0;
        if (resultList != null && resultList.size() > 0) {
            for (Object record : resultList) {
                checkerSearchResponse = new CheckerSearchResponse();
                Object[] row = (Object[]) record;

                if ((row[0]) != null) {
                    checkerSearchResponse.setMcRequestId(((BigDecimal) row[0]));
                }
                if ((row[1]) != null) {
                    checkerSearchResponse.setRefTableId(((BigDecimal) row[1]));
                }
                if ((row[2]) != null) {
                    checkerSearchResponse.setActionId(((String) row[2]));
                }
                if ((row[3]) != null) {
                    checkerSearchResponse.setFormName(((String) row[3]));
                }
                if ((row[4]) != null) {
                    checkerSearchResponse.setUserName(((String) row[4]));
                }
                if ((row[5]) != null) {
                    checkerSearchResponse.setMakeDate(((Date) row[5]));
                }
                if ((row[6]) != null) {
                    checkerSearchResponse.setStatusDescr(((String) row[6]));
                }
                if ((row[7]) != null) {
                    checkerSearchResponse.setTableName(((String) row[7]));
                }
                if ((row[8]) != null) {
                    checkerSearchResponse.setRequestType(((String) row[8]));
                }
                if ((row[9]) != null) {
                    checkerSearchResponse.setRequestTypeDescr(((String) row[9]));
                }
                if ((row[10]) != null) {
                    checkerSearchResponse.setMcPendingRequest(((BigDecimal) row[10]));
                }
                if ((row[11]) != null) {
                    checkerSearchResponse.setCheckerComments(((String) row[11]));
                }
                if ((row[12]) != null) {
                    checkerSearchResponse.setViewDetailUrl(((String) row[12]));
                }
                if ((row[13]) != null) {
                    checkerSearchResponse.setEditDetailUrl(((String) row[13]));
                }
                if ((row[14]) != null) {
                    checkerSearchResponse.setActionTaken(((String) row[14]));
                }
                if ((row[15]) != null) {
                    checkerSearchResponse.setSeq(((BigDecimal) row[15]));
                }
                checkerSearchResponseArrayList.add(checkerSearchResponse);
            }
            if (checkerSearchResponseArrayList.size() > 0) {
                return checkerSearchResponseArrayList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<AuthorizationHistoryResponse> getActionAuthorizationHistory(String refTableId, String tableName) {
        List<AuthorizationHistoryResponse> authorizationHistoryResponses = new ArrayList<AuthorizationHistoryResponse>();
        AuthorizationHistoryResponse authorizationHistoryResponse = new AuthorizationHistoryResponse();
        String sql = "SELECT P.SEQ ESCALATION_LEVEL, UA.EMPLOYEE_NAME CHECKED_BY, TO_CHAR(A.CHECK_DATE,'DD/MM/YYYY, HH:MI AM') CHECKED_ON, A.CHECKER_COMMENTS AUTHORIZER_COMMENTS,\n" +
                "       TO_CHAR(P.CREATEDATE,'DD/MM/YYYY, HH:MI AM') INTIMATION_ON, NVL(RP.ROLE_DESCR, UP.EMPLOYEE_NAME) INTIMATION_TO,\n" +
                "       NVL(SA.STATUS_DESCR,SP.STATUS_DESCR) AUTHORIZATION_STATUS, UR.EMPLOYEE_NAME MAKER, R.MC_REQUEST_ID\n" +
                "  FROM TBL_MC_REQUEST R\n" +
                " INNER JOIN TBL_MC_PENDING_REQUEST P ON R.MC_REQUEST_ID = P.MC_REQUEST_ID\n" +
                " INNER JOIN LKP_STATUS SP ON P.STATUS_ID = SP.STATUS_ID\n" +
                "  LEFT JOIN TBL_USER UP ON P.USER_ID = UP.USER_ID\n" +
                "  LEFT JOIN TBL_ROLE RP     ON P.ROLE_ID = RP.ROLE_ID\n" +
                "  LEFT JOIN TBL_MC_REQUEST_ACTION A ON P.MC_PENDING_REQUEST_ID = A.MC_PENDING_REQUEST_ID\n" +
                "  LEFT JOIN LKP_STATUS SA ON A.STATUS_ID = SA.STATUS_ID\n" +
                "  LEFT JOIN TBL_USER UA ON A.CHECKER_ID = UA.USER_ID\n" +
                "  LEFT JOIN TBL_USER UR ON UR.USER_ID=R.MAKER_ID\n" +
                " WHERE R.REF_TABLE_ID = ?\n" +
                "   AND R.TABLE_NAME = ?\n" +
                "ORDER BY P.MC_PENDING_REQUEST_ID";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, refTableId);
        query.setParameter(2, tableName);
        List<Object> resultList = (List<Object>) query.getResultList();
        int count = 0;
        if (resultList != null && resultList.size() > 0) {
            for (Object record : resultList) {
                authorizationHistoryResponse = new AuthorizationHistoryResponse();
                Object[] row = (Object[]) record;

                if ((row[0]) != null) {
                    authorizationHistoryResponse.setEscalationLevel(((BigDecimal) row[0]));
                }
                if ((row[1]) != null) {
                    authorizationHistoryResponse.setCheckedBy(((String) row[1]));
                }
                if ((row[2]) != null) {
                    authorizationHistoryResponse.setCheckedOn(((String) row[2]));
                }
                if ((row[3]) != null) {
                    authorizationHistoryResponse.setAuthorizerComments(((String) row[3]));
                }
                if ((row[4]) != null) {
                    authorizationHistoryResponse.setIntimationOn(((String) row[4]));
                }
                if ((row[5]) != null) {
                    authorizationHistoryResponse.setIntimationTo(((String) row[5]));
                }
                if ((row[6]) != null) {
                    authorizationHistoryResponse.setAuthorizationStatus(((String) row[6]));
                }
                if ((row[7]) != null) {
                    authorizationHistoryResponse.setMaker(((String) row[7]));
                }
                if ((row[8]) != null) {
                    authorizationHistoryResponse.setMcRequestId(((BigDecimal) row[8]));
                }

                authorizationHistoryResponses.add(authorizationHistoryResponse);
            }
            if (!authorizationHistoryResponses.isEmpty()) {
                return authorizationHistoryResponses;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public TblMcRequest getMcRequestByMcRequestId(String userId) {
        return tblMcRequestRepo.findById(Long.parseLong(userId)).orElse(null);
    }


    @Override
    public List<TblMcConfig> getAllRequestTypes() {
        return tblMcConfigRepo.findByIsActive("Y");
    }

    @Override
    public List<LkpStatus> getAllStatus() {
        return lkpStatusRepo.findByIsActive("Y");
    }
}