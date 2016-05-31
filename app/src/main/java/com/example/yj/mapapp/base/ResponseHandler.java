package com.example.yj.mapapp.base;

import org.apache.http.Header;

import com.example.yj.mapapp.util.JsonUtil;
import com.loopj.android.http.TextHttpResponseHandler;

public class ResponseHandler extends TextHttpResponseHandler implements
		IResponseHandler {

	@Override
	public void onCaceData(String content) {

	}

	@Override
	public void success(String data, String resCode ,String info) {

	}

	@Override
	public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
		
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, String content) {

		success(JsonUtil.getData(content), JsonUtil.getResCode(content),JsonUtil.getInfo(content));
	}

	
	
}
