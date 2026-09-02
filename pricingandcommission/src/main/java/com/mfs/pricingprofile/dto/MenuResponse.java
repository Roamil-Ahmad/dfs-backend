package com.mfs.pricingprofile.dto;

import java.math.BigDecimal;

public class MenuResponse {

	private BigDecimal menuId;

	private BigDecimal level;

	private String name;

	private BigDecimal parent_id;

	private String bussfunc;

	private String icon;

	private String url;

	public BigDecimal getMenuId() {
		return menuId;
	}

	public void setMenuId(BigDecimal menuId) {
		this.menuId = menuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBussfunc() {
		return bussfunc;
	}

	public void setBussfunc(String bussfunc) {
		this.bussfunc = bussfunc;
	}

	public BigDecimal getLevel() {
		return level;
	}

	public void setLevel(BigDecimal level) {
		this.level = level;
	}

	public BigDecimal getParent_id() {
		return parent_id;
	}

	public void setParent_id(BigDecimal parent_id) {
		this.parent_id = parent_id;
	}
}
