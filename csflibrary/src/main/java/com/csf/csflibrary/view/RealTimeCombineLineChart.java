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
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.csf.csflibrary.R;
import com.csf.csflibrary.Tools.Tools;
import com.csf.csflibrary.callback.CallBackPosition;
import com.csf.csflibrary.callback.DrawOver;
import com.csf.csflibrary.entity.LineEntity;
import com.csf.csflibrary.entity.OHLCCylinderEntity;
import com.csf.csflibrary.entity.OHLCDivideValueEntity;
import com.csf.csflibrary.entity.OHLCEntity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author jaily.zhang
 * @version V1.3.1
 * @Description LineChar 图表
 * @date 2014-8-19 上午10:45:50
 */
public class RealTimeCombineLineChart extends View {

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
    private List<LineEntity> lineData;
    private List<OHLCCylinderEntity> lineCylinderData;
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
    private int gridLineXNumber = 4;
    private float marginXtitleSpace;
    float stopY;
    float startX;
    float actualwidth;
    float actualhight;
    private int totalNumber;
    private boolean isShowLeftMaxMinYAxis;
    private boolean isShowRightMaxMinYAxis;
    private boolean isShowXtitleAxis;
    private float spaceValue;
    private boolean isShowTopLine;
    private boolean isShowBottomLine;
    private boolean isShowLeftLine;
    private boolean isShowRightLine;
    private boolean isShowGridXLine;
    private boolean isShowGridYLine;
    private boolean isShowCenterLine;
    private boolean isDrawLeftMiddleValue;
    private boolean isShowYvalueOutSide;
    private boolean isLandScape;
    private boolean isShowLegend;
    private int bordColor = getResources().getColor(R.color.a4b0);//边框颜色
    private float bordSize = 1.0f;//边框大小
    private List<OHLCDivideValueEntity> ohlcDivideValueEntityList;
    float eventX;
    float eventY;
    private DrawOver drawOver;
    Bitmap cacheBitmap = null;
    WeakReference<Bitmap> WeakcacheBitmap;
    Canvas cacheCanvas = null;

    Bitmap cacheGestureBitmap = null;
    WeakReference<Bitmap> WeakcacheGestureBitmap;
    Canvas cacheGestureCanvas = null;

    public RealTimeCombineLineChart(Context context) {
        super(context);
    }

    public RealTimeCombineLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RealTimeCombineLineChart(Context context, AttributeSet attrs) {
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

    public void showOnTouch(MotionEvent event, boolean isup, CallBackPosition callBackPosition) {
        try {
            cacheGestureBitmap = null;
            cacheGestureCanvas = null;
            eventX = event.getX();
            eventY = event.getY();
            try {
                if (eventX >= axisMarginLeft && eventX <= startX && eventY >= axisMarginTop) {

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
                        drawWithFingerClick(cacheGestureCanvas, callBackPosition);
                    }

                }
            } catch (Exception e) {

            }
            invalidate();
        } catch (Exception e) {

        }

    }


