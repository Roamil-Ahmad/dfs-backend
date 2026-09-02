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

@Getter
@Setter
public class AccountTypeUpdate {
    private Long accountTypeId;
    private String accountType;
    private String accountTypeCode;
    private String accountTypeDescr;
    private String isActive;
}
