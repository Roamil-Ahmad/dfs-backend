package com.workflow.utils;

import com.workflow.dto.McActionResponse;
import com.workflow.dto.McResponse;
import com.workflow.dto.ProcedureResponse;

public class Constants {

    private Constants(){

    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_AUTHENTICATION = "Bearer Authentication";
    public static final String BEARER = "Bearer";
    public static final String WORKFLOW = "workflow";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    public static final String MAX_AGE = "3600";
    public static final String S_1 = "*";
    public static final String S_1_TRUE = "true";
    public static final String EMPTY = "";
    public static final String USER_ID = "userId";
    public static final String EQUALS_IGNORE_CASE = "N";
    public static final String EXPIRED = "Expired";
    public static final int SEVEN = 7;
    public static final String UNHANDLE_EXCEPTION = null;
    public static final ProcedureResponse PROC_UNHANDLE_EXCEPTION = null;
    public static final McResponse RES_UNHANDLE_EXCEPTION = null;
    public static final McActionResponse ACT_UNHANDLE_EXCEPTION = null;
    public static final String GETALLUSECASES = "/getallusecases";
    public static final String GET_ALL_MC_CONFIG_DETAIL = "/getAllMcConfigDetail";
    public static final String GET_MC_CONFIG_AGAINST_ID_MC_CONFIG_ID = "/getMcConfigAgainstId/{mcConfigId}";
    public static final String GET_MC_CONFIG_DETAIL_AGAINST_ID_MC_CONFIG_DETAIL_ID = "/getMcConfigDetailAgainstId/{mcConfigDetailId}";
    public static final String GET_ALL_MC_REQUEST_AGAINST_USER_ID_USER_ID = "/getAllMcRequestAgainstUserId/{userId}";
    public static final String GET_PENDIG_MC_REQUEST_AGAINST_USER_ID_USER_ID = "/getPendigMcRequestAgainstUserId/{userId}";
    public static final String GET_APPROVED_MC_REQUEST_AGAINST_USER_ID = "/approvemyauthorizationrequests/{userId}";
    public static final String GET_ACTION_AUTHORIZATION_HISTORY = "/getActionAuthorizationHistory/{refTableId}/{tableName}";
    public static final String GET_REJECTED_MC_REQUEST_AGAINST_USER_ID = "/getRejectedMcRequestAgainstUserId/{userId}";
    public static final String CKECK_MC_APPLICABLE = "/ckeckMcApplicable";
    public static final String GETALLMYAUTHORIZATIONSREQUESTS = "/getallmyauthorizationsrequests";
    public static final String MC_REQUEST = "/mcRequest";
    public static final String MC_ACTION = "/mcAction";
    public static final String CREATEUSECASE = "/createusecase";
    public static final String UPDATEUSECASE = "/updateusecase";
    public static final String SAVE_MC_CONFIG_DETAIL = "/saveMcConfigDetail";
    public static final String UPDATE_MC_CONFIG_DETAIL = "/updateMcConfigDetail";
    public static final String FIELD_VALIDATION_CODE = "1001";
    public static final String SERACH_VALIDATION = "Please Select any one field";
    public static final String YES = "Y";
    public static final String NO = "N";
    public static final String NOTIFICATION_CONTROLLER = "/v1/notification";
    public static final String GET_NOTIFICATION = "/getnotification/{userId}";
    public static final String MC_REQUEST_FOR_CX_STATUS = "/mcRequestForCxStatus";
    public static final String COMPARE_VALUES_MC_REQUEST_ID = "/compareValues/{mcRequestId}";
    public static final String NOTIFICATION_VIEWED = "/notificationviewed";
}
