package com.dfs.backoffice.dto;

import com.dfs.backoffice.model.TblGlAccount;

import java.util.List;

public class GlTreeResponse {

	private String key;
	private TblGlAccount data;
	private List<GlTreeChildResponse> children;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public TblGlAccount getData() {
		return data;
	}

	public void setData(TblGlAccount data) {
		this.data = data;
	}

	public List<GlTreeChildResponse> getChildren() {
		return children;
	}

	public void setChildren(List<GlTreeChildResponse> children) {
		this.children = children;
	}

}
