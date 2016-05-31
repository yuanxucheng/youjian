package com.example.yj.mapapp.base;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BaseRequestHttpClient {
	private AsyncHttpClient client;
	private Context context;
	private long time;
	private long nowTime;
	private static final int timeOut = 1000 * 30;

	public BaseRequestHttpClient() {
		client = new AsyncHttpClient();
		client.setTimeout(timeOut);

	}

	public BaseRequestHttpClient(Context context) {
		this.context = context;
		client = new AsyncHttpClient();
		client.setTimeout(timeOut);
	}

	public BaseRequestHttpClient(Context context, String type) {
		this.context = context;
		client = new AsyncHttpClient();
		client.setTimeout(timeOut);
	}

	

	
	
	public void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}

	
	
}
