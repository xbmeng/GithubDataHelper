package com.mxb.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static String DateToStamp(String datatime) {
        String res = null;
        SimpleDateFormat beijingSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        beijingSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        try {
            Date bjDate = beijingSdf.parse(datatime);
            SimpleDateFormat londonSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            londonSdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            Date ldDate = londonSdf.parse(londonSdf.format(bjDate));
            res = ldDate.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     *
     * @param date yyyy-MM-dd HH:mm:ss 格式的日期
     * @return yyyy-MM-dd 格式的日期
     */
    public static String DateToDay(Date date)
    {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        String day = ft.format(date);

        return day;
    }
}
