/*
 * LineChart.java
 * Android-Charts
 *
 * Created by limc on 2011/05/29.
 *
 * Copyright 2011 limc.cn All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.csf.csflibrary.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.csf.csflibrary.R;
import com.csf.csflibrary.Tools.Tools;
import com.csf.csflibrary.callback.DrawOver;
import com.csf.csflibrary.entity.OHLCDivideValueEntity;
import com.csf.csflibrary.entity.OHLCEntity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author jaily.zhang
 * @version V1.3.1
 * @Description BarChar 图表
 * @date 2014-8-19 上午10:45:50
 */
public class RealTimeBarChart extends View {

    private boolean isDrawLeftMaxXAxis;
    private float width;// 宽度
    private float height;// 高度
    public static final float DEFAULT_AXIS_MARGIN_LEFT = 15.0f;
    public static final float DEFAULT_AXIS_MARGIN_BOTTOM = 5.0f;
    public static final float DEFAULT_AXIS_MARGIN_TOP = 10.0f;
    public static final float DEFAULT_AXIS_MARGIN_RIGHT = 15.0f;
    public static final int DEFAULT_LATITUDE_NUM = 4;
    public static final int DEFAULT_LONGITUDE_NUM = 3;

    public static final int DEFAULT_LONGITUDE_FONT_COLOR = Color.WHITE;
    public static final int DEFAULT_LONGITUDE_FONT_SIZE = 20;
    public static final int DEFAULT_LATITUDE_FONT_SIZE = 20;
    public static final int DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = 5;
    private boolean isShowGridYDivideLine;
    private List<Integer> divideCountList;
    // 距离左边的尺寸
    private float axisMarginLeft = DEFAULT_AXIS_MARGIN_LEFT;
    // 距离底部的尺寸
    private float axisMarginBottom = DEFAULT_AXIS_MARGIN_BOTTOM;
    // 距离顶部的尺寸
    private float axisMarginTop = DEFAULT_AXIS_MARGIN_TOP;
    // 距离右边的尺寸
    private float axisMarginRight = DEFAULT_AXIS_MARGIN_RIGHT;

    private int latitudeNum = DEFAULT_LATITUDE_NUM;
    private int longitudeNum = DEFAULT_LONGITUDE_NUM;

    private int longitudeFontColor = DEFAULT_LONGITUDE_FONT_COLOR;
    private int longitudeFontSize = DEFAULT_LONGITUDE_FONT_SIZE;
    private List<OHLCDivideValueEntity> ohlcDivideValueEntityList;

    // Y轴坐标字体大小
    private int latitudeFontSize = DEFAULT_LATITUDE_FONT_SIZE;
    // X轴的坐标
    private List<String> axisXTitles;
    // Y轴的坐标
    private List<String> axisYTitles;
    private int axisYMaxTitleLength = DEFAULT_AXIS_Y_MAX_TITLE_LENGTH;
    // 手指点击的X轴
    private float clickPostX = 0f;
    // 手指点击的Y轴
    private float clickPostY = 0f;
    private List<OHLCEntity> lineData;
    private int maxPointNum;
    // 最小值
    private double minLeftValue;
    // 最大值
    private double maxLeftValue;
    private double maxRightValue;
    private double minRightValue;
    // 是否被点击的标志
    private boolean touchflag = false;
    private final int YCOUNTS = 5;
    private int gridLineNumber = 4;
    float stopY;
    float startX;
    float actualwidth;
    float actualhight;
    private int totalNumber = 50;
    private float spaceValue;
    private boolean isPainStyle;
    private boolean isShowTopLine;
    private boolean isShowBottomLine;
    private boolean isShowLeftLine;
    private boolean isShowRightLine;
    private boolean isGridXTitleline;
    private boolean isShowXcenterLine;
    private String leftBottomValueString;
    private boolean isLandScape;
    float eventX;
    float eventY;
    private int bordColor = getResources().getColor(R.color.a4b0);//边框颜色
    private float bordSize = 1.0f;//边框大小
    private float eachwidth;
    private DrawOver drawOver;
    Bitmap cacheBitmap = null;
    WeakReference<Bitmap> WeakcacheBitmap;
    Canvas cacheCanvas = null;

