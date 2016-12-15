package com.csf.csflibrary.Tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Base64;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jaily.zhang on 2016/8/1.
 */
public class Tools {
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    public static String getToken(String accessKey, long t, String secretKey) {
        try {
            //生成token参数
            String data = accessKey + "," + t + "," + secretKey;
            SecretKeySpec keySpec = new SecretKeySpec(data.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            byte[] res = mac.doFinal();
            String token = Base64.encodeToString(res, Base64.NO_WRAP);
            return token;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */

    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * @param v1
     * @param v2
     * @return
     * @Description 浮点数相加
     * @author jaily.zhang
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * @return String
     * @Description: 保留两位小数
     * @author helen.yang
     * @da2015年11月24日
     */
    public static String formatNum(double data) {
        String num = new DecimalFormat("##0.00").format(data);
        return num;
    }

    /**
     * @return double 保留两位小数 @throws
     */
    public static String format(double amount) {
        BigDecimal money = new BigDecimal(amount);
        return money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
    }

    /**
     * @return String
     * @Description: 保留0位小数
     * @author helen.yang
     * @da2015年11月24日
     */
    public static String formatNoPoint(double data) {
        DecimalFormat df = new DecimalFormat("##0");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(data);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 判断wifi是否连接
     *
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return manager.isWifiEnabled();
        }
        return false;
    }

    /**
     * @param context
     * @return
     * @Description 判断Mobile网络是否可用
     * @author jaily.zhang
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
