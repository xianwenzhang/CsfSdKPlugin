package com.csf.csflibrary.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.csf.csflibrary.Tools.TimeUtils;
import com.csf.csflibrary.Tools.Tools;
import com.csf.csflibrary.callback.ContributorView;
import com.csf.csflibrary.constant.Constant;
import com.csf.csflibrary.httpUtils.HttpHeadParams;
import com.csf.csflibrary.httpUtils.LoadCacheResponseLoginouthandler;
import com.csf.csflibrary.httpUtils.LoadDatahandler;
import com.csf.csflibrary.httpUtils.RequstClient;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by kangzhe on 16/4/7.
 */
public class ContributorPresenter {
    private ContributorView contributorView;
    private Context context;
    private String accessKey;
    private String secretKey;
    private String code;
    private boolean stopThread = false;
    private Thread refreshThread;// 刷新线程
    private boolean isLandscape;//是否是横屏

    /**
     * @param contributorView 接口回调
     * @param context         Context
     * @param accessKey       AccessKey
     * @param secretKey       SecretKey
     * @param isLandscape     是否是横屏页面
     */
    public ContributorPresenter(ContributorView contributorView, Context context, String accessKey, String secretKey, boolean isLandscape) {
        this.context = context;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.contributorView = contributorView;
        this.isLandscape = isLandscape;
        HttpHeadParams.getInstance().setAccessKey(accessKey);
    }

    /**
     * 无网络参数的Get请求
     *
     * @param url
     * @param flag 标识
     */
    private void getCsfAsyncMessage(String url, final String flag) {
        getCsfAsyncMessage(url, null, flag);
    }

