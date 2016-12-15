package com.csf.csflibrary.javaBean;

import java.io.Serializable;

/**
 * 分时数据
 *
 * @author jaily.zhang
 * @date at 2016/4/26 11:18
 */
@SuppressWarnings("serial")
public class StockTimeShareBean implements Serializable {
    private Object days;//交易日
    private boolean state;//交易状态
    private Double close;//昨日收盘价
    private Double max;//最高价
    private Double min;//最低价
    private Object shares;//分时数据

    public Object getDays() {
        return days;
    }

    public void setDays(Object days) {
        this.days = days;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Object getShares() {
        return shares;
    }

    public void setShares(Object shares) {
        this.shares = shares;
    }
}
