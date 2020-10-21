package com.yd.api.wx.bean;

public class WxQrCodeReq {
	private Integer expire_seconds;
	private String action_name;
	private ActionInfo action_info;
	
	public Integer getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(Integer expire_seconds) {
		this.expire_seconds = expire_seconds;
	}

	public String getAction_name() {
		return action_name;
	}

	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}

	public ActionInfo getAction_info() {
		if(this.action_info==null) {
			this.action_info=new ActionInfo();
		}
		return this.action_info;
	}

	public void setAction_info(ActionInfo action_info) {
		this.action_info = action_info;
	}

	public static class ActionInfo{
		private Scene scene;

		public Scene getScene() {
			return scene;
		}

		public void setScene(String sceneStr) {
			Scene scene=new Scene(sceneStr);
			this.scene = scene;
		}
	}
	
	public static class Scene{
		private String scene_str;
		private Scene(String sceneStr) {
			this.scene_str=sceneStr;
		}
		public String getScene_str() {
			return scene_str;
		}
		public void setScene_str(String scene_str) {
			this.scene_str = scene_str;
		}
		
	}
	
}
