package com.example.yj.mapapp.base;
public interface IResponseHandler {
	public void onCaceData(String content);

	public void success(String data, String resCode, String info);
}
