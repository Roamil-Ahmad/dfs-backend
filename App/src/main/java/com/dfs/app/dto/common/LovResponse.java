package com.dfs.app.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LovResponse {
    private String name;
    private String code;
    private long id;
    private String description;
}
