package com.yd.api.crawer.result;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import java.util.Date;

/**
 *
 * This tools just a simple useful tools, gen table to javabean
 *
 * "I hope this tools can save your time"
 *
 * Generated by <tt>Generate</tt> 
 *
 * @author com.ypn.mapi.generate
 * @version v1.0
 */
public class YdBijiaResult implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	private Integer id;
	private Integer itemId;
	private Integer skuId;
	private Integer jdItemId;
	private Integer guomeiItemId;
	private Integer suningItemId;
	private Integer tmallItemId;
	private Integer officialItemId;
	private Date createTime;
	private Date updateTime;

    //========== getters and setters ==========
    
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getItemId() {
		return itemId;
	}
	
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getSkuId() {
		return skuId;
	}
	
	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}
	public Integer getJdItemId() {
		return jdItemId;
	}
	
	public void setJdItemId(Integer jdItemId) {
		this.jdItemId = jdItemId;
	}
	public Integer getGuomeiItemId() {
		return guomeiItemId;
	}
	
	public void setGuomeiItemId(Integer guomeiItemId) {
		this.guomeiItemId = guomeiItemId;
	}
	public Integer getSuningItemId() {
		return suningItemId;
	}
	
	public void setSuningItemId(Integer suningItemId) {
		this.suningItemId = suningItemId;
	}
	public Integer getTmallItemId() {
		return tmallItemId;
	}
	
	public void setTmallItemId(Integer tmallItemId) {
		this.tmallItemId = tmallItemId;
	}
	public Integer getOfficialItemId() {
		return officialItemId;
	}
	
	public void setOfficialItemId(Integer officialItemId) {
		this.officialItemId = officialItemId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	* 重载toString方法
	* @return
	*
	* @see java.lang.Object#toString()
	*/
	@Override
    public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

