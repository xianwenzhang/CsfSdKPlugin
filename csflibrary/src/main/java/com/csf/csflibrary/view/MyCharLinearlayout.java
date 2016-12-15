package com.csf.csflibrary.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csf.csflibrary.R;
import com.csf.csflibrary.Tools.JsonTools;
import com.csf.csflibrary.Tools.TimeUtils;
import com.csf.csflibrary.Tools.Tools;
import com.csf.csflibrary.adapter.ListStockRealAdapter;
import com.csf.csflibrary.callback.DrawOver;
import com.csf.csflibrary.constant.Constant;
import com.csf.csflibrary.entity.LineEntity;
import com.csf.csflibrary.entity.OHLCCylinderEntity;
import com.csf.csflibrary.entity.OHLCDivideValueEntity;
import com.csf.csflibrary.entity.OHLCEntity;
import com.csf.csflibrary.javaBean.HqStockPriceBean;
import com.csf.csflibrary.javaBean.HqStockPriceMaBean;
import com.csf.csflibrary.javaBean.StockRealtimeBean;
import com.csf.csflibrary.javaBean.StockRealtimeTradeState;
import com.csf.csflibrary.javaBean.StockTimeShareBean;
import com.csf.csflibrary.javaBean.StockTimeShareItemBean;
import com.csf.csflibrary.presenter.ContributorPresenter;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jaily.zhang on 2016/8/2.
 */
