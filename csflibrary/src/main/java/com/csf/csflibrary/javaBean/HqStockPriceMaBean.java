package com.csf.csflibrary.javaBean;

import java.io.Serializable;

/**
 * 分时数据
 *
 * @author jaily.zhang
 * @date at 2016/4/26 11:18
 */
@SuppressWarnings("serial")
public class HqStockPriceMaBean implements Serializable {
    private Double MA5;
    private Double MA10;
    private Double MA20;

    public Double getMA5() {
        return MA5;
    }

    public void setMA5(Double MA5) {
        this.MA5 = MA5;
    }

    public Double getMA10() {
        return MA10;
    }

    public void setMA10(Double MA10) {
        this.MA10 = MA10;
    }

    public Double getMA20() {
        return MA20;
    }

    public void setMA20(Double MA20) {
        this.MA20 = MA20;
    }
}
