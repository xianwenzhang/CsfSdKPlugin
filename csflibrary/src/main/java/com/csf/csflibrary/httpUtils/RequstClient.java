package com.csf.csflibrary.httpUtils;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 异步网络客户端
 *
 * @author jaily.zhang
 */
public class RequstClient {

    /**
     * get 请求
     *
     * @param url     url地址
     * @param params  参数
     * @param handler handler句柄
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        try {
//            if (params != null)
//                Log.e("error", "get" + url + "?" + params.toString());
//            else
//                Log.e("error", "get" + url);
            AsyncHttpClient mClient = new AsyncHttpClient();
            mClient.addHeader("access_key", HttpHeadParams.getInstance().getAccessKey());
            mClient.addHeader("t", HttpHeadParams.getInstance().getT());
            mClient.addHeader("token", HttpHeadParams.getInstance().getToken());
            mClient.get(url, params, handler);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
