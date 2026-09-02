package com.dfs.agentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAppScreenResponse {
    private String languageName;
    private String languageId;
    private String languageCode;
    private List<ScreenHeader> screenHeaders;
    private List<ScreenValidator> screenValidators;
    private List<ScreenLabel> screenLabels;
    private List<ScreenPlaceHolder> screenPlaceHolders;
    private List<ScreenButton> screenButtons;
}