    /**
     * @param canvas
     * @Description 画十字线
     * @author jaily.zhang
     */
    protected void drawWithFingerClick(Canvas canvas, CallBackPosition callBackPosition) {
        try {
            Paint mPaint = new Paint();
            mPaint.setColor(Color.rgb(82, 105, 131));
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(3.0f);
            // 设置空心
            mPaint.setStyle(Style.FILL);

            float eachwidth = (float) Tools.div(actualwidth, totalNumber);
            int position = (int) Tools.div(Tools.sub(eventX, axisMarginLeft), eachwidth);

            if (lineCylinderData != null && !lineCylinderData.isEmpty() && position < lineCylinderData.size()) {

                if (position < lineCylinderData.size()) {
                    callBackPosition.solve(position);
                    OHLCCylinderEntity line = (OHLCCylinderEntity) lineCylinderData.get(position);
                    float closeValue = (float) Tools.sub(line.getClose(), minLeftValue);
                    // 画经线
                    float maxvalue = (float) Tools.sub(maxLeftValue, minLeftValue);
                    canvas.drawLine(eventX, axisMarginTop, eventX, height, mPaint);
                    float pointy = (float) Tools.sub(stopY, Tools.div(Tools.mul(closeValue, actualhight), maxvalue));
                    canvas.drawLine(axisMarginLeft, pointy, startX, pointy, mPaint);
                    canvas.drawCircle(eventX, pointy, 10.0f, mPaint);
                    canvas.drawRect(0, pointy - latitudeFontSize, axisMarginLeft, pointy + latitudeFontSize, mPaint);
                    mPaint.setColor(Color.WHITE);
                    mPaint.setTextAlign(Align.RIGHT);
                    mPaint.setTextSize(latitudeFontSize);
                    canvas.drawText(Tools.formatNum(line.getClose()), axisMarginLeft - 5.0f, pointy + 7.0f, mPaint);
                }


            }
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

            // 画左右两条线
            if (isShowLeftLine)
                canvas.drawLine(axisMarginLeft, axisMarginTop, axisMarginLeft, stopY, mPaint);
            if (isShowRightLine)
                canvas.drawLine(startX, axisMarginTop, startX, stopY, mPaint);
            //画上下两根线
            if (isShowTopLine)
                canvas.drawLine(axisMarginLeft, axisMarginTop, startX, axisMarginTop, mPaint);
            if (isShowBottomLine)
                canvas.drawLine(axisMarginLeft, stopY, startX, stopY, mPaint);
            if (isShowCenterLine) {
                // 画中间虚线
                float centY = (float) Tools.add(axisMarginTop, Tools.div(actualhight, 2));
                canvas.drawLine(axisMarginLeft, centY, startX, centY, mPaint); // 同一个path对象，只需要一个drawPath()
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

            float subMarginLeft = (float) Tools.sub(axisMarginLeft, 5.0f);
            float addMarginLeft = (float) Tools.add(axisMarginLeft, 10);
            float addMarginTop = (float) Tools.add(axisMarginTop, 20);
            float subStopy = (float) Tools.sub(stopY, 5);
            double middleValue = Tools.div(Tools.add(maxLeftValue, minLeftValue), 2);
            float addMiddleHight = (float) Tools.add(Tools.add(axisMarginTop, Tools.div(actualhight, 2)), 6.0f);

            //显示左Y轴最大最小值
            if (isShowLeftMaxMinYAxis) {
                if (isShowYvalueOutSide) {
                    mPaintFont.setTextAlign(Align.RIGHT);
                    canvas.drawText(Tools.formatNum(maxLeftValue), subMarginLeft, addMarginTop, mPaintFont);
                    canvas.drawText(Tools.formatNum(minLeftValue), subMarginLeft, subStopy, mPaintFont);
                } else {
                    mPaintFont.setTextAlign(Align.LEFT);
                    canvas.drawText(Tools.formatNum(maxLeftValue), addMarginLeft, addMarginTop, mPaintFont);
                    canvas.drawText(Tools.formatNum(minLeftValue), addMarginLeft, subStopy, mPaintFont);
                }

            }
            //显示左中间值
            if (isDrawLeftMiddleValue) {
                if (isShowYvalueOutSide) {
                    mPaintFont.setTextAlign(Align.RIGHT);
                    canvas.drawText(Tools.formatNum(middleValue), subMarginLeft, addMiddleHight, mPaintFont);
                } else {
                    mPaintFont.setTextAlign(Align.LEFT);
                    canvas.drawText(Tools.formatNum(middleValue), addMarginLeft, addMiddleHight, mPaintFont);
                }
            }

        } catch (Exception e) {

        }


    }


    protected void drawLines(Canvas canvas) {
        try {
            // distance between two points
            // draw lines
            float eachwidth = (float) Tools.div(actualwidth, totalNumber);
            for (int i = 0; i < lineData.size(); i++) {
                LineEntity line = (LineEntity) lineData.get(i);
                Paint mPaint = new Paint();
                mPaint.setColor(line.getLineColor());
                mPaint.setStyle(Style.STROKE.FILL);
                mPaint.setStrokeWidth(2);
                mPaint.setAntiAlias(true);
                List<OHLCEntity> lineDatavalue = line.getLineData();
                double maxDataValue = line.getMaxValue();
                double minDataValue = line.getMinValue();
                float maxvalue = (float) Tools.sub(maxDataValue, minDataValue);
                if (lineDatavalue != null && !lineDatavalue.isEmpty()) {
                    float x = 0;
                    float y = 0;
                    Path path = new Path();
                    path.moveTo(axisMarginLeft, stopY);
                    int lineDataSize = lineDatavalue.size();
                    for (int j = 0; j < lineDataSize; j++) {
                        float value = (float) Tools.sub(lineDatavalue.get(j).getOpen(), minDataValue);
                        float calculateValue = (float) Tools.div(Tools.mul(value, actualhight), maxvalue);
                        if (calculateValue < 0)
                            calculateValue = 0;
                        if (j == 0) {
                            x = axisMarginLeft;
                            y = (float) Tools.sub(stopY, calculateValue);
                            path.lineTo(x, y);
                        } else {
                            float pointx = (float) Tools.add(Tools.mul(j, eachwidth), axisMarginLeft);
                            float pointy = (float) Tools.sub(stopY, calculateValue);

                            if (pointx <= startX) {
                                canvas.drawLine(x, y, pointx, pointy, mPaint);
                                x = pointx;
                                y = pointy;
                                path.lineTo(pointx, pointy);
                            }
                        }
                    }

                    path.lineTo(x, stopY);
                    path.close();

                    if (line.isShowShader()) {
                        int red = (line.getLineColor() & 0xff0000) >> 16;
                        int green = (line.getLineColor() & 0x00ff00) >> 8;
                        int blue = (line.getLineColor() & 0x0000ff);
                        Shader mShader = new LinearGradient(axisMarginLeft, axisMarginTop, axisMarginLeft, height - axisMarginBottom, Color.argb(80, red, green, blue), Color.argb(20, red, green, blue), Shader.TileMode.MIRROR);
                        mPaint.setShader(mShader);
                        canvas.drawPath(path, mPaint);
                    }

                }
            }

            //画横分割线
            if (ohlcDivideValueEntityList != null && !ohlcDivideValueEntityList.isEmpty()) {
                Paint mPaintFont = new Paint();
                mPaintFont.setColor(bordColor);
                mPaintFont.setTextSize(latitudeFontSize);
                mPaintFont.setAntiAlias(true);
                // 设置空心
                mPaintFont.setStyle(Style.FILL);
                int sizeCount = ohlcDivideValueEntityList.size();
                for (int i = 0; i < sizeCount; i++) {
                    OHLCDivideValueEntity ohlcDivideValueEntity = ohlcDivideValueEntityList.get(i);
                    int position = ohlcDivideValueEntity.getPosition();
                    String title = ohlcDivideValueEntity.getTitle();
                    float statLineX = (float) Tools.add(axisMarginLeft, Tools.mul(position, eachwidth));
                    if (isLandScape) {
                        canvas.drawLine(statLineX, axisMarginTop, statLineX, stopY, mPaintFont);
                        canvas.drawText(title, (float) Tools.sub(statLineX, 20.0f), height - 5.0f, mPaintFont);
                    } else {
                        canvas.drawLine(statLineX, axisMarginTop, statLineX, height, mPaintFont);
                        canvas.drawText(title, (float) Tools.sub(Tools.mul(eachwidth, position), marginXtitleSpace), height - 5.0f, mPaintFont);
                    }

                }
            }


            if (isShowLegend) {
                Paint mPaintFont = new Paint();
                mPaintFont.setColor(Color.rgb(247, 251, 255));
                mPaintFont.setStyle(Style.FILL);
                mPaintFont.setTextSize(latitudeFontSize);
                mPaintFont.setAntiAlias(true);
                canvas.drawRect(axisMarginLeft, axisMarginTop, axisMarginLeft + 600.0f, axisMarginTop + 50.0f, mPaintFont);
                mPaintFont.setColor(getResources().getColor(R.color.ffc415));
                canvas.drawCircle(axisMarginLeft + 30.0f, axisMarginTop + 25.0f, 8.0f, mPaintFont);
                canvas.drawText("MA5", axisMarginLeft + 50.0f, axisMarginTop + 32.0f, mPaintFont);
                mPaintFont.setColor(getResources().getColor(R.color.cfc));
                canvas.drawCircle(axisMarginLeft + 160.0f, axisMarginTop + 25.0f, 8.0f, mPaintFont);
                canvas.drawText("MA10", axisMarginLeft + 180.0f, axisMarginTop + 32.0f, mPaintFont);
                mPaintFont.setColor(getResources().getColor(R.color.ff8ed9));
                canvas.drawCircle(axisMarginLeft + 300.0f, axisMarginTop + 25.0f, 8.0f, mPaintFont);
                canvas.drawText("MA20", axisMarginLeft + 320.0f, axisMarginTop + 32.0f, mPaintFont);
            }
        } catch (Exception e) {

        }


    }

    //画柱形图
    protected void drawCylinderLines(Canvas canvas) {
        try {
            // distance between two points
            // draw lines
            float eachwidth = (float) Tools.div(actualwidth, totalNumber);
            float maxvalue = (float) Tools.sub(maxLeftValue, minLeftValue);
            for (int i = 0; i < lineCylinderData.size(); i++) {
                OHLCCylinderEntity line = (OHLCCylinderEntity) lineCylinderData.get(i);
                Paint mPaint = new Paint();
                mPaint.setColor(line.getLineColor());
                if (line.isShowFillOrStroke()) {
                    mPaint.setStyle(Style.STROKE);
                    mPaint.setStrokeWidth((float) (Tools.sub(eachwidth, 10)));
                } else {
                    mPaint.setStyle(Style.STROKE.FILL);
                    mPaint.setStrokeWidth(eachwidth);
                }

                mPaint.setAntiAlias(true);
                float openValue = (float) Tools.sub(line.getOpen(), minLeftValue);
                float closeValue = (float) Tools.sub(line.getClose(), minLeftValue);
                float heighValue = (float) Tools.sub(line.getHigh(), minLeftValue);
                float lowValue = (float) Tools.sub(line.getLow(), minLeftValue);
                float pointx = (float) Tools.add(Tools.mul(i, eachwidth), axisMarginLeft);
                float rightPosition = (float) Tools.sub(Tools.add(pointx, eachwidth), spaceValue);

                float pointy = 0;
                float bottomPosiont = 0;
                if (openValue == closeValue)
                    closeValue = (float) Tools.sub(openValue, 0.5);
                if (openValue > closeValue) {
                    pointy = (float) Tools.sub(stopY, Tools.div(Tools.mul(openValue, actualhight), maxvalue));
                    bottomPosiont = (float) Tools.sub(stopY, Tools.div(Tools.mul(closeValue, actualhight), maxvalue));
                } else {
                    bottomPosiont = (float) Tools.sub(stopY, Tools.div(Tools.mul(openValue, actualhight), maxvalue));
                    pointy = (float) Tools.sub(stopY, Tools.div(Tools.mul(closeValue, actualhight), maxvalue));
                }

                float heightY = (float) Tools.sub(stopY, Tools.div(Tools.mul(heighValue, actualhight), maxvalue));
                float lowY = (float) Tools.sub(stopY, Tools.div(Tools.mul(lowValue, actualhight), maxvalue));
                float lineXPosition = (float) Tools.add(pointx, Tools.div(Tools.sub(eachwidth, spaceValue), 2));
                Paint mPaintNew = new Paint();
                mPaintNew.setColor(line.getLineColor());
                mPaintNew.setStyle(Style.STROKE.FILL);
                mPaintNew.setStrokeWidth((float) (Tools.sub(eachwidth, 10)));
                mPaintNew.setAntiAlias(true);

                if (lowY > stopY)
                    lowY = stopY;
                canvas.drawLine(lineXPosition, heightY, lineXPosition, lowY, mPaintNew);
                if (bottomPosiont > stopY)
                    bottomPosiont = stopY;
                canvas.drawRect(pointx, pointy, rightPosition, bottomPosiont, mPaint); //绘制矩形

            }
        } catch (Exception e) {

        }

    }

    // 重新刷新
    public void refresh() {
        cacheBitmap = null;
        cacheCanvas = null;
        stopY = (float) Tools.sub(height, axisMarginBottom);//最底部
        startX = (float) Tools.sub(width, axisMarginRight);//最右部分
        actualwidth = (float) Tools.sub(startX, axisMarginLeft);
        actualhight = (float) Tools.sub(stopY, axisMarginTop);
        try {
            cacheBitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
            WeakcacheBitmap = new WeakReference<Bitmap>(cacheBitmap);
            cacheCanvas = new Canvas();
            cacheCanvas.setBitmap(WeakcacheBitmap.get());
            if (this.lineData != null && !lineData.isEmpty()) {
                // 画四条边框
                drawBorder(cacheCanvas);
                // 画纬线及纵坐标
                drawAxisGridY(cacheCanvas);
                drawLines(cacheCanvas);
                //画柱形图
                if (this.lineCylinderData != null && !lineCylinderData.isEmpty()) {
                    drawCylinderLines(cacheCanvas);
                }
                drawOver.drawOver();

            }
        } catch (Exception e) {

        }
        invalidate();
    }

    /**
     * @return the lineData
     */
    public List<LineEntity> getLineData() {
        return lineData;
    }

    /**
     * @param lineData the lineData to set
     */
    public void setLineData(List<LineEntity> lineData) {
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

    public List<OHLCCylinderEntity> getLineCylinderData() {
        return lineCylinderData;
    }

    public void setLineCylinderData(List<OHLCCylinderEntity> lineCylinderData) {
        this.lineCylinderData = lineCylinderData;
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

    public float getSpaceValue() {
        return spaceValue;
    }

    public void setSpaceValue(float spaceValue) {
        this.spaceValue = spaceValue;
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

    public boolean isShowGridYLine() {
        return isShowGridYLine;
    }

    public void setShowGridYLine(boolean showGridYLine) {
        isShowGridYLine = showGridYLine;
    }

    public boolean isShowCenterLine() {
        return isShowCenterLine;
    }

    public void setShowCenterLine(boolean showCenterLine) {
        isShowCenterLine = showCenterLine;
    }

    public List<OHLCDivideValueEntity> getOhlcDivideValueEntityList() {
        return ohlcDivideValueEntityList;
    }

    public void setOhlcDivideValueEntityList(List<OHLCDivideValueEntity> ohlcDivideValueEntityList) {
        this.ohlcDivideValueEntityList = ohlcDivideValueEntityList;
    }

    public float getMarginXtitleSpace() {
        return marginXtitleSpace;
    }

    public void setMarginXtitleSpace(float marginXtitleSpace) {
        this.marginXtitleSpace = marginXtitleSpace;
    }

    public boolean isDrawLeftMiddleValue() {
        return isDrawLeftMiddleValue;
    }

    public void setDrawLeftMiddleValue(boolean drawLeftMiddleValue) {
        isDrawLeftMiddleValue = drawLeftMiddleValue;
    }

    public boolean isShowYvalueOutSide() {
        return isShowYvalueOutSide;
    }

    public void setShowYvalueOutSide(boolean showYvalueOutSide) {
        isShowYvalueOutSide = showYvalueOutSide;
    }

    public boolean isLandScape() {
        return isLandScape;
    }

    public void setLandScape(boolean landScape) {
        isLandScape = landScape;
    }

    public boolean isShowLegend() {
        return isShowLegend;
    }

    public void setShowLegend(boolean showLegend) {
        isShowLegend = showLegend;
    }

    public boolean isShowXtitleAxis() {
        return isShowXtitleAxis;
    }

    public void setShowXtitleAxis(boolean showXtitleAxis) {
        isShowXtitleAxis = showXtitleAxis;
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

    public int getGridLineXNumber() {
        return gridLineXNumber;
    }

    public void setGridLineXNumber(int gridLineXNumber) {
        this.gridLineXNumber = gridLineXNumber;
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

    public boolean isShowLeftMaxMinYAxis() {
        return isShowLeftMaxMinYAxis;
    }

    public void setShowLeftMaxMinYAxis(boolean showLeftMaxMinYAxis) {
        isShowLeftMaxMinYAxis = showLeftMaxMinYAxis;
    }

    public boolean isShowRightMaxMinYAxis() {
        return isShowRightMaxMinYAxis;
    }

    public void setShowRightMaxMinYAxis(boolean showRightMaxMinYAxis) {
        isShowRightMaxMinYAxis = showRightMaxMinYAxis;
    }

    public DrawOver getDrawOver() {
        return drawOver;
    }

    public void setDrawOver(DrawOver drawOver) {
        this.drawOver = drawOver;
    }
}
