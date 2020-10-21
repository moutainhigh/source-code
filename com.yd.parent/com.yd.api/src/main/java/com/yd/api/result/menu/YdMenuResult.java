package com.yd.api.result.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
public class YdMenuResult implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	private Integer id;
	private String name;
	private String icon;
	private Integer parentMenuId;
	private String permission;
	private Integer sort;
	private String linkUrl;
	private String groupCode;
	private List<YdMenuResult> children=new ArrayList<YdMenuResult>();
    //========== getters and setters ==========
    
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
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
	public Integer getParentMenuId() {
		return parentMenuId;
	}
	
	public void setParentMenuId(Integer parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	public String getPermission() {
		return permission;
	}
	
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public List<YdMenuResult> getChildren() {
		return children;
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

