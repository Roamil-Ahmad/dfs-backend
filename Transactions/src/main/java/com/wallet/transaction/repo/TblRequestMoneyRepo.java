/*
Author Name: romail.ahmed

Project Name: transactions

Package Name: com.wallet.transaction.repo

Interface Name: TblRequestMoneyRepo

Date and Time:1/11/2025 10:51 PM

Version:1.0
*/

package com.wallet.transaction.repo;

import com.wallet.transaction.model.TblRequestMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblRequestMoneyRepo extends JpaRepository<TblRequestMoney, Long> {

    @Query(value = " SELECT" +
            "    m.request_money_id, " +
            "    m.amount, " +
            "    m.status, " +
            "    DECODE(m.status, 'A', 'Approved', 'P', 'Pending', 'Rejected') AS statusDescr, " +
            "    a.account_title, " +
            "    m.comments, " +
            "    TO_CHAR(m.createdate, 'YYYY-MM-DD HH24:MI:SS') AS requestDate, " +
            "    a.account_no" +
            " FROM " +
            "    tbl_request_money m " +
            " INNER JOIN " +
            "    tbl_account a " +
            " ON " +
            "    a.account_id = m.requester_id " +
            " WHERE " +
            "    m.requestee_id =:accountId ", nativeQuery = true)
    List<Object> findRequestMoneyAgainstRequesteeAccount(long accountId);

    @Query(value = " SELECT " +
            "        m.request_money_id, " +
            "        m.amount, " +
            "        m.status, " +
            "        DECODE(m.status, 'A', 'Approved', 'P', 'Pending', 'Rejected') AS statusDescr, " +
            "        a.account_title, " +
            "        m.comments, " +
            "        TO_CHAR(m.createdate, 'YYYY-MM-DD HH24:MI:SS') AS requestDate " +
            "    FROM  " +
            "        tbl_request_money m " +
            "    INNER JOIN  " +
            "        tbl_account a  " +
            "    ON  " +
            "        a.account_id = m.requestee_id " +
            "    WHERE  " +
            "        m.requester_id =:accountId", nativeQuery = true)
    List<Object> findRequestMoneyAgainstRequesterAccount(long accountId);

    @Query(value = " SELECT m.request_money_id, m.amount, m.status,  " +
            "       DECODE(m.status, 'A', 'Approved', 'P', 'Pending', 'Rejected') AS statusDescr,  " +
            "       a.account_title, m.comments, \n" +
            "       TO_CHAR(m.createdate, 'YYYY-MM-DD HH24:MI:SS') AS requestDate, a.account_no, 'E' " +
            "  FROM tbl_request_money m  " +
            " INNER JOIN tbl_account a ON a.account_id = m.requester_id " +
            " WHERE m.requestee_id = :accountId " +
            "UNION ALL  " +
            "SELECT m.request_money_id, m.amount, m.status,  " +
            "       DECODE(m.status, 'A', 'Approved', 'P', 'Pending', 'Rejected') AS statusDescr,  " +
            "       a.account_title, m.comments,  " +
            "       TO_CHAR(m.createdate, 'YYYY-MM-DD HH24:MI:SS') AS requestDate, a.account_no, 'R'  " +
            "  FROM tbl_request_money m  " +
            " INNER JOIN tbl_account a ON a.account_id = m.requestee_id  " +
            " WHERE m.requester_id = :accountId ", nativeQuery = true)
    List<Object> findRequestMoneyHistory(long accountId);
}
