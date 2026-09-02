/*
Author Name: romail.ahmed

Project Name: backoffice

Package Name: com.dfs.backoffice.dto

Class Name: AccountTypeCreation

Date and Time:1/25/2025 7:45 PM

Version:1.0
*/
package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountTypeCreation {
    private String accountType;
    private String accountTypeCode;
    private String accountTypeDescr;
    private String isActive;
}
