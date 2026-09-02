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

@Getter
@Setter
public class SearchTransactionRequest {
    private String customerName;
    private String cardNo;
    private Long transDocsId;
    private String transRefNum;
    private String status;
    private String dateFrom;
    private String dateTo;
}
