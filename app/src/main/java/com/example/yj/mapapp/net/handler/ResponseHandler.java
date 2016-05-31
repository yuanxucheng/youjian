package com.example.yj.mapapp.net.handler;

import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public abstract  class ResponseHandler extends TextHttpResponseHandler implements
		IResponseHandler {

	@Override
	public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
		LogUtil.w("========ResponseHandler_fail=========" + arg2);
		LogUtil.w("========ResponseHandler_fail====arg3====="+arg3.getMessage());
		for (Header h:arg1) {
			LogUtil.w("header==========="+h);
		}
		onFail(arg0, arg2, arg3);
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, String content) {
		// TODO Auto-generated method stub
		LogUtil.w("========ResponseHandler=========" + content);
		System.out.println();
		LogUtil.w(arg0 + "");
		for (Header h:arg1) {
			LogUtil.w("header==========="+h);
		}
//		success(JsonUtil.getData(content), JsonUtil.getShiKuCode(content), JsonUtil.getShiKuUserId(content));
		success(JsonUtil.getData(content), "", "");//处理了
		LogUtil.d("content=========="+JsonUtil.getData(content));
//		success(content, "", "");//没处理
	}

	
	
}
