package com.csf.csflibrary.javaBean;

import java.io.Serializable;

/**
 * 股票实时状态买卖档位
 *
 * @author jaily.zhang
 * @date at 2016/4/25 15:50
 */
@SuppressWarnings("serial")
public class StockRealtimeBean implements Serializable {
    private String title;
    private String price;
    private String count;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
