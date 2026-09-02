package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TblTransLimitDetailRequest {

	private BigDecimal transLimitDetailId;

	private BigDecimal transDocsId;

}