package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.VwMiniStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VwMiniStatementRepo extends JpaRepository<VwMiniStatement, Long> {

    @Query(value = "SELECT * \n" +
            " FROM VW_MINI_STATEMENT \n" +
            " WHERE ((FROM_ACCOUNT_ID = :fromAccountId AND FROM_ACCOUNT_TYPE = 'C' AND AMOUNT_TYPE = 'D')  \n" +
            "    OR (TO_ACCOUNT_ID = :fromAccountId AND TO_ACCOUNT_TYPE = 'C' AND AMOUNT_TYPE = 'C')) \n" +
            "FETCH FIRST 10 ROWS ONLY", nativeQuery = true)
    List<VwMiniStatement> getLastTenTransactions(String fromAccountId);

    @Query(value = "SELECT R.MW_REQUEST_ID, H.TRANS_HEAD_ID, S.ERRORRESPONSE, SUBSTR(S.RESPONSEDESCR,INSTR(S.RESPONSEDESCR,'-')+2) RESPONSEDESCR, \n" +
            "       R.STAN, R.RRN, NVL(H.AMOUNT, R.TRANSACTIONAMOUNT) AMOUNT, CAST(R.CREATEDATE AS DATE) TRANS_DATE, T.TRANS_DOCS_DESCR TRANSACTION_TYPE, \n" +
            "       CASE WHEN ERRORRESPONSE = '0000' THEN 'Approved' ELSE 'Decline' END AS STATUS, \n" +
            "       H.FROM_ACCOUNT_NO, H.FROM_ACCOUNT_TITLE, H.TO_ACCOUNT_TITLE, H.TO_ACCOUNT_NO, H.TRANS_REFNUM, R.CARDNO \n" +
            "  FROM TBL_MW_REQUEST R \n" +
            " INNER JOIN TBL_MW_RESPONSE S ON R.MW_REQUEST_ID = S.MW_REQUEST_ID \n" +
            " INNER JOIN TBL_TRANS_DOCS T ON R.TRANS_DOCS_ID = T.TRANS_DOCS_ID \n" +
            "  LEFT JOIN VW_TRANS_DETAIL_REPORT H ON R.MW_REQUEST_ID = H.MW_REQUEST_ID \n" +
            " WHERE ((UPPER(H.FROM_ACCOUNT_TITLE) LIKE '%'||UPPER(:customerName)||'%') \n" +
            "       OR (UPPER(H.TO_ACCOUNT_TITLE) LIKE '%'||UPPER(:customerName)||'%')) \n" +
            "   AND NVL(R.CARDNO,0) = NVL(:cardNo,NVL(R.CARDNO,0)) \n" +
            "   AND R.TRANS_DOCS_ID = NVL(:transDocsId, R.TRANS_DOCS_ID) \n" +
            "   AND NVL(H.TRANS_REFNUM,0) = NVL(:transRefNum, NVL(H.TRANS_REFNUM,0)) \n" +
            "   AND CASE WHEN ERRORRESPONSE = '0000' THEN 'Approved' ELSE 'Decline' END = NVL(:status, CASE WHEN ERRORRESPONSE = '0000' THEN 'Approved' ELSE 'Decline' END) \n" +
            "   AND CAST(R.CREATEDATE AS DATE) BETWEEN NVL(TO_DATE(NVL2(:dateFrom,:dateFrom||' 00:00:00',''),'YYYY-MM-DD HH24:MI:SS'),CAST(R.CREATEDATE AS DATE)) \n" +
            "                                     AND NVL(TO_DATE(NVL2(:dateTo,:dateTo||' 23:59:59',''),'YYYY-MM-DD HH24:MI:SS'),CAST(R.CREATEDATE AS DATE))", nativeQuery = true)
    List<Object[]> getResult(String cardNo, String customerName, String dateFrom, String dateTo, String status, Long transDocsId, String transRefNum);

    @Query(value = "SELECT R.MW_REQUEST_ID, H.TRANS_HEAD_ID, S.ERRORRESPONSE, SUBSTR(S.RESPONSEDESCR,INSTR(S.RESPONSEDESCR,'-')+2) RESPONSEDESCR," +
            "      R.STAN, R.RRN, NVL(H.AMOUNT, R.TRANSACTIONAMOUNT) AMOUNT, CAST(R.CREATEDATE AS DATE) TRANS_DATE, T.TRANS_DOCS_DESCR TRANSACTION_TYPE," +
            "      CASE WHEN ERRORRESPONSE = '0000' THEN 'Approved' ELSE 'Decline' END AS STATUS," +
            "      H.FROM_ACCOUNT_NO, H.FROM_ACCOUNT_TITLE, H.TO_ACCOUNT_TITLE, H.TO_ACCOUNT_NO, H.TRANS_REFNUM, R.CARDNO" +
            " FROM TBL_MW_REQUEST R " +
            "INNER JOIN TBL_MW_RESPONSE S ON R.MW_REQUEST_ID = S.MW_REQUEST_ID " +
            "INNER JOIN TBL_TRANS_DOCS T ON R.TRANS_DOCS_ID = T.TRANS_DOCS_ID " +
            "LEFT JOIN VW_TRANS_DETAIL_REPORT H ON R.MW_REQUEST_ID = H.MW_REQUEST_ID " +
            "WHERE R.MW_REQUEST_ID =:mwRequestId", nativeQuery = true)
    List<Object[]> getResultTrsanctionDetail(String mwRequestId);


    @Query(value = " SELECT R.MW_REQUEST_ID, H.TRANS_HEAD_ID, S.ERRORRESPONSE, SUBSTR(S.RESPONSEDESCR,INSTR(S.RESPONSEDESCR,'-')+2) RESPONSEDESCR, " +
            "       R.STAN, R.RRN, NVL(H.AMOUNT, R.TRANSACTIONAMOUNT) AMOUNT, CAST(R.CREATEDATE AS DATE) TRANS_DATE, T.TRANS_DOCS_DESCR TRANSACTION_TYPE," +
            "       CASE WHEN ERRORRESPONSE = '0000' THEN 'Approved' ELSE 'Decline' END AS STATUS," +
            "       H.FROM_ACCOUNT_NO, H.FROM_ACCOUNT_TITLE, H.TO_ACCOUNT_TITLE, H.TO_ACCOUNT_NO, H.TRANS_REFNUM, R.CARDNO" +
            "  FROM TBL_MW_REQUEST R" +
            " INNER JOIN TBL_MW_RESPONSE S ON R.MW_REQUEST_ID = S.MW_REQUEST_ID" +
            " INNER JOIN TBL_TRANS_DOCS T ON R.TRANS_DOCS_ID = T.TRANS_DOCS_ID " +
            " LEFT JOIN VW_TRANS_DETAIL_REPORT H ON R.MW_REQUEST_ID = H.MW_REQUEST_ID" +
            " WHERE ((NVL(H.FROM_ACCOUNT_TITLE,0) = NVL(:agentName, NVL(H.FROM_ACCOUNT_TITLE,0))) OR (NVL(H.TO_ACCOUNT_TITLE,0) = NVL(:agentName, NVL(H.TO_ACCOUNT_TITLE,0))))  " +
            "   AND ((NVL(H.FROM_ACCOUNT_NO,0) = NVL(:agentId, NVL(H.FROM_ACCOUNT_NO,0))) OR (NVL(H.TO_ACCOUNT_NO,0) = NVL(:agentId, NVL(H.TO_ACCOUNT_NO,0))))", nativeQuery = true)
    List<Object[]> agentTracking(String agentId, String agentName);


    @Query(value = " SELECT * FROM VW_TRANSACTION_DASHBOARD1 ", nativeQuery = true)
    List<Object[]> transactionDashboardOne();


    @Query(value = " SELECT * FROM VW_TRANSACTION_DASHBOARD2 ", nativeQuery = true)
    List<Object[]> transactionDashboardTwo();

    @Query(value = " SELECT * FROM VW_CUST_MANAGEMENT_DASHBOARD1", nativeQuery = true)
    List<Object[]> getCustomerDashboardOne();

    @Query(value = " SELECT * FROM VW_CUST_MANAGEMENT_DASHBOARD2", nativeQuery = true)
    List<Object[]> getCustomerDashboardTwo();

    @Query(value = "SELECT * FROM VW_AGENT_MANAGEMENT_DASHBOARD1", nativeQuery = true)
    List<Object[]> getAgentDashboardOne();

    @Query(value = "SELECT * FROM VW_AGENT_MANAGEMENT_DASHBOARD2", nativeQuery = true)
    List<Object[]> getAgentDashboardTwo();

    @Query(value = "SELECT * FROM VW_INDIVIDUAL_DASHBOARD1", nativeQuery = true)
    List<Object[]> getIndividualDashboardOne();

    @Query(value = "SELECT * FROM VW_INDIVIDUAL_DASHBOARD2", nativeQuery = true)
    List<Object[]> getIndividualDashboardTwo();

    @Query(value = " SELECT *  " +
            "  FROM VW_MINI_STATEMENT  " +
            " WHERE ((FROM_ACCOUNT_ID = :accountId AND FROM_ACCOUNT_TYPE = 'C' AND AMOUNT_TYPE = 'D')  " +
            "       OR (TO_ACCOUNT_ID = :accountId AND TO_ACCOUNT_TYPE = 'C' AND AMOUNT_TYPE = 'C'))  " +
            "   AND (TRANS_DOCS_ID IN (177, 179, 1, 68) OR (UDF1 = 'BP-ISS')) " +
            " ORDER BY TRANS_DATE DESC  " +
            " FETCH FIRST 10 ROWS ONLY", nativeQuery = true)
    List<VwMiniStatement> getCardLastTransactions(String accountId);
}
