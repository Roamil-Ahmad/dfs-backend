package com.dfs.backoffice.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DynamicJsonRequest {

    private long reportId;
    private String reportName;
    private String reportType;
    private Map<Object,Object> reportParams;
    
}
