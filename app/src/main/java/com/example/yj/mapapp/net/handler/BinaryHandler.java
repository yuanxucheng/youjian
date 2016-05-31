package com.example.yj.mapapp.net.handler;


import com.loopj.android.http.BinaryHttpResponseHandler;

import org.apache.http.Header;

public abstract class BinaryHandler extends BinaryHttpResponseHandler {

    @Override
    public String[] getAllowedContentTypes() {
        return new String[]{".*"};
    }
    
//	@Override
//	public void onProgress(int bytesWritten, int totalSize) {
//		super.onProgress(bytesWritten, totalSize);
//		progress(bytesWritten, totalSize);
//	}

    @Override
    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
        progress(bytesWritten, totalSize);
    }

    @Override
	public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
		success(headers,binaryData);
	}
	
	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
		
		failure(statusCode,headers,errorResponse,e);
	}
	
	public abstract void progress(long bytesWritten, long totalSize);
	
	public abstract void success(Header[] headers,byte[] binaryData);
	
	public abstract void failure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e);

}
