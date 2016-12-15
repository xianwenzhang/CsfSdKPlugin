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
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
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
import com.csf.csflibrary.entity.OHLCEntity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author jaily.zhang
 * @version V1.3.1
 * @Description LineChar 图表
 * @date 2014-8-19 上午10:45:50
 */
public class RealTimeLineChart extends View {

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
    float stopY;
    float startX;
    float actualwidth;
    float actualhight;
    private int totalNumber;
    private boolean isShowLeftMaxMinYAxis;
    private boolean isShowRightMaxMinYAxis;
    private boolean isShowXtitleAxis;
    private boolean isShowTopLine;
    private boolean isShowBottomLine;
    private boolean isShowLeftLine;
    private boolean isShowRightLine;
    private boolean isShowGridXLine;
    private boolean isShowGridYLine;
    private boolean isShowGridYDivideLine;
    private boolean isShowCenterLine;
    private boolean isShowXcenterLine;
    private boolean isDefaultXtitle;
    private boolean isShowYvalueOutSide;
    private boolean isLandScape;
    private List<String> divideStringList;
    float eventX;
    float eventY;
    private int bordColor = getResources().getColor(R.color.a4b0);//边框颜色
    private float bordSize = 1.0f;//边框大小
    public boolean isDrawLeftMiddleValue;
    public boolean isDrawRightMiddleValue;
    private List<Integer> divideCountList;
    private DrawOver drawOver;
    Bitmap cacheBitmap = null;
    WeakReference<Bitmap> WeakcacheBitmap;
    Canvas cacheCanvas = null;

    Bitmap cacheGestureBitmap = null;
    WeakReference<Bitmap> WeakcacheGestureBitmap;
    Canvas cacheGestureCanvas = null;
    private Context context;

    public RealTimeLineChart(Context context) {
        super(context);
    }

    public RealTimeLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RealTimeLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 图形的宽度
        this.context = context;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
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

