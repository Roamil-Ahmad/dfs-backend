package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BvsRequest {
    private String templateType;
    private int fingerIndex;
    private String isoTemplate;
    private String base64Template;
}
