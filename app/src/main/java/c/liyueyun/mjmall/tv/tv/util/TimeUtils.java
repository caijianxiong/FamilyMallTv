package c.liyueyun.mjmall.tv.tv.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {


    private static SimpleDateFormat utcFormat;//yyyy-MM-dd'T'HH:mm:ss.SSS Z
    private static SimpleDateFormat defaultFormat01;//yyyy-MM-dd HH:mm:ss
    private static SimpleDateFormat defaultFormat02;//yyyy-MM-dd

    /**
     * @param UTCTime 2016-08-15T16:00:00.000Z-->2016-08-16 00:00:00
     * @return
     */
    public static String timeUtcToBJ(String UTCTime) {
        try {
            UTCTime = UTCTime.replace("Z", " UTC");
            if (utcFormat == null)
                utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z", Locale.ENGLISH);
            if (defaultFormat01 == null)
                defaultFormat01 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//            SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = utcFormat.parse(UTCTime);
            return defaultFormat01.format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }


    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSS Z  --->转换成毫秒级时间戳
     *
     * @param utcTime
     * @return
     */
    public static long UTCTimeToStamp(String utcTime) {
        try {
            utcTime = utcTime.replace("Z", " UTC");//注意是空格+UTC
            if (utcFormat == null)
                utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z", Locale.ENGLISH);//注意格式化的表达式
            Date d = utcFormat.parse(utcTime);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * @param UTCTime 2016-08-15T16:00:00.000Z
     * @return 2016-08-16
     */
    public static String timeUtcToBJ02(String UTCTime) {
        try {
            UTCTime = UTCTime.replace("Z", " UTC");
            if (utcFormat == null)
                utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z", Locale.ENGLISH);
            if (defaultFormat02 == null)
                defaultFormat02 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = utcFormat.parse(UTCTime);
            return defaultFormat02.format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }

    /**
     * 格林威治时间转时间戳
     *
     * @param UTCTime
     * @return
     */
    public static long timeUtcToStamp(String UTCTime) {
        try {
            UTCTime = UTCTime.replace("Z", " UTC");
            if (utcFormat == null)
                utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z", Locale.ENGLISH);
            if (defaultFormat01 == null)
                defaultFormat01 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//            SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = utcFormat.parse(UTCTime);
            return timeStandardToStamp(defaultFormat01.format(date));
        } catch (ParseException pe) {
            pe.printStackTrace();
            return 0;
        }
    }


    /**
     * @param standardTime 2016-08-16 00:00:00-->时间戳
     * @return
     */
    public static long timeStandardToStamp(String standardTime) {
        if (defaultFormat01 == null)
            defaultFormat01 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            Date date = defaultFormat01.parse(standardTime);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * @param stampTime 时间戳-->2016-08-16 00:00:00
     * @return
     */
    public static String timestampToSatndard(long stampTime) {
        Date date = new Date(stampTime);
        if (defaultFormat01 == null)
            defaultFormat01 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return defaultFormat01.format(date);
    }

    /**
     * @param stampTime 时间戳-->2016-08-16
     * @return
     */
    public static String timestampToSatndard02(long stampTime) {
        Date date = new Date(stampTime);
        if (defaultFormat02 == null)
            defaultFormat02 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return defaultFormat02.format(date);
    }


    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14 ）
     *
     * @param time
     * @return
     */
    public static String timeYMD(String time) {
        if (defaultFormat02 == null)
            defaultFormat02 = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = defaultFormat02.format(new Date(i * 1000L));
        return times;

    }

}
