package com.dfs.thirdparties.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidationError {
    private String errorCode;
    private String errorMessage;

}
