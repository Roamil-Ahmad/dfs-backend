/*
Author Name: romail.ahmed

Project Name: bank

Package Name: com.qr.bank.dto

Class Name: QrDecodeResponse

Date and Time:5/8/2024 9:52 PM

Version:1.0
*/
package com.dfs.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QrDecodeResponse {
    private String title;
    private String iban;
    private String amount;
    private Date dueDate;
    private String amountAfterDueDate;


}
