package com.dfs.backoffice.dto;

public class ReportFileResponse {
	
	private byte[] pdfByteContent;


    public byte[] getPdfByteContent() {
        return pdfByteContent;
    }
    public void setPdfByteContent(byte[] pdfByteContent) {
        this.pdfByteContent = pdfByteContent;
    }

}
