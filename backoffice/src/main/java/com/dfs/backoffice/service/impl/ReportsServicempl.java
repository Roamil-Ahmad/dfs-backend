/*Author Name:muhammad.kashif
Project Name: helper
Package Name:com.mfs.helper.service.impl
Class Name: helperImpl
Date and Time:2/20/2023 12:37 PM
Version:1.0*/
package com.dfs.backoffice.service.impl;


import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.DynamicJsonRequest;
import com.dfs.backoffice.dto.GenerateReportResponse;
import com.dfs.backoffice.dto.ReportFilterRequest;
import com.dfs.backoffice.dto.ReportFilterResponse;
import com.dfs.backoffice.model.*;
import com.dfs.backoffice.repo.TblAppUserRepo;
import com.dfs.backoffice.repo.TblReportRepo;
import com.dfs.backoffice.repo.TblUserRepo;
import com.dfs.backoffice.service.ReportsService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.CustomException;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.JwtConstants;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("java:S1874")
public class ReportsServicempl extends HelperClass implements ReportsService {


    public static final String WINDOWS = "Windows";
    public static final String OS_NAME = "os.name";

    @Autowired
    private TblReportRepo tblReportRepo;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private TblUserRepo tblUserRepo;

    @Value("${jasperJsonFilePath.server}")
    private String jasperJsonFilePathServer;

    @Value("${jasperJsonFilePath.widnows}")
    private String jasperJsonFilePathWindows;

    @Autowired
    private EntityManager entityManager;

    @Override
    public ReportFilterResponse getReportFilterList(ReportFilterRequest reportFilterRequest) {

        TblReport reportLoaded = tblReportRepo.getReport(reportFilterRequest.getReportId());
        if (reportLoaded == null) {
            return new ReportFilterResponse();
        }

        ReportFilterResponse reportFilter = new ReportFilterResponse();
        reportFilter.setTblReport(reportLoaded);
        reportFilter.setTblReportFilters(reportLoaded.getTblReportFilters());
        return reportFilter;
    }

    @Override
    public GenerateReportResponse generateJsonReportData(DynamicJsonRequest dynamicJsonRequest, HttpServletRequest httpServletRequest) throws IOException {
        GenerateReportResponse dynamicKmsJsonResponse = new GenerateReportResponse();

        TblReport reportLoaded = tblReportRepo.getReport(dynamicJsonRequest.getReportId());

        if (reportLoaded == null) {
            throw new CustomException("Report Query Not Found");
        }

        String fileName = reportLoaded.getFileName() == null ? "jasperReport" : reportLoaded.getFileName();

        List<TblReportFilter> reportFilterList = reportLoaded.getTblReportFilters();
        List<TblReportQuery> reportQueryList = reportLoaded.getTblReportQueries();


        if (isNullOrEmpty(reportQueryList)) {
            throw new CustomException("Report Query Not Found");
        }

        TblReportQuery reportQueryLoaded = reportQueryList.get(0);
        String queryParam = reportQueryLoaded.getReportQuerySample();
        Map<Object, Object> reportParamsMap = dynamicJsonRequest.getReportParams();

        Query query = entityManager.createNativeQuery(queryParam);


        if (reportFilter(dynamicKmsJsonResponse, reportFilterList, reportParamsMap, query))
            return dynamicKmsJsonResponse;


        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> reportQueryResultList = null;
        try {
            reportQueryResultList = query.getResultList();
        } catch (Exception e) {
            throw new CustomException(Constants.WRONG_QUERY_MESSAGE);
        }

        List<Map<String, Object>> decryptedResultList = reportQueryResultList.stream().map(rqs -> {
            return rqs;
        }).collect(Collectors.toList());

        String filePathWithRespectToOS = "";
        String json = convertObjecttoJson(decryptedResultList);
        String osName = System.getProperty(OS_NAME);

        if (osName.startsWith(WINDOWS)) {

            filePathWithRespectToOS = jasperJsonFilePathWindows;
        } else {

            filePathWithRespectToOS = jasperJsonFilePathServer;
        }

        File file = new File(filePathWithRespectToOS);

        if (!file.exists()) {
            throw new CustomException(Constants.REPORT_FOLDER_NOT_EXIST + filePathWithRespectToOS);
        }

        String filePath = createJsonFile(json, fileName);

        filePath = filePath.replace("//", "/");
        dynamicKmsJsonResponse.setFilePath(filePath);
        dynamicKmsJsonResponse.setFileName(fileName);
        dynamicKmsJsonResponse.setJsonResult(decryptedResultList);

        return dynamicKmsJsonResponse;
    }

