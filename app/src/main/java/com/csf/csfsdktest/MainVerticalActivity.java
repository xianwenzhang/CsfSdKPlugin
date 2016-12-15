package com.csf.csfsdktest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.csf.csflibrary.Tools.JsonTools;
import com.csf.csflibrary.callback.ContributorView;
import com.csf.csflibrary.callback.DrawOver;
import com.csf.csflibrary.constant.Constant;
import com.csf.csflibrary.javaBean.RespObj;
import com.csf.csflibrary.presenter.ContributorPresenter;
import com.csf.csflibrary.view.MyCharLinearlayout;

/**
 * 竖屏Activity
 */
public class MainVerticalActivity extends Activity implements ContributorView, DrawOver {


    private ApplicationInfo appInfo;
    private String AccessKey;//AccessKey
    private String SecretKey;//SecretKey
    ContributorPresenter contributorPresenter;
    private String code;
    private MyCharLinearlayout mycharlinearlayout;
    String selectedFlag = Constant.CSF_SHARE_TIMEDATA;
    private boolean charClickOneboolean;
    private boolean charClickTwoboolean;
    private boolean charClickThreeboolean;
    private boolean charClickFourboolean;
    private boolean charClickFiveboolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        code = getIntent().getExtras().getString("input");
        AccessKey = getAppInfo().metaData.getString("CSF_ACCESSKEY");
        SecretKey = getAppInfo().metaData.getString("CSF_SECRETKEY");
        contributorPresenter = new ContributorPresenter(this, this, AccessKey, SecretKey, false);
        //分时数据
        contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_SHARE_TIMEDATA);
    }

    private void initView() {
        mycharlinearlayout = (MyCharLinearlayout) findViewById(R.id.mycharlinearlayout);
        mycharlinearlayout.setDrawOver(this);
        //点击该控件进入图表横屏界面
        mycharlinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainVerticalActivity.this, MainLandScapeActivity.class);
                intent.putExtra("selectedFlag", selectedFlag);
                intent.putExtra("input", code);
                startActivity(intent);
            }
        });
        //分时点击事件
        mycharlinearlayout.setTimeSharedClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFlag = Constant.CSF_SHARE_TIMEDATA;
                //改变View
                mycharlinearlayout.changeTimeSharedView();
                //股票分时数据
                contributorPresenter.stopRefresh();
                contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_SHARE_TIMEDATA);
            }
        });
        //五日点击事件
        mycharlinearlayout.setFiveDailyClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFlag = Constant.CSF_SHARE_FIVEDATA;
                //改变View
                mycharlinearlayout.changeFiveDailyView();
                //股票五日分时数据
                contributorPresenter.stopRefresh();
                contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_SHARE_FIVEDATA);
            }
        });
        //日K点击事件
        mycharlinearlayout.setDailyKClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFlag = Constant.CSF_DAILY_KDATA;
                try {
                    //改变View
                    mycharlinearlayout.changeDailyKView();
                    //股票日度行情历史数据
                    contributorPresenter.stopRefresh();
                    contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_DAILY_KDATA);
                } catch (Exception e) {

                }

            }
        });
        //周Ｋ点击事件
        mycharlinearlayout.setWeekKClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFlag = Constant.CSF_WEEK_KDATA;
                try {
                    //改变View
                    mycharlinearlayout.changeWeekView();
                    //股票周度行情历史数据
                    contributorPresenter.stopRefresh();
                    contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_WEEK_KDATA);
                } catch (Exception e) {

                }
            }
        });
        //月Ｋ点击事件
        mycharlinearlayout.setMonthClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFlag = Constant.CSF_MONTH_KDATA;
                try {
                    //改变View
                    mycharlinearlayout.changeMonthView();
                    //股票月度行情历史数据
                    contributorPresenter.stopRefresh();
                    contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_MONTH_KDATA);
                } catch (Exception e) {

                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        contributorPresenter.stopRefresh();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //分时点击事件
        if (selectedFlag.equals(Constant.CSF_SHARE_TIMEDATA)) {
            contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_SHARE_TIMEDATA);
        }
        //五日点击事件
        if (selectedFlag.equals(Constant.CSF_SHARE_FIVEDATA)) {
            contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_SHARE_FIVEDATA);
        }

        //日K点击事件
        if (selectedFlag.equals(Constant.CSF_DAILY_KDATA)) {
            contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_DAILY_KDATA);
        }

        //周Ｋ点击事件
        if (selectedFlag.equals(Constant.CSF_WEEK_KDATA)) {
            contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_WEEK_KDATA);
        }

        //月Ｋ点击事件
        if (selectedFlag.equals(Constant.CSF_MONTH_KDATA)) {
            contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_MONTH_KDATA);
        }
    }

    public ApplicationInfo getAppInfo() {
        if (appInfo == null) {
            try {
                appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            } catch (PackageManager.NameNotFoundException e) {

            }
        }
        return appInfo;
    }


    //网络请求接口之前操作，比如显示progerssbar条
    @Override
    public void onLoadContributorStart() {
        if (selectedFlag.equals(Constant.CSF_SHARE_TIMEDATA)) {
            if (!charClickOneboolean)
                mycharlinearlayout.setProgressVisible();
            charClickOneboolean = true;

        } else if (selectedFlag.equals(Constant.CSF_SHARE_FIVEDATA)) {
            if (!charClickTwoboolean)
                mycharlinearlayout.setProgressVisible();
            charClickTwoboolean = true;

        } else if (selectedFlag.equals(Constant.CSF_DAILY_KDATA)) {
            if (!charClickThreeboolean)
                mycharlinearlayout.setProgressVisible();
            charClickThreeboolean = true;

        } else if (selectedFlag.equals(Constant.CSF_WEEK_KDATA)) {
            if (!charClickFourboolean)
                mycharlinearlayout.setProgressVisible();
            charClickFourboolean = true;

        } else if (selectedFlag.equals(Constant.CSF_MONTH_KDATA)) {
            if (!charClickFiveboolean)
                mycharlinearlayout.setProgressVisible();
            charClickFiveboolean = true;

        }

    }


    @Override
    public void onLoadContributorSuccess(String jsonString, String flag) {
        //股票五档委托信息
        if (flag.equals(Constant.CSF_FIVE_STATE)) {
            try {
                RespObj respObj = JsonTools.jsonStringToObject(jsonString, RespObj.class);
                if (respObj.getCode().equals("200") && respObj.getMessage() != null) {
                    String messageString = JsonTools.objectToJsonString(respObj.getMessage());
                    //5档数据显示
                    mycharlinearlayout.showStockRealtimeState(this, messageString);
                }
            } catch (Exception e) {

            }
        }
        //股票分时数据
        else if (flag.equals(Constant.CSF_SHARE_TIMEDATA)) {
            try {
                RespObj respObj = JsonTools.jsonStringToObject(jsonString, RespObj.class);
                if (respObj.getCode().equals("200") && respObj.getMessage() != null) {
                    final String messageString = JsonTools.objectToJsonString(respObj.getMessage());
                    //绘图的数据需计算，需耗时，建议加个线程操作
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //分时图表显示
                            mycharlinearlayout.showStockTimeShare(messageString, Constant.CSF_SHARE_TIMEDATA);
                        }
                    }).start();

                }
            } catch (Exception e) {

            }
        }
        //股票五日分时数据
        else if (flag.equals(Constant.CSF_SHARE_FIVEDATA)) {
            try {
                RespObj respObj = JsonTools.jsonStringToObject(jsonString, RespObj.class);
                if (respObj.getCode().equals("200") && respObj.getMessage() != null) {
                    final String messageString = JsonTools.objectToJsonString(respObj.getMessage());
                    //绘图的数据需计算，需耗时，建议加个线程操作
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //五日图表显示
                            mycharlinearlayout.showStockTimeShare(messageString, Constant.CSF_SHARE_FIVEDATA);
                        }
                    }).start();

                }
            } catch (Exception e) {

            }
        }
        //股票日度行情历史数据
        else if (flag.equals(Constant.CSF_DAILY_KDATA)) {
            try {
                RespObj respObj = JsonTools.jsonStringToObject(jsonString, RespObj.class);
                if (respObj.getCode().equals("200") && respObj.getMessage() != null) {
                    final String messageString = JsonTools.objectToJsonString(respObj.getMessage());
                    //绘图的数据需计算，需耗时，建议加个线程操作
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //日K图表显示
                            mycharlinearlayout.showKvalueJsonString(messageString, Constant.CSF_DAILY_KDATA);
                        }
                    }).start();

                }
            } catch (Exception e) {

            }
        }
        //股票周度行情历史数据
        else if (flag.equals(Constant.CSF_WEEK_KDATA)) {
            try {
                RespObj respObj = JsonTools.jsonStringToObject(jsonString, RespObj.class);
                if (respObj.getCode().equals("200") && respObj.getMessage() != null) {
                    final String messageString = JsonTools.objectToJsonString(respObj.getMessage());
                    //绘图的数据需计算，需耗时，建议加个线程操作
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //周Ｋ图表显示
                            mycharlinearlayout.showKvalueJsonString(messageString, Constant.CSF_WEEK_KDATA);
                        }
                    }).start();

                }
            } catch (Exception e) {

            }
        }
        //股票月度行情历史数据
        else if (flag.equals(Constant.CSF_MONTH_KDATA)) {
            try {
                RespObj respObj = JsonTools.jsonStringToObject(jsonString, RespObj.class);
                if (respObj.getCode().equals("200") && respObj.getMessage() != null) {
                    final String messageString = JsonTools.objectToJsonString(respObj.getMessage());
                    //绘图的数据需计算，需耗时，建议加个线程操作
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //月Ｋ图表显示
                            mycharlinearlayout.showKvalueJsonString(messageString, Constant.CSF_MONTH_KDATA);
                        }
                    }).start();

                }
            } catch (Exception e) {

            }
        }

    }


    //网络接口请求完成，比如进度条消失
    @Override
    public void onLoadContributorComplete() {
    }

    @Override
    public void drawOver() {
        mycharlinearlayout.setProgressInvisible();
    }

    @Override
    public void onLoadContributorError() {

    }
}
