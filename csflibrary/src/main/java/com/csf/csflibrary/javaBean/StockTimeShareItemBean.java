package com.csf.csflibrary.javaBean;

import java.io.Serializable;

/**
 * 分时数据
 *
 * @author jaily.zhang
 * @date at 2016/4/26 11:18
 */
@SuppressWarnings("serial")
public class StockTimeShareItemBean implements Serializable {
    private String dt;
    private Double price;
    private Double volume;
    private Double amount;
    private Double ratio;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }
}
