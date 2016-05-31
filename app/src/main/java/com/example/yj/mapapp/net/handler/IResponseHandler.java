package com.example.yj.mapapp.net.handler;

public interface IResponseHandler {
	public void onCacheData(String content);

	public void success(String data, String resCode, String info);

    public void onFail(int arg0, String arg2, Throwable arg3);
}
