package com.barq.nadra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Request {
    private String channel;
    private String segment;
    private String imieNo;
    private String latitude;
    private String logitude;
    private String language;
    private Object payload;

}
