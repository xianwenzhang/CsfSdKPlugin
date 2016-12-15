package com.csf.csflibrary.javaBean;

import java.io.Serializable;

/**
 * 
 * @Description 股票详情里的股票值
 * @author jaily.zhang
 * @date 2014-9-4 下午1:58:32
 * @version V1.3.1
 */
@SuppressWarnings("serial")
public class CompanyStatusData implements Serializable {
	private String code;// 股票代码
	private String tick;// 股票tick
	private String name;// 股票名称
	private Double price;// 当前股价
	private Double chg;// 股价变动值
	private Double ratio;// 股价变动率
	private Double min;// 最低价
	private Double max;// 最高价
	private Double open;// 今开盘
	private Double close;// 昨收盘
	private String state;// 交易字段
	private String lsdt;// 日期
	private Double tun;// 换手率
	private Long dt;// 当前时间
	private Double volume;

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Long getDt() {
		return dt;
	}

	public void setDt(Long dt) {
		this.dt = dt;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTick() {
		return tick;
	}

	public void setTick(String tick) {
		this.tick = tick;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getChg() {
		return chg;
	}

	public void setChg(Double chg) {
		this.chg = chg;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLsdt() {
		return lsdt;
	}

	public void setLsdt(String lsdt) {
		this.lsdt = lsdt;
	}

	public Double getTun() {
		return tun;
	}

	public void setTun(Double tun) {
		this.tun = tun;
	}

}
