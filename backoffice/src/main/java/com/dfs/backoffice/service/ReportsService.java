package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface ReportsService {

    GenerateReportResponse generateJsonReportData(DynamicJsonRequest dynamicJsonRequest, HttpServletRequest httpServletRequest) throws IOException;

    ReportFilterResponse getReportFilterList(ReportFilterRequest reportFilterRequest) ;

    byte[] callPdfJasperReport(String reportName, String dateFrom, String dateTo, String jsonDataStream, HttpServletRequest httpServletRequest);

    public byte[] callExcelJasperReport(String reportName, String dateFrom, String dateTo, String jsonDataStream, HttpServletRequest httpServletRequest);

}
