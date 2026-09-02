package com.dfs.backoffice.dto;

import java.util.List;
import java.util.Map;

public class GenerateReportResponse {
    private String filePath;

    private String fileName;
    List<Map<String, Object>> jsonResult;
    private int totalRecordCount;
    private int totalPagesCount;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Map<String, Object>> getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(List<Map<String, Object>> jsonResult) {
        this.jsonResult = jsonResult;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

	public int getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	public int getTotalPagesCount() {
		return totalPagesCount;
	}

	public void setTotalPagesCount(int totalPagesCount) {
		this.totalPagesCount = totalPagesCount;
	}

}