    /**
     * 有网络参数的Get请求
     *
     * @param url
     * @param requestParams 网络参数
     * @param flag          标识
     */
    private void getCsfAsyncMessage(String url, RequestParams requestParams, final String flag) {
        RequstClient.get(url, requestParams, new LoadCacheResponseLoginouthandler(context, new LoadDatahandler() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                if (contributorView != null) {
                    contributorView.onLoadContributorStart();
                }
            }

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                super.onSuccess(data);
                if (contributorView != null) {
                    contributorView.onLoadContributorSuccess(data, flag);
                }
            }

            @Override
            public void onFailure(String error, String message) {
                // TODO Auto-generated method stub
                super.onFailure(error, message);
                if (contributorView != null) {
                    contributorView.onLoadContributorError();
                }
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                super.onFinish();
                if (contributorView != null) {
                    contributorView.onLoadContributorComplete();
                }
            }

        }));
    }

    /**
     * 股票五档委托信息网络请求
     *
     * @param code
     * @param flag
     */
    public void getStockRealtimeTradeState(String code, String flag) {
        try {
            //时间戳参数
            Long t = new Date().getTime();
            String token = Tools.getToken(accessKey, t, secretKey);
            HttpHeadParams.getInstance().setT(t.toString());
            HttpHeadParams.getInstance().setToken(token);
            RequestParams requestParams = new RequestParams();
            requestParams.put(Constant.CODE, code);
            getCsfAsyncMessage(Constant.Stock_Realtime_Trade_State, requestParams, flag);

        } catch (Exception e) {

        }
    }

    /**
     * 股票分时数据请求
     *
     * @param code
     * @param flag
     */
    public void getStockTimeShareDaily(String code, int incr, String flag) {
        try {
            //时间戳参数
            Long t = new Date().getTime();
            String token = Tools.getToken(accessKey, t, secretKey);
            HttpHeadParams.getInstance().setT(t.toString());
            HttpHeadParams.getInstance().setToken(token);
            RequestParams requestParams = new RequestParams();
            requestParams.put(Constant.CODE, code);
            requestParams.put(Constant.INCR, incr);
            getCsfAsyncMessage(Constant.Stock_Time_Share_Daily, requestParams, flag);

        } catch (Exception e) {

        }
    }

    /**
     * 股票五日分时数据请求
     *
     * @param code
     * @param flag
     */
    public void getStockTimeShareWeek(String code, int incr, String flag) {
        try {
            //时间戳参数
            Long t = new Date().getTime();
            String token = Tools.getToken(accessKey, t, secretKey);
            HttpHeadParams.getInstance().setT(t.toString());
            HttpHeadParams.getInstance().setToken(token);
            RequestParams requestParams = new RequestParams();
            requestParams.put(Constant.CODE, code);
            requestParams.put(Constant.INCR, incr);
            getCsfAsyncMessage(Constant.Stock_Time_Share_Week, requestParams, flag);

        } catch (Exception e) {

        }
    }

    /**
     * 股票日度行情历史数据请求
     *
     * @param contributorPresenter
     * @param code
     * @param flag
     */
    public void getStockPriceDailyData(String code, String flag) {
        try {
            String dateParem = TimeUtils.convert2Stringdate(Calendar.getInstance().getTimeInMillis());
            String dateToParem = dateParem;
            Calendar calendar = Calendar.getInstance();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateParem);
            calendar.setTime(date);
            //横屏
            if (isLandscape) {
                calendar.add(Calendar.MONTH, -9);
            }
            //竖屏
            else {
                calendar.add(Calendar.MONTH, -6);
            }

            dateParem = TimeUtils.convert2Stringdate(calendar.getTimeInMillis());
            //股票日度行情历史数据
            getStockPriceDaily(code, dateParem, dateToParem, Constant.CSF_DAILY_KDATA);
        } catch (Exception e) {

        }
    }

    /**
     * 股票日度行情历史数据请求
     *
     * @param code
     * @param flag
     */
    public void getStockPriceDaily(String code, String from, String to, String flag) {
        try {
            //时间戳参数
            Long t = new Date().getTime();
            String token = Tools.getToken(accessKey, t, secretKey);
            HttpHeadParams.getInstance().setT(t.toString());
            HttpHeadParams.getInstance().setToken(token);
            RequestParams requestParams = new RequestParams();
            requestParams.put(Constant.CODE, code);
            requestParams.put(Constant.FROM, from);
            requestParams.put(Constant.TO, to);
            if (isLandscape) {
                requestParams.put(Constant.NUM, 180);
            } else {
                requestParams.put(Constant.NUM, 123);
            }
            getCsfAsyncMessage(Constant.Stock_Price_Daily, requestParams, flag);

        } catch (Exception e) {

        }
    }

    /**
     * 股票周度行情历史数据
     *
     * @param contributorPresenter
     * @param code
     * @param flag
     */
    public void getStockPriceWeeklyData(String code, String flag) {
        try {
            String dateParem = TimeUtils.convert2Stringdate(Calendar.getInstance().getTimeInMillis());
            String dateToParem = dateParem;
            Calendar calendar = Calendar.getInstance();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateParem);
            calendar.setTime(date);
            //横屏
            if (isLandscape) {
                calendar.add(Calendar.YEAR, -4);
            }
            //竖屏
            else {
                calendar.add(Calendar.YEAR, -2);
                calendar.add(Calendar.MONTH, -6);
            }
            dateParem = TimeUtils.convert2Stringdate(calendar.getTimeInMillis());
            //股票周度行情历史数据
            getStockPriceWeekly(code, dateParem, dateToParem, flag);
        } catch (Exception e) {

        }
    }

    /**
     * 股票周度行情历史数据请求
     *
     * @param code
     * @param flag
     */
    public void getStockPriceWeekly(String code, String from, String to, String flag) {
        try {
            //时间戳参数
            Long t = new Date().getTime();
            String token = Tools.getToken(accessKey, t, secretKey);
            HttpHeadParams.getInstance().setT(t.toString());
            HttpHeadParams.getInstance().setToken(token);
            RequestParams requestParams = new RequestParams();
            requestParams.put(Constant.CODE, code);
            requestParams.put(Constant.FROM, from);
            requestParams.put(Constant.TO, to);
            if (isLandscape) {
                requestParams.put(Constant.NUM, 180);
            } else {
                requestParams.put(Constant.NUM, 123);
            }
            getCsfAsyncMessage(Constant.Stock_Price_Weekly, requestParams, flag);

        } catch (Exception e) {

        }
    }

    /**
     * 股票月度行情历史数据
     *
     * @param contributorPresenter
     * @param code
     * @param flag
     */
    public void getStockPriceMonthlyData(String code, String flag) {
        try {
            String dateParem = TimeUtils.convert2Stringdate(Calendar.getInstance().getTimeInMillis());
            String dateToParem = dateParem;
            Calendar calendar = Calendar.getInstance();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateParem);
            calendar.setTime(date);
            if (isLandscape) {
                calendar.add(Calendar.YEAR, -11);
            } else {
                calendar.add(Calendar.YEAR, -13);
            }
            dateParem = TimeUtils.convert2Stringdate(calendar.getTimeInMillis());
            //股票月度行情历史数据
            getStockPriceMonthly(code, dateParem, dateToParem, flag);
        } catch (Exception e) {

        }

    }

    /**
     * 股票月度行情历史数据请求
     *
     * @param code
     * @param flag
     */
    public void getStockPriceMonthly(String code, String from, String to, String flag) {
        try {
            //时间戳参数
            Long t = new Date().getTime();
            String token = Tools.getToken(accessKey, t, secretKey);
            HttpHeadParams.getInstance().setT(t.toString());
            HttpHeadParams.getInstance().setToken(token);
            RequestParams requestParams = new RequestParams();
            requestParams.put(Constant.CODE, code);
            requestParams.put(Constant.FROM, from);
            requestParams.put(Constant.TO, to);
            if (isLandscape) {
                requestParams.put(Constant.NUM, 180);
            } else {
                requestParams.put(Constant.NUM, 123);
            }
            getCsfAsyncMessage(Constant.Stock_Price_Monthly, requestParams, flag);

        } catch (Exception e) {

        }
    }

    /**
     * 获取实时刷新数据
     *
     * @param code
     * @param flag
     */
    public void getCharDataAsyncTask(String code, String flag) {
        if (isLandscape) {
            //股票实时状态
            getStockRealtimeState(code, Constant.CSF_REALTIME_STATE);
        }
        if (flag.equals(Constant.CSF_SHARE_TIMEDATA)) {
            //股票五档委托信息
            getStockRealtimeTradeState(code, Constant.CSF_FIVE_STATE);
            //股票分时数据
            getStockTimeShareDaily(code, 0, Constant.CSF_SHARE_TIMEDATA);
        } else if (flag.equals(Constant.CSF_SHARE_FIVEDATA)) {
            //五日数据
            getStockTimeShareWeek(code, 0, Constant.CSF_SHARE_FIVEDATA);
        } else if (flag.equals(Constant.CSF_DAILY_KDATA)) {
            //日K数据
            getStockPriceDailyData(code, Constant.CSF_DAILY_KDATA);
        } else if (flag.equals(Constant.CSF_WEEK_KDATA)) {
            //周K数据
            getStockPriceWeeklyData(code, Constant.CSF_WEEK_KDATA);
        } else if (flag.equals(Constant.CSF_MONTH_KDATA)) {
            //月Ｋ数据
            getStockPriceMonthlyData(code, Constant.CSF_MONTH_KDATA);
        }
    }

    /**
     * 股票实时状态
     *
     * @param code
     * @param flag
     */
    public void getStockRealtimeState(String code, String flag) {
        try {
            //时间戳参数
            Long t = new Date().getTime();
            String token = Tools.getToken(accessKey, t, secretKey);
            HttpHeadParams.getInstance().setT(t.toString());
            HttpHeadParams.getInstance().setToken(token);
            RequestParams requestParams = new RequestParams();
            requestParams.put(Constant.CODE, code);
            getCsfAsyncMessage(Constant.Stock_Realtime_State, requestParams, flag);

        } catch (Exception e) {

        }
    }

    /**
     * 实时刷新
     *
     * @param code        股票代码
     * @param refreshTime 刷新时间
     * @param flag        标志
     */
    public void RealTimeRefresh(String code, int refreshTime, String flag) {
        stopThread = false;
        this.code = code;
        if (context != null) {
            // wifi 连接
            if (Tools.isWifiConnected(context)) {

                if (refreshThread == null) {
                    refreshThread = new Thread(new RefreshRunnable(refreshTime, flag, stopThread));
                    refreshThread.start();
                } else {
                    AsyncTaskAll(flag);
                }

            } else {
                // 本地网络连接
                if (Tools.isMobileConnected(context)) {

                    if (refreshThread == null) {
                        refreshThread = new Thread(new RefreshRunnable(refreshTime, flag, stopThread));
                        refreshThread.start();
                    } else {
                        AsyncTaskAll(flag);
                    }

                } else {
                    stopThread = true;
                    if (refreshThread != null) {
                        refreshThread.interrupt();
                        refreshThread = null;
                    } else {
                        AsyncTaskAll(flag);
                    }
                }

            }

        } else {
            AsyncTaskAll(flag);
        }

    }

    /**
     * 刷新线程
     */
    private class RefreshRunnable implements Runnable {
        private String flag;
        private int sleepTime;
        private boolean stopThread;

        public RefreshRunnable(int sleepTime, String flag, boolean stopThread) {
            this.sleepTime = sleepTime;
            this.flag = flag;
            this.stopThread = stopThread;
        }

        public void run() {
            // TODO Auto-generated method stub
            while (!stopThread) {
                try {
                    // 判断是否是工作时间，如果是工作时间
                    Calendar calendar = Calendar.getInstance();
                    if (!TimeUtils.isWeekend(calendar) && TimeUtils.isWorktime(calendar)) {
                        // 执行所有的异步任务
                        if (!stopThread)
                            AsyncTaskAll(flag);
                    } else {
                        AsyncTaskAll(flag);
                        stopThread = true;
                    }

                    Thread.sleep(sleepTime * 1000);

                } catch (Exception e) {
                    stopThread = true;
                }
            }
        }
    }

    // 所有异步任务执行
    private void AsyncTaskAll(String flag) {
        Message msg = new Message();// 获取设置一个信息保存点
        msg.what = 0x15;
        msg.obj = flag;
        myHandler.sendMessage(msg);

    }

    // 五秒之后实时更新数据
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x15:
                    String flag = (String) msg.obj;
                    getCharDataAsyncTask(code, flag);

                    break;
                default:

                    break;
            }

        }
    };


    /**
     * 停止线程刷新
     */
    public void stopRefresh() {
        stopThread = true;
        if (refreshThread != null) {
            refreshThread.interrupt();
            refreshThread = null;
        }
    }

    public boolean isStopThread() {
        return stopThread;
    }

    public void setStopThread(boolean stopThread) {
        this.stopThread = stopThread;
    }

}
