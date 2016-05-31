package com.example.yj.mapapp.model;
/**
 * 注册返回的个人信息，登录返回的个人信息
 * @author Administrator
 *
 */

public class UserInfo {
	private String user_id;//用户ID
	private String user_name;//用户名/手机号
	private String nick_name;//昵称
	private String user_img;//默认头像链接
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getUser_img() {
		return user_img;
	}
	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}
	@Override
	public String toString() {
		return "UserInfo [user_id=" + user_id + ", user_name=" + user_name
				+ ", nick_name=" + nick_name + ", user_img=" + user_img + "]";
	}
	
	
}
