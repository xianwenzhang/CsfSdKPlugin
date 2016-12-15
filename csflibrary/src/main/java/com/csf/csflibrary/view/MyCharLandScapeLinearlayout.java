package com.csf.csflibrary.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csf.csflibrary.R;
import com.csf.csflibrary.Tools.JsonTools;
import com.csf.csflibrary.Tools.TimeUtils;
import com.csf.csflibrary.Tools.Tools;
import com.csf.csflibrary.adapter.ListStockRealAdapter;
import com.csf.csflibrary.callback.CallBackPosition;
import com.csf.csflibrary.callback.DrawOver;
import com.csf.csflibrary.constant.Constant;
import com.csf.csflibrary.entity.LineEntity;
import com.csf.csflibrary.entity.OHLCCylinderEntity;
import com.csf.csflibrary.entity.OHLCDivideValueEntity;
import com.csf.csflibrary.entity.OHLCEntity;
import com.csf.csflibrary.javaBean.CompanyStatusData;
import com.csf.csflibrary.javaBean.HqStockPriceBean;
import com.csf.csflibrary.javaBean.HqStockPriceMaBean;
import com.csf.csflibrary.javaBean.StockRealtimeBean;
import com.csf.csflibrary.javaBean.StockRealtimeTradeState;
import com.csf.csflibrary.javaBean.StockTimeShareBean;
import com.csf.csflibrary.javaBean.StockTimeShareItemBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jaily.zhang on 2016/8/2.
 */
public class MyCharLandScapeLinearlayout extends LinearLayout implements CallBackPosition, DrawOver {
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
    private ListView lv_sale;//卖出五档
    private ListView lv_buy;//买入五档
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
    private float defaultMarginBottom;
    private float defaultMarginLeft;
    private float defaultMarginRight;
    private int latitudeFontSize;
    private LinearLayout layout_buyandsellgear;
    List<StockTimeShareItemBean> listsharitemrealtime;
    ArrayList<OHLCEntity> yValuesAveragerealtime;
    ArrayList<OHLCEntity> yVolumeValuerealtime;
    List<StockTimeShareItemBean> listsharitemday;
    ArrayList<OHLCEntity> yVolumeValueday;
    ArrayList<OHLCEntity> yValuesAverageday;
    ArrayList<OHLCEntity> yFiveDayRatiovalue;
    List<HqStockPriceBean> hqStockPriceBeanListday;
    List<HqStockPriceBean> hqStockPriceBeanListweek;
    List<HqStockPriceBean> hqStockPriceBeanListmonth;
    private RelativeLayout layout_total;
    private String ClickFlag = Constant.CSF_SHARE_TIMEDATA;
    private RelativeLayout layout_previoushead;
    private LinearLayout layout_realtime;
    private LinearLayout layout_realtimeKChar;
    private TextView tv_realtime;
    private TextView tv_KChartime;
    private TextView tv_realtimepricedata;
    private TextView tv_realtimealtitudedata;
    private TextView tv_realtimealveragepricedata;
    private TextView tv_realtimechengjiaocount;
    private TextView tv_kcharpriceopen;
    private TextView tv_Kcharheightvalue;
    private TextView tv_KCharLowValue;
    private TextView tv_KCharValueClose;
    private TextView tv_KCharValueAltitude;
    private TextView tv_name;
    private TextView tv_price;
    private TextView tv_altitude;
    private TextView tv_time;
    private int dailyKCount;
    private int weekKCount;
    private int monthKCount;
    private int shareTimeCount;
    private int fiveDayCount;
    private ProgressBar progressbar;
    private DrawOver drawOver;
    // 五秒之后实时更新数据
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //分时
                case 0x10:
                    stocktimeshareLinechar.refresh();
                    stocktimeshareBarchar.refresh();
                    break;
                //五日
                case 0x11:
                    stocktimeshareFiveDaysLinechar.refresh();
                    stocktimeshareFiveDaysBarchar.refresh();
                    break;
                //日K
                case 0x12:
                    stockCombineDayChar.refresh();
                    stockBarDayChar.refresh();
                    break;
                //周k
                case 0x13:
                    stockCombineWeekChar.refresh();
                    stockBarWeekChar.refresh();
                    break;
                //月K
                case 0x14:
                    stockCombineMonthChar.refresh();
                    stockBarMonthChar.refresh();
                    break;

                //分时
                case 0x25:
                    try {
                        int position = (Integer) msg.obj;
                        if (listsharitemrealtime != null && !listsharitemrealtime.isEmpty()) {
                            if (position < listsharitemrealtime.size()) {
                                showLineCharMove(listsharitemrealtime, position);
                            }
                        }

                    } catch (Exception e) {

                    }
                    break;
                //五日
                case 0x35:
                    try {
                        int positionlast = (Integer) msg.obj;
                        if (listsharitemday != null && !listsharitemday.isEmpty()) {
                            if (positionlast < listsharitemday.size()) {
                                showLineCharMove(listsharitemday, positionlast);
                            }

                        }
                    } catch (Exception e) {

                    }
                    break;
                //日K
                case 0x45:
                    try {
                        int positionlast = (Integer) msg.obj;
                        if (hqStockPriceBeanListday != null && !hqStockPriceBeanListday.isEmpty()) {
                            if (positionlast < hqStockPriceBeanListday.size()) {
                                showKCharMove(hqStockPriceBeanListday, positionlast);
                            }

                        }
                    } catch (Exception e) {

                    }

                    break;
                //周K
                case 0x55:

                    try {
                        int positionlast = (Integer) msg.obj;
                        if (hqStockPriceBeanListweek != null && !hqStockPriceBeanListweek.isEmpty()) {
                            if (positionlast < hqStockPriceBeanListweek.size()) {
                                showKCharMove(hqStockPriceBeanListweek, positionlast);

                            }

                        }
                    } catch (Exception e) {

                    }
                    break;
                //月K
                case 0x65:
                    try {
                        int positionlast = (Integer) msg.obj;
                        if (hqStockPriceBeanListmonth != null && !hqStockPriceBeanListmonth.isEmpty()) {
                            if (positionlast < hqStockPriceBeanListmonth.size()) {
                                showKCharMove(hqStockPriceBeanListmonth, positionlast);
                            }

                        }
                    } catch (Exception e) {

                    }
                    break;
                default:

