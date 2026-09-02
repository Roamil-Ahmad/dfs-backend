package com.wallet.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FetchBillRequest {
    private String pan;
    private String accountNo;
    private String billRefNo;
    private String serviceId;
}
