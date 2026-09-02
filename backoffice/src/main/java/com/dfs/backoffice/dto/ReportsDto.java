package com.dfs.backoffice.dto;

import java.util.List;
import java.util.Map;

public class ReportsDto {
    private String reportName;
    private String dateFrom;
    private String dateTo;
    private List<Map<String, Object>> json;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<Map<String, Object>> getJson() {
        return json;
    }

    public void setJson(List<Map<String, Object>> json) {
        this.json = json;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
}