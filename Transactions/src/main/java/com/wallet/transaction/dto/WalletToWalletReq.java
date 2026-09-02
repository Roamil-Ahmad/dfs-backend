package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletToWalletReq {
    private String mobileNumber;
    private String relationshipid;
    private String transmissiondate;
    private String transmissiontime;
    private String stan;
    private String rrn;
    private String datelocaltran;
    private String timelocaltran;
    private String acqinstcode;
    private String merchanttype;
    private String posentrymode;
    private String cardacceptornamelocation;
    private String cardacceptorterminalid;
    private String fromaccountnumber;
    private String fromaccounttype;
    private String fromaccountcurrency;
    private String toaccountnumber;
    private String toaccounttype;
    private String toaccountcurrency;
    private String transactionamount;
    private String transactioncurrency;
    private String transactionfee;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private String reserved4;
    private String reserved5;
    private String clientId;
    private String channelId;
    private String clientSecret;

}
