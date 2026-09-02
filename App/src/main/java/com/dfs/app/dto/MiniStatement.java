package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * MiniStatement Class.
 * 
 * @author Arsalan Amjad
 * @version 1.0
 * @see RuntimeException
 * @since 1st october 2018
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MiniStatement implements Serializable {
	private static final long serialVersionUID = 1L;
	private int rowkey;
	private String transDocsDescr;
	private Date transDate;
	private String transRefnum;
	private BigDecimal refNum;
	private BigDecimal txnAmt;
	private BigDecimal feeAmt;
	private String amountType;
	private String amountType2;
	private String toAccountTitle;
	private BigDecimal fAccountId;
	private String fromAccountNo;
	private String fromAccountTitle;
	private BigDecimal taccountId;
	private String toAccountNo;
	private String comments;
	private BigDecimal openingbalance;
	private BigDecimal closingBalance;
	private String printflag;
	private String transDocsCode;
	private String valueDate;
	private BigDecimal totaltransamount;
	private String stan;
	private String rrn;
	private String status;
	private String sourceBank;
	private String destinationBank;
	private String channel;
	private String fAccountType;
	private String tAccountType;
	private BigDecimal chargesId;
	private BigDecimal categoryId;

}
