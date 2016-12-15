package com.csf.csflibrary.callback;


/**
 * Created by kangzhe on 16/4/7.
 */
public interface ContributorView {

    void onLoadContributorStart();

    /**
     * 获得网络接口传回来的json字符串
     * @param flag
     * @param jsonString
     */
    void onLoadContributorSuccess(String jsonString,String flag);

    void onLoadContributorComplete();

    void onLoadContributorError();
}
