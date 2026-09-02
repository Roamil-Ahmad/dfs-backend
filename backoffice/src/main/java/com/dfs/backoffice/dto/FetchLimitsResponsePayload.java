package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FetchLimitsResponsePayload {
    private Map<String, Object> accountLimits;
    private List<Map<String, Object>> cardLimits;
}
