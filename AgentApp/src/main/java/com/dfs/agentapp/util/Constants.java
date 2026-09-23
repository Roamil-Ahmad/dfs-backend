package com.dfs.agentapp.util;

public interface Constants {
    public static String EMPTY="";
    public static final String ACCEPT = "accept";
    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTENT_TYPE = "content-type";
    public static final String AUTHORIZATION = "Authorization";
    public static final String YES = "Y";

    /**
     * TBL_AGENT.AGENT_TYPE. C marks an agent that sits under a parent, P one that does not.
     *
     * <p>The same letters backoffice writes, so an agent is typed the same whichever service
     * created it.</p>
     */
    public static final String AGENT_TYPE_CHILD = "C";
    public static final String AGENT_TYPE_PARENT = "P";
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
