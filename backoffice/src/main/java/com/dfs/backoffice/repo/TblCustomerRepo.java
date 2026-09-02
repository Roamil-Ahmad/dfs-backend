package com.dfs.backoffice.repo;

import com.dfs.backoffice.dto.SearchCustomerRequest;
import com.dfs.backoffice.model.TblCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblCustomerRepo extends JpaRepository<TblCustomer, Long> {

    @Query(value = "SELECT C.CUSTOMER_ID,C.FIRST_NAME,C.MIDDLE_NAME,C.LAST_NAME,C.FATHER_NAME,C.GRANDFATHER_NAME,C.NID_NO,C.DOB,C.EMAIL, \n" +
            "       C.ADDRESS_C,C.ADDRESS_M,C.ADDRESS_P,C.CREATEDATE,C.FULL_NAME,C.GENDER,C.IS_ACTIVE,C.POB,C.NID_EXPIRY_DATE, \n" +
            "       C.NID_ISSUE_DATE,A.ACCOUNT_ID, L.ACCOUNT_LEVEL_DESCR, S.ACCOUNT_STATUS_DESCR, A.ACCOUNT_NO, C.NATIONALITY,\n" +
            "       O.OCCUPATION_DESCR, G.SEGMENT_DESCR, C.RISK_PROFILE, A.MOBILE_NO, P.PROVINCE_DESCR, D.DISTRICT_DESCR, Y.CITY_DESCR,\n" +
            "       A.CURRENT_BALANCE, CASE WHEN TF.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || TF.DOCUMENT_PATH END NID_FRONT, CASE WHEN TB.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || TB.DOCUMENT_PATH END NID_BACK, CASE WHEN POA.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || POA.DOCUMENT_PATH END PROOF_OF_ADDRESS, \n" +
            "       CASE WHEN SF.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || SF.DOCUMENT_PATH END SELFIE, CASE WHEN SG.DOCUMENT_PATH IS NULL THEN NULL ELSE 'backoffice/' || SG.DOCUMENT_PATH END SIGNATURE, V.UUID DEVICE_ID,\n" +
            "       CL.DAILY_AMT_LIMIT_DR, CL.MONTHLY_AMT_LIMIT_DR, CL.YEARLY_AMT_LIMIT_DR,\n" +
            "       CL.DAILY_AMT_LIMIT_CR, CL.MONTHLY_AMT_LIMIT_CR, CL.YEARLY_AMT_LIMIT_CR,\n" +
            "       CL.DAILY_TRANS_LIMIT_DR, CL.MONTHLY_TRANS_LIMIT_DR, CL.YEARLY_TRANS_LIMIT_DR,\n" +
            "       CL.DAILY_TRANS_LIMIT_CR, CL.MONTHLY_TRANS_LIMIT_CR, CL.YEARLY_TRANS_LIMIT_CR\n" +
            "  FROM TBL_CUSTOMER C \n" +
            " INNER JOIN TBL_ACCOUNT A ON C.CUSTOMER_ID = A.CUSTOMER_ID \n" +
            " INNER JOIN LKP_ACCOUNT_STATUS S ON A.ACCOUNT_STATUS_ID = S.ACCOUNT_STATUS_ID \n" +
            " INNER JOIN TBL_ACCOUNT_LEVEL L ON A.ACCOUNT_LEVEL_ID = L.ACCOUNT_LEVEL_ID\n" +
            " INNER JOIN LKP_SEGMENT G ON C.SEGMENT_ID = G.SEGMENT_ID \n" +
            " INNER JOIN TBL_APP_USER AU ON C.CUSTOMER_ID = AU.CUSTOMER_ID\n" +
            " LEFT JOIN TBL_DEVICE_INFO V ON AU.APP_USER_ID = V.APP_USER_ID\n" +
            " LEFT JOIN LKP_OCCUPATION O ON C.OCCUPATION_ID = O.OCCUPATION_ID\n" +
            " LEFT JOIN LKP_PROVINCE P ON C.PROVINCE_ID = P.PROVINCE_ID\n" +
            " LEFT JOIN LKP_DISTRICT D ON C.DISTRICT_ID = D.DISTRICT_ID\n" +
            " LEFT JOIN LKP_CITY Y ON C.CITY_ID = Y.CITY_ID\n" +
            " LEFT JOIN (SELECT ACCOUNT_ID, ASONDATE,\n" +
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
            "             FROM TBL_ACCOUNT_LIMIT) CL ON A.ACCOUNT_ID = CL.ACCOUNT_ID\n" +
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
            "             WHERE DOCUMENT_TYPE_ID = 5 AND IS_ACTIVE = 'Y') SG ON AU.APP_USER_ID = SG.APP_USER_ID             \n" +
            " WHERE A.MOBILE_NO = NVL(:#{#searchCustomerRequest.mobileNo}, A.MOBILE_NO) \n" +
            "   AND C.NID_NO = NVL(:#{#searchCustomerRequest.nidNo}, C.NID_NO) \n" +
            "   AND UPPER(DECRYPT_DATA(C.FULL_NAME)) LIKE '%'||UPPER(:#{#searchCustomerRequest.fullName})||'%' \n" +
            "   AND A.ACCOUNT_STATUS_ID = NVL(:#{#searchCustomerRequest.accountStatusId}, A.ACCOUNT_STATUS_ID) \n" +
            "   AND CAST(A.CREATEDATE AS DATE) BETWEEN NVL(TO_DATE(:dateFromInput,'YYYY-MM-DD HH24:MI:SS'), CAST(A.CREATEDATE AS DATE)) \n" +
            "                                      AND NVL(TO_DATE(:dateToInput,'YYYY-MM-DD HH24:MI:SS'), CAST(A.CREATEDATE AS DATE))", nativeQuery = true)
    List<Object> searchCustomer(SearchCustomerRequest searchCustomerRequest, String dateFromInput, String dateToInput);

    @Query(value = "SELECT * \n" +
            "  FROM TBL_CUSTOMER C\n" +
            "   WHERE C.NID_NO = NVL(:nidNo, C.NID_NO)", nativeQuery = true)
    TblCustomer findCustomerByNidNo(String nidNo);
}
