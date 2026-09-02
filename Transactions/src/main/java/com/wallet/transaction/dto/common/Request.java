package com.wallet.transaction.dto.common;

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
    private String longitude;
    private String language;
    private Object payload;

}
