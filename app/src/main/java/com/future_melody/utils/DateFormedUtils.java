package com.future_melody.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormedUtils {
    private final int one_day = 1000 * 60 * 60 * 24;

    // 详情页面的时间的格式
    public static String format(String time) {
        long timeDate = Long.parseLong(time);
        Date date = new Date(timeDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd,yyyy hh:mm aaa z",
                Locale.US);
        // SimpleDateFormat sdf = new
        // SimpleDateFormat("'Date'yyyy-MM-dd'Time'HH:mm:ss'Z'");

        return sdf.format(date);
    }

    /**
     * 转化为有时区的日期的格式
     *
     * @param time
     * @return
     */
    public static String format(long time) {

        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd,yyyy hh:mm aaa z",
                Locale.US);
        // SimpleDateFormat sdf = new
        // SimpleDateFormat("'Date'yyyy-MM-dd'Time'HH:mm:ss'Z'");

        return sdf.format(date);
    }

    public static String formatMouth(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MM");

        return sdf.format(date);
    }

    /**
     * 格式化日期
     *
     * @param time
     * @return
     */
    public static String formatDay(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(date);
    }

    /**
     * 转化日期为月日年
     *
     * @param time
     * @return
     */
    public static String formateDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String formatHour(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
        // hh:mm aaa
        return sdf.format(time);
    }

    // EEE, d MMM yyyy HH:mm:ss Z "MMMM dd,yyyy hh:mm aaa z"
    public static String formatSimeplDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy hh:mm aaa Z",
                Locale.US);
        return sdf.format(date);
    }

    /**
     * 2014/12/12
     *
     * @return
     */
    public static String formatDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(date);
    }

    public static String getDataTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getDataTime_yyyy_MM(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    public static String formatDate(String time) {
        if (time != null) {
            try {
                long t = Long.parseLong(time);
                return formatDate(t);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

    public static String getMouth(String time) {
        try {
            long longMouth = Long.parseLong(time);
            Date date = new Date(longMouth);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.US);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getNeedModelSimpleTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aaa z", Locale.US);
        return sdf.format(date);
    }

    /**
     * 实际使用的日期格式
     *
     * @param time
     * @return
     */
    public static String getActualNeedModelTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        return sdf.format(date);
    }
    /**
     * 实际使用的日期格式
     *
     * @param time
     * @return
     */
    public static String getTime_yy_MM_DD_HH_mm(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return sdf.format(date);
    }
    public static String getTime_yy_MM_DD(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(date);
    }
    public static String get_Long_MM_dd(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(date);
    }

    public static String get_time_MM_dd(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        return sdf.format(date);
    }

    public static String get_time_MM(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(date);
    }

    public static String get_time_dd(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(date);
    }

    public static String getActualZone(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("z", Locale.US);
        String zone = sdf.format(date);
        return zone.split(":")[0];
    }

    public static String getNeedModelTimeOuter(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    public static String getTime_yy_mm_dd(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(date);
    }

    /**
     * 获取显示的时间的格式
     *
     * @param time
     * @return
     */
    public static String getActualTime(long time) {
        return getNeedModelTime(time) + " " + getActualZone(time);
    }

    public static String getActualTimeOuter(long time) {
        return getNeedModelTimeOuter(time) + " " + getActualZone(time);
    }

    public static String getNeedModelTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getTime_MM_dd_HH_MM(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        return sdf.format(date);
    }

    public static String getTime_yyyy_MM(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(date);
    }

    public static String getZone(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
        return sdf.format(date);
    }

    public static String getZone2(long time) {
        TimeZone tz = TimeZone.getDefault();
        String s = "TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID();
        return s;
    }

    public static String getMinute(long time) {
        return null;
    }


    public static Date getStringToDate(String time) {
        if (time.length() > 20) {
            time = time.substring(0, 19) + "Z";
        }
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    public static String getDateTime(Calendar calendar, String pattern) {
        String dataTime = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dataTime = dateFormat.format(calendar.getTime());
        } catch (Exception ex) {
            LogUtil.e("","Calendar转字符串失败");
        }
        return dataTime;
    }

    public static String getStringToDate2(String time) {
        time = time.substring(5, 10);
        String[] date = time.split("-");
        StringBuffer sb = new StringBuffer();
        sb.append(date[0]);
        sb.append("/");
        sb.append(date[1]);
        String result = sb.toString();
        return result;
    }

    public static String getTime(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // sdf.setTimeZone(getTimeZone());
        LogUtil.i("time", sdf.format(date));
        return sdf.format(date);
    }

    public static String getTime_HH_MM(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        // sdf.setTimeZone(getTimeZone());
        LogUtil.i("time", sdf.format(date));
        return sdf.format(date);
    }

    public static String getTime_HH_MM(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//		 sdf.setTimeZone(getTimeZone());
        if (date != null) {
            LogUtil.i("time", sdf.format(date));
            return sdf.format(date);
        }
        return "";
    }

    public static String getTime_MM_DD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            return sdf.format(date);
        } catch (NullPointerException e) {
            e.printStackTrace();
            LogUtil.e("time", "getTime_MM_DD_HH_MM*err**" + e.getMessage());
        }
        return "";
    }

    public static String getTimeZ(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
//		 sdf.setTimeZone(getTimeZone());
        if (date != null) {
            LogUtil.i("time", sdf.format(date));
            return sdf.format(date);
        }
        return "";
    }

    public static String getTime_MM_DD2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            return sdf.format(date);
        } catch (NullPointerException e) {
            e.printStackTrace();
            LogUtil.e("time", "getTime_MM_DD_HH_MM*err**" + e.getMessage());
        }
        return "";
    }

    public static String getTime_YY_MM_DD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            return sdf.format(date);
        } catch (NullPointerException e) {
            e.printStackTrace();
            LogUtil.e("time", "getTime_MM_DD_HH_MM*err**" + e.getMessage());
        }
        return "";
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getWeekOfLong(long time) {
        Date date = new Date(time);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 获取指定时间段的long类型时间
     * @param pattern
     * @param define
     * @return
     * @throws ParseException
     */
    public static long getDefineTime(String pattern, String define)  {
        //先把字符串转成Date类型
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        //此处会抛异常
        Date date = null;
        try {
            date = sdf.parse(define);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取毫秒数
        return date.getTime();
    }


}
