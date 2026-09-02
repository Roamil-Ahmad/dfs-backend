package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComplaintRequest {
    private String mobileNumber;
    private String accountLevelCode;
    private long issueTypeId;
    private String issueDecr;
    private String contactOption;
    private List<Document> documents;
}
