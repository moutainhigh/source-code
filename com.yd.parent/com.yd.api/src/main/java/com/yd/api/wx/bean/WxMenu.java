package com.yd.api.wx.bean;

import java.util.List;

public class WxMenu {
	private List<WxMenuButton> button;
	private WxMatchrule	matchrule;

	public List<WxMenuButton> getButton() {
		return button;
	}

	public void setButton(List<WxMenuButton> button) {
		this.button = button;
	}

	public WxMatchrule getMatchrule() {
		return matchrule;
	}

	public void setMatchrule(WxMatchrule matchrule) {
		this.matchrule = matchrule;
	}
}
