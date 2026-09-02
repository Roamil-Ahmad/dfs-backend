package com.dfs.agentapp.util;

public interface Constants {
    public static String EMPTY="";
    public static final String ACCEPT = "accept";
    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTENT_TYPE = "content-type";
    public static final String AUTHORIZATION = "Authorization";
    public static final String YES = "Y";

    /**
     * TBL_AGENT.AGENT_TYPE. Selected purely by whether the KYC request carried a parent agent id:
     * one supplied gives P, none gives C. Named after that condition rather than after a meaning
     * for the letters, which the request does not define.
     */
    public static final String AGENT_TYPE_WITH_PARENT = "P";
    public static final String AGENT_TYPE_STANDALONE = "C";
    public static final String BACK_SLASH = "/";
    public static final String PRE_LOGIN ="P" ;
    public static final String AFTER_LOGIN ="A" ;
    public static final String N ="N" ;
    public static final String LOG_OUT_TIME ="LOGOUT_TIME" ;
    String S = "S";
    String MOBILE = "M";
    String TABLE_NAME_AGENT = "TBL_AGENT";
    String FORM_NAME_AGENT = "Create Agent";
    String REQUEST_TYPE_SAVE = "I";
    String NOT_MAKER_CHECKER = "102010201020";

    String VERIFIED = "V";
}