                    break;
            }

        }
    };

    public MyCharLandScapeLinearlayout(Context context) {
        super(context);
        this.mContext = context;
        initView(context);
    }

    public MyCharLandScapeLinearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(context);
    }

    public MyCharLandScapeLinearlayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView(context);
    }

    private void initView(final Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.landscape_layout, this);
        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_price = (TextView) v.findViewById(R.id.tv_price);
        tv_altitude = (TextView) v.findViewById(R.id.tv_altitude);
        tv_time = (TextView) v.findViewById(R.id.tv_time);
        progressbar = (ProgressBar) v.findViewById(R.id.progressbar);
        layout_buyandsellgear = (LinearLayout) v.findViewById(R.id.layout_buyandsellgear);
        layout_stockchar = (LinearLayout) v.findViewById(R.id.layout_stockchar);
        tv_timeshared = (TextView) v.findViewById(R.id.tv_timeshared);
        tv_fivedayily = (TextView) v.findViewById(R.id.tv_fivedayily);
        tv_dailyk = (TextView) v.findViewById(R.id.tv_dailyk);
        tv_weekk = (TextView) v.findViewById(R.id.tv_weekk);
        tv_monthk = (TextView) v.findViewById(R.id.tv_monthk);

        layout_shareTime = (LinearLayout) v.findViewById(R.id.layout_shareTime);
        stocktimeshareLinechar = (RealTimeLineChart) v.findViewById(R.id.stocktimeshareLinechar);//分时折线图
        stocktimeshareBarchar = (RealTimeBarChart) v.findViewById(R.id.stocktimeshareBarchar);//分时Bar图
        lv_sale = (ListView) v.findViewById(R.id.lv_sale);//卖出五档
        lv_sale.setDivider(null);
        lv_buy = (ListView) v.findViewById(R.id.lv_buy);//买入五档
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
        layout_total = (RelativeLayout) v.findViewById(R.id.layout_total);
        layout_previoushead = (RelativeLayout) v.findViewById(R.id.layout_previoushead);
        layout_realtime = (LinearLayout) v.findViewById(R.id.layout_realtime);
        layout_realtimeKChar = (LinearLayout) v.findViewById(R.id.layout_realtimeKChar);
        tv_realtime = (TextView) v.findViewById(R.id.tv_realtime);
        tv_KChartime = (TextView) v.findViewById(R.id.tv_KChartime);
        tv_realtimepricedata = (TextView) v.findViewById(R.id.tv_realtimepricedata);
        tv_realtimealtitudedata = (TextView) v.findViewById(R.id.tv_realtimealtitudedata);
        tv_realtimealveragepricedata = (TextView) v.findViewById(R.id.tv_realtimealveragepricedata);
        tv_realtimechengjiaocount = (TextView) v.findViewById(R.id.tv_realtimechengjiaocount);
        tv_kcharpriceopen = (TextView) v.findViewById(R.id.tv_kcharpriceopen);
        tv_Kcharheightvalue = (TextView) v.findViewById(R.id.tv_Kcharheightvalue);
        tv_KCharLowValue = (TextView) v.findViewById(R.id.tv_KCharLowValue);
        tv_KCharValueClose = (TextView) v.findViewById(R.id.tv_KCharValueClose);
        tv_KCharValueAltitude = (TextView) v.findViewById(R.id.tv_KCharValueAltitude);
        initCharView();
    }

    /**
     * 初始化CharView 控件
     */
    private void initCharView() {

        charCount = 180;
        defaultMarginRight = Tools.dip2px(mContext, 50);
        defaultMarginLeft = Tools.dip2px(mContext, 40);
        defaultMarginBottom = Tools.dip2px(mContext, 20);
        latitudeFontSize = Tools.sp2px(mContext, 12);
        //分时
        stocktimeshareLinechar = (RealTimeLineChart) findViewById(R.id.stocktimeshareLinechar);
        stocktimeshareLinechar.setShowLeftMaxMinYAxis(true);
        stocktimeshareLinechar.setShowTopLine(true);
        stocktimeshareLinechar.setShowBottomLine(true);
        stocktimeshareLinechar.setShowRightMaxMinYAxis(true);
        stocktimeshareLinechar.setShowCenterLine(true);
        stocktimeshareLinechar.setAxisMarginBottom(defaultMarginBottom);
        stocktimeshareLinechar.setAxisMarginLeft(defaultMarginLeft);
        stocktimeshareLinechar.setAxisMarginRight(defaultMarginRight);
        stocktimeshareLinechar.setLatitudeFontSize(latitudeFontSize);
        stocktimeshareLinechar.setDefaultXtitle(true);
        stocktimeshareLinechar.setShowRightLine(true);
        stocktimeshareLinechar.setShowLeftLine(true);
        stocktimeshareLinechar.setShowCenterLine(true);
        stocktimeshareLinechar.setShowXcenterLine(true);
        stocktimeshareLinechar.setDrawLeftMiddleValue(true);
        stocktimeshareLinechar.setDrawRightMiddleValue(true);
        stocktimeshareLinechar.setShowYvalueOutSide(true);
        stocktimeshareLinechar.setLandScape(true);
        stocktimeshareLinechar.setDrawOver(this);
        stocktimeshareBarchar = (RealTimeBarChart) findViewById(R.id.stocktimeshareBarchar);
        stocktimeshareBarchar.setShowBottomLine(true);
        stocktimeshareBarchar.setShowTopLine(true);
        stocktimeshareBarchar.setShowLeftLine(true);
        stocktimeshareBarchar.setShowRightLine(true);
        stocktimeshareBarchar.setLatitudeFontSize(latitudeFontSize);
        stocktimeshareBarchar.setShowXcenterLine(true);
        stocktimeshareBarchar.setAxisMarginLeft(defaultMarginLeft);
        stocktimeshareBarchar.setAxisMarginRight(defaultMarginRight);
        stocktimeshareBarchar.setLandScape(true);

        //五日
        stocktimeshareFiveDaysLinechar = (RealTimeLineChart) findViewById(R.id.stocktimeshareFiveDaysLinechar);
        stocktimeshareFiveDaysLinechar.setShowLeftMaxMinYAxis(true);
        stocktimeshareFiveDaysLinechar.setShowTopLine(true);
        stocktimeshareFiveDaysLinechar.setShowBottomLine(true);
        stocktimeshareFiveDaysLinechar.setShowRightMaxMinYAxis(true);
        stocktimeshareFiveDaysLinechar.setAxisMarginBottom(defaultMarginBottom);
        stocktimeshareFiveDaysLinechar.setAxisMarginLeft(defaultMarginLeft);
        stocktimeshareFiveDaysLinechar.setAxisMarginRight(defaultMarginRight);
        stocktimeshareFiveDaysLinechar.setShowXtitleAxis(true);
        stocktimeshareFiveDaysLinechar.setShowCenterLine(true);
        stocktimeshareFiveDaysLinechar.setShowLeftLine(true);
        stocktimeshareFiveDaysLinechar.setShowRightLine(true);
        stocktimeshareFiveDaysLinechar.setShowGridYDivideLine(true);
        stocktimeshareFiveDaysLinechar.setLatitudeFontSize(latitudeFontSize);
        stocktimeshareFiveDaysLinechar.setDrawLeftMiddleValue(true);
        stocktimeshareFiveDaysLinechar.setDrawRightMiddleValue(true);
        stocktimeshareFiveDaysLinechar.setShowYvalueOutSide(true);
        stocktimeshareFiveDaysLinechar.setLandScape(true);
        stocktimeshareFiveDaysLinechar.setDrawOver(this);
        stocktimeshareFiveDaysBarchar = (RealTimeBarChart) findViewById(R.id.stocktimeshareFiveDaysBarchar);
        stocktimeshareFiveDaysBarchar.setShowBottomLine(true);
        stocktimeshareFiveDaysBarchar.setShowTopLine(true);
        stocktimeshareFiveDaysBarchar.setShowLeftLine(true);
        stocktimeshareFiveDaysBarchar.setShowGridYDivideLine(true);
        stocktimeshareFiveDaysBarchar.setShowRightLine(true);
        stocktimeshareFiveDaysBarchar.setLatitudeFontSize(latitudeFontSize);
        stocktimeshareFiveDaysBarchar.setAxisMarginLeft(defaultMarginLeft);
        stocktimeshareFiveDaysBarchar.setAxisMarginRight(defaultMarginRight);
        stocktimeshareFiveDaysBarchar.setLandScape(true);
        //日K
        stockCombineDayChar = (RealTimeCombineLineChart) findViewById(R.id.stockCombineDayChar);
        stockCombineDayChar.setShowTopLine(true);
        stockCombineDayChar.setShowBottomLine(true);
        stockCombineDayChar.setShowCenterLine(true);
        stockCombineDayChar.setAxisMarginBottom(defaultMarginBottom);
        stockCombineDayChar.setAxisMarginLeft(defaultMarginLeft);
        stockCombineDayChar.setAxisMarginRight(defaultMarginRight);
        stockCombineDayChar.setShowLeftLine(true);
        stockCombineDayChar.setShowRightLine(true);
        stockCombineDayChar.setLandScape(true);
        stockCombineDayChar.setLatitudeFontSize(latitudeFontSize);
        stockCombineDayChar.setDrawLeftMiddleValue(true);
        stockCombineDayChar.setShowYvalueOutSide(true);
        stockCombineDayChar.setShowLegend(true);
        stockCombineDayChar.setDrawOver(this);
        stockBarDayChar = (RealTimeBarChart) findViewById(R.id.stockBarDayChar);
        stockBarDayChar.setShowBottomLine(true);
        stockBarDayChar.setShowTopLine(true);
        stockBarDayChar.setShowLeftLine(true);
        stockBarDayChar.setShowRightLine(true);
        stockBarDayChar.setLandScape(true);
        stockBarDayChar.setLatitudeFontSize(latitudeFontSize);
        stockBarDayChar.setAxisMarginLeft(defaultMarginLeft);
        stockBarDayChar.setAxisMarginRight(defaultMarginRight);

        //周K
        stockCombineWeekChar = (RealTimeCombineLineChart) findViewById(R.id.stockCombineWeekChar);
        stockCombineWeekChar.setShowTopLine(true);
        stockCombineWeekChar.setShowBottomLine(true);
        stockCombineWeekChar.setShowCenterLine(true);
        stockCombineWeekChar.setAxisMarginBottom(defaultMarginBottom);
        stockCombineWeekChar.setAxisMarginLeft(defaultMarginLeft);
        stockCombineWeekChar.setAxisMarginRight(defaultMarginRight);
        stockCombineWeekChar.setLatitudeFontSize(latitudeFontSize);
        stockCombineWeekChar.setShowLeftLine(true);
        stockCombineWeekChar.setShowRightLine(true);
        stockCombineWeekChar.setDrawLeftMiddleValue(true);
        stockCombineWeekChar.setShowYvalueOutSide(true);
        stockCombineWeekChar.setLandScape(true);
        stockCombineWeekChar.setShowLegend(true);
        stockCombineWeekChar.setDrawOver(this);
        stockBarWeekChar = (RealTimeBarChart) findViewById(R.id.stockBarWeekChar);
        stockBarWeekChar.setShowBottomLine(true);
        stockBarWeekChar.setShowTopLine(true);
        stockBarWeekChar.setShowLeftLine(true);
        stockBarWeekChar.setShowRightLine(true);
        stockBarWeekChar.setLandScape(true);
        stockBarWeekChar.setAxisMarginLeft(defaultMarginLeft);
        stockBarWeekChar.setLatitudeFontSize(latitudeFontSize);
        stockBarWeekChar.setAxisMarginRight(defaultMarginRight);
        //月K
        stockCombineMonthChar = (RealTimeCombineLineChart) findViewById(R.id.stockCombineMonthChar);
        stockCombineMonthChar.setShowTopLine(true);
        stockCombineMonthChar.setShowBottomLine(true);
        stockCombineMonthChar.setShowCenterLine(true);
        stockCombineMonthChar.setAxisMarginBottom(defaultMarginBottom);
        stockCombineMonthChar.setShowLeftLine(true);
        stockCombineMonthChar.setShowRightLine(true);
        stockCombineMonthChar.setDrawLeftMiddleValue(true);
        stockCombineMonthChar.setAxisMarginLeft(defaultMarginLeft);
        stockCombineMonthChar.setAxisMarginRight(defaultMarginRight);
        stockCombineMonthChar.setLatitudeFontSize(latitudeFontSize);
        stockCombineMonthChar.setShowYvalueOutSide(true);
        stockCombineMonthChar.setLandScape(true);
        stockCombineMonthChar.setShowLegend(true);
        stockCombineMonthChar.setDrawOver(this);
        stockBarMonthChar = (RealTimeBarChart) findViewById(R.id.stockBarMonthChar);
        stockBarMonthChar.setShowBottomLine(true);
        stockBarMonthChar.setShowTopLine(true);
        stockBarMonthChar.setShowLeftLine(true);
        stockBarMonthChar.setShowRightLine(true);
        stockBarMonthChar.setLandScape(true);
        stockBarMonthChar.setLatitudeFontSize(latitudeFontSize);
        stockBarMonthChar.setAxisMarginLeft(defaultMarginLeft);
        stockBarMonthChar.setAxisMarginRight(defaultMarginRight);

    }

    public void setClickFlag(String clickFlag) {
        ClickFlag = clickFlag;
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
     * 股票实时信息
     *
     * @param jsonString
     */
    public void showStockRealtimeInfo(Context context, String jsonString) {

        try {
            CompanyStatusData companyStatusData = JsonTools.jsonStringToObject(jsonString, CompanyStatusData.class);
            if (companyStatusData != null) {
                tv_name.setText(companyStatusData.getName() + "(" + companyStatusData.getTick() + ")");
                Long dt = companyStatusData.getDt();
                if (dt != null) {
                    tv_time.setText(TimeUtils.convert2Stringtimehour(dt));
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    tv_time.setText(sdf.format(new Date(System.currentTimeMillis())));
                }
                Double price = companyStatusData.getPrice();
                Double altitude = companyStatusData.getRatio();
                if (altitude < 0) {
                    tv_price.setTextColor(getResources().getColor(R.color.eb7));
                    tv_altitude.setTextColor(getResources().getColor(R.color.eb7));
                } else {
                    // 等于0
                    if (altitude == 0) {
                        tv_price.setTextColor(getResources().getColor(R.color.aa));
                        tv_altitude.setTextColor(getResources().getColor(R.color.aa));
                    } else {
                        tv_price.setTextColor(getResources().getColor(R.color.f24957));
                        tv_altitude.setTextColor(getResources().getColor(R.color.f24957));
                    }

                }
                if (altitude != null) {
                    tv_altitude.setText(Tools.formatNum(altitude) + "%");
                } else {
                    tv_altitude.setText("--");
                }
                if (price != null) {
                    tv_price.setText(Tools.formatNum(price));
                } else {
                    tv_price.setText("--");
                }

            }
        } catch (Exception e) {

        }

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
                layout_buyandsellgear.setVisibility(View.VISIBLE);
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

                lv_sale.setAdapter(new ListStockRealAdapter(context, listsalrealtimebean, true));
                lv_buy.setAdapter(new ListStockRealAdapter(context, listbuyrealtimebean, true));

            } else {
                layout_buyandsellgear.setVisibility(View.GONE);
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
                double closeValue = stockTimeShareBean.getClose();
                double maxValue = stockTimeShareBean.getMax();
                double minValue = stockTimeShareBean.getMin();
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
                                listsharitemrealtime = listDetails;
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

                                yValuesAveragerealtime = yValuesAverage;
                                yVolumeValuerealtime = yVolumeValue;

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

                                String maxpriceString = Tools.formatNum(maxpricevalue);
                                double volumeDeal = Tools.div(volumemax, 100);
                                if (volumeDeal > 10000) {
                                    volumeDeal = Tools.div(volumeDeal, 10000);
                                } else {
                                }

                                String volumemaxString = Tools.formatNum(volumeDeal);
                                int stringLength = maxpriceString.length();

                                if (maxpriceString.length() < volumemaxString.length()) {
                                    stringLength = volumemaxString.length();
                                }
                                if (stringLength > 5) {
                                    int lenght = stringLength - 5;
                                    float axisLeft = (float) Tools.add(defaultMarginLeft, Tools.mul(latitudeFontSize, lenght));
                                    stocktimeshareLinechar.setAxisMarginLeft(axisLeft);
                                    stocktimeshareBarchar.setAxisMarginLeft(axisLeft);
                                }

                                String minratioString = Tools.formatNum(-ratiovalue) + "%";
                                if (minratioString.length() > 7) {
                                    int lenght = minratioString.length() - 7;
                                    float axisRight = (float) Tools.add(defaultMarginRight, Tools.mul(latitudeFontSize, lenght));
                                    stocktimeshareLinechar.setAxisMarginRight(axisRight);
                                    stocktimeshareBarchar.setAxisMarginRight(axisRight);
                                }

                                List<String> Xtitles = new ArrayList<>();
                                Xtitles.add("09:30");
                                Xtitles.add("11:30/13:00");
                                Xtitles.add("15:00");
                                stocktimeshareLinechar.setAxisXTitles(Xtitles);


                                stocktimeshareBarchar.setMaxLeftValue(volumemax);
                                stocktimeshareBarchar.setDrawLeftMaxXAxis(true);
                                stocktimeshareBarchar.setLineData(yVolumeValue);
                                Message message = new Message();
                                message.what = 0x10;
                                myHandler.sendMessage(message);

                                shareTimeCount = sizecount;
                            }


                        }
                        //五日
                        else if (flag.equals(Constant.CSF_SHARE_FIVEDATA)) {

                            if (fiveDayCount != sizecount) {
                                listsharitemday = listDetails;
                                double volumemax = 0;
                                String dt_division = "";
                                for (int i = 0; i < sizecount; i++) {
                                    StockTimeShareItemBean stockTimeShareItemBean = listDetails.get(i);
                                    String dt = stockTimeShareItemBean.getDt();
                                    Double price = stockTimeShareItemBean.getPrice();
                                    Double ratio = 0.0;
                                    if (closeValue == 0) {
                                        ratio = stockTimeShareItemBean.getRatio();
                                    } else {
                                        ratio = Tools.mul(Tools.div(Tools.sub(price, closeValue), closeValue), 100);
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
                                yValuesAverageday = yValuesAverage;
                                yVolumeValueday = yVolumeValue;
                                listdatas.add(new LineEntity(ypricevalue, "price", getResources().getColor(R.color.aff179), true, maxpricevalue, minpricevalue));
                                listdatas.add(new LineEntity(yValuesAverage, "averageprice", getResources().getColor(R.color.ffcc67), false, maxpricevalue,
                                        minpricevalue));
                                listdatas.add(new LineEntity(yratiovalue, "ratioValue", getResources().getColor(R.color.aff179), true, ratiovalue, -ratiovalue));
                                yFiveDayRatiovalue = yratiovalue;
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
                                String maxpriceString = Tools.formatNum(maxpricevalue);
                                double volumeDeal = Tools.div(volumemax, 100);
                                if (volumeDeal > 10000) {
                                    volumeDeal = Tools.div(volumeDeal, 10000);
                                } else {
                                }
                                String volumemaxString = Tools.formatNum(volumeDeal);
                                int stringLength = maxpriceString.length();

                                if (maxpriceString.length() < volumemaxString.length()) {
                                    stringLength = volumemaxString.length();
                                }
                                if (stringLength > 5) {
                                    int lenght = stringLength - 5;
                                    float axisLeft = (float) Tools.add(defaultMarginLeft, Tools.mul(latitudeFontSize, lenght));
                                    stocktimeshareFiveDaysLinechar.setAxisMarginLeft(axisLeft);
                                    stocktimeshareFiveDaysBarchar.setAxisMarginLeft(axisLeft);
                                }

                                String minratioString = Tools.formatNum(-ratiovalue) + "%";
                                if (minratioString.length() > 7) {
                                    int lenght = minratioString.length() - 7;
                                    float axisRight = (float) Tools.add(defaultMarginRight, Tools.mul(latitudeFontSize, lenght));
                                    stocktimeshareFiveDaysLinechar.setAxisMarginRight(axisRight);
                                    stocktimeshareFiveDaysBarchar.setAxisMarginRight(axisRight);
                                }

                                stocktimeshareFiveDaysBarchar.setMaxLeftValue(volumemax);
                                stocktimeshareFiveDaysBarchar.setDrawLeftMaxXAxis(true);
                                stocktimeshareFiveDaysBarchar.setLineData(yVolumeValue);
                                stocktimeshareFiveDaysBarchar.setDivideCountList(divideCountList);
                                Message message = new Message();
                                message.what = 0x11;
                                myHandler.sendMessage(message);

                                fiveDayCount = sizecount;
                            }

                        }

                    } else {
                    }

                }

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
                    if (dailyKCount != countsize) {
                        showKValueAsync(hqStockPriceBeanList, countsize, flag);
                        hqStockPriceBeanListday = hqStockPriceBeanList;
                        dailyKCount = countsize;
                    }

                }
                //周K
                else if (flag.equals(Constant.CSF_WEEK_KDATA)) {
                    if (weekKCount != countsize) {
                        showKValueAsync(hqStockPriceBeanList, countsize, flag);
                        hqStockPriceBeanListweek = hqStockPriceBeanList;
                        weekKCount = countsize;
                    }

                }
                //月K
                else if (flag.equals(Constant.CSF_MONTH_KDATA)) {
                    if (monthKCount != countsize) {
                        showKValueAsync(hqStockPriceBeanList, countsize, flag);
                        hqStockPriceBeanListmonth = hqStockPriceBeanList;
                        monthKCount = countsize;
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

                double volValue = hqStockPriceBean.getVol();//万手为单位
                if (volumemax < volValue)
                    volumemax = volValue;
                yVolumeValue.add(new OHLCEntity(volValue, dt, colorValue, false));
                yCylinderValue.add(new OHLCCylinderEntity(open, close, high, low, dt, colorValue, false));

                String jsonMaString = JsonTools.objectToJsonString(Ma);
                HqStockPriceMaBean hqStockPriceMaBean = JsonTools.jsonStringToObject(jsonMaString, HqStockPriceMaBean.class);
                if (hqStockPriceBean != null) {
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

                String maxpriceString = Tools.formatNum(hightValue);
                double volumeDeal = Tools.div(volumemax, 100);
                if (volumeDeal > 10000) {
                    volumeDeal = Tools.div(volumeDeal, 10000);
                }

                String volumemaxString = Tools.formatNum(volumeDeal);
                int stringLength = maxpriceString.length();
                if (maxpriceString.length() < volumemaxString.length()) {
                    stringLength = volumemaxString.length();
                }
                if (stringLength > 5) {
                    int lenght = stringLength - 5;
                    float axisLeft = (float) Tools.add(defaultMarginLeft, Tools.mul(latitudeFontSize, lenght));
                    stockCombineDayChar.setAxisMarginLeft(axisLeft);
                    stockBarDayChar.setAxisMarginLeft(axisLeft);
                }

                stockBarDayChar.setMaxLeftValue(volumemax);
                stockBarDayChar.setDrawLeftMaxXAxis(true);
                stockBarDayChar.setLineData(yVolumeValue);

                stockBarDayChar.setSpaceValue(1.0f);
                stockBarDayChar.setOhlcDivideValueEntityList(dayKOhlcDivide);

                Message message = new Message();
                message.what = 0x12;
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
                String maxpriceString = Tools.formatNum(hightValue);
                double volumeDeal = Tools.div(volumemax, 100);
                if (volumeDeal > 10000) {
                    volumeDeal = Tools.div(volumeDeal, 10000);
                }

                String volumemaxString = Tools.formatNum(volumeDeal);
                int stringLength = maxpriceString.length();

                if (maxpriceString.length() < volumemaxString.length()) {
                    stringLength = volumemaxString.length();
                }
                if (stringLength > 5) {
                    int lenght = stringLength - 5;
                    float axisLeft = (float) Tools.add(defaultMarginLeft, Tools.mul(latitudeFontSize, lenght));
                    stockCombineWeekChar.setAxisMarginLeft(axisLeft);
                    stockBarWeekChar.setAxisMarginLeft(axisLeft);
                }

                stockBarWeekChar.setMaxLeftValue(volumemax);
                stockBarWeekChar.setDrawLeftMaxXAxis(true);
                stockBarWeekChar.setLineData(yVolumeValue);

                stockBarWeekChar.setSpaceValue(1.0f);
                stockBarWeekChar.setOhlcDivideValueEntityList(dayKOhlcDivide);
                Message message = new Message();
                message.what = 0x13;
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
                String maxpriceString = Tools.formatNum(hightValue);
                double volumeDeal = Tools.div(volumemax, 100);
                if (volumeDeal > 10000) {
                    volumeDeal = Tools.div(volumeDeal, 10000);
                } else {
                }

                String volumemaxString = Tools.formatNum(volumeDeal);
                int stringLength = maxpriceString.length();
                if (maxpriceString.length() < volumemaxString.length()) {
                    stringLength = volumemaxString.length();
                }
                if (stringLength > 5) {
                    int lenght = stringLength - 5;
                    float axisLeft = (float) Tools.add(defaultMarginLeft, Tools.mul(latitudeFontSize, lenght));
                    stockCombineMonthChar.setAxisMarginLeft(axisLeft);
                    stockBarMonthChar.setAxisMarginLeft(axisLeft);
                }

                stockBarMonthChar.setMaxLeftValue(volumemax);
                stockBarMonthChar.setDrawLeftMaxXAxis(true);
                stockBarMonthChar.setLineData(yVolumeValue);

                stockBarMonthChar.setSpaceValue(1.0f);
                stockBarMonthChar.setOhlcDivideValueEntityList(dayKOhlcDivide);

                Message message = new Message();
                message.what = 0x14;
                myHandler.sendMessage(message);
            }
        } catch (Exception e) {

        }


    }

    /**
     * 控件Touch事件
     */
    public void setOnTouchListenerEvent() {
        layout_total.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showOnTouchEvent(motionEvent, false);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    showOnTouchEvent(motionEvent, false);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    showOnTouchEvent(motionEvent, true);
                }
                return true;
            }
        });
    }

    private void showOnTouchEvent(MotionEvent event, boolean isUp) {
        //分时
        if (ClickFlag.equals(Constant.CSF_SHARE_TIMEDATA)) {
            stocktimeshareLinechar.showOnTouch(event, isUp, this);
            stocktimeshareBarchar.showOnTouch(event, isUp);
            if (isUp) {
                layout_previoushead.setVisibility(View.VISIBLE);
                layout_realtime.setVisibility(View.GONE);
                layout_realtimeKChar.setVisibility(View.GONE);
            } else {
                layout_previoushead.setVisibility(View.GONE);
                layout_realtime.setVisibility(View.VISIBLE);
                layout_realtimeKChar.setVisibility(View.GONE);
            }
        }
        //五日
        else if (ClickFlag.equals(Constant.CSF_SHARE_FIVEDATA)) {
            stocktimeshareFiveDaysLinechar.showOnTouch(event, isUp, this);
            stocktimeshareFiveDaysBarchar.showOnTouch(event, isUp);
            if (isUp) {
                layout_previoushead.setVisibility(View.VISIBLE);
                layout_realtime.setVisibility(View.GONE);
                layout_realtimeKChar.setVisibility(View.GONE);
            } else {
                layout_previoushead.setVisibility(View.GONE);
                layout_realtime.setVisibility(View.VISIBLE);
                layout_realtimeKChar.setVisibility(View.GONE);
            }
        }
        //日K
        else if (ClickFlag.equals(Constant.CSF_DAILY_KDATA)) {
            stockCombineDayChar.showOnTouch(event, isUp, this);
            stockBarDayChar.showOnTouch(event, isUp);

            if (hqStockPriceBeanListday != null && !hqStockPriceBeanListday.isEmpty()) {
                if (isUp) {
                    layout_previoushead.setVisibility(View.VISIBLE);
                    layout_realtime.setVisibility(View.GONE);
                    layout_realtimeKChar.setVisibility(View.GONE);
                } else {
                    layout_previoushead.setVisibility(View.GONE);
                    layout_realtimeKChar.setVisibility(View.VISIBLE);
                    layout_realtime.setVisibility(View.GONE);
                }
            }


        }
        //周K
        else if (ClickFlag.equals(Constant.CSF_WEEK_KDATA)) {
            stockCombineWeekChar.showOnTouch(event, isUp, this);
            stockBarWeekChar.showOnTouch(event, isUp);

            if (hqStockPriceBeanListweek != null && !hqStockPriceBeanListweek.isEmpty()) {
                if (isUp) {
                    layout_previoushead.setVisibility(View.VISIBLE);
                    layout_realtime.setVisibility(View.GONE);
                    layout_realtimeKChar.setVisibility(View.GONE);
                } else {
                    layout_previoushead.setVisibility(View.GONE);
                    layout_realtimeKChar.setVisibility(View.VISIBLE);
                    layout_realtime.setVisibility(View.GONE);
                }
            }

        }
        //月K
        else if (ClickFlag.equals(Constant.CSF_MONTH_KDATA)) {
            stockCombineMonthChar.showOnTouch(event, isUp, this);
            stockBarMonthChar.showOnTouch(event, isUp);
            if (hqStockPriceBeanListmonth != null && !hqStockPriceBeanListmonth.isEmpty()) {
                if (isUp) {
                    layout_previoushead.setVisibility(View.VISIBLE);
                    layout_realtime.setVisibility(View.GONE);
                    layout_realtimeKChar.setVisibility(View.GONE);
                } else {
                    layout_previoushead.setVisibility(View.GONE);
                    layout_realtimeKChar.setVisibility(View.VISIBLE);
                    layout_realtime.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public void solve(int position) {
        //分时
        if (ClickFlag.equals(Constant.CSF_SHARE_TIMEDATA)) {
            Message message = new Message();
            message.what = 0x25;
            message.obj = position;
            myHandler.sendMessage(message);
        }
        //五日
        else if (ClickFlag.equals(Constant.CSF_SHARE_FIVEDATA)) {

            Message message = new Message();
            message.what = 0x35;
            message.obj = position;
            myHandler.sendMessage(message);

        }
        //日K
        else if (ClickFlag.equals(Constant.CSF_DAILY_KDATA)) {
            Message message = new Message();
            message.what = 0x45;
            message.obj = position;
            myHandler.sendMessage(message);
        }
        //周K
        else if (ClickFlag.equals(Constant.CSF_WEEK_KDATA)) {
            Message message = new Message();
            message.what = 0x55;
            message.obj = position;
            myHandler.sendMessage(message);
        }
        //月K
        else if (ClickFlag.equals(Constant.CSF_MONTH_KDATA)) {
            Message message = new Message();
            message.what = 0x65;
            message.obj = position;
            myHandler.sendMessage(message);
        }
    }


    private void showLineCharMove(List<StockTimeShareItemBean> listsharitemrealtime, int position) {

        try {

            if (ClickFlag.equals(Constant.CSF_SHARE_TIMEDATA)) {
                StockTimeShareItemBean stockTimeShareItemBean = listsharitemrealtime.get(position);
                String[] dt = stockTimeShareItemBean.getDt().split(":");
                String dtlast = dt[0] + ":" + dt[1];
                String price = Tools.format(stockTimeShareItemBean.getPrice());
                String altitude = Tools.format(stockTimeShareItemBean.getRatio());

                tv_realtime.setText(dtlast);
                tv_realtimepricedata.setText(price);
                tv_realtimealtitudedata.setText(altitude + "%");

                if (altitude.contains("-")) {
                    tv_realtimepricedata.setTextColor(getResources().getColor(R.color.eb7));
                    tv_realtimealtitudedata.setTextColor(getResources().getColor(R.color.eb7));
                } else {
                    tv_realtimepricedata.setTextColor(getResources().getColor(R.color.f24957));
                    tv_realtimealtitudedata.setTextColor(getResources().getColor(R.color.f24957));

                }
                if (yValuesAveragerealtime != null && !yValuesAveragerealtime.isEmpty()) {
                    if (position < yValuesAveragerealtime.size()) {
                        OHLCEntity ohlcEntity = yValuesAveragerealtime.get(position);
                        tv_realtimealveragepricedata.setText(Tools.format(ohlcEntity.getOpen()));
                    }
                }
                if (yVolumeValuerealtime != null && !yVolumeValuerealtime.isEmpty()) {
                    if (position < yVolumeValuerealtime.size()) {
                        OHLCEntity ohlcEntity = yVolumeValuerealtime.get(position);
                        double openValue = Tools.div(ohlcEntity.getOpen(), 100);
                        if (openValue > 10000) {
                            tv_realtimechengjiaocount.setText(Tools.div(openValue, 10000) + "万手");
                        } else {
                            tv_realtimechengjiaocount.setText(openValue + "手");
                        }
                    }
                }
            } else if (ClickFlag.equals(Constant.CSF_SHARE_FIVEDATA)) {
                StockTimeShareItemBean stockTimeShareItemBean = listsharitemrealtime.get(position);
                String dt = stockTimeShareItemBean.getDt().split(" ")[1];
                String dtlast = dt.split(":")[0] + ":" + dt.split(":")[1];
                String price = Tools.format(stockTimeShareItemBean.getPrice());
                String altitude = Tools.format(yFiveDayRatiovalue.get(position).getOpen());

                tv_realtime.setText(dtlast);
                tv_realtimepricedata.setText(price);
                tv_realtimealtitudedata.setText(altitude + "%");

                if (altitude.contains("-")) {
                    tv_realtimepricedata.setTextColor(getResources().getColor(R.color.eb7));
                    tv_realtimealtitudedata.setTextColor(getResources().getColor(R.color.eb7));
                } else {
                    tv_realtimepricedata.setTextColor(getResources().getColor(R.color.f24957));
                    tv_realtimealtitudedata.setTextColor(getResources().getColor(R.color.f24957));

                }
                if (yValuesAverageday != null && !yValuesAverageday.isEmpty()) {
                    if (position < yValuesAverageday.size()) {
                        OHLCEntity ohlcEntity = yValuesAverageday.get(position);
                        tv_realtimealveragepricedata.setText(Tools.format(ohlcEntity.getOpen()));
                    }
                }
                if (yVolumeValueday != null && !yVolumeValueday.isEmpty()) {
                    if (position < yVolumeValueday.size()) {
                        OHLCEntity ohlcEntity = yVolumeValueday.get(position);
                        double openValue = Tools.div(ohlcEntity.getOpen(), 100);
                        if (openValue > 10000) {
                            tv_realtimechengjiaocount.setText(Tools.formatNum(Tools.div(openValue, 10000)) + "万手");
                        } else {
                            tv_realtimechengjiaocount.setText(Tools.formatNum(openValue) + "手");
                        }
                    }
                }
            }

        } catch (Exception e) {

        }


    }

    private void showKCharMove(List<HqStockPriceBean> hqStockPriceBeanListmonth, int positionlast) {

        try {
            HqStockPriceBean stockTimeShareItemBean = hqStockPriceBeanListmonth.get(positionlast);
            String[] dt = stockTimeShareItemBean.getDt().split("-");
            String dtlast = dt[1] + "-" + dt[2];
            String openValue = Tools.format(stockTimeShareItemBean.getOpen());
            String closeValue = Tools.format(stockTimeShareItemBean.getClose());
            String heightvalue = Tools.format(stockTimeShareItemBean.getHigh());
            String lowvalue = Tools.format(stockTimeShareItemBean.getLow());
            String altitude = Tools.format(stockTimeShareItemBean.getInc());

            tv_KChartime.setText(dtlast);
            tv_kcharpriceopen.setText(openValue);
            tv_Kcharheightvalue.setText(heightvalue);
            tv_KCharLowValue.setText(lowvalue);
            tv_KCharValueClose.setText(closeValue);
            tv_KCharValueAltitude.setText(altitude + "%");
            if (altitude.contains("-")) {
                tv_realtimepricedata.setTextColor(getResources().getColor(R.color.eb7));
                tv_realtimealtitudedata.setTextColor(getResources().getColor(R.color.eb7));
                tv_kcharpriceopen.setTextColor(getResources().getColor(R.color.eb7));
                tv_Kcharheightvalue.setTextColor(getResources().getColor(R.color.eb7));
                tv_KCharLowValue.setTextColor(getResources().getColor(R.color.eb7));
                tv_KCharValueClose.setTextColor(getResources().getColor(R.color.eb7));
                tv_KCharValueAltitude.setTextColor(getResources().getColor(R.color.eb7));

            } else {
                tv_realtimepricedata.setTextColor(getResources().getColor(R.color.f24957));
                tv_realtimealtitudedata.setTextColor(getResources().getColor(R.color.f24957));
                tv_kcharpriceopen.setTextColor(getResources().getColor(R.color.f24957));
                tv_Kcharheightvalue.setTextColor(getResources().getColor(R.color.f24957));
                tv_KCharLowValue.setTextColor(getResources().getColor(R.color.f24957));
                tv_KCharValueClose.setTextColor(getResources().getColor(R.color.f24957));
                tv_KCharValueAltitude.setTextColor(getResources().getColor(R.color.f24957));
            }
        } catch (Exception e) {

        }

    }

    public LinearLayout getLayout_stockchar() {
        return layout_stockchar;
    }

    public TextView getTv_timeshared() {
        return tv_timeshared;
    }

    public TextView getTv_fivedayily() {
        return tv_fivedayily;
    }

    public TextView getTv_dailyk() {
        return tv_dailyk;
    }

    public TextView getTv_weekk() {
        return tv_weekk;
    }

    public TextView getTv_monthk() {
        return tv_monthk;
    }

    public LinearLayout getLayout_shareTime() {
        return layout_shareTime;
    }

    public RealTimeLineChart getStocktimeshareLinechar() {
        return stocktimeshareLinechar;
    }

    public RealTimeBarChart getStocktimeshareBarchar() {
        return stocktimeshareBarchar;
    }

    public ListView getLv_sale() {
        return lv_sale;
    }

    public ListView getLv_buy() {
        return lv_buy;
    }

    public LinearLayout getLayout_fivedays() {
        return layout_fivedays;
    }

    public RealTimeLineChart getStocktimeshareFiveDaysLinechar() {
        return stocktimeshareFiveDaysLinechar;
    }

    public RealTimeBarChart getStocktimeshareFiveDaysBarchar() {
        return stocktimeshareFiveDaysBarchar;
    }

    public LinearLayout getLayout_daysKchar() {
        return layout_daysKchar;
    }

    public RealTimeCombineLineChart getStockCombineDayChar() {
        return stockCombineDayChar;
    }

    public RealTimeBarChart getStockBarDayChar() {
        return stockBarDayChar;
    }

    public LinearLayout getLayout_weekKchar() {
        return layout_weekKchar;
    }

    public RealTimeCombineLineChart getStockCombineWeekChar() {
        return stockCombineWeekChar;
    }

    public RealTimeBarChart getStockBarWeekChar() {
        return stockBarWeekChar;
    }

    public LinearLayout getLayout_monthKchar() {
        return layout_monthKchar;
    }

    public RealTimeCombineLineChart getStockCombineMonthChar() {
        return stockCombineMonthChar;
    }

    public RealTimeBarChart getStockBarMonthChar() {
        return stockBarMonthChar;
    }


    public LinearLayout getLayout_buyandsellgear() {
        return layout_buyandsellgear;
    }

    public RelativeLayout getLayout_total() {
        return layout_total;
    }

    public RelativeLayout getLayout_previoushead() {
        return layout_previoushead;
    }

    public LinearLayout getLayout_realtime() {
        return layout_realtime;
    }

    public LinearLayout getLayout_realtimeKChar() {
        return layout_realtimeKChar;
    }

    public TextView getTv_realtime() {
        return tv_realtime;
    }

    public TextView getTv_KChartime() {
        return tv_KChartime;
    }

    public TextView getTv_realtimepricedata() {
        return tv_realtimepricedata;
    }

    public TextView getTv_realtimealtitudedata() {
        return tv_realtimealtitudedata;
    }

    public TextView getTv_realtimealveragepricedata() {
        return tv_realtimealveragepricedata;
    }

    public TextView getTv_realtimechengjiaocount() {
        return tv_realtimechengjiaocount;
    }

    public TextView getTv_kcharpriceopen() {
        return tv_kcharpriceopen;
    }

    public TextView getTv_Kcharheightvalue() {
        return tv_Kcharheightvalue;
    }

    public TextView getTv_KCharLowValue() {
        return tv_KCharLowValue;
    }

    public TextView getTv_KCharValueClose() {
        return tv_KCharValueClose;
    }

    public TextView getTv_KCharValueAltitude() {
        return tv_KCharValueAltitude;
    }

    public ProgressBar getProgressbar() {
        return progressbar;
    }

    public void setProgressbarVisible() {
        progressbar.setVisibility(View.VISIBLE);
    }

    public void setProgressbarInvisible() {
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

    public TextView getTv_time() {
        return tv_time;
    }

    public TextView getTv_altitude() {
        return tv_altitude;
    }

    public TextView getTv_price() {
        return tv_price;
    }

    public TextView getTv_name() {
        return tv_name;
    }
}
