package com.wallet.transaction.switching.ibft.outgoing;

import lombok.Data;

/**
 * One entry in the bank picker the customer chooses from before entering a beneficiary account.
 *
 * <p>Only the two values the app needs are exposed - the label to show and the IMD to send back
 * on titleFetch and advice. The rest of LKP_BANK stays server side.</p>
 */
@Data
public class IbftBankOption {

    /** LKP_BANK.BANK_IMD - the value the app must echo back as {@code beneficiaryBankImd}. */
    private String bankImd;

    /** LKP_BANK.BANK_NAME - what the customer sees in the list. */
    private String bankName;

    public IbftBankOption(String bankImd, String bankName) {
        this.bankImd = bankImd;
        this.bankName = bankName;
    }
}
