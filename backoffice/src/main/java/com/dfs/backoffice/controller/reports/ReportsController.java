package com.dfs.backoffice.controller.reports;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.service.ReportsService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ReportsController extends HelperClass {

    @Autowired
    private ReportsService reportsService;
    @Value("${jasperJsonFilePath.server}")
    private String reportPathServer;
    @Value("${jasperJsonFilePath.widnows}")
    private String reportPathWindows;
    public static final String WINDOWS = "Windows";
    public static final String OS_NAME = "os.name";
    private static final int WEB_REQUEST_BUFFER_SZIE_IN_MB = 50;


    @PostMapping(value = "/reportfilter", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getReportFilters(@Valid @RequestBody ReportFilterRequest reportFilterRequest, HttpServletRequest request) throws JsonProcessingException {
        Response response = new Response();
        ReportFilterResponse reportFilterList = reportsService.getReportFilterList(reportFilterRequest);
        if (reportFilterList != null) {
            setResponse(response, Constants.ONE, reportFilterList, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/generateReport", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity generateReportFile(@Valid @RequestBody DynamicJsonRequest dynamicJsonRequest, HttpServletRequest httpServletRequest) throws IOException {
        // Generate the Dynamic JSON Response
        GenerateReportResponse dynamicKmsJsonResponse = reportsService.generateJsonReportData(dynamicJsonRequest, httpServletRequest);

        // Create the ReportsDto object and populate it
        ReportsDto reportsDto = buildReportsDto(dynamicJsonRequest, dynamicKmsJsonResponse);
        return generateReport(reportsDto, dynamicJsonRequest, httpServletRequest);
    }

    private ReportsDto buildReportsDto(DynamicJsonRequest dynamicJsonRequest, GenerateReportResponse dynamicKmsJsonResponse) {
        ReportsDto reportsDto = new ReportsDto();
        reportsDto.setReportName(dynamicJsonRequest.getReportName().replaceAll("\\s", "").toUpperCase());
        reportsDto.setDateFrom((String) dynamicJsonRequest.getReportParams().get(Constants.DATE_FROM));
        reportsDto.setDateTo((String) dynamicJsonRequest.getReportParams().get(Constants.DATE_TO));
        reportsDto.setJson(dynamicKmsJsonResponse.getJsonResult());
        return reportsDto;
    }

    public ResponseEntity<?> generateReport(@Valid @RequestBody ReportsDto reportsDto, @Valid DynamicJsonRequest dynamicJsonRequest, HttpServletRequest httpServletRequest) throws IOException {
        Response response = new Response();
        String json = convertObjecttoJson(reportsDto.getJson());
        String reportName = null;
        byte[] reportContent = new byte[0];
        String osName = System.getProperty(OS_NAME);

        if (osName.startsWith(WINDOWS)) {
            reportPathServer = reportPathWindows;
        }
        if (json != null && !json.isEmpty()) {
            if ("E".equalsIgnoreCase(dynamicJsonRequest.getReportType())) {
                reportName = reportPathServer + reportsDto.getReportName() + "_EXCEL.jrxml";
                reportContent = reportsService.callExcelJasperReport(reportName, reportsDto.getDateFrom() != null ? reportsDto.getDateFrom() : "", reportsDto.getDateTo() != null ? reportsDto.getDateTo() : "", json, httpServletRequest);
            } else {
                reportName = reportPathServer + reportsDto.getReportName() + ".jrxml";
                reportContent = reportsService.callPdfJasperReport(reportName, reportsDto.getDateFrom() != null ? reportsDto.getDateFrom() : "", reportsDto.getDateTo() != null ? reportsDto.getDateTo() : "", json, httpServletRequest);
            }
        }
        if (reportContent != null && reportContent.length > 0) {
            HttpHeaders headers = new HttpHeaders();
            Resource fileResource = new ByteArrayResource(reportContent);
            headers.add(Constants.HEADER_NAME_CONTENT_DISPOSITION, Constants.HEADER_VALUE_CONTENT_DISPOSITION + reportName + Constants.EMPTY);
            headers.add(HttpHeaders.CONTENT_TYPE, "E".equalsIgnoreCase(dynamicJsonRequest.getReportType()) ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" : "application/pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(reportContent.length)
                    .body(fileResource);
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }
}

