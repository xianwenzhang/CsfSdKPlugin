package com.csf.csflibrary.javaBean;

import java.io.Serializable;

/**
 * 分时数据
 *
 * @author jaily.zhang
 * @date at 2016/4/26 11:18
 */
@SuppressWarnings("serial")
public class HqStockPriceBean implements Serializable {
    private String dt;
    private String tick;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double inc;
    private Double vol;
    private Object macd;
    private Object boll;
    private Object kdj;
    private Object ma;
    private Object rsi;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getTick() {
        return tick;
    }

    public void setTick(String tick) {
        this.tick = tick;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getInc() {
        return inc;
    }

    public void setInc(Double inc) {
        this.inc = inc;
    }

    public Double getVol() {
        return vol;
    }

    public void setVol(Double vol) {
        this.vol = vol;
    }

    public Object getMacd() {
        return macd;
    }

    public void setMacd(Object macd) {
        this.macd = macd;
    }

    public Object getBoll() {
        return boll;
    }

    public void setBoll(Object boll) {
        this.boll = boll;
    }

    public Object getKdj() {
        return kdj;
    }

    public void setKdj(Object kdj) {
        this.kdj = kdj;
    }

    public Object getMa() {
        return ma;
    }

    public void setMa(Object ma) {
        this.ma = ma;
    }

    public Object getRsi() {
        return rsi;
    }

    public void setRsi(Object rsi) {
        this.rsi = rsi;
    }
}
