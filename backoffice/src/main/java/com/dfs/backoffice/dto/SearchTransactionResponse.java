/*
Author Name: romail.ahmed

Project Name: backoffice

Package Name: com.dfs.backoffice.dto

Class Name: TransactionRequest

Date and Time:1/26/2025 10:24 PM

Version:1.0
*/
package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SearchTransactionResponse {
    private BigDecimal mwRequestId;
    private BigDecimal transHeadId;
    private String errorResponse;
    private String responseDescr;
    private String stan;
    private String rrn;
    private BigDecimal amount;
    private Date transDate;
    private String transactionType;
    private String status;
    private String fromAccountNo;
    private String fromAccountTitle;
    private String toAccountTitle;
    private String toAccountNo;
    private BigDecimal transRefNum;
    private String cardNo;
}

