package com.yd.api.crawer.result;

import java.io.Serializable;
import java.util.List;

public class CrawerSpecNameWithSpecVal implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String specName;
	private List<CrawerSpecVal> specValList;
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	public List<CrawerSpecVal> getSpecValList() {
		return specValList;
	}
	public void setSpecValList(List<CrawerSpecVal> specValList) {
		this.specValList = specValList;
	}
}
