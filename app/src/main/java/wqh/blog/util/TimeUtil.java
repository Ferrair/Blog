package wqh.blog.util;

import android.text.format.Time;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {
    public final static String FORMAT_HOUR_MIN = "HH:mm";
    public final static String FORMAT_YEAR_MOUTH_DAY_HOUR_MIN = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_ONLY_DAY = "dd";
    public final static String FORMAT_DATA = "yyyy-MM-dd";

    /**
     * @param beforeTime ԭ对于当前时间的前面的时间
     * @return 格式: "昨天 13:23"
     */
    public static String getTimeToNow(long beforeTime) {
        long clearTime = beforeTime * 1000;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_ONLY_DAY);
        Date today = new Date(System.currentTimeMillis());
        Date beforeDay = new Date(clearTime);

        String result;
        int dayGap = Integer.parseInt(simpleDateFormat.format(today)) - Integer.parseInt(simpleDateFormat.format(beforeDay));
        switch (dayGap) {
            case 0:
                result = "今天" + getHourAndMin(clearTime);
                break;
            case 1:
                result = "昨天" + getHourAndMin(clearTime);
                break;
            case 2:
                result = "前天" + getHourAndMin(clearTime);
                break;

            default:
                result = getDate(clearTime);
                break;
        }
        return result;
    }


    private static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_HOUR_MIN);
        return format.format(new Date(time));
    }


    /**
     * @return 格式：2015-3-12 23:21
     */
    public static String getDateAndTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YEAR_MOUTH_DAY_HOUR_MIN);
        return format.format(new Date(time));
    }

    /**
     * @return 格式：2015-3-12
     */
   /* public static Date getDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATA);
        try {
            return format.parse(getDate(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * @return 格式：2015-3-12
     */
    public static String getDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATA);
        return format.format(new Date(time));
    }

    /**
     * @param time 要和当前时间比较的时间
     * @return 如果time在当前时间之前return true
     */
    public static boolean isThisTimeBeforeNow(long time) {
        return System.currentTimeMillis() > time;
    }

    //解析Bmob createdAt 这一的时间 2015-11-09 17:47:15
    public static String parseBmobTime(String time) {
        return time.substring(5, 10);
    }

    //解析Bmob createdAt 这一的时间 2015-11-09 17:47:15
    public static String getPreciseTime(String time) {
        return time.substring(5, 16);
    }

    /**
     * @return time: 今日0:00的Unix时间戳
     */
    public static long getTodayStart() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return currentDate.getTimeInMillis();
    }

    /**
     * @return time: 本周0:00的Unix时间戳
     */
    public static long getWeekStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTimeInMillis();
    }

    /**
     * @return time: 本月0:00的Unix时间戳
     */
    public static long getMonthStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 2, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }

    /**
     * @return time: 本日24:00的Unix时间戳
     */
    public static long getTodayEnd() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return currentDate.getTimeInMillis();
    }

    /**
     * @return time: 昨日24:00的Unix时间戳
     */
    public static long getYesterdayStart() {
        return getTodayStart() - 24 * 60 * 60 * 1000;
    }

    /**
     * @return time: 今日 22:00的Unix时间戳
     */
    public static long getNextAlarmTime() {
        Calendar c = Calendar.getInstance();
        Time time = new Time("GMT+8");
        time.setToNow();
        c.set(time.year, time.month, time.monthDay, 22, 0, 0);
        Log.i("executeTimeTask", c.getTime().toString());
        return c.getTimeInMillis();
    }

    public static String date2time(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATA);
        return sdf.format(new Date(date));
    }
}