            //横坐标中间虚线
            if (isShowCenterLine) {
                // 画中间虚线
                PathEffect effects = new DashPathEffect(new float[]{10, 10, 10, 10}, 1);
                mPaint.setPathEffect(effects);
                mPaint.setColor(getResources().getColor(R.color.ffb3b3));
                Path path1 = new Path();
                float pointy = (float) Tools.add(axisMarginTop, Tools.div(actualhight, 2));
                path1.moveTo(axisMarginLeft, pointy);
                path1.lineTo(startX, pointy);
                canvas.drawPath(path1, mPaint); // 同一个path对象，只需要一个drawPath()
            }
            //纵坐标中间虚线
            if (isShowXcenterLine) {
                mPaint.setColor(bordColor);
                float starx = (float) Tools.add(axisMarginLeft, Tools.div(actualwidth, 2));
                canvas.drawLine(starx, axisMarginTop, starx, stopY, mPaint);
            }

        } catch (Exception e) {

        }

    }

    /**
     * @param canvas
     * @Description 画纵坐标
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
            mPaintFont.setColor(getResources().getColor(R.color.a4b0));
            float leftstartx = (float) Tools.sub(axisMarginLeft, 5.0f);
            float topstarty = (float) Tools.add(axisMarginTop, 20);
            float bottomstarty = (float) Tools.sub(stopY, 5);
            float leftstrartxanother = (float) Tools.add(axisMarginLeft, 10);
            float rightaddstarx = (float) Tools.add(startX, 5.0f);
            float rightsubstarx = (float) Tools.sub(startX, 5);

            double middleValue = Tools.div(Tools.add(maxRightValue, minRightValue), 2);
            double middleLeftValue = Tools.div(Tools.add(maxLeftValue, minLeftValue), 2);
            float centerpointy = (float) Tools.add(Tools.add(axisMarginTop, Tools.div(actualhight, 2)), 6.0f);

            //显示左Y轴最大最小值
            if (isShowLeftMaxMinYAxis) {

                //右对齐
                if (isShowYvalueOutSide) {
                    mPaintFont.setTextAlign(Align.RIGHT);
                    canvas.drawText(Tools.formatNum(maxLeftValue), leftstartx, topstarty, mPaintFont);
                    canvas.drawText(Tools.formatNum(minLeftValue), leftstartx, bottomstarty, mPaintFont);
                } else {
                    //左对齐
                    mPaintFont.setTextAlign(Align.LEFT);
                    canvas.drawText(Tools.formatNum(maxLeftValue), leftstrartxanother, topstarty, mPaintFont);
                    canvas.drawText(Tools.formatNum(minLeftValue), leftstrartxanother, bottomstarty, mPaintFont);
                }

            }

            //显示右Y轴最大最小值
            if (isShowRightMaxMinYAxis) {
                if (isShowYvalueOutSide) {
                    mPaintFont.setTextAlign(Align.LEFT);
                    canvas.drawText(Tools.formatNum(maxRightValue) + "%", rightaddstarx, topstarty, mPaintFont);
                    canvas.drawText(Tools.formatNum(minRightValue) + "%", rightaddstarx, bottomstarty, mPaintFont);
                } else {
                    mPaintFont.setTextAlign(Align.RIGHT);
                    canvas.drawText(Tools.formatNum(maxRightValue) + "%", rightsubstarx, topstarty, mPaintFont);
                    canvas.drawText(Tools.formatNum(minRightValue) + "%", rightsubstarx, bottomstarty, mPaintFont);
                }

            }

            //显示左中间值
            if (isDrawLeftMiddleValue) {
                if (isShowYvalueOutSide) {
                    mPaintFont.setTextAlign(Align.RIGHT);
                    canvas.drawText(Tools.formatNum(middleLeftValue), leftstartx, centerpointy, mPaintFont);
                } else {
                    mPaintFont.setTextAlign(Align.LEFT);
                    canvas.drawText(Tools.formatNum(middleLeftValue), leftstrartxanother, centerpointy, mPaintFont);
                }
            }

            //显示右中间值
            if (isDrawRightMiddleValue) {
                if (isShowYvalueOutSide) {
                    mPaintFont.setTextAlign(Align.LEFT);
                    canvas.drawText(Tools.formatNum(middleValue) + "%", rightaddstarx, centerpointy, mPaintFont);
                } else {
                    mPaintFont.setTextAlign(Align.RIGHT);
                    canvas.drawText(Tools.formatNum(middleValue) + "%", rightsubstarx, centerpointy, mPaintFont);
                }
            }


        } catch (Exception e) {

        }


    }


    /**
     * @param canvas
     * @Description 画默认的X横坐标
     * @author jaily.zhang
     */
    protected void drawDefaultXAxisGridX(Canvas canvas) {

        try {
            if (axisXTitles != null && !axisXTitles.isEmpty()) {
                int counts = axisXTitles.size();
                float eachwidth = (float) Tools.div(actualwidth, counts - 1);
                Paint mPaintFont = new Paint();
                mPaintFont.setColor(bordColor);
                mPaintFont.setTextSize(latitudeFontSize);
                mPaintFont.setAntiAlias(true);
                // 设置空心
                mPaintFont.setStyle(Style.FILL);
                float heightfont = (float) Tools.sub(height, Tools.div(Tools.sub(axisMarginBottom, latitudeFontSize), 2));
                for (int i = 0; i < counts; i++) {
                    if (i == 0) {
                        canvas.drawText(axisXTitles.get(i), (float) Tools.add(Tools.mul(eachwidth, i), axisMarginLeft),
                                heightfont, mPaintFont);
                    } else if (i == counts - 1) {
                        mPaintFont.setTextAlign(Align.RIGHT);
                        canvas.drawText(axisXTitles.get(i), startX, heightfont, mPaintFont);
                    } else {
                        canvas.drawText(axisXTitles.get(i), (float) Tools.sub(Tools.add(Tools.mul(eachwidth, i), axisMarginLeft), 70.0f), heightfont, mPaintFont);
                    }

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

            if (isShowGridYDivideLine) {
                if (divideCountList != null && !divideCountList.isEmpty()) {
                    Paint mPaint = new Paint();
                    // 去掉锯齿
                    mPaint.setAntiAlias(true);
                    // 设置画笔颜色
                    mPaint.setColor(bordColor);
                    mPaint.setStrokeWidth(bordSize);
                    int countListSize = divideStringList.size();
                    float heightfont = (float) Tools.sub(height, Tools.div(Tools.sub(axisMarginBottom, latitudeFontSize), 2));
                    double lastXPadding = 0;
                    float statLineX = axisMarginLeft;
                    for (int i = 0; i < countListSize; i++) {
                        //画纵坐标分割线
                        if (isLandScape) {
                            if (i < divideCountList.size()) {
                                // 设置空心
                                mPaint.setStyle(Style.STROKE);
                                double count = divideCountList.get(i);
                                float statLineXpointX = (float) Tools.add(axisMarginLeft, Tools.mul(count, eachwidth));
                                canvas.drawLine(statLineXpointX, axisMarginTop, statLineXpointX, stopY, mPaint);
                            }

                        }
                        //画横坐标
                        mPaint.setTextSize(latitudeFontSize);
                        mPaint.setStyle(Style.FILL);
                        String[] split = divideStringList.get(i).split("-");
                        String title = split[1] + "-" + split[2];
                        if (i < countListSize - 1) {
                            double countPadding = divideCountList.get(i);
                            double actualPading = Tools.sub(countPadding, lastXPadding);
                            statLineX = (float) Tools.add(statLineX, Tools.div(Tools.mul(actualPading, eachwidth), 3));
                            canvas.drawText(title, statLineX, heightfont, mPaint);
                            lastXPadding = countPadding;
                            statLineX = (float) Tools.add(axisMarginLeft, Tools.mul(countPadding, eachwidth));
                        } else {
                            double actualPading = Tools.sub(startX, statLineX);
                            statLineX = (float) Tools.add(statLineX, Tools.div(actualPading, 3));
                            canvas.drawText(title, statLineX, heightfont, mPaint);
                        }
                    }


                }
            }


            int lineDataSize = lineData.size();
            //draw Line
            for (int i = 0; i < lineDataSize; i++) {
                Paint mPaint = new Paint();
                // 去掉锯齿
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Style.STROKE.FILL);
                mPaint.setStrokeWidth(2);
                LineEntity line = (LineEntity) lineData.get(i);
                mPaint.setColor(line.getLineColor());
                if (line.getTitle().equals("ratioValue")) {
                    continue;
                }
                List<OHLCEntity> lineDatavalue = line.getLineData();
                double maxDataValue = line.getMaxValue();
                double minDataValue = line.getMinValue();
                float maxvalue = (float) Tools.sub(maxDataValue, minDataValue);
                int lineDataValueSize = lineDatavalue.size();
                if (lineDatavalue != null && !lineDatavalue.isEmpty()) {
                    float x = 0;
                    float y = 0;
                    Path path = new Path();
                    path.moveTo(axisMarginLeft, height - axisMarginBottom);
                    for (int j = 0; j < lineDataValueSize; j++) {
                        float value;
                        Double openValue = lineDatavalue.get(j).getOpen();
                        if (openValue == null) {
//                            x = axisMarginLeft;
//                            y = stopY;
//                            path.lineTo(x, y);
                            continue;
                        }
                        value = (float) Tools.sub(openValue, minDataValue);

                        if (j == 0) {
                            x = axisMarginLeft;
                            y = (float) Tools.sub(stopY, Tools.div(Tools.mul(value, actualhight), maxvalue));
                            path.lineTo(x, y);
                        } else if (j == totalNumber - 1) {
                            float pointx = startX;
                            float pointy = (float) Tools.sub(stopY, Tools.div(Tools.mul(value, actualhight), maxvalue));
                            if (pointx <= startX) {
                                canvas.drawLine(x, y, pointx, pointy, mPaint);
                                x = pointx;
                                y = pointy;
                                path.lineTo(pointx, pointy);
                            }

                        } else {
                            float pointx = (float) Tools.add(Tools.mul(j, eachwidth), axisMarginLeft);
                            float pointy = (float) Tools.sub(stopY, Tools.div(Tools.mul(value, actualhight), maxvalue));
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
                        Shader mShader = new LinearGradient(axisMarginLeft, axisMarginTop, axisMarginLeft, stopY, Color.argb(20, red, green, blue), Color.argb(20, red, green, blue), Shader.TileMode.MIRROR);
                        mPaint.setShader(mShader);
                        canvas.drawPath(path, mPaint);
                    }

                }
            }
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

            if (lineData != null && !lineData.isEmpty()) {

                LineEntity line = (LineEntity) lineData.get(0);
                List<OHLCEntity> ohlcEntityList = line.getLineData();

                if (position < ohlcEntityList.size()) {
                    callBackPosition.solve(position);
                    // 画经线
                    float maxvalue = (float) Tools.sub(maxLeftValue, minLeftValue);
                    canvas.drawLine(eventX, axisMarginTop, eventX, height, mPaint);
                    double price = ohlcEntityList.get(position).getOpen();
                    float value = (float) Tools.sub(price, minLeftValue);
                    float pointy = (float) Tools.sub(stopY, Tools.div(Tools.mul(value, actualhight), maxvalue));
                    canvas.drawLine(axisMarginLeft, pointy, startX, pointy, mPaint);
                    canvas.drawCircle(eventX, pointy, 10.0f, mPaint);

                    float recttop = pointy - latitudeFontSize;
                    float rectbottom = pointy + latitudeFontSize;
                    canvas.drawRect(0, recttop, axisMarginLeft, rectbottom, mPaint);
                    canvas.drawRect(startX, recttop, width, rectbottom, mPaint);

                    mPaint.setColor(Color.WHITE);
                    mPaint.setTextAlign(Align.RIGHT);
                    mPaint.setTextSize(latitudeFontSize);
                    canvas.drawText(Tools.formatNum(price), axisMarginLeft - 5.0f, pointy + 10.0f, mPaint);
                    mPaint.setTextAlign(Align.LEFT);
                    double ratio = lineData.get(2).getLineData().get(position).getOpen();
                    canvas.drawText(Tools.formatNum(ratio) + "%", startX + 5.0f, pointy + 10.0f, mPaint);
                }


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
                //画默认的XTitle
                if (isDefaultXtitle) {
                    drawDefaultXAxisGridX(cacheCanvas);
                }
                // 画纬线及纵坐标
                drawAxisGridY(cacheCanvas);
                //画线
                drawLines(cacheCanvas);
                drawOver.drawOver();
            }
        } catch (Exception e) {

        }
        invalidate();
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

    public void setShowGridYDivideLine(boolean showGridYDivideLine) {
        isShowGridYDivideLine = showGridYDivideLine;
    }

    public void setDivideCountList(List<Integer> divideCountList) {
        this.divideCountList = divideCountList;
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

    public void setAxisXdivideTitles(List<String> divideStringList) {
        this.divideStringList = divideStringList;
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

    public boolean isShowGridXLine() {
        return isShowGridXLine;
    }

    public void setShowGridXLine(boolean showGridXLine) {
        isShowGridXLine = showGridXLine;
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

    public boolean isDefaultXtitle() {
        return isDefaultXtitle;
    }

    public void setDefaultXtitle(boolean defaultXtitle) {
        isDefaultXtitle = defaultXtitle;
    }

    public boolean isShowXcenterLine() {
        return isShowXcenterLine;
    }

    public void setShowXcenterLine(boolean showXcenterLine) {
        isShowXcenterLine = showXcenterLine;
    }

    public boolean isDrawLeftMiddleValue() {
        return isDrawLeftMiddleValue;
    }

    public void setDrawLeftMiddleValue(boolean drawLeftMiddleValue) {
        isDrawLeftMiddleValue = drawLeftMiddleValue;
    }

    public boolean isDrawRightMiddleValue() {
        return isDrawRightMiddleValue;
    }

    public void setDrawRightMiddleValue(boolean drawRightMiddleValue) {
        isDrawRightMiddleValue = drawRightMiddleValue;
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

    public DrawOver getDrawOver() {
        return drawOver;
    }

    public void setDrawOver(DrawOver drawOver) {
        this.drawOver = drawOver;
    }
}