    Bitmap cacheGestureBitmap = null;
    WeakReference<Bitmap> WeakcacheGestureBitmap;
    Canvas cacheGestureCanvas = null;

    public RealTimeBarChart(Context context) {
        super(context);
    }

    public RealTimeBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RealTimeBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            // 图形的宽度
            width = super.getWidth();
            // 图形的高度
            height = super.getHeight();
            stopY = (float) Tools.sub(height, axisMarginBottom);//最底部
            startX = (float) Tools.sub(width, axisMarginRight);//最右部分
            actualwidth = (float) Tools.sub(startX, axisMarginLeft);
            actualhight = (float) Tools.sub(stopY, axisMarginTop);

            //画图
            if (cacheBitmap == null) {
                cacheBitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
                WeakcacheBitmap = new WeakReference<Bitmap>(cacheBitmap);
                cacheCanvas = new Canvas();
                cacheCanvas.setBitmap(WeakcacheBitmap.get());
            }

            //画手势
            if (cacheGestureBitmap == null) {
                cacheGestureBitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
                WeakcacheGestureBitmap = new WeakReference<Bitmap>(cacheGestureBitmap);
                cacheGestureCanvas = new Canvas();
                cacheGestureCanvas.setBitmap(WeakcacheGestureBitmap.get());
            }

            Paint bmppaint = new Paint();
            canvas.drawBitmap(WeakcacheBitmap.get(), 0, 0, bmppaint);
            canvas.drawBitmap(WeakcacheGestureBitmap.get(), 0, 0, bmppaint);
        } catch (Exception e) {

        }


    }


