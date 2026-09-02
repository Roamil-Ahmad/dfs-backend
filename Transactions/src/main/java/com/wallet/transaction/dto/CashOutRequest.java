package com.wallet.transaction.dto;

import lombok.Getter;

@Getter
public class CashOutRequest {
  private FundTransferRequest fundTransferRequest;
  private VerifyOtpRequest verifyOtpRequest;
}
