package com.csf.csfsdktest;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.csf.csflibrary.Tools.JsonTools;
import com.csf.csflibrary.callback.ContributorView;
import com.csf.csflibrary.callback.DrawOver;
import com.csf.csflibrary.constant.Constant;
import com.csf.csflibrary.javaBean.RespObj;
import com.csf.csflibrary.presenter.ContributorPresenter;
import com.csf.csflibrary.view.MyCharLandScapeLinearlayout;

/**
 * @author jaily.zhang
 * @version V1.3.1
 * @Description 个股图表横屏页面
 * @date 2015-6-10 下午2:34:50
 */
public class MainLandScapeActivity extends Activity implements ContributorView,DrawOver {

    private ApplicationInfo appInfo;
    private String AccessKey;//AccessKey
    private String SecretKey;//SecretKey
    ContributorPresenter contributorPresenter;
    private MyCharLandScapeLinearlayout mycharlinearlayout;
    String code;
    String selectedFlag;
    private boolean charClickOneboolean;
    private boolean charClickTwoboolean;
    private boolean charClickThreeboolean;
    private boolean charClickFourboolean;
    private boolean charClickFiveboolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landscapeactivity_main);
        initView();
        code = getIntent().getExtras().getString("input");
        selectedFlag = getIntent().getExtras().getString("selectedFlag");
        AccessKey = getAppInfo().metaData.getString("CSF_ACCESSKEY");
        SecretKey = getAppInfo().metaData.getString("CSF_SECRETKEY");
        contributorPresenter = new ContributorPresenter(this, this, AccessKey, SecretKey, true);

        if (selectedFlag.equals(Constant.CSF_SHARE_TIMEDATA)) {
            //改变View
            mycharlinearlayout.changeTimeSharedView();
            //设置标签
            mycharlinearlayout.setClickFlag(Constant.CSF_SHARE_TIMEDATA);
            //股票分时数据
            contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_SHARE_TIMEDATA);
        }

        if (selectedFlag.equals(Constant.CSF_SHARE_FIVEDATA)) {
            //改变View
            mycharlinearlayout.changeFiveDailyView();
            //设置标签
            mycharlinearlayout.setClickFlag(Constant.CSF_SHARE_FIVEDATA);
            //股票五日分时数据
            contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_SHARE_FIVEDATA);
        }

        if (selectedFlag.equals(Constant.CSF_DAILY_KDATA)) {
            //改变View
            mycharlinearlayout.changeDailyKView();
            //设置标签
            mycharlinearlayout.setClickFlag(Constant.CSF_DAILY_KDATA);
            //股票日度行情历史数据
            contributorPresenter.RealTimeRefresh(code, 10, Constant.CSF_DAILY_KDATA);
        }

        if (selectedFlag.equals(Constant.CSF_WEEK_KDATA)) {
            //改变View
            mycharlinearlayout.changeWeekView();
            //设置标签
            mycharlinearlayout.setClickFlag(Constant.CSF_WEEK_KDATA);
            //股票周度行情历史数据
            contributorPresenter.RealTimeRefresh(code, 20, Constant.CSF_WEEK_KDATA);
        }


        if (selectedFlag.equals(Constant.CSF_MONTH_KDATA)) {
            //改变View
            mycharlinearlayout.changeMonthView();
            //设置标签
            mycharlinearlayout.setClickFlag(Constant.CSF_MONTH_KDATA);
            //股票月度行情历史数据
            contributorPresenter.RealTimeRefresh(code, 30, Constant.CSF_MONTH_KDATA);
        }

    }


    private void initView() {
        mycharlinearlayout = (MyCharLandScapeLinearlayout) findViewById(R.id.mycharlinearlayout);
        mycharlinearlayout.setDrawOver(this);
        mycharlinearlayout.setOnTouchListenerEvent();
        mycharlinearlayout.setTimeSharedClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //改变View
                mycharlinearlayout.changeTimeSharedView();
                //设置标签
                mycharlinearlayout.setClickFlag(Constant.CSF_SHARE_TIMEDATA);
                selectedFlag=Constant.CSF_SHARE_TIMEDATA;
                //股票分时数据
                contributorPresenter.stopRefresh();
                contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_SHARE_TIMEDATA);

            }
        });
        mycharlinearlayout.setFiveDailyClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //改变View
                mycharlinearlayout.changeFiveDailyView();
                //设置标签
                mycharlinearlayout.setClickFlag(Constant.CSF_SHARE_FIVEDATA);
                selectedFlag=Constant.CSF_SHARE_FIVEDATA;
                //股票五日分时数据
                contributorPresenter.stopRefresh();
                contributorPresenter.RealTimeRefresh(code, 5, Constant.CSF_SHARE_FIVEDATA);

            }
        });
        mycharlinearlayout.setDailyKClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //改变View
                    mycharlinearlayout.changeDailyKView();
                    //设置标签
                    mycharlinearlayout.setClickFlag(Constant.CSF_DAILY_KDATA);
                    selectedFlag=Constant.CSF_DAILY_KDATA;
                    //股票日度行情历史数据
                    contributorPresenter.stopRefresh();
                    contributorPresenter.RealTimeRefresh(code, 10, Constant.CSF_DAILY_KDATA);

                } catch (Exception e) {

                }

            }
        });
        mycharlinearlayout.setWeekKClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //改变View
                    mycharlinearlayout.changeWeekView();
                    //设置标签
                    mycharlinearlayout.setClickFlag(Constant.CSF_WEEK_KDATA);
                    selectedFlag = Constant.CSF_WEEK_KDATA;
                    //股票周度行情历史数据
                    contributorPresenter.stopRefresh();
                    contributorPresenter.RealTimeRefresh(code, 20, Constant.CSF_WEEK_KDATA);

                } catch (Exception e) {

                }
            }
        });
        mycharlinearlayout.setMonthClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //改变View
                    mycharlinearlayout.changeMonthView();
                    //设置标签
                    mycharlinearlayout.setClickFlag(Constant.CSF_MONTH_KDATA);
                    selectedFlag = Constant.CSF_MONTH_KDATA;
                    //股票月度行情历史数据
                    contributorPresenter.stopRefresh();
                    contributorPresenter.RealTimeRefresh(code, 30, Constant.CSF_MONTH_KDATA);

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

    public ApplicationInfo getAppInfo() {
        if (appInfo == null) {
            try {
                appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            } catch (PackageManager.NameNotFoundException e) {

            }
        }
        return appInfo;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }

    @Override
    public void onLoadContributorStart() {
        if (selectedFlag.equals(Constant.CSF_SHARE_TIMEDATA)) {
            if (!charClickOneboolean)
                mycharlinearlayout.setProgressbarVisible();
            charClickOneboolean = true;

        } else if (selectedFlag.equals(Constant.CSF_SHARE_FIVEDATA)) {
            if (!charClickTwoboolean)
                mycharlinearlayout.setProgressbarVisible();
            charClickTwoboolean = true;

        } else if (selectedFlag.equals(Constant.CSF_DAILY_KDATA)) {
            if (!charClickThreeboolean)
                mycharlinearlayout.setProgressbarVisible();
            charClickThreeboolean = true;

        } else if (selectedFlag.equals(Constant.CSF_WEEK_KDATA)) {
            if (!charClickFourboolean)
                mycharlinearlayout.setProgressbarVisible();
            charClickFourboolean = true;

        } else if (selectedFlag.equals(Constant.CSF_MONTH_KDATA)) {
            if (!charClickFiveboolean)
                mycharlinearlayout.setProgressbarVisible();
            charClickFiveboolean = true;

        }
    }


    @Override
    public void onLoadContributorSuccess(String jsonString, String flag) {
        //实时个股信息
        if (flag.equals(Constant.CSF_REALTIME_STATE)) {
            try {
                RespObj respObj = JsonTools.jsonStringToObject(jsonString, RespObj.class);
                if (respObj.getCode().equals("200") && respObj.getMessage() != null) {
                    String messageString = JsonTools.objectToJsonString(respObj.getMessage());
                    mycharlinearlayout.showStockRealtimeInfo(this, messageString);
                }
            } catch (Exception e) {

            }
        }
        //股票五档委托信息
        if (flag.equals(Constant.CSF_FIVE_STATE)) {
            try {
                RespObj respObj = JsonTools.jsonStringToObject(jsonString, RespObj.class);
                if (respObj.getCode().equals("200") && respObj.getMessage() != null) {
                    String messageString = JsonTools.objectToJsonString(respObj.getMessage());
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
                            mycharlinearlayout.showKvalueJsonString(messageString, Constant.CSF_MONTH_KDATA);
                        }
                    }).start();

                }
            } catch (Exception e) {

            }
        }

    }


    @Override
    public void onLoadContributorComplete() {
    }

    @Override
    public void drawOver() {
        mycharlinearlayout.setProgressbarInvisible();
    }

    @Override
    public void onLoadContributorError() {

    }
}
