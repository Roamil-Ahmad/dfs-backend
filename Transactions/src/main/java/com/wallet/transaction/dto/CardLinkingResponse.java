package com.wallet.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardLinkingResponse {
    private long accountDebitCardId;
    private String accountNo;
    private String bankName;
    private long bankId;
    private Date createDate;
    private String currencyName;
    private String cvv;
    private String expiryDate;
    private String isActive;
    private Date lastUpdateDate;
    private BigDecimal lastUpdateUser;
    private String linkedCard;
    private String nameOnCard;
    private String pan;
    private BigDecimal renewalChargesCount;
    private BigDecimal updateIndex;
    private Long accountId;    // Instead of TblAccount, map only ID
    private String isDefault;
    private String cardTitle;
}
