package com.dfs.backoffice.repo;

import com.dfs.backoffice.dto.SearchAgent;
import com.dfs.backoffice.dto.SearchAgentResponse;
import com.dfs.backoffice.model.TblAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TblAgentRepo extends JpaRepository<TblAgent, Long> {

    @Query(value = "SELECT * FROM TBL_AGENT WHERE NID_NO = :agentNidNo OR MOBILE = :agentMobileNo", nativeQuery = true)
    List<TblAgent> checkAgentExistance(String agentNidNo, String agentMobileNo);

    @Query(value = "SELECT *  FROM TBL_AGENT  WHERE IS_ACTIVE = 'Y' AND STATUS_ID = 2  AND AGENT_TYPE IN ('P','C') ", nativeQuery = true)
    List<TblAgent> getParentAgents();

    @Query(value = "SELECT  NVL(AGENT_LEVEL,0) + 1 AGENT_LEVEL\n" +
            "   FROM TBL_AGENT  " +
            "  WHERE AGENT_ID =:id ", nativeQuery = true)
    int getAgentLevel(String id);

    @Query(value = "SELECT * \n" +
            "              FROM TBL_AGENT\n" +
            "             WHERE NID_NO = NVL(:#{#searchAgent.cnic}, NID_NO)\n" +
            "               AND MOBILE = NVL(:#{#searchAgent.mobileNo}, MOBILE)\n" +
            "               AND AGENT_CLASS_ID = NVL(:#{#searchAgent.agentClassId}, AGENT_CLASS_ID)\n" +
            "               AND CAST(CREATEDATE AS DATE) BETWEEN NVL(TO_DATE(:dateFromInput,'YYYY-MM-DD HH24:MI:SS'), CAST(CREATEDATE AS DATE))\n" +
            "                                                  AND NVL(TO_DATE(:dateToInput,'YYYY-MM-DD HH24:MI:SS'), CAST(CREATEDATE AS DATE))", nativeQuery = true)
    List<TblAgent> getAllAgents(SearchAgent searchAgent, String dateFromInput, String dateToInput);

        @Query(
            value =
                "SELECT " +
                    " G.AGENT_ID AS agentId, " +
                    " G.NAME AS name, " +
                    " G.FATHER_HUSBAND_NAME AS fatherHusbandName, " +
                    " G.GRANDFATHER_NAME AS grandfatherName, " +
                    " G.NID_NO AS nidNo, " +
                    " G.DOB AS dob, " +
                    " G.EMAIL AS email, " +
                    " G.RESIDENTIAL_ADDRESS AS residentialAddress, " +
                    " G.PERMANENT_ADDRESS AS permanentAddress, " +
                    " G.CREATEDATE AS createDate, " +
                    " G.GENDER AS gender, " +
                    " G.IS_ACTIVE AS isActive, " +
                    " G.POB AS pob, " +
                    " G.NID_EXPIRY_DATE AS nidExpiryDate, " +
                    " G.NID_ISSUE_DATE AS nidIssueDate, " +
                    " A.ACCOUNT_ID AS accountId, " +
                    " L.ACCOUNT_LEVEL_DESCR AS accountLevelDescr, " +
                    " S.ACCOUNT_STATUS_DESCR AS accountStatusDescr " +
                    "FROM TBL_AGENT G " +
                    "INNER JOIN TBL_ACCOUNT A ON G.AGENT_ID = A.AGENT_ID " +
                    "INNER JOIN LKP_ACCOUNT_STATUS S ON A.ACCOUNT_STATUS_ID = S.ACCOUNT_STATUS_ID " +
                    "INNER JOIN TBL_ACCOUNT_LEVEL L ON A.ACCOUNT_LEVEL_ID = L.ACCOUNT_LEVEL_ID " +
                    "WHERE A.MOBILE_NO = NVL(:mobileNo, A.MOBILE_NO) " +
                    "  AND G.NID_NO = NVL(:nidNo, G.NID_NO) " +
                    "  AND UPPER(DECRYPT_DATA(G.NAME)) LIKE '%' || UPPER(:fullName) || '%' " +
                    "  AND A.ACCOUNT_STATUS_ID = NVL(:accountStatusId, A.ACCOUNT_STATUS_ID) " +
                    "  AND CAST(A.CREATEDATE AS DATE) BETWEEN " +
                    "        NVL(TO_DATE(:dateFromInput,'YYYY-MM-DD HH24:MI:SS'), CAST(A.CREATEDATE AS DATE)) " +
                    "    AND NVL(TO_DATE(:dateToInput,'YYYY-MM-DD HH24:MI:SS'), CAST(A.CREATEDATE AS DATE))",
            nativeQuery = true
        )
        List<Object[]> searchAgents(
            @Param("mobileNo") String mobileNo,
            @Param("nidNo") String nidNo,
            @Param("fullName") String fullName,
            @Param("accountStatusId") String accountStatusId,
            @Param("dateFromInput") String dateFromInput,
            @Param("dateToInput") String dateToInput
        );

    @Query(value = "SELECT * \n" +
        "  FROM TBL_AGENT A\n" +
        "   WHERE A.NID_NO = NVL(:nidNo, A.NID_NO)", nativeQuery = true)
    TblAgent findAgentByNidNo(String nidNo);

    @Query(value = "SELECT A.AGENT_ID, A.FATHER_HUSBAND_NAME, A.RESIDENTIAL_ADDRESS, A.CITY_ID, C.CITY_DESCR, A.DISTRICT_ID, D.DISTRICT_DESCR, A.PROVINCE_ID, P.PROVINCE_DESCR,\n" +
            "       A.EMAIL, A.NOK, A.NAME, A.OCCUPATION, A.PERMANENT_ADDRESS, A.NID_NO, A.PHONE, A.MOBILE, A.PARENT_AGENT_ID, A.AGENT_CODE, A.AGENT_TYPE, A.IS_ACTIVE,\n" +
            "       A.STATUS_ID, A.CREATEUSER, A.CREATEDATE, A.POB, A.NID_ISSUE_DATE, A.NID_EXPIRY_DATE, A.GRANDFATHER_NAME, A.IS_FILER, PA.NAME PARENT_AGENT,\n" +
            "       A.AGENT_CLASS_ID, A.AGENT_LEVEL, A.IS_BLACKLISTED, A.IS_DEVICE_REGISTERED, A.LAST_NAME, A.GENDER, A.DOB, A.CUSTOMER_ALL_ID, A.MRZ_DATA,\n" +
            "       A.FACEMATCH_SCORE, A.LIVENESS_SCORE, CA.BIOVERIFIED, CA.UUID, CA.OTPVERIFIED, CA.SELFIEVERIFIED, CA.DEVICE_MODEL, N.CHANNEL_DESCR,\n" +
            "       T.ACCOUNT_NO, T.CURRENT_BALANCE, T.IBAN, T.ACCOUNT_TITLE, AL.ACCOUNT_LEVEL_DESCR, AST.ACCOUNT_STATUS_DESCR,\n" +
            "       CL.DAILY_AMT_LIMIT_DR, CL.MONTHLY_AMT_LIMIT_DR, CL.YEARLY_AMT_LIMIT_DR,\n" +
            "       CL.DAILY_AMT_LIMIT_CR, CL.MONTHLY_AMT_LIMIT_CR, CL.YEARLY_AMT_LIMIT_CR,\n" +
            "       CL.DAILY_TRANS_LIMIT_DR, CL.MONTHLY_TRANS_LIMIT_DR, CL.YEARLY_TRANS_LIMIT_DR,\n" +
            "       CL.DAILY_TRANS_LIMIT_CR, CL.MONTHLY_TRANS_LIMIT_CR, CL.YEARLY_TRANS_LIMIT_CR,\n" +
            "       CASE WHEN TF.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || TF.DOCUMENT_PATH END NID_FRONT, CASE WHEN TB.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || TB.DOCUMENT_PATH END NID_BACK, CASE WHEN POA.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || POA.DOCUMENT_PATH END PROOF_OF_ADDRESS, \n" +
            "       CASE WHEN SF.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || SF.DOCUMENT_PATH END SELFIE, CASE WHEN SG.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || SG.DOCUMENT_PATH END SIGNATURE, CASE WHEN LC.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || LC.DOCUMENT_PATH END LICENSE, CASE WHEN SHF.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || SHF.DOCUMENT_PATH END SHOP_FRONT, CASE WHEN SHB.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || SHB.DOCUMENT_PATH END SHOP_BACK,\n" +
            "       AB.LICENSE_TYPE_ID, AB.LICENSE_NUMBER, AB.LICENSE_ISSUE_DATE, AB.LICENSE_EXPIRY_DATE, AB.ISSUING_AUTHORITY, AB.LICENSE_TYPE, AB.BUSINESS_NAME,\n" +
            "       AB.LEGAL_STRUCTURE, AB.SHOP_NAME, AB.BUSINESS_TYPE_ID, BT.BUSINESS_TYPE_NAME, AB.FULL_ADDRESS BUSINESS_ADDRESS, BDT.DISTRICT_DESCR BUSINESS_DISTRICT,\n" +
            "       BPR.PROVINCE_DESCR BUSINESS_PROVINCE\n" +
            "FROM TBL_AGENT A\n" +
            "INNER JOIN TBL_ACCOUNT T ON A.AGENT_ID = T.AGENT_ID\n" +
            "INNER JOIN TBL_APP_USER AU ON A.AGENT_ID = AU.AGENT_ID\n" +
            "INNER JOIN TBL_AGENT_BUSINESS AB ON A.AGENT_ID = AB.AGENT_ID\n" +
            "INNER JOIN LKP_BUSINESS_TYPE BT ON AB.BUSINESS_TYPE_ID = BT.BUSINESS_TYPE_ID\n" +
            "LEFT JOIN LKP_DISTRICT BDT ON AB.DISTRICT_ID = BDT.DISTRICT_ID\n" +
            "LEFT JOIN LKP_PROVINCE BPR ON AB.PROVINCE_ID = BPR.PROVINCE_ID\n" +
            "LEFT JOIN LKP_CITY C ON A.CITY_ID = C.CITY_ID\n" +
            "LEFT JOIN LKP_DISTRICT D ON A.DISTRICT_ID = D.DISTRICT_ID\n" +
            "LEFT JOIN LKP_PROVINCE P ON A.PROVINCE_ID = P.PROVINCE_ID\n" +
            "LEFT JOIN TBL_AGENT PA ON A.PARENT_AGENT_ID = PA.AGENT_ID\n" +
            "INNER JOIN LKP_STATUS S ON A.STATUS_ID = S.STATUS_ID\n" +
            "INNER JOIN TBL_CUSTOMER_ALL CA ON A.CUSTOMER_ALL_ID = CA.CUSTOMER_ALL_ID\n" +
            "INNER JOIN LKP_CHANNEL N ON CA.CHANNEL_ID = N.CHANNEL_ID\n" +
            "INNER JOIN TBL_ACCOUNT_LEVEL AL ON T.ACCOUNT_LEVEL_ID = AL.ACCOUNT_LEVEL_ID\n" +
            "INNER JOIN LKP_ACCOUNT_STATUS AST ON T.ACCOUNT_STATUS_ID = AST.ACCOUNT_STATUS_ID\n" +
            "LEFT JOIN (SELECT ACCOUNT_ID, ASONDATE,\n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'DD-MM-YYYY') <> TO_CHAR(SYSDATE,'DD-MM-YYYY') THEN 0 ELSE DAILY_AMT_LIMIT_DR END AS DAILY_AMT_LIMIT_DR, \n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'MM-YYYY') <> TO_CHAR(SYSDATE,'MM-YYYY') THEN 0 ELSE MONTHLY_AMT_LIMIT_DR END AS MONTHLY_AMT_LIMIT_DR, \n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'YYYY') <> TO_CHAR(SYSDATE,'YYYY') THEN 0 ELSE YEARLY_AMT_LIMIT_DR END AS YEARLY_AMT_LIMIT_DR,\n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'DD-MM-YYYY') <> TO_CHAR(SYSDATE,'DD-MM-YYYY') THEN 0 ELSE DAILY_AMT_LIMIT_CR END AS DAILY_AMT_LIMIT_CR, \n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'MM-YYYY') <> TO_CHAR(SYSDATE,'MM-YYYY') THEN 0 ELSE MONTHLY_AMT_LIMIT_CR END AS MONTHLY_AMT_LIMIT_CR, \n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'YYYY') <> TO_CHAR(SYSDATE,'YYYY') THEN 0 ELSE YEARLY_AMT_LIMIT_CR END AS YEARLY_AMT_LIMIT_CR,\n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'DD-MM-YYYY') <> TO_CHAR(SYSDATE,'DD-MM-YYYY') THEN 0 ELSE DAILY_TRANS_LIMIT_DR END AS DAILY_TRANS_LIMIT_DR, \n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'MM-YYYY') <> TO_CHAR(SYSDATE,'MM-YYYY') THEN 0 ELSE MONTHLY_TRANS_LIMIT_DR END AS MONTHLY_TRANS_LIMIT_DR, \n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'YYYY') <> TO_CHAR(SYSDATE,'YYYY') THEN 0 ELSE YEARLY_TRANS_LIMIT_DR END AS YEARLY_TRANS_LIMIT_DR,\n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'DD-MM-YYYY') <> TO_CHAR(SYSDATE,'DD-MM-YYYY') THEN 0 ELSE DAILY_TRANS_LIMIT_CR END AS DAILY_TRANS_LIMIT_CR, \n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'MM-YYYY') <> TO_CHAR(SYSDATE,'MM-YYYY') THEN 0 ELSE MONTHLY_TRANS_LIMIT_CR END AS MONTHLY_TRANS_LIMIT_CR, \n" +
            "                   CASE WHEN TO_CHAR(ASONDATE,'YYYY') <> TO_CHAR(SYSDATE,'YYYY') THEN 0 ELSE YEARLY_TRANS_LIMIT_CR END AS YEARLY_TRANS_LIMIT_CR\n" +
            "             FROM TBL_ACCOUNT_LIMIT) CL ON T.ACCOUNT_ID = CL.ACCOUNT_ID\n" +
            " LEFT JOIN (SELECT APP_USER_ID, DOCUMENT_PATH \n" +
            "              FROM TBL_DOCUMENT \n" +
            "             WHERE DOCUMENT_TYPE_ID = 1 AND IS_ACTIVE = 'Y') TF ON AU.APP_USER_ID = TF.APP_USER_ID\n" +
            " LEFT JOIN (SELECT APP_USER_ID, DOCUMENT_PATH \n" +
            "              FROM TBL_DOCUMENT \n" +
            "             WHERE DOCUMENT_TYPE_ID = 2 AND IS_ACTIVE = 'Y') TB ON AU.APP_USER_ID = TB.APP_USER_ID\n" +
            " LEFT JOIN (SELECT APP_USER_ID, DOCUMENT_PATH \n" +
            "              FROM TBL_DOCUMENT \n" +
            "             WHERE DOCUMENT_TYPE_ID = 3 AND IS_ACTIVE = 'Y') POA ON AU.APP_USER_ID = POA.APP_USER_ID\n" +
            " LEFT JOIN (SELECT APP_USER_ID, DOCUMENT_PATH \n" +
            "              FROM TBL_DOCUMENT \n" +
            "             WHERE DOCUMENT_TYPE_ID = 4 AND IS_ACTIVE = 'Y') SF ON AU.APP_USER_ID = SF.APP_USER_ID\n" +
            " LEFT JOIN (SELECT APP_USER_ID, DOCUMENT_PATH \n" +
            "              FROM TBL_DOCUMENT \n" +
            "             WHERE DOCUMENT_TYPE_ID = 5 AND IS_ACTIVE = 'Y') SG ON AU.APP_USER_ID = SG.APP_USER_ID\n" +
            " LEFT JOIN (SELECT APP_USER_ID, DOCUMENT_PATH \n" +
            "              FROM TBL_DOCUMENT \n" +
            "             WHERE DOCUMENT_TYPE_ID = 7 AND IS_ACTIVE = 'Y') LC ON AU.APP_USER_ID = LC.APP_USER_ID\n" +
            " LEFT JOIN (SELECT APP_USER_ID, DOCUMENT_PATH \n" +
            "              FROM TBL_DOCUMENT \n" +
            "             WHERE DOCUMENT_TYPE_ID = 8 AND IS_ACTIVE = 'Y') SHF ON AU.APP_USER_ID = SHF.APP_USER_ID\n" +
            " LEFT JOIN (SELECT APP_USER_ID, DOCUMENT_PATH \n" +
            "              FROM TBL_DOCUMENT \n" +
            "             WHERE DOCUMENT_TYPE_ID = 7 AND IS_ACTIVE = 'Y') SHB ON AU.APP_USER_ID = SHB.APP_USER_ID\n" +
            " WHERE A.AGENT_ID = :agentId", nativeQuery = true)
    List<Object[]> getAgentDetail(@Param("agentId") String agentId);
}
