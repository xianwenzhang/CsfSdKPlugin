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

public class OHLCEntity {

    private Double open;

    private String date;

    private int lineColor;

    private boolean isShowFillOrStroke;


    public OHLCEntity(Double double1, String date) {
        super();
        this.open = double1;
        this.date = date;
    }

    public OHLCEntity(Double open, String date, int lineColor) {
        this.open = open;
        this.date = date;
        this.lineColor = lineColor;
    }

    public OHLCEntity(Double open, String date, int lineColor, boolean isShowFillOrStroke) {
        this.open = open;
        this.date = date;
        this.lineColor = lineColor;
        this.isShowFillOrStroke = isShowFillOrStroke;
    }

    public boolean isShowFillOrStroke() {
        return isShowFillOrStroke;
    }

    public void setShowFillOrStroke(boolean showFillOrStroke) {
        isShowFillOrStroke = showFillOrStroke;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * <p>
     * Constructor of OHLCEntity
     * </p>
     * <p>
     * OHLCEntity类对象的构造函数
     * </p>
     * <p>
     * OHLCEntityのコンストラクター
     * </p>
     */
    public OHLCEntity() {
        super();
    }

    /**
     * @return the open
     */
    public Double getOpen() {
        return open;
    }

    /**
     * @param open the open to set
     */
    public void setOpen(Double open) {
        this.open = open;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }
}