//    /**
//     * @param canvas
//     * @Description 画横坐标
//     * @author jaily.zhang
//     */
//    protected void drawAxisGridX(Canvas canvas) {
//
//        if (axisXTitles != null) {
//            int counts = axisXTitles.size();
//            float Xwidth = width - axisMarginRight - axisMarginLeft - 45;
//            float Xeachwidth = Xwidth / (counts - 1);
//
//            Paint mPaintFont = new Paint();
//            mPaintFont.setColor(getResources().getColor(R.color.sixfive));
//            mPaintFont.setTextSize(latitudeFontSize);
//            mPaintFont.setAntiAlias(true);
//            // 设置空心
//            mPaintFont.setStyle(Style.FILL);
//            try {
//                int size = (counts - 1 - 2) / 3;
//                for (int i = 0; i < counts; i++) {
//                    if (counts <= 5) {
//                        canvas.drawText(axisXTitles.get(i), i * Xeachwidth + axisMarginLeft, height, mPaintFont);
//                    } else {
//                        if ((i + 1 + size) % size == 0) {
//                            if (i != 0 && i != counts - 1) {
//                                mPaintFont.setColor(getResources().getColor(R.color.sixfive));
//                                canvas.drawText(axisXTitles.get(i), i * Xeachwidth + axisMarginLeft, height, mPaintFont);
//                                mPaintFont.setColor(getResources().getColor(R.color.e3));
//                                canvas.drawLine(i * Xeachwidth + axisMarginLeft, axisMarginTop, i * Xeachwidth + axisMarginLeft, height - axisMarginBottom, mPaintFont);
//                            }
//                        }
//                        if (i == 0) {
//                            canvas.drawText(axisXTitles.get(i), axisMarginLeft, height, mPaintFont);
//                        }
//
//                        if (i == counts - 1) {
//                            mPaintFont.setColor(getResources().getColor(R.color.sixfive));
//                            canvas.drawText(axisXTitles.get(i), width - axisMarginRight - 20, height, mPaintFont);
//                        }
//
//                    }
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//            }
//
//        }
//    }


    // 重新刷新
    public void refresh() {
        try {
            cacheBitmap = null;
            cacheCanvas = null;
            stopY = (float) Tools.sub(height, axisMarginBottom);//最底部
            startX = (float) Tools.sub(width, axisMarginRight);//最右部分
            actualwidth = (float) Tools.sub(startX, axisMarginLeft);
            actualhight = (float) Tools.sub(stopY, axisMarginTop);
            eachwidth = (float) Tools.div(actualwidth, totalNumber);
            try {
                cacheBitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
                WeakcacheBitmap = new WeakReference<Bitmap>(cacheBitmap);
                cacheCanvas = new Canvas();
                cacheCanvas.setBitmap(WeakcacheBitmap.get());
                if (this.lineData != null && !lineData.isEmpty()) {
                    // 画四条边框
                    drawBorder(cacheCanvas);
                    // 画纬线及纵坐标
                    if (isDrawLeftMaxXAxis)
                        drawAxisGridY(cacheCanvas);
                    drawLines(cacheCanvas);
                    drawOver.drawOver();
                }
            } catch (Exception e) {

            }
            invalidate();
        } catch (Exception e) {

        }

    }

    /**
     * @param canvas
     * @Description 画边框
     * @author jaily.zhang
     */
    protected void drawBorder(Canvas canvas) {

        try {
            Paint mPaint = new Paint();
            // 设置画笔颜色
            mPaint.setColor(bordColor);
            mPaint.setStrokeWidth(bordSize);
            // 去掉锯齿
            mPaint.setAntiAlias(true);
            // 设置空心
            mPaint.setStyle(Style.STROKE);

            if (isLandScape) {
                // 画左右两条线
                canvas.drawLine(axisMarginLeft, axisMarginTop, axisMarginLeft, stopY, mPaint);
                canvas.drawLine(startX, axisMarginTop, startX, stopY, mPaint);
                //画上下两根线
                canvas.drawLine(axisMarginLeft, axisMarginTop, startX, axisMarginTop, mPaint);
                canvas.drawLine(axisMarginLeft, stopY, startX, stopY, mPaint);
            }

            if (isShowXcenterLine) {
                mPaint.setColor(bordColor);
                canvas.drawLine((float) Tools.add(axisMarginLeft, Tools.div(actualwidth, 2)), axisMarginTop, (float) Tools.add(axisMarginLeft, Tools.div(actualwidth, 2)), stopY, mPaint);
            }
        } catch (Exception e) {

        }

    }

    /**
     * @param canvas
     * @Description 画纬线及纵坐标
     * @author jaily.zhang
     */
    protected void drawAxisGridY(Canvas canvas) {

        try {
            // 写字体的画笔
            Paint mPaintFont = new Paint();
            mPaintFont.setTextSize(latitudeFontSize);
            mPaintFont.setAntiAlias(true);
            // 设置空心
            mPaintFont.setStyle(Style.FILL);
            mPaintFont.setColor(bordColor);
            mPaintFont.setTextAlign(Align.RIGHT);
            double maxleftvalue = Tools.div(maxLeftValue, 100);//手为单位
            if (maxleftvalue > 10000) {
                maxleftvalue = Tools.div(maxleftvalue, 10000);
                leftBottomValueString = "万手";
            } else {
                leftBottomValueString = "手";
            }
            canvas.drawText(Tools.formatNum(maxleftvalue), (float) Tools.sub(axisMarginLeft, 5.0f), (float) Tools.add(axisMarginTop, latitudeFontSize), mPaintFont);
            canvas.drawText(leftBottomValueString, (float) Tools.sub(axisMarginLeft, 5.0f), (float) Tools.sub(stopY, 5), mPaintFont);
        } catch (Exception e) {

        }

    }

    /**
     * <p>
     * draw line
     * <p>
     * 绘制线条
     * </p>
     *
     * @param canvas
     */
    protected void drawLines(Canvas canvas) {

        try {
            // distance between two points
            // draw lines
            float maxvalue = (float) Tools.sub(maxLeftValue, minLeftValue);
            Paint mPaint = new Paint();
            // 去掉锯齿
            mPaint.setAntiAlias(true);
            // 设置空心
            mPaint.setStyle(Style.STROKE);
            // 设置画笔颜色
            mPaint.setColor(bordColor);
            mPaint.setStrokeWidth(bordSize);

            if (isShowGridYDivideLine) {
                if (divideCountList != null && !divideCountList.isEmpty()) {
                    for (int i = 0; i < divideCountList.size(); i++) {
                        double count = divideCountList.get(i);
                        float statLineX = (float) Tools.add(axisMarginLeft, Tools.mul(count, eachwidth));
                        canvas.drawLine(statLineX, axisMarginTop, statLineX, stopY, mPaint);
                    }
                }
            }

            //画横分割线
            if (ohlcDivideValueEntityList != null && !ohlcDivideValueEntityList.isEmpty()) {

                for (int i = 0; i < ohlcDivideValueEntityList.size(); i++) {
                    OHLCDivideValueEntity ohlcDivideValueEntity = ohlcDivideValueEntityList.get(i);
                    int position = ohlcDivideValueEntity.getPosition();
                    float statLineX = (float) Tools.add(axisMarginLeft, Tools.mul(position, eachwidth));
                    if (isLandScape) {
                        canvas.drawLine(statLineX, axisMarginTop, statLineX, stopY, mPaint);
                    } else {
                        canvas.drawLine(statLineX, 0, statLineX, stopY, mPaint);
                    }

                }
            }


            for (int i = 0; i < lineData.size(); i++) {
                OHLCEntity line = (OHLCEntity) lineData.get(i);
                mPaint.setColor(line.getLineColor());
                if (line.isShowFillOrStroke()) {
                    mPaint.setStyle(Style.STROKE);
                    mPaint.setStrokeWidth((float) (Tools.sub(eachwidth, 10)));
                } else {
                    mPaint.setStyle(Style.STROKE.FILL);
                    mPaint.setStrokeWidth(eachwidth);
                }

                float value = (float) Tools.sub(line.getOpen(), minLeftValue);
                float pointx = (float) Tools.add(Tools.mul(i, eachwidth), axisMarginLeft);
                float pointy = (float) Tools.sub(stopY, Tools.div(Tools.mul(value, actualhight), maxvalue));
                canvas.drawRect(pointx, pointy, (float) Tools.sub(Tools.add(pointx, eachwidth), spaceValue), stopY, mPaint);                 //绘制矩形
            }


        } catch (Exception e) {

        }

    }


    public void showOnTouch(MotionEvent event, boolean isup) {
        cacheGestureBitmap = null;
        cacheGestureCanvas = null;
        eventX = event.getX();
        eventY = event.getY();
        try {
            if (eventX >= axisMarginLeft && eventX <= startX) {

                if (isup) {
                    cacheGestureBitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
                    WeakcacheGestureBitmap = new WeakReference<Bitmap>(cacheGestureBitmap);
                    cacheGestureCanvas = new Canvas();
                    cacheGestureCanvas.setBitmap(WeakcacheGestureBitmap.get());
                } else {
                    cacheGestureBitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
                    WeakcacheGestureBitmap = new WeakReference<Bitmap>(cacheGestureBitmap);
                    cacheGestureCanvas = new Canvas();
                    cacheGestureCanvas.setBitmap(WeakcacheGestureBitmap.get());
                    // 如果被点击，显示十字线
                    drawWithFingerClick(cacheGestureCanvas);
                }

            }
        } catch (Exception e) {

        }
        invalidate();
    }

    /**
     * @param canvas
     * @Description 画十字线
     * @author jaily.zhang
     */
    protected void drawWithFingerClick(Canvas canvas) {

        try {
            Paint mPaint = new Paint();
            mPaint.setColor(Color.rgb(82, 105, 131));
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(3.0f);
            // 设置空心
            mPaint.setStyle(Style.FILL);
            if (lineData != null && !lineData.isEmpty()) {
                int position = (int) Tools.div(Tools.sub(eventX, axisMarginLeft), eachwidth);
                if (position < lineData.size()) {
                    canvas.drawLine(eventX, 0, eventX, height - 5.0f, mPaint);
                    mPaint.setTextSize(latitudeFontSize);
                    mPaint.setTextAlign(Align.RIGHT);
                    double openValue = Tools.div(lineData.get(position).getOpen(), 100);
                    if (openValue > 10000) {
                        canvas.drawText(Tools.formatNum(Tools.div(openValue, 10000)) + "万手", startX, axisMarginTop + latitudeFontSize, mPaint);
                    } else {
                        canvas.drawText(Tools.formatNum(openValue) + "手", startX, axisMarginTop + latitudeFontSize, mPaint);
                    }

                }

            }
        } catch (Exception e) {

        }


    }

    /**
     * @return the lineData
     */
    public List<OHLCEntity> getLineData() {
        return lineData;
    }

    /**
     * @param lineData the lineData to set
     */
    public void setLineData(List<OHLCEntity> lineData) {
        this.lineData = lineData;
    }

    /**
     * @return the maxPointNum
     */
    public int getMaxPointNum() {
        return maxPointNum;
    }

    /**
     * @param maxPointNum the maxPointNum to set
     */
    public void setMaxPointNum(int maxPointNum) {
        this.maxPointNum = maxPointNum;
    }

    public float getAxisMarginLeft() {
        return axisMarginLeft;
    }

    public void setAxisMarginLeft(float axisMarginLeft) {
        this.axisMarginLeft = axisMarginLeft;
    }

    public float getAxisMarginBottom() {
        return axisMarginBottom;
    }

    public void setAxisMarginBottom(float axisMarginBottom) {
        this.axisMarginBottom = axisMarginBottom;
    }

    public float getAxisMarginTop() {
        return axisMarginTop;
    }

    public void setAxisMarginTop(float axisMarginTop) {
        this.axisMarginTop = axisMarginTop;
    }

    public float getAxisMarginRight() {
        return axisMarginRight;
    }

    public void setAxisMarginRight(float axisMarginRight) {
        this.axisMarginRight = axisMarginRight;
    }

    public int getLatitudeNum() {
        return latitudeNum;
    }

    public void setLatitudeNum(int latitudeNum) {
        this.latitudeNum = latitudeNum;
    }

    public int getLongitudeNum() {
        return longitudeNum;
    }

    public void setLongitudeNum(int longitudeNum) {
        this.longitudeNum = longitudeNum;
    }

    public int getLongitudeFontColor() {
        return longitudeFontColor;
    }

    public void setLongitudeFontColor(int longitudeFontColor) {
        this.longitudeFontColor = longitudeFontColor;
    }

    public int getLongitudeFontSize() {
        return longitudeFontSize;
    }

    public void setLongitudeFontSize(int longitudeFontSize) {
        this.longitudeFontSize = longitudeFontSize;
    }

    public int getLatitudeFontSize() {
        return latitudeFontSize;
    }

    public void setLatitudeFontSize(int latitudeFontSize) {
        this.latitudeFontSize = latitudeFontSize;
    }

    public List<String> getAxisXTitles() {
        return axisXTitles;
    }

    public void setAxisXTitles(List<String> axisXTitles) {
        this.axisXTitles = axisXTitles;
    }

    public List<String> getAxisYTitles() {
        return axisYTitles;
    }

    public void setAxisYTitles(List<String> axisYTitles) {
        this.axisYTitles = axisYTitles;
    }

    public int getAxisYMaxTitleLength() {
        return axisYMaxTitleLength;
    }

    public void setAxisYMaxTitleLength(int axisYMaxTitleLength) {
        this.axisYMaxTitleLength = axisYMaxTitleLength;
    }

    public float getClickPostX() {
        return clickPostX;
    }

    public void setClickPostX(float clickPostX) {
        this.clickPostX = clickPostX;
    }

    public float getClickPostY() {
        return clickPostY;
    }

    public void setClickPostY(float clickPostY) {
        this.clickPostY = clickPostY;
    }


    public double getMinLeftValue() {
        return minLeftValue;
    }

    public void setMinLeftValue(double minLeftValue) {
        this.minLeftValue = minLeftValue;
    }

    public double getMaxLeftValue() {
        return maxLeftValue;
    }

    public void setMaxLeftValue(double maxLeftValue) {
        this.maxLeftValue = maxLeftValue;
    }

    public double getMaxRightValue() {
        return maxRightValue;
    }

    public void setMaxRightValue(double maxRightValue) {
        this.maxRightValue = maxRightValue;
    }

    public double getMinRightValue() {
        return minRightValue;
    }

    public void setMinRightValue(double minRightValue) {
        this.minRightValue = minRightValue;
    }

    public boolean isShowTopLine() {
        return isShowTopLine;
    }

    public void setShowTopLine(boolean showTopLine) {
        isShowTopLine = showTopLine;
    }

    public boolean isShowBottomLine() {
        return isShowBottomLine;
    }

    public void setShowBottomLine(boolean showBottomLine) {
        isShowBottomLine = showBottomLine;
    }

    public boolean isShowLeftLine() {
        return isShowLeftLine;
    }

    public void setShowLeftLine(boolean showLeftLine) {
        isShowLeftLine = showLeftLine;
    }

    public boolean isShowRightLine() {
        return isShowRightLine;
    }

    public void setShowRightLine(boolean showRightLine) {
        isShowRightLine = showRightLine;
    }

    public boolean isGridXTitleline() {
        return isGridXTitleline;
    }

    public void setGridXTitleline(boolean gridXTitleline) {
        isGridXTitleline = gridXTitleline;
    }

    public List<OHLCDivideValueEntity> getOhlcDivideValueEntityList() {
        return ohlcDivideValueEntityList;
    }

    public void setOhlcDivideValueEntityList(List<OHLCDivideValueEntity> ohlcDivideValueEntityList) {
        this.ohlcDivideValueEntityList = ohlcDivideValueEntityList;
    }

    public boolean isShowXcenterLine() {
        return isShowXcenterLine;
    }

    public void setShowXcenterLine(boolean showXcenterLine) {
        isShowXcenterLine = showXcenterLine;
    }

    public String getLeftBottomValueString() {
        return leftBottomValueString;
    }

    public void setLeftBottomValueString(String leftBottomValueString) {
        this.leftBottomValueString = leftBottomValueString;
    }

    public boolean isLandScape() {
        return isLandScape;
    }

    public void setLandScape(boolean landScape) {
        isLandScape = landScape;
    }

    public List<Integer> getDivideCountList() {
        return divideCountList;
    }

    public void setDivideCountList(List<Integer> divideCountList) {
        this.divideCountList = divideCountList;
    }

    public boolean isShowGridYDivideLine() {
        return isShowGridYDivideLine;
    }

    public void setShowGridYDivideLine(boolean showGridYDivideLine) {
        isShowGridYDivideLine = showGridYDivideLine;
    }

    //设置画笔为空心
    public boolean isPainStyle() {
        return isPainStyle;
    }

    public void setPainStyle(boolean painStyle) {
        isPainStyle = painStyle;
    }

    public float getSpaceValue() {
        return spaceValue;
    }

    public void setSpaceValue(float spaceValue) {
        this.spaceValue = spaceValue;
    }

    public boolean isDrawLeftMaxXAxis() {
        return isDrawLeftMaxXAxis;
    }

    public void setDrawLeftMaxXAxis(boolean drawLeftMaxXAxis) {
        isDrawLeftMaxXAxis = drawLeftMaxXAxis;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getGridLineNumber() {
        return gridLineNumber;
    }

    public void setGridLineNumber(int gridLineNumber) {
        this.gridLineNumber = gridLineNumber;
    }

    public float getBordSize() {
        return bordSize;
    }

    public void setBordSize(float bordSize) {
        this.bordSize = bordSize;
    }

    public int getBordColor() {
        return bordColor;
    }

    public void setBordColor(int bordColor) {
        this.bordColor = bordColor;
    }

    public DrawOver getDrawOver() {
        return drawOver;
    }

    public void setDrawOver(DrawOver drawOver) {
        this.drawOver = drawOver;
    }
}
