package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {
    private HashMap<String ,Boolean> docStatus;
}
