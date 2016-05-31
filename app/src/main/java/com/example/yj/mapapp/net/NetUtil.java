package com.example.yj.mapapp.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NetUtil {
//    private static final String BASE_URL = "http://192.168.1.105/";
        private static final String BASE_URL = "http://api.51buyjc.com/api/User/UserLogin";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void setTimeout(){
        client.setTimeout(60000);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void download(String url, RequestParams params, FileAsyncHttpResponseHandler fileAsyncHttpResponseHandler){
        client.get(getAbsoluteUrl(url), params, fileAsyncHttpResponseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

//    private static String getAbsoluteUrl(String relativeUrl) {
//        return  relativeUrl;
//    }
}

