/*
 * LineEntity.java
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

import java.util.List;


public class LineEntity {

    private List<OHLCEntity> lineData;

    private String title;

    private int lineColor;

    public LineEntity() {
        super();
    }

    private boolean isShowShader;//是否显示阴影,默认不显示

    private double maxValue;

    private double minValue;

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public LineEntity(List<OHLCEntity> lineData, String title, int lineColor, boolean isShowShader, double maxValue, double minValue) {
        this.lineData = lineData;
        this.title = title;
        this.lineColor = lineColor;
        this.isShowShader = isShowShader;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public boolean isShowShader() {
        return isShowShader;
    }

    public void setShowShader(boolean showShader) {
        isShowShader = showShader;
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the lineColor
     */
    public int getLineColor() {
        return lineColor;
    }

    /**
     * @param lineColor the lineColor to set
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

}
