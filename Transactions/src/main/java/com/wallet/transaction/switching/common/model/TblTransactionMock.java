package com.wallet.transaction.switching.common.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The persistent class for the TBL_TRANSACTION_MOCK database table.
 *
 * Reference data for the mock switch flows: one row per beneficiary account, holding the details a
 * real title fetch would return. The application only ever READS this table - it is maintained by
 * the database team.
 *
 * The table already carries columns for bill payment (UTILITY_*), so the same entity serves the
 * future Bill Payment flow without a schema change.
 */
@Entity
@Table(name = "TBL_TRANSACTION_MOCK")
@Data
public class TblTransactionMock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TRANSACTION_MOCK_ID")
    private BigDecimal transactionMockId;

    @Column(name = "BENEFICIARY_ACCOUNT_NO")
    private String beneficiaryAccountNo;

    @Column(name = "BENEFICIARY_ACCOUNT_TITLE")
    private String beneficiaryAccountTitle;

    /** Interchange Member Descriptor of the beneficiary bank - DE-120 destination IMD. */
    @Column(name = "IMD_NO")
    private String imdNo;

    @Column(name = "BENEFICIARY_BANK_NAME")
    private String beneficiaryBankName;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    @Column(name = "UTILITY_CONSUMER_ID")
    private String utilityConsumerId;

    @Column(name = "UTILITY_COMPANY_NAME")
    private String utilityCompanyName;

    @Column(name = "BILL_AMOUNT")
    private BigDecimal billAmount;

    @Column(name = "BILL_DUE_DATE")
    private String billDueDate;

    @Column(name = "UTILITY_COMPANY_CODE")
    private String utilityCompanyCode;

    @Column(name = "BILL_PAID")
    private String billPaid;

    @Column(name = "LASTUPDATEUSER")
    private BigDecimal lastupdateuser;
}
