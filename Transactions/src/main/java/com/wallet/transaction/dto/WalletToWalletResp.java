package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletToWalletResp {

    private String reserved1;
    private String authidresponse;
    private String errorresponse;
    private String availablebalance;
    private String actualbalance;
    private String responseDescr;
    private Integer responseStatus;
    private String checkPoint;
    private Integer transHeadId;

}
