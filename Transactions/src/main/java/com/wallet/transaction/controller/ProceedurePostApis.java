package com.wallet.transaction.controller;

import com.wallet.transaction.dto.*;
import com.wallet.transaction.service.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/procedures")
public class ProceedurePostApis {

    @Autowired
    private ProcedureService procedureService;

    @PostMapping("/wallet-to-card-acquirer")
    public ResponseEntity<ProcResponse> walletToCardAcquirer(@RequestBody WalletToCardAcquirerRequest request) {
        ProcResponse response = procedureService.walletToCardAcquirer(
                request.getFromAccountId(),
                request.getToCardNo(),
                request.getToBankName(),
                request.getToAccountTitle(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/wallet-to-card-issuer")
    public ResponseEntity<ProcResponse> walletToCardIssuer(@RequestBody WalletToCardIssuerRequest request) {
        ProcResponse response = procedureService.walletToCardIssuer(
                request.getToAccountNumber(),
                request.getFromCardNo(),
                request.getFromBankName(),
                request.getFromAccountTitle(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getClientSecret(),
                request.getChannelCode()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/card-to-wallet-acquirer")
    public ResponseEntity<ProcResponse> cardToWalletAcquirer(@RequestBody CardToWalletAcquirerRequest request) {
        ProcResponse response = procedureService.cardToWalletAcquirer(
                request.getFromCardNo(),
                request.getFromBankName(),
                request.getFromAccountTitle(),
                request.getToAccountId(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getClientSecret(),
                request.getChannelCode()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/card-to-wallet-issuer")
    public ResponseEntity<ProcResponse> cardToWalletIssuer(@RequestBody CardToWalletIssuerRequest request) {
        ProcResponse response = procedureService.cardToWalletIssuer(
                request.getToCardNo(),
                request.getToBankName(),
                request.getToAccountTitle(),
                request.getFromAccountNumber(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getClientSecret(),
                request.getChannelCode()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/cashin-acquirer")
    public ResponseEntity<ProcResponse> cashinAcquirer(@RequestBody CashinAcquirerRequest request) {
        ProcResponse response = procedureService.cashinAcquirer(
                request.getToCardNo(),
                request.getToBankName(),
                request.getToAccountTitle(),
                request.getFromAccountId(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getProvince(),
                request.getDitrict()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/cashin-issuer")
    public ResponseEntity<ProcResponse> cashinIssuer(@RequestBody CashinIssuerRequest request) {
        ProcResponse response = procedureService.cashinIssuer(
                request.getFromCardNo(),
                request.getFromBankName(),
                request.getFromAccountTitle(),
                request.getToAccountNumber(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getClientSecret(),
                request.getChannelCode()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/cashout-acquirer")
    public ResponseEntity<ProcResponse> cashoutAcquirer(@RequestBody CashoutAcquirerRequest request) {
        ProcResponse response = procedureService.cashoutAcquirer(
                request.getFromCardNo(),
                request.getFromBankName(),
                request.getFromAccountTitle(),
                request.getToAccountId(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getProvince(),
                request.getDitrict()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/cashout-issuer")
    public ResponseEntity<ProcResponse> cashoutIssuer(@RequestBody CashoutIssuerRequest request) {
        ProcResponse response = procedureService.cashoutIssuer(
                request.getFromCardNo(),
                request.getToCardNo(),
                request.getToBankName(),
                request.getToAccountTitle(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getClientSecret(),
                request.getChannelCode()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/agent-cash-in")
    public ResponseEntity<AgentCashInResponse> agentCashIn(@RequestBody AgentCashInRequest request) {
        AgentCashInResponse response = procedureService.agentCashIn(
                request.getClientSecret(),
                request.getChannelCode(),
                request.getAppUserId(),
                request.getRelationshipId(),
                request.getTransmissionDate(),
                request.getTransmissionTime(),
                request.getStan(),
                request.getRrn(),
                request.getFromAccountNumber(),
                request.getToAccountNumber(),
                request.getTransactionAmount(),
                request.getLatitude(),
                request.getLongitude()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/balance-inquiry")
    public ResponseEntity<BalanceInquiryResponse> balanceInquiry(@RequestBody BalanceInquiryRequest request) {
        BalanceInquiryResponse response = procedureService.balanceInquiry(
                request.getClientSecret(),
                request.getChannelCode(),
                request.getAccountNumber(),
                request.getStan(),
                request.getRrn()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/card-to-card-credit")
    public ResponseEntity<ProcResponse> cardToCardCredit(@RequestBody CardToCardCreditRequest request) {
        ProcResponse procResponse = procedureService.cardToCardCredit(
                request.getToCardNo(),
                request.getToBankName(),
                request.getToAccountTitle(),
                request.getFromAccountId(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn()
        );
        return ResponseEntity.ok(procResponse);
    }
    @PostMapping("/card-to-card-debit")
    public ResponseEntity<ProcResponse> cardToCardDebit(@RequestBody CardToCardDebitRequest request) {
        ProcResponse response = procedureService.cardToCardDebit(
                request.getFromCardNo(),
                request.getFromBankName(),
                request.getFromAccountTitle(),
                request.getToAccountId(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/purchase-acquirer")
    public ResponseEntity<ProcResponse> purchaseAcquirer(@RequestBody PurchaseAcquirerRequest request) {
        ProcResponse response = procedureService.purchaseAcquirer(
                request.getToAccountId(),
                request.getFromCardNo(),
                request.getFromBankName(),
                request.getFromAccountTitle(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getCardAcceptorNameLocation(),
                request.getCardAcceptorTerminalId()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/purchase-issuer")
    public ResponseEntity<ProcResponse> purchaseIssuer(@RequestBody PurchaseIssuerRequest request) {
        ProcResponse response = procedureService.purchaseIssuer(
                request.getFromCardNo(),
                request.getToCardNo(),
                request.getToBankName(),
                request.getToAccountTitle(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getCardAcceptorNameLocation(),
                request.getCardAcceptorTerminalId(),
                request.getClientSecret(),
                request.getChannelCode()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/title-fetch")
    public ResponseEntity<TitleFetchResponse> titleFetch(@RequestBody TitleFetchRequest request) {
        TitleFetchResponse response = procedureService.titleFetch(
                request.getClientSecret(),
                request.getChannelCode(),
                request.getAccountNumber(),
                request.getStan(),
                request.getRrn()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/wallet-to-gl")
    public ResponseEntity<ProcResponse> walletToGl(@RequestBody WalletToGlRequest request) {
        ProcResponse response = procedureService.walletToGl(
                request.getFromAccountId(),
                request.getToGlAccountId(),
                request.getTransAmount(),
                request.getAppUserId()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/wallet-to-wallet")
    public ResponseEntity<WalletToWalletResponse> walletToWallet(@RequestBody WalletToWalletRequest request) {
        WalletToWalletResponse response = procedureService.walletToWallet(
                request.getClientSecret(),
                request.getChannelCode(),
                request.getAppUserId(),
                request.getRelationshipId(),
                request.getTransmissionDate(),
                request.getTransmissionTime(),
                request.getStan(),
                request.getRrn(),
                request.getDateLocalTran(),
                request.getTimeLocalTran(),
                request.getAcqInstCode(),
                request.getMerchantType(),
                request.getPosEntryMode(),
                request.getCardAcceptorNameLocation(),
                request.getCardAcceptorTerminalId(),
                request.getFromAccountNumber(),
                request.getFromAccountType(),
                request.getFromAccountCurrency(),
                request.getToAccountNumber(),
                request.getToAccountType(),
                request.getToAccountCurrency(),
                request.getTransactionAmount(),
                request.getTransactionCurrency(),
                request.getTransactionFee(),
                request.getUdf1(),
                request.getUdf2(),
                request.getUdf3(),
                request.getUdf4(),
                request.getUdf5()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/reversal")
    public ResponseEntity<ProcResponse> reversal(@RequestBody ReversalRequest request) {
        ProcResponse response = procedureService.reversal(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/card-to-card")
    public ResponseEntity<ProcResponse> cardToCard(@RequestBody CardToCardProcRequest request) {
        ProcResponse response = procedureService.cardToCard(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/bill-payment-acquirer")
    public ResponseEntity<ProcResponse> billPaymentAcquirer(@RequestBody BillPaymentAcquirerRequest request) {
        ProcResponse response = procedureService.billPaymentAcquirer(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/bill-payment-issuer")
    public ResponseEntity<ProcResponse> billPaymentIssuer(@RequestBody BillPaymentIssuerRequest request) {
        ProcResponse response = procedureService.billPaymentIssuer(
                request.getFromCardNo(),
                request.getUtilityCompanyCode(),
                request.getUtilityConsumerNo(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getClientSecret(),
                request.getChannelCode()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/wallet-to-wallet-acquirer")
    public ResponseEntity<ProcResponse> walletToWalletAcquirer(@RequestBody WalletToWalletRqst request) {
        ProcResponse response = procedureService.walletToWalletAcquirer(
                request.getAccountNo(),
                request.getPan(),
                request.getAmount(),
                request.getAppUserId(),
                request.getToBankName(),
                request.getStan(),
                request.getRrn()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/wallet-to-wallet-issuer")
    public ResponseEntity<ProcResponse> walletToWalletIssuer(@RequestBody WalletToWalletIssuerRequest request) {
        ProcResponse response = procedureService.walletToWalletIssuer(
                request.getToAccountNo(),
                request.getFromAccountNo(),
                request.getAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getClientSecret(),
                request.getChannelCode()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/purchase")
    public ResponseEntity<ProcResponse> purchase(@RequestBody PurchaseProcRequest request) {
        ProcResponse response = procedureService.purchase(
                request.getToAccountId(),
                request.getFromCardNo(),
                request.getFromBankName(),
                request.getFromAccountTitle(),
                request.getTransAmount(),
                request.getAppUserId(),
                request.getStan(),
                request.getRrn(),
                request.getCardAcceptorNameLocation(),
                request.getCardAcceptorTerminalId(),
                request.getClientSecret(),
                request.getChannelCode()
        );
        return ResponseEntity.ok(response);
    }
}