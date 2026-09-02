package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NidBvsRequest {
    private String mobileNumber;
    private String nidNumber;
    ArrayList<BvsRequest> bvsRequest;
}
