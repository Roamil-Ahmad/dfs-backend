package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeMpinRequest {
    private String mobileNumber;
    private String currentMpin;
    private String newMpin;
    private String confirmMpin;


}
