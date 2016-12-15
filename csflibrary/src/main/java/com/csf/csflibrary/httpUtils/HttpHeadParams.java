package com.csf.csflibrary.httpUtils;

/**
 * @author jaily.zhang http head 字段类
 */
public class HttpHeadParams {

    private String t;// 时间戳
    private String accessKey;//
    private String token;// 推送设备token

    private static final HttpHeadParams instance = new HttpHeadParams();

    public static HttpHeadParams getInstance() {
        return instance;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
