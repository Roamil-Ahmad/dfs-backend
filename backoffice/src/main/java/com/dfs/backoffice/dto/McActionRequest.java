package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class McActionRequest {

    private String mcRequestId;
    private String mcPeindingRequestId;
    private String checkerId;
    private String checkerComments;
    private String action;
    private BigDecimal updatedIndex;

}
