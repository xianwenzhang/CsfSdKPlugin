/*
 * OHLCEntity.java
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

package com.csf.csflibrary.entity;


public class OHLCCylinderEntity {

    private double open;
    private double close;
    private double high;
    private double low;
    private String date;
    private int lineColor;
    private boolean isShowFillOrStroke;

    public OHLCCylinderEntity(double open, double close, double high, double low, String date, int lineColor, boolean isShowFillOrStroke) {
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.date = date;
        this.lineColor = lineColor;
        this.isShowFillOrStroke = isShowFillOrStroke;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public boolean isShowFillOrStroke() {
        return isShowFillOrStroke;
    }

    public void setShowFillOrStroke(boolean showFillOrStroke) {
        isShowFillOrStroke = showFillOrStroke;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }
}
