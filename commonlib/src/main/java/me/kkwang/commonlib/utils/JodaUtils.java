package me.kkwang.commonlib.utils;

import org.joda.time.DateTime;

/**
 * Created by wangkai on 2016/9/15.
 */
public class JodaUtils {

    public static final String FORMAT1 = "yyyy-MM-dd  HH:mm:ss";
    public static final String FORMAT2 = "yyyy.MM.dd";
    public static final String FORMAT3 = "yyyyMMdd";
    public static final String FORMAT4 = "HH:mm";
    public static final String FORMAT5 = "MM.dd HH:mm";
    public static final String FORMAT6 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT7 = "yyyy.MM.dd HH:mm";

    public static long getTime(){
        return new DateTime().getMillis();
    }
    public static long getCurrentDayStartLong(){
        return new DateTime().withTimeAtStartOfDay().getMillis();
    }
    public static long getCurrentDayEndLong(){
        return new DateTime().millisOfDay().withMaximumValue().getMillis();
    }
    public static long getTomorrowStartLong(){
        return new DateTime().plusDays(1).withTimeAtStartOfDay().getMillis();
    }
    public static long getDayStartLong(long time){
        return new DateTime(time).withTimeAtStartOfDay().getMillis();
    }
    public static long getDayEndLong(DateTime dateTime){
        return dateTime.millisOfDay().withMaximumValue().getMillis();
    }
    public static long getDayEndLong(long time){
        return new DateTime(time).millisOfDay().withMaximumValue().getMillis();
    }

    //1-7
    public static int getDayOfWeek(long time){
        return new DateTime(time).getDayOfWeek();
    }

    public static int getDayOfMonth(long time){
        return new DateTime(time).getDayOfMonth();
    }

    public static String format1(long time){
        if(time == 0){
            return "";
        }
        return new DateTime(time).toString(FORMAT1);
    }

    public static String format2(long time){
        if(time == 0){
            return "";
        }
       return new DateTime(time).toString(FORMAT2);
    }

    public static int format3Int(long time){
        if(time == 0){
            return 0;
        }
        return Integer.valueOf(new DateTime(time).toString(FORMAT3));
    }

    public static String format4(long time){
        if(time == 0){
            return "";
        }
        return new DateTime(time).toString(FORMAT4);
    }

    public static String format5(long time){
        if(time == 0){
          return "";
        }
        return new DateTime(time).toString(FORMAT5);
    }

	public static String format5(DateTime time){
		if(time.getMillis() == 0){
			return "";
		}
		return time.toString(FORMAT5);
	}

    public static String format6(long time){
        if(time == 0){
            return "";
        }
        return new DateTime(time).toString(FORMAT6);
    }

    public static String format7(long time){
        if(time == 0){
            return "";
        }
        return new DateTime(time).toString(FORMAT7);
    }




}
