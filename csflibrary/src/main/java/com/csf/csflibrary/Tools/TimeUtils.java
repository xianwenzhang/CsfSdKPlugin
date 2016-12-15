package com.csf.csflibrary.Tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jaily.zhang on 2016/8/3.
 */
public class TimeUtils {
    // 短日期格式
    public static String DATE_FORMAT = "yyyy-MM-dd";
    private final static int nine = 9;
    private final static int ten = 10;
    private final static int eleven = 11;
    private final static int thirteen = 13;
    private final static int fifteen = 15;
    private final static int thirty = 30;
    public static String TimeHour_FORMAT = "HH:mm";
    /**
     * 将长整型数字转换为日期格式的字符串,只有年月日
     *
     * @param time
     * @return
     */
    public static String convert2Stringdate(long time) {
        if (time > 0l) {
            SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
            Date date = new Date(time);
            return sf.format(date);
        }
        return "";
    }

    public static boolean isWeekend(Calendar calendar) {
        boolean result = false;
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1 || day == 7)
            result = true;
        return result;
    }

    /**
     * @param calendar
     * @return
     * @Description 是否是工作时间
     * @author jaily.zhang
     */
    public static boolean isWorktime(Calendar calendar) {
        boolean result = false;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if ((hour >= thirteen && hour < fifteen) || (hour == ten) || (hour == nine && minute >= thirty) || (hour == eleven && minute <= thirty))
            result = true;
        return result;
    }

    /**
     * 将长整型数字转换为日期格式的字符串,只有hh-mm-ss
     *
     * @param time
     * @return
     */
    public static String convert2Stringtimehour(long time) {
        if (time > 0l) {
            SimpleDateFormat sf = new SimpleDateFormat(TimeHour_FORMAT);
            Date date = new Date(time);
            return sf.format(date);
        }
        return "";
    }
}
