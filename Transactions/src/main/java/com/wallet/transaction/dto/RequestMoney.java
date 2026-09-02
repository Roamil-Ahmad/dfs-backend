package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RequestMoney {
    private String mobileNumber;
    private BigDecimal amount;
    private String comments;
    private char status;
    private String statusDescr;
    private String accountTitle;
    private BigDecimal requesterMoneyId;
    private String requestDate;
    private String historyStatus;
    private String accountNo;


}