public class MyCharLinearlayout extends LinearLayout implements DrawOver {
    private Context mContext;
    private LinearLayout layout_stockchar;//标题栏
    private TextView tv_timeshared;//分时
    private TextView tv_fivedayily;//五日
    private TextView tv_dailyk;//日K
    private TextView tv_weekk;//周K
    private TextView tv_monthk;//月K
    private LinearLayout layout_shareTime;
    private RealTimeLineChart stocktimeshareLinechar;//分时折线图
    private RealTimeBarChart stocktimeshareBarchar;//分时Bar图
    private MyListView lv_sale;//卖出五档
    private MyListView lv_buy;//买入五档
    private LinearLayout layout_fivedays;
    private RealTimeLineChart stocktimeshareFiveDaysLinechar;//五日折线图
    private RealTimeBarChart stocktimeshareFiveDaysBarchar;//五日Bar图
    private LinearLayout layout_daysKchar;
    private RealTimeCombineLineChart stockCombineDayChar;//日K
    private RealTimeBarChart stockBarDayChar;//日K
    private LinearLayout layout_weekKchar;
    private RealTimeCombineLineChart stockCombineWeekChar;//周K
    private RealTimeBarChart stockBarWeekChar;//周K
    private LinearLayout layout_monthKchar;
    private RealTimeCombineLineChart stockCombineMonthChar;//月K
    private RealTimeBarChart stockBarMonthChar;//月K
    private int charCount;
    private int monthDataCount;
    private int weekDataCount;
    private int dailyDataCount;
    private int fiveDayCount;
    private int shareTimeCount;
    private ProgressBar progressbar;
    private DrawOver drawOver;
    // 五秒之后实时更新数据
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //分时
                case 0x25:
                    stocktimeshareLinechar.refresh();
                    stocktimeshareBarchar.refresh();
                    break;
                //五日
                case 0x35:
                    stocktimeshareFiveDaysLinechar.refresh();
                    stocktimeshareFiveDaysBarchar.refresh();
                    break;
                //日K
                case 0x45:
                    stockCombineDayChar.refresh();
                    stockBarDayChar.refresh();
                    break;
                //周k
                case 0x55:
                    stockCombineWeekChar.refresh();
                    stockBarWeekChar.refresh();
                    break;
                //月K
                case 0x65:
                    stockCombineMonthChar.refresh();
                    stockBarMonthChar.refresh();
                    break;
                default:
                    break;
            }

        }
    };

    public MyCharLinearlayout(Context context) {
        super(context);
        this.mContext = context;
        initView(context);
    }

    public MyCharLinearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(context);
    }

    public MyCharLinearlayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.vertical_layout, this);
        progressbar = (ProgressBar) v.findViewById(R.id.progressbar);
        layout_stockchar = (LinearLayout) v.findViewById(R.id.layout_stockchar);
        tv_timeshared = (TextView) v.findViewById(R.id.tv_timeshared);
        tv_fivedayily = (TextView) v.findViewById(R.id.tv_fivedayily);
        tv_dailyk = (TextView) v.findViewById(R.id.tv_dailyk);
        tv_weekk = (TextView) v.findViewById(R.id.tv_weekk);
        tv_monthk = (TextView) v.findViewById(R.id.tv_monthk);

        layout_shareTime = (LinearLayout) v.findViewById(R.id.layout_shareTime);
        stocktimeshareLinechar = (RealTimeLineChart) v.findViewById(R.id.stocktimeshareLinechar);//分时折线图
        stocktimeshareBarchar = (RealTimeBarChart) v.findViewById(R.id.stocktimeshareBarchar);//分时Bar图
        lv_sale = (MyListView) v.findViewById(R.id.lv_sale);//卖出五档
        lv_sale.setDivider(null);
        lv_buy = (MyListView) v.findViewById(R.id.lv_buy);//买入五档
        lv_buy.setDivider(null);
        layout_fivedays = (LinearLayout) v.findViewById(R.id.layout_fivedays);
        stocktimeshareFiveDaysLinechar = (RealTimeLineChart) v.findViewById(R.id.stocktimeshareFiveDaysLinechar);//五日折线图
        stocktimeshareFiveDaysBarchar = (RealTimeBarChart) v.findViewById(R.id.stocktimeshareFiveDaysBarchar);//五日Bar图
        layout_daysKchar = (LinearLayout) v.findViewById(R.id.layout_daysKchar);
        stockCombineDayChar = (RealTimeCombineLineChart) v.findViewById(R.id.stockCombineDayChar);//日K
        stockBarDayChar = (RealTimeBarChart) v.findViewById(R.id.stockBarDayChar);//日K
        layout_weekKchar = (LinearLayout) v.findViewById(R.id.layout_weekKchar);
        stockCombineWeekChar = (RealTimeCombineLineChart) v.findViewById(R.id.stockCombineWeekChar);//周K
        stockBarWeekChar = (RealTimeBarChart) v.findViewById(R.id.stockBarWeekChar);//周K
        layout_monthKchar = (LinearLayout) v.findViewById(R.id.layout_monthKchar);
        stockCombineMonthChar = (RealTimeCombineLineChart) v.findViewById(R.id.stockCombineMonthChar);//月K
        stockBarMonthChar = (RealTimeBarChart) v.findViewById(R.id.stockBarMonthChar);//月K

        initCharView(v);
    }

    /**
     * 初始化CharView 控件
     */
    private void initCharView(View v) {

        charCount = 123;
        //分时
        stocktimeshareLinechar = (RealTimeLineChart) v.findViewById(R.id.stocktimeshareLinechar);
        stocktimeshareLinechar.setShowLeftMaxMinYAxis(true);
        stocktimeshareLinechar.setShowTopLine(true);
        stocktimeshareLinechar.setShowBottomLine(true);
        stocktimeshareLinechar.setShowRightMaxMinYAxis(true);
        stocktimeshareLinechar.setShowCenterLine(true);
        stocktimeshareLinechar.setAxisMarginBottom(30.0f);
        stocktimeshareLinechar.setDefaultXtitle(true);
        stocktimeshareLinechar.setDrawOver(this);
        stocktimeshareBarchar = (RealTimeBarChart) v.findViewById(R.id.stocktimeshareBarchar);
        stocktimeshareBarchar.setShowBottomLine(true);

        //五日
        stocktimeshareFiveDaysLinechar = (RealTimeLineChart) v.findViewById(R.id.stocktimeshareFiveDaysLinechar);
        stocktimeshareFiveDaysLinechar.setShowLeftMaxMinYAxis(true);
        stocktimeshareFiveDaysLinechar.setShowTopLine(true);
        stocktimeshareFiveDaysLinechar.setShowBottomLine(true);
        stocktimeshareFiveDaysLinechar.setShowRightMaxMinYAxis(true);
        stocktimeshareFiveDaysLinechar.setAxisMarginBottom(30.0f);
        stocktimeshareFiveDaysLinechar.setShowXtitleAxis(true);
        stocktimeshareFiveDaysLinechar.setShowCenterLine(true);
        stocktimeshareFiveDaysLinechar.setShowGridYDivideLine(true);
        stocktimeshareFiveDaysLinechar.setDrawOver(this);
        stocktimeshareFiveDaysBarchar = (RealTimeBarChart) v.findViewById(R.id.stocktimeshareFiveDaysBarchar);
        stocktimeshareFiveDaysBarchar.setShowBottomLine(true);

        //日K
        layout_daysKchar = (LinearLayout) v.findViewById(R.id.layout_daysKchar);
        stockCombineDayChar = (RealTimeCombineLineChart) v.findViewById(R.id.stockCombineDayChar);
        stockCombineDayChar.setShowTopLine(true);
        stockCombineDayChar.setShowBottomLine(true);
        stockCombineDayChar.setShowCenterLine(true);
        stockCombineDayChar.setAxisMarginBottom(30.0f);
        stockCombineDayChar.setDrawOver(this);
        stockBarDayChar = (RealTimeBarChart) v.findViewById(R.id.stockBarDayChar);
        stockBarDayChar.setShowBottomLine(true);

        //周K
        layout_weekKchar = (LinearLayout) v.findViewById(R.id.layout_weekKchar);
        stockCombineWeekChar = (RealTimeCombineLineChart) v.findViewById(R.id.stockCombineWeekChar);
        stockCombineWeekChar.setShowTopLine(true);
        stockCombineWeekChar.setShowBottomLine(true);
        stockCombineWeekChar.setShowCenterLine(true);
        stockCombineWeekChar.setAxisMarginBottom(30.0f);
        stockCombineWeekChar.setDrawOver(this);
        stockBarWeekChar = (RealTimeBarChart) v.findViewById(R.id.stockBarWeekChar);
        stockBarWeekChar.setShowBottomLine(true);

        //月K
        layout_monthKchar = (LinearLayout) v.findViewById(R.id.layout_monthKchar);
        stockCombineMonthChar = (RealTimeCombineLineChart) v.findViewById(R.id.stockCombineMonthChar);
        stockCombineMonthChar.setShowTopLine(true);
        stockCombineMonthChar.setShowBottomLine(true);
        stockCombineMonthChar.setShowCenterLine(true);
        stockCombineMonthChar.setAxisMarginBottom(30.0f);
        stockCombineMonthChar.setDrawOver(this);
        stockBarMonthChar = (RealTimeBarChart) v.findViewById(R.id.stockBarMonthChar);
        stockBarMonthChar.setShowBottomLine(true);

    }

    /**
     * 分时按钮点击事件
     */
    public void setTimeSharedClick(OnClickListener onClickListener) {
        tv_timeshared.setOnClickListener(onClickListener);
    }

    /**
     * 改变分时控件下的View状态
     */
    public void changeTimeSharedView() {
        initStockCharView();
        layout_shareTime.setVisibility(View.VISIBLE);
        tv_timeshared.setTextColor(getResources().getColor(R.color.fce));
        tv_timeshared.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.cursor));
    }

    /**
     * 五日按钮点击事件
     */
    public void setFiveDailyClick(OnClickListener onClickListener) {
        tv_fivedayily.setOnClickListener(onClickListener);
    }

    /**
     * 改变五日控件下的View状态
     */
    public void changeFiveDailyView() {
        initStockCharView();
        layout_fivedays.setVisibility(View.VISIBLE);
        tv_fivedayily.setTextColor(getResources().getColor(R.color.fce));
        tv_fivedayily.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.cursor));
    }

    /**
     * 日K按钮点击事件
     */
    public void setDailyKClick(OnClickListener onClickListener) {
        tv_dailyk.setOnClickListener(onClickListener);
    }

    /**
     * 改变日K控件下的View状态
     */
    public void changeDailyKView() {
        initStockCharView();
        layout_daysKchar.setVisibility(View.VISIBLE);
        tv_dailyk.setTextColor(getResources().getColor(R.color.fce));
        tv_dailyk.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.cursor));
    }

    /**
     * 周K按钮点击事件
     */
    public void setWeekKClick(OnClickListener onClickListener) {
        tv_weekk.setOnClickListener(onClickListener);
    }

    /**
     * 改变周K控件下的View状态
     */
    public void changeWeekView() {
        initStockCharView();
        layout_weekKchar.setVisibility(View.VISIBLE);
        tv_weekk.setTextColor(getResources().getColor(R.color.fce));
        tv_weekk.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.cursor));
    }

    /**
     * 月K按钮点击事件
     */
    public void setMonthClick(OnClickListener onClickListener) {
        tv_monthk.setOnClickListener(onClickListener);
    }

    /**
     * 改变月K控件下的View状态
     */
    public void changeMonthView() {
        initStockCharView();
        layout_monthKchar.setVisibility(View.VISIBLE);
        tv_monthk.setTextColor(getResources().getColor(R.color.fce));
        tv_monthk.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.cursor));
    }

    private void initStockCharView() {
        tv_timeshared.setTextColor(getResources().getColor(R.color.threefour));
        tv_timeshared.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        tv_fivedayily.setTextColor(getResources().getColor(R.color.threefour));
        tv_fivedayily.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        tv_dailyk.setTextColor(getResources().getColor(R.color.threefour));
        tv_dailyk.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        tv_weekk.setTextColor(getResources().getColor(R.color.threefour));
        tv_weekk.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        tv_monthk.setTextColor(getResources().getColor(R.color.threefour));
        tv_monthk.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        layout_fivedays.setVisibility(View.GONE);
        layout_shareTime.setVisibility(View.GONE);
        layout_daysKchar.setVisibility(View.GONE);
        layout_weekKchar.setVisibility(View.GONE);
        layout_monthKchar.setVisibility(View.GONE);
    }

    /**
     * 显示买卖档位
     *
     * @param jsonString
     */
    public void showStockRealtimeState(Context context, String jsonString) {
        try {
            StockRealtimeTradeState stockRealtimeTradeState = JsonTools.jsonStringToObject(jsonString, StockRealtimeTradeState.class);
            if (stockRealtimeTradeState != null) {
                List<StockRealtimeBean> listsalrealtimebean = new ArrayList<>();
                StockRealtimeBean salefive = new StockRealtimeBean();
                salefive.setTitle("卖5");
                salefive.setPrice(Tools.formatNum(Double.parseDouble(stockRealtimeTradeState.getSp5())));
                salefive.setCount(Tools.formatNoPoint(Tools.div(Double.parseDouble(stockRealtimeTradeState.getSc5()), 100)));
                listsalrealtimebean.add(salefive);

                StockRealtimeBean salefour = new StockRealtimeBean();
                salefour.setTitle("卖4");
                salefour.setPrice(Tools.formatNum(Double.parseDouble(stockRealtimeTradeState.getSp4())));
                salefour.setCount(Tools.formatNoPoint(Tools.div(Double.parseDouble(stockRealtimeTradeState.getSc4()), 100)));
                listsalrealtimebean.add(salefour);

                StockRealtimeBean salethree = new StockRealtimeBean();
                salethree.setTitle("卖3");
                salethree.setPrice(Tools.formatNum(Double.parseDouble(stockRealtimeTradeState.getSp3())));
                salethree.setCount(Tools.formatNoPoint(Tools.div(Double.parseDouble(stockRealtimeTradeState.getSc3()), 100)));
                listsalrealtimebean.add(salethree);

                StockRealtimeBean saletwo = new StockRealtimeBean();
                saletwo.setTitle("卖2");
                saletwo.setPrice(Tools.formatNum(Double.parseDouble(stockRealtimeTradeState.getSp2())));
                saletwo.setCount(Tools.formatNoPoint(Tools.div(Double.parseDouble(stockRealtimeTradeState.getSc2()), 100)));
                listsalrealtimebean.add(saletwo);

                StockRealtimeBean saleone = new StockRealtimeBean();
                saleone.setTitle("卖1");
                saleone.setPrice(Tools.formatNum(Double.parseDouble(stockRealtimeTradeState.getSp1())));
                saleone.setCount(Tools.formatNoPoint(Tools.div(Double.parseDouble(stockRealtimeTradeState.getSc1()), 100)));
                listsalrealtimebean.add(saleone);

                List<StockRealtimeBean> listbuyrealtimebean = new ArrayList<>();
                StockRealtimeBean buyone = new StockRealtimeBean();
                buyone.setTitle("买1");
                buyone.setPrice(Tools.formatNum(Double.parseDouble(stockRealtimeTradeState.getBp1())));
                buyone.setCount(Tools.formatNoPoint(Tools.div(Double.parseDouble(stockRealtimeTradeState.getBc1()), 100)));
                listbuyrealtimebean.add(buyone);

                StockRealtimeBean buytwo = new StockRealtimeBean();
                buytwo.setTitle("买2");
                buytwo.setPrice(Tools.formatNum(Double.parseDouble(stockRealtimeTradeState.getBp2())));
                buytwo.setCount(Tools.formatNoPoint(Tools.div(Double.parseDouble(stockRealtimeTradeState.getBc2()), 100)));
                listbuyrealtimebean.add(buytwo);

                StockRealtimeBean buythree = new StockRealtimeBean();
                buythree.setTitle("买3");
                buythree.setPrice(Tools.formatNum(Double.parseDouble(stockRealtimeTradeState.getBp3())));
                buythree.setCount(Tools.formatNoPoint(Tools.div(Double.parseDouble(stockRealtimeTradeState.getBc3()), 100)));
                listbuyrealtimebean.add(buythree);

                StockRealtimeBean buyfour = new StockRealtimeBean();
                buyfour.setTitle("买4");
                buyfour.setPrice(Tools.formatNum(Double.parseDouble(stockRealtimeTradeState.getBp4())));
                buyfour.setCount(Tools.formatNoPoint(Tools.div(Double.parseDouble(stockRealtimeTradeState.getBc4()), 100)));
                listbuyrealtimebean.add(buyfour);

                StockRealtimeBean buyfive = new StockRealtimeBean();
                buyfive.setTitle("买5");
                buyfive.setPrice(Tools.formatNum(Double.parseDouble(stockRealtimeTradeState.getBp5())));
                buyfive.setCount(Tools.formatNoPoint(Tools.div(Double.parseDouble(stockRealtimeTradeState.getBc5()), 100)));
                listbuyrealtimebean.add(buyfive);

                lv_sale.setAdapter(new ListStockRealAdapter(context, listsalrealtimebean, false));
                lv_buy.setAdapter(new ListStockRealAdapter(context, listbuyrealtimebean, false));
            }
        } catch (Exception e) {

        }

    }


    /**
     * 显示分时五日数据
     *
     * @param jsonString
     */

    public void showStockTimeShare(String jsonString, String flag) {
        try {
            List<Integer> divideCountList = new ArrayList<>();
            StockTimeShareBean stockTimeShareBean = JsonTools.jsonStringToObject(jsonString, StockTimeShareBean.class);
            if (stockTimeShareBean != null) {
                Object daysObject = stockTimeShareBean.getDays();
                String dayString = JsonTools.objectToJsonString(daysObject);
                List<String> daySlist = JsonTools.jsonStringToList(dayString, String.class);

                Object sharesobject = stockTimeShareBean.getShares();
                Double closeValue = stockTimeShareBean.getClose();
                Double maxValue = stockTimeShareBean.getMax();
                Double minValue = stockTimeShareBean.getMin();
                if (maxValue == minValue)
                    minValue = maxValue - 1;
                double maxAbsValue = Math.abs(Tools.sub(maxValue, closeValue));
                double minAbsValue = Math.abs(Tools.sub(minValue, closeValue));
                if (maxAbsValue <= minAbsValue)
                    maxAbsValue = minAbsValue;

                double ratiovalue = 0;
                double maxpricevalue = 0;
                double minpricevalue = 0;
                if (closeValue != 0) {
                    ratiovalue = Tools.mul(Tools.div(maxAbsValue, closeValue), 100);
                    maxpricevalue = Tools.mul(closeValue, Tools.add(1, Tools.div(ratiovalue, 100)));
                    minpricevalue = Tools.mul(closeValue, Tools.sub(1, Tools.div(ratiovalue, 100)));
                } else {
                    ratiovalue = Tools.mul(Tools.div(Tools.sub(maxValue, minValue), minValue), 100);
                    maxpricevalue = maxValue;
                    minpricevalue = Tools.mul(maxValue, Tools.sub(1, Tools.div(ratiovalue, 100)));
                }


                if (sharesobject != null) {
                    String shares = JsonTools.objectToJsonString(sharesobject);
                    List<StockTimeShareItemBean> listDetails = JsonTools.jsonStringToList(shares, StockTimeShareItemBean.class);
                    if (listDetails != null && !listDetails.isEmpty()) {
                        ArrayList<OHLCEntity> ypricevalue = new ArrayList<>();
                        ArrayList<OHLCEntity> yratiovalue = new ArrayList<>();
                        ArrayList<OHLCEntity> yValuesAverage = new ArrayList<>();
                        ArrayList<OHLCEntity> yVolumeValue = new ArrayList<>();
                        List<LineEntity> listdatas = new ArrayList<>();
                        double oldRealtimeBarcharratio = 0;
                        double realtimeBarcharvolumeold = 0.0;
                        int sizecount = listDetails.size();
                        //该判断是因为服务器是1分钟刷新数据，app客户端刷新时间小于1分钟，防止1分钟之内多次绘图造成卡顿
                        //分时
                        if (flag.equals(Constant.CSF_SHARE_TIMEDATA)) {
                            if (shareTimeCount != sizecount) {
                                double volumemax = 0;
                                for (int i = 0; i < sizecount; i++) {
                                    StockTimeShareItemBean stockTimeShareItemBean = listDetails.get(i);
                                    String dt = stockTimeShareItemBean.getDt();
                                    Double price = stockTimeShareItemBean.getPrice();
                                    Double ratio = stockTimeShareItemBean.getRatio();
                                    Double amount = stockTimeShareItemBean.getAmount();//成交额
                                    Double volumevalue = stockTimeShareItemBean.getVolume();//成交量

                                    ypricevalue.add(new OHLCEntity(price, dt));
                                    yratiovalue.add(new OHLCEntity(ratio, dt));
                                    if (volumevalue == 0) {
                                        yValuesAverage.add(new OHLCEntity(null, dt));
                                    } else {
                                        yValuesAverage.add(new OHLCEntity(Tools.div(amount, volumevalue), dt));
                                    }

                                    int colorValue = getResources().getColor(R.color.f24957);
                                    if (ratio >= oldRealtimeBarcharratio) {
                                        //红色
                                        colorValue = getResources().getColor(R.color.f24957);
                                    } else {
                                        //绿色
                                        colorValue = getResources().getColor(R.color.c268);
                                    }
                                    double actualvolumevalue = Tools.sub(volumevalue, realtimeBarcharvolumeold);
                                    if (volumemax < actualvolumevalue)
                                        volumemax = actualvolumevalue;
                                    yVolumeValue.add(new OHLCEntity(actualvolumevalue, dt, colorValue));
                                    oldRealtimeBarcharratio = ratio;
                                    realtimeBarcharvolumeold = volumevalue;

                                }

                                listdatas.add(new LineEntity(ypricevalue, "price", getResources().getColor(R.color.aff179), true, maxpricevalue, minpricevalue));
                                listdatas.add(new LineEntity(yValuesAverage, "averageprice", getResources().getColor(R.color.ffcc67), false, maxpricevalue,
                                        minpricevalue));
                                listdatas.add(new LineEntity(yratiovalue, "ratioValue", getResources().getColor(R.color.aff179), true, ratiovalue, -ratiovalue));
                                stocktimeshareLinechar.setMaxLeftValue(maxpricevalue);
                                stocktimeshareLinechar.setMinLeftValue(minpricevalue);
                                stocktimeshareLinechar.setMaxRightValue(ratiovalue);
                                stocktimeshareLinechar.setMinRightValue(-ratiovalue);
                                stocktimeshareLinechar.setLineData(listdatas);
                                if (sizecount > 241) {
                                    stocktimeshareLinechar.setTotalNumber(sizecount);
                                    stocktimeshareBarchar.setTotalNumber(sizecount);
                                } else {
                                    stocktimeshareLinechar.setTotalNumber(241);
                                    stocktimeshareBarchar.setTotalNumber(241);
                                }

                                List<String> Xtitles = new ArrayList<>();
                                Xtitles.add("09:30");
                                Xtitles.add("11:30/13:00");
                                Xtitles.add("15:00");
                                stocktimeshareLinechar.setAxisXTitles(Xtitles);

                                stocktimeshareBarchar.setMaxLeftValue(volumemax);
                                stocktimeshareBarchar.setLineData(yVolumeValue);
                                Message message = new Message();
                                message.what = 0x25;
                                myHandler.sendMessage(message);
                                shareTimeCount = sizecount;
                            }


                        }
                        //五日
                        else if (flag.equals(Constant.CSF_SHARE_FIVEDATA)) {
                            if (fiveDayCount != sizecount) {
                                double volumemax = 0;
                                String dt_division = "";
                                for (int i = 0; i < sizecount; i++) {
                                    StockTimeShareItemBean stockTimeShareItemBean = listDetails.get(i);
                                    String dt = stockTimeShareItemBean.getDt();
                                    Double price = stockTimeShareItemBean.getPrice();
                                    Double ratio = 0.0;
                                    if (closeValue != 0) {
                                        ratio = Tools.mul(Tools.div(Tools.sub(price, closeValue), closeValue), 100);
                                    } else {
                                        ratio = stockTimeShareItemBean.getRatio();
                                    }

                                    Double volume = stockTimeShareItemBean.getVolume();
                                    Double amount = stockTimeShareItemBean.getAmount();
                                    double actualvolumevalue = 0;
                                    if (dt.split(" ")[0].equals(dt_division)) {
                                        actualvolumevalue = Tools.sub(volume, realtimeBarcharvolumeold);
                                    } else {
                                        if (i != 0)
                                            divideCountList.add(i + 1);
                                        actualvolumevalue = volume;
                                    }
                                    dt_division = dt.split(" ")[0];

                                    ypricevalue.add(new OHLCEntity(price, dt));
                                    yratiovalue.add(new OHLCEntity(ratio, dt));
                                    if (volume == 0) {
                                        yValuesAverage.add(new OHLCEntity(null, dt));
                                    } else {
                                        yValuesAverage.add(new OHLCEntity(Tools.div(amount, volume), dt));
                                    }

                                    int colorValue = getResources().getColor(R.color.f24957);
                                    if (ratio >= oldRealtimeBarcharratio) {
                                        colorValue = getResources().getColor(R.color.f24957);
                                    } else {
                                        colorValue = getResources().getColor(R.color.c268);
                                    }
                                    if (volumemax < actualvolumevalue)
                                        volumemax = actualvolumevalue;
                                    yVolumeValue.add(new OHLCEntity(actualvolumevalue, dt, colorValue));
                                    oldRealtimeBarcharratio = ratio;
                                    realtimeBarcharvolumeold = volume;

                                }

                                if (daySlist != null && !daySlist.isEmpty()) {
                                    if (daySlist.size() == 1) {
                                        daySlist.add("");
                                    }
                                    stocktimeshareFiveDaysLinechar.setAxisXdivideTitles(daySlist);
                                }
                                if (sizecount > 410) {
                                    stocktimeshareFiveDaysLinechar.setTotalNumber(sizecount);
                                    stocktimeshareFiveDaysBarchar.setTotalNumber(sizecount);
                                } else {
                                    stocktimeshareFiveDaysLinechar.setTotalNumber(410);
                                    stocktimeshareFiveDaysBarchar.setTotalNumber(410);
                                }

                                listdatas.clear();
                                listdatas.add(new LineEntity(ypricevalue, "price", getResources().getColor(R.color.aff179), true, maxpricevalue, minpricevalue));
                                listdatas.add(new LineEntity(yValuesAverage, "averageprice", getResources().getColor(R.color.ffcc67), false, maxpricevalue,
                                        minpricevalue));
                                listdatas.add(new LineEntity(yratiovalue, "ratioValue", getResources().getColor(R.color.aff179), true, ratiovalue, -ratiovalue));
                                stocktimeshareFiveDaysLinechar.setMaxLeftValue(maxpricevalue);
                                stocktimeshareFiveDaysLinechar.setMinLeftValue(minpricevalue);
                                stocktimeshareFiveDaysLinechar.setMaxRightValue(ratiovalue);
                                stocktimeshareFiveDaysLinechar.setMinRightValue(-ratiovalue);
                                stocktimeshareFiveDaysLinechar.setLineData(listdatas);
                                if (divideCountList.isEmpty()) {
                                    if (sizecount > 84) {
                                        divideCountList.add(sizecount);
                                    } else {
                                        divideCountList.add(84);
                                    }

                                }
                                stocktimeshareFiveDaysLinechar.setDivideCountList(divideCountList);
                                stocktimeshareFiveDaysBarchar.setMaxLeftValue(volumemax);
                                stocktimeshareFiveDaysBarchar.setLineData(yVolumeValue);

                                stocktimeshareFiveDaysBarchar.setGridLineNumber(5);

                                Message message = new Message();
                                message.what = 0x35;
                                myHandler.sendMessage(message);

                                fiveDayCount = sizecount;
                            }

                        }


                    } else {

                    }

                }

            } else {
            }
        } catch (Exception e) {

        }

    }


    /**
     * 展示K线图的数据
     *
     * @param jsonString
     */
    public void showKvalueJsonString(String jsonString, String flag) {
        try {
            List<HqStockPriceBean> hqStockPriceBeanList = JsonTools.jsonStringToList(jsonString, HqStockPriceBean.class);
            if (hqStockPriceBeanList != null && !hqStockPriceBeanList.isEmpty()) {
                int countsize = hqStockPriceBeanList.size();
                //日K
                if (flag.equals(Constant.CSF_DAILY_KDATA)) {
                    if (dailyDataCount != countsize) {
                        showKValueAsync(hqStockPriceBeanList, countsize, flag);
                        dailyDataCount = countsize;
                    }

                }
                //周K
                else if (flag.equals(Constant.CSF_WEEK_KDATA)) {
                    if (weekDataCount != countsize) {
                        showKValueAsync(hqStockPriceBeanList, countsize, flag);
                        weekDataCount = countsize;
                    }

                }
                //月K
                else if (flag.equals(Constant.CSF_MONTH_KDATA)) {
                    if (monthDataCount != countsize) {
                        showKValueAsync(hqStockPriceBeanList, countsize, flag);
                        monthDataCount = countsize;
                    }

                }

            } else {
            }
        } catch (Exception e) {

        }

    }

    private void showKValueAsync(List<HqStockPriceBean> hqStockPriceBeanList, int countsize, String flag) {
        try {
            ArrayList<OHLCCylinderEntity> yCylinderValue = new ArrayList<>();
            ArrayList<OHLCEntity> yVolumeValue = new ArrayList<>();
            ArrayList<LineEntity> listdatas = new ArrayList<>();
            ArrayList<OHLCEntity> MA5ValueList = new ArrayList<>();
            ArrayList<OHLCEntity> MA10ValueList = new ArrayList<>();
            ArrayList<OHLCEntity> MA20ValueList = new ArrayList<>();
            double volumemax = Double.MIN_VALUE;
            double hightValue = Double.MIN_VALUE;
            double lowValue = Double.MAX_VALUE;
            String actualDateString = "";
            List<OHLCDivideValueEntity> dayKOhlcDivide = new ArrayList<>();
            int countflag = 0;
            for (int i = 0; i < countsize; i++) {
                HqStockPriceBean hqStockPriceBean = hqStockPriceBeanList.get(i);
                double inc = hqStockPriceBean.getInc();
                double high = hqStockPriceBean.getHigh();
                double low = hqStockPriceBean.getLow();
                double open = hqStockPriceBean.getOpen();
                double close = hqStockPriceBean.getClose();
                Object Ma = hqStockPriceBean.getMa();

                if (hightValue < high)
                    hightValue = high;
                if (lowValue > low)
                    lowValue = low;

                int colorValue = getResources().getColor(R.color.f24957);
                if (inc < 0) {
                    colorValue = getResources().getColor(R.color.c268);
                } else {
                    colorValue = getResources().getColor(R.color.f24957);
                }

                String dt = hqStockPriceBean.getDt();
                String[] dts = dt.split("-");
                String yearString = dts[0];
                String monthString = dts[1];
                String previouString = yearString + "-" + monthString;
                //日K,周K
                if (flag.equals(Constant.CSF_DAILY_KDATA) || flag.equals(Constant.CSF_WEEK_KDATA)) {
                    if (!previouString.equals(actualDateString)) {

                        if (i - countflag > 30) {
                            dayKOhlcDivide.add(new OHLCDivideValueEntity(i, previouString));
                            countflag = i;
                            actualDateString = previouString;
                        }

                    }
                }
                //月k
                else {
                    if (!yearString.equals(actualDateString)) {

                        if (i - countflag > 30) {
                            dayKOhlcDivide.add(new OHLCDivideValueEntity(i, yearString));
                            countflag = i;
                            actualDateString = yearString;
                        }

                    }
                }

                double volValue = hqStockPriceBean.getVol();//手为单位
                if (volumemax < volValue)
                    volumemax = volValue;
                yVolumeValue.add(new OHLCEntity(volValue, dt, colorValue, false));
                yCylinderValue.add(new OHLCCylinderEntity(open, close, high, low, dt, colorValue, false));

                String jsonMaString = JsonTools.objectToJsonString(Ma);
                HqStockPriceMaBean hqStockPriceMaBean = JsonTools.jsonStringToObject(jsonMaString, HqStockPriceMaBean.class);
                Double MA5 = hqStockPriceMaBean.getMA5();
                Double MA10 = hqStockPriceMaBean.getMA10();
                Double MA20 = hqStockPriceMaBean.getMA20();
                if (MA5 != null) {
                    MA5ValueList.add(new OHLCEntity(MA5, dt));
                    if (hightValue < MA5)
                        hightValue = MA5;
                    if (lowValue > MA5)
                        lowValue = MA5;

                } else {
                    MA5ValueList.add(new OHLCEntity(0.0, dt));
                }

                if (MA10 != null) {
                    MA10ValueList.add(new OHLCEntity(MA10, dt));
                    if (hightValue < MA10)
                        hightValue = MA10;
                    if (lowValue > MA10)
                        lowValue = MA10;

                } else {
                    MA10ValueList.add(new OHLCEntity(0.0, dt));
                }

                if (MA20 != null) {
                    MA20ValueList.add(new OHLCEntity(MA20, dt));
                    if (hightValue < MA20)
                        hightValue = MA20;
                    if (lowValue > MA20)
                        lowValue = MA20;

                } else {
                    MA20ValueList.add(new OHLCEntity(0.0, dt));
                }


            }


            if (hightValue == lowValue)
                lowValue = hightValue - 1;
            listdatas.add(new LineEntity(MA5ValueList, "MA5", getResources().getColor(R.color.ffc415), false, hightValue, lowValue));
            listdatas.add(new LineEntity(MA10ValueList, "MA10", getResources().getColor(R.color.cfc), false, hightValue, lowValue));
            listdatas.add(new LineEntity(MA20ValueList, "MA20", getResources().getColor(R.color.ff8ed9), false, hightValue, lowValue));

            //日K
            if (flag.equals(Constant.CSF_DAILY_KDATA)) {
                stockCombineDayChar.setMaxLeftValue(hightValue);
                stockCombineDayChar.setMinLeftValue(lowValue);
                stockCombineDayChar.setLineData(listdatas);
                stockCombineDayChar.setLineCylinderData(yCylinderValue);
                if (countsize > charCount) {
                    stockCombineDayChar.setTotalNumber(countsize);
                    stockBarDayChar.setTotalNumber(countsize);
                } else {
                    stockCombineDayChar.setTotalNumber(charCount);
                    stockBarDayChar.setTotalNumber(charCount);
                }
                stockCombineDayChar.setSpaceValue(1.0f);
                stockCombineDayChar.setMarginXtitleSpace(20.0f);
                stockCombineDayChar.setShowLeftMaxMinYAxis(true);
                stockCombineDayChar.setOhlcDivideValueEntityList(dayKOhlcDivide);

                stockBarDayChar.setMaxLeftValue(volumemax);
                stockBarDayChar.setLineData(yVolumeValue);
                stockBarDayChar.setSpaceValue(1.0f);
                stockBarDayChar.setOhlcDivideValueEntityList(dayKOhlcDivide);

                Message message = new Message();
                message.what = 0x45;
                myHandler.sendMessage(message);

            }
            //周K
            else if (flag.equals(Constant.CSF_WEEK_KDATA)) {
                stockCombineWeekChar.setMaxLeftValue(hightValue);
                stockCombineWeekChar.setMinLeftValue(lowValue);
                stockCombineWeekChar.setLineData(listdatas);
                stockCombineWeekChar.setLineCylinderData(yCylinderValue);
                if (countsize > charCount) {
                    stockCombineWeekChar.setTotalNumber(countsize);
                    stockBarWeekChar.setTotalNumber(countsize);
                } else {
                    stockCombineWeekChar.setTotalNumber(charCount);
                    stockBarWeekChar.setTotalNumber(charCount);
                }
                stockCombineWeekChar.setSpaceValue(1.0f);
                stockCombineWeekChar.setMarginXtitleSpace(20.0f);
                stockCombineWeekChar.setShowLeftMaxMinYAxis(true);
                stockCombineWeekChar.setOhlcDivideValueEntityList(dayKOhlcDivide);


                stockBarWeekChar.setMaxLeftValue(volumemax);
                stockBarWeekChar.setLineData(yVolumeValue);
                stockBarWeekChar.setSpaceValue(1.0f);
                stockBarWeekChar.setOhlcDivideValueEntityList(dayKOhlcDivide);
                Message message = new Message();
                message.what = 0x55;
                myHandler.sendMessage(message);

            }
            //月Ｋ
            else {
                stockCombineMonthChar.setMaxLeftValue(hightValue);
                stockCombineMonthChar.setMinLeftValue(lowValue);
                stockCombineMonthChar.setLineData(listdatas);
                stockCombineMonthChar.setLineCylinderData(yCylinderValue);
                if (countsize > charCount) {
                    stockCombineMonthChar.setTotalNumber(countsize);
                    stockBarMonthChar.setTotalNumber(countsize);
                } else {
                    stockCombineMonthChar.setTotalNumber(charCount);
                    stockBarMonthChar.setTotalNumber(charCount);
                }
                stockCombineMonthChar.setSpaceValue(1.0f);
                stockCombineMonthChar.setMarginXtitleSpace(10.0f);
                stockCombineMonthChar.setShowLeftMaxMinYAxis(true);
                stockCombineMonthChar.setOhlcDivideValueEntityList(dayKOhlcDivide);

                stockBarMonthChar.setMaxLeftValue(volumemax);
                stockBarMonthChar.setLineData(yVolumeValue);
                stockBarMonthChar.setSpaceValue(1.0f);
                stockBarMonthChar.setOhlcDivideValueEntityList(dayKOhlcDivide);

                Message message = new Message();
                message.what = 0x65;
                myHandler.sendMessage(message);

            }
        } catch (Exception e) {

        }


    }

    public LinearLayout getLayout_stockchar() {
        return layout_stockchar;
    }

    public void setLayout_stockchar(LinearLayout layout_stockchar) {
        this.layout_stockchar = layout_stockchar;
    }

    public TextView getTv_timeshared() {
        return tv_timeshared;
    }

    public void setTv_timeshared(TextView tv_timeshared) {
        this.tv_timeshared = tv_timeshared;
    }

    public TextView getTv_fivedayily() {
        return tv_fivedayily;
    }

    public void setTv_fivedayily(TextView tv_fivedayily) {
        this.tv_fivedayily = tv_fivedayily;
    }

    public TextView getTv_dailyk() {
        return tv_dailyk;
    }

    public void setTv_dailyk(TextView tv_dailyk) {
        this.tv_dailyk = tv_dailyk;
    }

    public TextView getTv_weekk() {
        return tv_weekk;
    }

    public void setTv_weekk(TextView tv_weekk) {
        this.tv_weekk = tv_weekk;
    }

    public TextView getTv_monthk() {
        return tv_monthk;
    }

    public void setTv_monthk(TextView tv_monthk) {
        this.tv_monthk = tv_monthk;
    }

    public LinearLayout getLayout_shareTime() {
        return layout_shareTime;
    }

    public void setLayout_shareTime(LinearLayout layout_shareTime) {
        this.layout_shareTime = layout_shareTime;
    }

    public RealTimeLineChart getStocktimeshareLinechar() {
        return stocktimeshareLinechar;
    }

    public void setStocktimeshareLinechar(RealTimeLineChart stocktimeshareLinechar) {
        this.stocktimeshareLinechar = stocktimeshareLinechar;
    }

    public RealTimeBarChart getStocktimeshareBarchar() {
        return stocktimeshareBarchar;
    }

    public void setStocktimeshareBarchar(RealTimeBarChart stocktimeshareBarchar) {
        this.stocktimeshareBarchar = stocktimeshareBarchar;
    }

    public MyListView getLv_sale() {
        return lv_sale;
    }

    public void setLv_sale(MyListView lv_sale) {
        this.lv_sale = lv_sale;
    }

    public MyListView getLv_buy() {
        return lv_buy;
    }

    public void setLv_buy(MyListView lv_buy) {
        this.lv_buy = lv_buy;
    }

    public LinearLayout getLayout_fivedays() {
        return layout_fivedays;
    }

    public void setLayout_fivedays(LinearLayout layout_fivedays) {
        this.layout_fivedays = layout_fivedays;
    }

    public RealTimeLineChart getStocktimeshareFiveDaysLinechar() {
        return stocktimeshareFiveDaysLinechar;
    }

    public void setStocktimeshareFiveDaysLinechar(RealTimeLineChart stocktimeshareFiveDaysLinechar) {
        this.stocktimeshareFiveDaysLinechar = stocktimeshareFiveDaysLinechar;
    }

    public RealTimeBarChart getStocktimeshareFiveDaysBarchar() {
        return stocktimeshareFiveDaysBarchar;
    }

    public void setStocktimeshareFiveDaysBarchar(RealTimeBarChart stocktimeshareFiveDaysBarchar) {
        this.stocktimeshareFiveDaysBarchar = stocktimeshareFiveDaysBarchar;
    }

    public LinearLayout getLayout_daysKchar() {
        return layout_daysKchar;
    }

    public void setLayout_daysKchar(LinearLayout layout_daysKchar) {
        this.layout_daysKchar = layout_daysKchar;
    }

    public RealTimeCombineLineChart getStockCombineDayChar() {
        return stockCombineDayChar;
    }

    public void setStockCombineDayChar(RealTimeCombineLineChart stockCombineDayChar) {
        this.stockCombineDayChar = stockCombineDayChar;
    }

    public RealTimeBarChart getStockBarDayChar() {
        return stockBarDayChar;
    }

    public void setStockBarDayChar(RealTimeBarChart stockBarDayChar) {
        this.stockBarDayChar = stockBarDayChar;
    }

    public LinearLayout getLayout_weekKchar() {
        return layout_weekKchar;
    }

    public void setLayout_weekKchar(LinearLayout layout_weekKchar) {
        this.layout_weekKchar = layout_weekKchar;
    }

    public RealTimeCombineLineChart getStockCombineWeekChar() {
        return stockCombineWeekChar;
    }

    public void setStockCombineWeekChar(RealTimeCombineLineChart stockCombineWeekChar) {
        this.stockCombineWeekChar = stockCombineWeekChar;
    }

    public RealTimeBarChart getStockBarWeekChar() {
        return stockBarWeekChar;
    }

    public void setStockBarWeekChar(RealTimeBarChart stockBarWeekChar) {
        this.stockBarWeekChar = stockBarWeekChar;
    }

    public LinearLayout getLayout_monthKchar() {
        return layout_monthKchar;
    }

    public void setLayout_monthKchar(LinearLayout layout_monthKchar) {
        this.layout_monthKchar = layout_monthKchar;
    }

    public RealTimeCombineLineChart getStockCombineMonthChar() {
        return stockCombineMonthChar;
    }

    public void setStockCombineMonthChar(RealTimeCombineLineChart stockCombineMonthChar) {
        this.stockCombineMonthChar = stockCombineMonthChar;
    }

    public RealTimeBarChart getStockBarMonthChar() {
        return stockBarMonthChar;
    }

    public void setStockBarMonthChar(RealTimeBarChart stockBarMonthChar) {
        this.stockBarMonthChar = stockBarMonthChar;
    }

    public ProgressBar getProgressbar() {
        return progressbar;
    }

    public void setProgressVisible() {
        progressbar.setVisibility(View.VISIBLE);
    }

    public void setProgressInvisible() {
        progressbar.setVisibility(View.INVISIBLE);
    }

    public DrawOver getDrawOver() {
        return drawOver;
    }

    public void setDrawOver(DrawOver drawOver) {
        this.drawOver = drawOver;
    }

    @Override
    public void drawOver() {
        drawOver.drawOver();
    }
}
