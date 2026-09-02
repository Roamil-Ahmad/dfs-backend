package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDeviceRequest {

    private String username;
    private String accountTypeCode;
    private String accountRegTypeCode;


}
