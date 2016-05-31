package com.example.yj.mapapp.model;

import java.io.Serializable;

/**
 * 应用程序更新实体类
 */
public class Update implements Serializable {

	private String edition;// 版本号
	private String versionName;
	private String url;// 下载连接
	private String content;// 更新内容
	private int level;// 更新等级 1必须更新 2可选更新

	public String getVersionCode() {
		return edition;
	}

	public void setVersionCode(String versionCode) {
		this.edition = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDownloadUrl() {
		return url;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.url = downloadUrl;
	}

	public String getUpdateLog() {
		return content;
	}

	public void setUpdateLog(String updateLog) {
		this.content = updateLog;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
