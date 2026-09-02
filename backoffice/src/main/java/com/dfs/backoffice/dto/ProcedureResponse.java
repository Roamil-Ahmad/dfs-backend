package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcedureResponse {

    private int status;
    private String statusDescr;
    private int mcApplicability;

}