    private boolean reportFilter(GenerateReportResponse dynamicKmsJsonResponse, List<TblReportFilter> reportFilterList, Map<Object, Object> reportParamsMap, Query query) {
        for (TblReportFilter rf : reportFilterList) {

            String mappedFilterName = rf.getMappedFilterName();

            String mapValue = (String) reportParamsMap.get(mappedFilterName);
            if (mappedFilterName.contains(Constants.DATE_FROM)) {
                String dateFromInput = "";

                if (!isNullOrEmpty(mapValue)) {
                    dateFromInput = mapValue;
                }

                query.setParameter(mappedFilterName, dateFromInput);

            } else if (mappedFilterName.contains(Constants.DATE_TO)) {

                String dateToInput = "";

                if (!isNullOrEmpty(mapValue)) {
                    dateToInput = mapValue;
                }

                query.setParameter(mappedFilterName, dateToInput);

            } else {
                try {
                    query.setParameter(mappedFilterName, mapValue);
                } catch (Exception e) {
                    throw new CustomException(Constants.WRONG_PARAMETER_SET_QUERY + mappedFilterName + io.swagger.v3.core.util.Constants.COMMA);
                }
            }

        }
        return false;
    }

    /**
     * Create JSON file and Populate with JSON text
     *
     * @param json
     * @param reportName
     * @return
     */
    private String createJsonFile(String json, String reportName) throws IOException {

        String filePath = "";
        String filePathWithRespectToOS = "";


        String osName = System.getProperty(OS_NAME);

        if (osName.startsWith(WINDOWS)) {

            filePathWithRespectToOS = jasperJsonFilePathWindows;
        } else {

            filePathWithRespectToOS = jasperJsonFilePathServer;
        }

        filePath = filePathWithRespectToOS + reportName + Constants.JSON;

        // Create a FileWriter object
        //FileWriter fileWriter = new FileWriter(filePath);
        Writer fileWriter = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8);
        try {

            // Write the JSON object to the file
            fileWriter.write(json);

            // Close the FileWriter object


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            fileWriter.close();

        }

        return filePath;
    }

    /**
     * Updates the original request map with the decrypted values from the sender's map.
     *
     * @param rqsSenderCopy          The copy of the sender's request map.
     * @param rqs                    The original request map.
     * @param senderEncryptedColumns The list of encrypted columns related to the sender.
     */
    private void updateRequestMap(Map<String, Object> rqsSenderCopy, Map<String, Object> rqs, List<String> senderEncryptedColumns) {
        senderEncryptedColumns.forEach(e -> {
            Object value = rqsSenderCopy.get(e);
            rqs.put(e, value);
        });
    }

    @Override
    public byte[] callPdfJasperReport(String reportName, String dateFrom, String dateTo, String jsonDataStream, HttpServletRequest httpServletRequest) {
        try {
            Object appUserIdObj = httpServletRequest.getAttribute(JwtConstants.APP_USER_ID);
            TblAppUser tblAppUser = tblAppUserRepo.findById(Long.parseLong(appUserIdObj.toString())).orElseThrow(() -> new CustomException(GenericResponseCode.USER_NOT_FOUND.getResponseCode()));
            TblUser tblUser = tblUserRepo.findById(tblAppUser.getUserId().longValue()).orElseThrow(() -> new CustomException(GenericResponseCode.USER_NOT_FOUND.getResponseCode()));
            HashMap<String, Object> parameters = new HashMap<>();
            JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(jsonDataStream.getBytes()));
            parameters.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, jsonDataSource);
            parameters.put("dateFrom", dateFrom);
            parameters.put("dateTo", dateTo);
            parameters.put("user", tblUser.getEmployeeName());
            JasperReport jp = JasperCompileManager.compileReport(reportName);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jp, parameters, jsonDataSource);

            List<JRPrintPage> pages = jasperPrint.getPages();
            if (pages.isEmpty()) {
                return new byte[0];
            }

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outputStream);
            pdfExporter.setExporterOutput(exporterOutput);

            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            pdfExporter.setConfiguration(configuration);

            pdfExporter.exportReport();

            return outputStream.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }

    }

    @Override
    public byte[] callExcelJasperReport(String reportName, String dateFrom, String dateTo, String jsonDataStream, HttpServletRequest httpServletRequest) {
        try {
            Object appUserIdObj = httpServletRequest.getAttribute(JwtConstants.APP_USER_ID);
            TblAppUser tblAppUser = tblAppUserRepo.findById(Long.parseLong(appUserIdObj.toString())).orElseThrow(() -> new CustomException(GenericResponseCode.USER_NOT_FOUND.getResponseCode()));
            TblUser tblUser = tblUserRepo.findById(tblAppUser.getUserId().longValue()).orElseThrow(() -> new CustomException(GenericResponseCode.USER_NOT_FOUND.getResponseCode()));
            HashMap<String, Object> parameters = new HashMap<>();
            JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(jsonDataStream.getBytes()));
            parameters.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, jsonDataSource);
            parameters.put("dateFrom", dateFrom);
            parameters.put("dateTo", dateTo);
            parameters.put("user", tblUser.getEmployeeName());

            JasperReport jp = JasperCompileManager.compileReport(reportName);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jp, parameters, jsonDataSource);

            List<JRPrintPage> pages = jasperPrint.getPages();
            if (pages.isEmpty()) {
                return new byte[0];
            }

            JRXlsxExporter xlsxExporter = new JRXlsxExporter();
            xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outputStream);
            xlsxExporter.setExporterOutput(exporterOutput);

            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);
            configuration.setCollapseRowSpan(false);
            xlsxExporter.setConfiguration(configuration);

            xlsxExporter.exportReport();

            return outputStream.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }
}