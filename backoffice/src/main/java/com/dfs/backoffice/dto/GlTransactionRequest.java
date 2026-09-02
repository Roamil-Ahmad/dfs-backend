package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlTransactionRequest {
	private long fromAccountId;
	private long toAccountId;
	private long transAmount;
}
