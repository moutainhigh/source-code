package com.yd.api.crawer.result;

import java.io.Serializable;

public class CrawerSpecVal implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String propId;
	private String value;
	
	public String getPropId() {
		return propId;
	}
	public void setPropId(String propId) {
		this.propId = propId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
