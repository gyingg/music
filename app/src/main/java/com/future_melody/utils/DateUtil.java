package com.future_melody.utils;
import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 时间戳
 */
public class DateUtil {

    public static Date getDate(String timeStr) {
        long time = Long.parseLong(timeStr);
        return new Date(time);
    }

    public static String getDateByTag(String timeStr) {
        if (timeStr == null || "".equals(timeStr.trim())) {
            return "";
        }
        Date date = getDate(timeStr);
        DateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.US);
        return dateFormat.format(date);
    }

    public static String getDateBySearch(String timeStr) {
        if (timeStr == null || "".equals(timeStr.trim())) {
            return "";
        }
        Date date = getDate(timeStr);
        DateFormat dateFormat = new SimpleDateFormat("yyyy MM/dd hh:mm aaa z",
                Locale.US);
        return dateFormat.format(date);
    }

    public static String getNewsDetailTime(String timeStr) {
        if (timeStr == null || "".equals(timeStr.trim())) {
            return "";
        }
        Date date = getDate(timeStr);
        DateFormat dateFormat = new SimpleDateFormat("yyyy MM/dd hh:mm aaa z",
                Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat(
                "'Date'yyyy-MM-dd'Time'HH:mm:ss'Z'");

        return dateFormat.format(date);
    }

    public static String getDateByDay(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd", Locale.US);
        return dateFormat.format(date);
    }

    public static String getDateByMonth(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MMM", Locale.US);
        return dateFormat.format(date);
    }

    public static String getDateByWeek(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE", Locale.US);
        return dateFormat.format(date);
    }

    public static String getDateByFilePath(String timeStr) {
        if (timeStr == null || "".equals(timeStr.trim())) {
            return "";
        }
        Date date = getDate(timeStr);
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
        return dateFormat.format(date);
    }

    public static String getDateByFilePathXml(String timeStr) {
        if (timeStr == null || "".equals(timeStr.trim())) {
            return "";
        }
        Date date = getDate(timeStr);
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd", Locale.US);
        return dateFormat.format(date);
    }

    /**
     * 获取时间轴的时间
     *
     * @param timeStr
     * @return
     */
    public static List<Map<String, String>> getDateArray(String timeStr) {
        List<Map<String, String>> dateList = new ArrayList<Map<String, String>>();

        Date date = getDate(timeStr);
        Map<String, String> dateMap = new HashMap<String, String>();
        dateMap.put("day", getDateByDay(date));
        dateMap.put("month", getDateByMonth(date));
        dateMap.put("week", getDateByWeek(date));
        dateMap.put("longTime", String.valueOf(date.getTime()));
        dateList.add(dateMap);

        Date nowDate = date;
        for (int i = 0; i < 30; i++) {
            // 获取前一天的时间
            nowDate = getNowBeforeDay(nowDate);
            Map<String, String> nowDateMap = new HashMap<String, String>();
            nowDateMap.put("day", getDateByDay(nowDate));
            nowDateMap.put("month", getDateByMonth(nowDate));
            nowDateMap.put("week", getDateByWeek(nowDate));
            nowDateMap.put("longTime", String.valueOf(nowDate.getTime()));
            dateList.add(nowDateMap);
        }
        return dateList;
    }

    // 获取指定的某一天的所有的数据
    public static Map<String, String> getSingleDate(String timeStr) {
        Date date = getDate(timeStr);
        Map<String, String> dateMap = new HashMap<String, String>();
        dateMap.put("day", getDateByDay(date));
        dateMap.put("month", getDateByMonth(date));
        dateMap.put("week", getDateByWeek(date));
        dateMap.put("longTime", String.valueOf(date.getTime()));
        return dateMap;
    }

    // 获取指定的某一天所在的位置
    public int getSingleIndex(Map<String, String> map,
                              List<Map<String, String>> list) {
        try {
            if (list != null && list.size() > 0 && map != null
                    && map.size() > 0) {
                return list.indexOf(map);
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * 获取前一天
     *
     * @param date
     * @return
     */
    public static Date getNowBeforeDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果

        return date;
    }

    /**
     * 获取前n天或后n天
     *
     * @param date
     * @return
     */
    public static Date getNowBeforeOeADay(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果

        return date;
    }

    /**
     * 获取天
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // int year = cal.get(Calendar.YEAR);//获取年份
        // int month=cal.get(Calendar.MONTH);//获取月份
        // int day=cal.get(Calendar.DATE);//获取日
        // int hour=cal.get(Calendar.HOUR);//小时
        // int minute=cal.get(Calendar.MINUTE);//分
        // int second=cal.get(Calendar.SECOND);//秒
        // int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天
        int day = cal.get(Calendar.DATE);// 获取日

        return day;
    }

    /**
     * 获取月份
     *
     * @param date
     * @return
     */
    public static int getMouth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int mouth = calendar.get(Calendar.MONTH);
        return mouth;
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    /**
     * 获取时间格式(yyyy-MM-dd HH:mm:ss)
     *
     * @param date
     * @return
     */
    public static String getDateFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.US);
        return dateFormat.format(date);
    }

    public static String getDateFormatMMDD(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm",
                Locale.US);
        return dateFormat.format(date);
    }

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 返回文字描述的日期
     *
     * @return
     */
    public static String getTimeFormatText(long l) {
        Date date = new Date(l);
        if (date == null) {
            return null;
        }
//		long diff = System.currentTimeMillis() - l;
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
//		if (diff > year) {
//			r = (diff / year);
//			return r + "年前";
//		}
//		if (diff > month) {
//			r = (diff / month);
//			return r + "个月前";
//		}
        if (diff > day) {
//			r = (diff / day);
//			return r + "天前";
            return DateFormedUtils.getTime_MM_dd_HH_MM(l);
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    public static final long SECOND = 1000;
    public static final long MINITE = 60 * SECOND;
    public static final long HOUR = 60 * MINITE;
    public static final long DAY = 24 * HOUR;

    public static String getShowTime(long publishtime) {
        if (true) {
            return getShowTimeVisitor(publishtime);
        }
        long extraTime = System.currentTimeMillis() - publishtime;

        LogUtil.e("time", "systemTime = " + System.currentTimeMillis() + "\npublishTime = " + publishtime);

        // 刚刚
        if (extraTime < 30 * MINITE) {
            return "刚刚";
        } else if ((30 * MINITE < extraTime || extraTime == 30 * MINITE)
                && extraTime < HOUR) {
            return "半小时前";
        } else if ((HOUR < extraTime || extraTime == HOUR)
                && extraTime < 2 * HOUR) {
            return "1小时前";
        } else if ((2 * HOUR < extraTime || extraTime == 2 * HOUR)
                && extraTime < 4 * HOUR) {
            return "2小时前";
        } else if ((4 * HOUR < extraTime || extraTime == 4 * HOUR)
                && extraTime < 8 * HOUR) {
            return "4小时前";
        } else if ((8 * HOUR < extraTime || extraTime == 8 * HOUR)
                && extraTime < DAY) {
            return "8小时前";
        } else if ((DAY < extraTime || extraTime == DAY)
                && extraTime < 2 * DAY) {
            return "1天前";
        } else if ((2 * DAY < extraTime || extraTime == 2 * DAY)
                && extraTime < 3 * DAY) {
            return "2天前";
        } else if (extraTime > 3 * DAY) {
//            return "3天前";
            return DateFormedUtils.formateDate(publishtime);
        }
        return DateFormedUtils.formateDate(publishtime);
    }

    public static String getShowTimeVisitor(long publishtime) {
        long extraTime = System.currentTimeMillis() - publishtime;
        if (isToday(DateFormedUtils.getNeedModelTime(publishtime))) {
            return DateFormedUtils.getTime_HH_MM(publishtime);
        } else if (isYesterday(DateFormedUtils.getNeedModelTime(publishtime))) {
            return "昨天";
        } else {
//            return DateFormedUtils.get_time_MM(publishtime) + "月"
//                    + DateFormedUtils.get_time_dd(publishtime) + "日";
            return DateFormedUtils.get_time_MM_dd(publishtime);
        }
//        if (extraTime < DAY) {
//            return DateFormedUtils.getTime_HH_MM(publishtime);
//        } else if (extraTime >= DAY && extraTime < 2 * DAY) {
//            return "昨天";
//        } else if (extraTime >= 2 * DAY) {
//            return DateFormedUtils.get_time_MM_dd(publishtime);
//        }
//        return "";
    }

    public static String[] weekName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static CustomDate getNextSunday() {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7 - getWeekDay() + 1);
        CustomDate date = new CustomDate(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        return date;
    }

    public static int[] getWeekSunday(int year, int month, int day, int pervious) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DAY_OF_MONTH, pervious);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH) + 1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;

    }

    public static int getWeekDayFromDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return week_index;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : ("0" + month))
                + "-01";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString);
        } catch (ParseException e) {
//			System.out.println(e.getMessage());
        }
        return date;
    }

    public static boolean isToday(CustomDate date) {
        return (date.year == DateUtil.getYear() &&
                date.month == DateUtil.getMonth()
                && date.day == DateUtil.getCurrentMonthDay());
    }

    public static boolean isCurrentMonth(CustomDate date) {
        return (date.year == DateUtil.getYear() &&
                date.month == DateUtil.getMonth());
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isToday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isYesterday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }


    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    /**
     * 获取指定年份月份中指定某天是星期几
     */
    public static String getWeekDayString(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);//指定年份
        calendar.set(Calendar.MONTH, month - 1);//指定月份 Java月份从0开始算
//        int daysCountOfMonth = calendar.getActualMaximum(Calendar.DATE);//获取指定年份中指定月份有几天

        //获取指定年份月份中指定某天是星期几
        calendar.set(Calendar.DAY_OF_MONTH, day);  //指定日
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
            default:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
        }
    }
}
