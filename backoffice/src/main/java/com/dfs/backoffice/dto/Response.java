/*
Author Name: romail.ahmed

Project Name: configurations

Package Name: com.workflow.configurations.dto

Class Name: Response

Date and Time:3/13/2023 12:34 PM

Version:1.0
*/
package com.dfs.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private String responseCode;
    private String message;
    private Object payload;

}
