/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.kkwang.commonlib.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.DateTimeParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.kkwang.commonlib.R;
import me.kkwang.commonlib.widget.TimeTextView;

public class TimeUtils {

    private static final String TAG = "TimeUtils";

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    private static final DateTimeFormatter DOUBAN_DATE_TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .append(DateTimeFormatter.ISO_LOCAL_DATE)
                    .appendLiteral(' ')
                    .append(DateTimeFormatter.ISO_LOCAL_TIME)
                    .toFormatter()
                    .withChronology(IsoChronology.INSTANCE);

    private static final ZoneId DOUBAN_ZONED_ID = ZoneId.of("Asia/Shanghai");

    private static final Duration JUST_NOW_DURATION = Duration.ofMinutes(5);
    private static final Duration MINUTE_PATTERN_DURATION = Duration.ofHours(1);
    private static final Duration HOUR_PATTERN_DURATION = Duration.ofHours(2);

    /**
     * @throws DateTimeParseException
     */
    public static ZonedDateTime parseDoubanDateTime(String doubanDateTime) {
        return ZonedDateTime.of(LocalDateTime.parse(doubanDateTime, DOUBAN_DATE_TIME_FORMATTER),
                DOUBAN_ZONED_ID);
    }

	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	public static String formatDateTime(ZonedDateTime dateTime, Context context) {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(dateTime.getZone());
        LocalDate date = dateTime.toLocalDate();
        LocalDate nowDate = now.toLocalDate();
        if (date.equals(nowDate)) {
            Duration duration = Duration.between(dateTime, now);
            if (duration.compareTo(Duration.ZERO) > 0) {
                if (duration.compareTo(JUST_NOW_DURATION) < 0) {
                    return context.getString(R.string.just_now);
                } else if (duration.compareTo(MINUTE_PATTERN_DURATION) < 0) {
                    return context.getString(R.string.minute_format,
                            Math.round((float) duration.getSeconds() / SECONDS_PER_MINUTE));
                } else if (duration.compareTo(HOUR_PATTERN_DURATION) < 0) {
                    return context.getString(R.string.hour_format,
                            Math.round((float) duration.getSeconds() / SECONDS_PER_HOUR));
                }
            }
            return DateTimeFormatter
                    .ofPattern(context.getString(R.string.today_hour_minute_pattern))
                    .format(dateTime);
        }
        if (date.plusDays(1).equals(nowDate)) {
            return DateTimeFormatter
                    .ofPattern(context.getString(R.string.yesterday_hour_minute_pattern))
                    .format(dateTime);
        } else if (date.getYear() == nowDate.getYear()) {
            return DateTimeFormatter
                    .ofPattern(context.getString(R.string.month_day_hour_minute_pattern))
                    .format(dateTime);
        } else {
            return DateTimeFormatter
                    .ofPattern(context.getString(R.string.date_hour_minute_pattern))
                    .format(dateTime);
        }
    }

    /**
     * Use {@link TimeTextView} instead if the text is to be set on a
     * {@code TextView}.
     */
    public static String formatDoubanDateTime(String doubanDateTime, Context context) {
        try {
            return formatDateTime(parseDoubanDateTime(doubanDateTime), context);
        } catch (DateTimeParseException e) {
            Log.e(TAG, "Unable to parse date time: " + doubanDateTime);
            e.printStackTrace();
            return doubanDateTime;
        }
    }

    public static long getCurrentTime(){
		return new Date().getTime();
	}

    public static String getCurrentDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }

    public static int getCurrentYear() {
        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        Date mydate = new Date(); // 获取当前日期Date对象
        mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
        return mycalendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        Date mydate = new Date(); // 获取当前日期Date对象
        mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
        return mycalendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        Date mydate = new Date(); // 获取当前日期Date对象
        mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
        return mycalendar.get(Calendar.DAY_OF_MONTH);
    }

    // 获得当天0点时间
    public static Date getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得当天0点时间
    public static Date getTimesmorningByTime(long time) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得当天0点时间
    public static long getTimesmorningLongByTime(long time) {

        return new DateTime(new Date(time)).millisOfDay().withMaximumValue().getMillis();
//
//        DateTime endOfDay = nowTime.millisOfDay().withMaximumValue();
//
//        Calendar cal = Calendar.getAppContext();
//        cal.setTime(new Date(time));
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        return cal.getTime();
    }

    // 获得昨天0点时间
    public static Date getYesterdaymorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTimesmorning().getTime()-3600*24*1000);
        return cal.getTime();
    }
    // 获得当天近7天时间
    public static Date getWeekFromNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( getTimesmorning().getTime()-3600*24*1000*7);
        return cal.getTime();
    }

    // 获得当天24点时间
    public static Date getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得当天24点时间
    public static Date getTimesnightByTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static long getTimesnightTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    // 获得本周一0点时间
    public static Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    // 获得本周日24点时间
    public static Date getTimesWeeknight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekmorning());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    // 获得本月第一天0点时间
    public static Date getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    // 获得本月最后一天24点时间
    public static Date getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    public static Date getLastMonthStartMorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesMonthmorning());
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentQuarterStartTime());
        cal.add(Calendar.MONTH, 3);
        return cal.getTime();
    }


    public static Date getCurrentYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
        return cal.getTime();
    }

    public static Date getCurrentYearEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentYearStartTime());
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }

    public static Date getLastYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentYearStartTime());
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

	/**
	 * 得到仿微信日期格式输出
	 *
	 * @param msgTimeMillis
	 * @return
	 */
	public static String getMsgFormatTime(long msgTimeMillis) {
		DateTime nowTime = new DateTime();
//        LogUtils.sf("nowTime = " + nowTime);
		DateTime msgTime = new DateTime(msgTimeMillis);
//        LogUtils.sf("msgTime = " + msgTime);
		int days = Math.abs(Days.daysBetween(msgTime, nowTime).getDays());
//        LogUtils.sf("days = " + days);
		if (days < 1) {
			//早上、下午、晚上 1:40
			return getTime(msgTime);
		} else if (days == 1) {
			//昨天
			return "昨天 " + getTime(msgTime);
		} else if (days <= 7) {
			//星期
			switch (msgTime.getDayOfWeek()) {
				case DateTimeConstants.SUNDAY:
					return "周日 " + getTime(msgTime);
				case DateTimeConstants.MONDAY:
					return "周一 " + getTime(msgTime);
				case DateTimeConstants.TUESDAY:
					return "周二 " + getTime(msgTime);
				case DateTimeConstants.WEDNESDAY:
					return "周三 " + getTime(msgTime);
				case DateTimeConstants.THURSDAY:
					return "周四 " + getTime(msgTime);
				case DateTimeConstants.FRIDAY:
					return "周五 " + getTime(msgTime);
				case DateTimeConstants.SATURDAY:
					return "周六 " + getTime(msgTime);
			}
			return "";
		} else {
			//12月22日
			return msgTime.toString("MM月dd日 " + getTime(msgTime));
		}
	}

	@NonNull
	private static String getTime(DateTime msgTime) {
		int hourOfDay = msgTime.getHourOfDay();
		String when;
		if (hourOfDay >= 18) {//18-24
			when = "晚上";
		} else if (hourOfDay >= 13) {//13-18
			when = "下午";
		} else if (hourOfDay >= 11) {//11-13
			when = "中午";
		} else if (hourOfDay >= 5) {//5-11
			when = "早上";
		} else {//0-5
			when = "凌晨";
		}
		return when + " " + msgTime.toString("hh:mm");
	}
}
