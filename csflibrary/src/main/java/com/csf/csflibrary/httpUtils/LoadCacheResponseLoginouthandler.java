package com.csf.csflibrary.httpUtils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

public class LoadCacheResponseLoginouthandler extends AsyncHttpResponseHandler {

	private Context context;
	private LoadDatahandler mHandler;

	public LoadCacheResponseLoginouthandler(Context context,
			LoadDatahandler mHandler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mHandler = mHandler;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mHandler.onStart();
	
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		mHandler.onFailure("", "网络连接超时");
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] content) {
		// TODO Auto-generated method stub
		try {
			switch (statusCode) {
			case 200:
				mHandler.onSuccess(new String(content));
				break;
			case 401:
				onFailure("401", "没有登录");
				break;
			case 403:
				onFailure("404", "没有权限");
				break;
			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		super.onFinish();
		mHandler.onFinish();
	}

	/**
	 * 出错
	 * 
	 * @param error
	 * @param errorMessage
	 */
	public void onFailure(String error, String errorMessage) {
		if (errorMessage != null) {
			mHandler.onFailure(error, errorMessage);
		}
	}

}
