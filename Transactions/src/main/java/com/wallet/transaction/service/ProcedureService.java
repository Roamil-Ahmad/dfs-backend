package com.wallet.transaction.service;

import com.wallet.transaction.dto.*;

import java.math.BigDecimal;

public interface ProcedureService {

    ProcResponse walletToCardAcquirer(Long fromAccountId, String toCardNo, String toBankName, String toAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn);

    ProcResponse cardToWalletAcquirer(String fromCardNo, String fromBankName, String fromAccountTitle, String toAccountId, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode);

    ProcResponse billPaymentIssuer(String fromCardNo, String utilityCompanyCode, String utilityConsumerNo, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode);

    ProcResponse cashinAcquirer(String toCardNo, String toBankName, String toAccountTitle, String fromAccountId, BigDecimal transAmount, Long appUserId, String stan, String rrn, String province, String district);

    ProcResponse cashoutAcquirer(String fromCardNo, String fromBankName, String fromAccountTitle, String toAccountId, BigDecimal transAmount, Long appUserId, String stan, String rrn, String province, String district);

    AgentCashInResponse agentCashIn(String clientSecret, String channelCode, Long appUserId, String relationshipId, String transmissionDate, String transmissionTime, String stan, String rrn, String fromAccountNumber, String toAccountNumber, String transactionAmount, Float latitude, Float longitude);

    BalanceInquiryResponse balanceInquiry(String clientSecret, String channelCode, String accountNumber, String stan, String rrn);

    ProcResponse cardToCardCredit(String toCardNo, String toBankName, String toAccountTitle, String fromAccountId, BigDecimal transAmount, Long appUserId, String stan, String rrn);

    ProcResponse cardToCardDebit(String fromCardNo, String fromBankName, String fromAccountTitle, String toAccountId, BigDecimal transAmount, Long appUserId, String stan, String rrn);

    ProcResponse cardToWalletIssuer(String toCardNo, String toBankName, String toAccountTitle, String fromAccountNumber, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode);

    ProcResponse cashinIssuer(String fromCardNo, String fromBankName, String fromAccountTitle, String toAccountId, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode);

    ProcResponse cashoutIssuer(String fromAccountNumber, String toCardNo, String toBankName, String toAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode);

    ProcResponse purchaseAcquirer(Long toAccountId, String fromCardNo, String fromBankName, String fromAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn, String cardAcceptorNameLocation, String cardAcceptorTerminalId);

    ProcResponse purchaseIssuer(String fromCardNo, String toCardNo, String toBankName, String toAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn, String cardAcceptorNameLocation, String cardAcceptorTerminalId, String clientSecret, String channelCode);

    TitleFetchResponse titleFetch(String clientSecret, String channelCode, String accountNumber, String stan, String rrn);

    ProcResponse walletToCardIssuer(String toAccountNumber, String fromCardNo, String fromBankName, String fromAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode);

    ProcResponse walletToGl(Long fromAccountId, Long toGlAccountId, BigDecimal transAmount, Long appUserId);

    WalletToWalletResponse walletToWallet(String clientSecret, String channelCode, Long appUserId, String relationshipId, String transmissionDate, String transmissionTime, String stan, String rrn, String dateLocalTran, String timeLocalTran, String acqInstCode, String merchantType, String posEntryMode, String cardAcceptorNameLocation, String cardAcceptorTerminalId, String fromAccountNumber, String fromAccountType, String fromAccountCurrency, String toAccountNumber, String toAccountType, String toAccountCurrency, String transactionAmount, String transactionCurrency, String transactionFee, String udf1, String udf2, String udf3, String udf4, String udf5);

    ProcResponse reversal(ReversalRequest reversalRequest);

    ProcResponse cardToCard(CardToCardProcRequest request);

    ProcResponse billPaymentAcquirer(BillPaymentAcquirerRequest request);

    ProcResponse walletToWalletAcquirer(String accountNo, String pan, String amount, BigDecimal appUserId, String toBankName, String stan, String rrn);

    ProcResponse walletToWalletIssuer(String toAccountNo, String fromAccountNo, String amount, BigDecimal appUserId, String stan, String rrn, String clientSecret, String channelCode);

    ProcResponse purchase(Long toAccountId, String fromCardNo, String fromBankName, String fromAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn, String cardAcceptorNameLocation, String cardAcceptorTerminalId, String clientSecret, String channelCode);
}