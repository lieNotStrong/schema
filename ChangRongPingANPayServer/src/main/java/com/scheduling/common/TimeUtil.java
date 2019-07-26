  package com.scheduling.common;

  import java.text.SimpleDateFormat;
  import java.util.Calendar;
  import java.util.Date;
  import java.util.TimeZone;

  public class TimeUtil
  {
    static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    static SimpleDateFormat dateTimeFormatNotMiles = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    static SimpleDateFormat dateTimeNotSplitFormat = new SimpleDateFormat("yyyyMMddHHmmss:SSS");
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    static SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
    static SimpleDateFormat yearMonthFormat = new SimpleDateFormat("yyyy-MM");
    static SimpleDateFormat MMddFormat = new SimpleDateFormat("MM-dd");
    static SimpleDateFormat YYMMddFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat YYMMddHHmmssFormatSlash = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    static SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {
      Calendar c = Calendar.getInstance();
      int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
      if (day_of_week == 0)
        day_of_week = 7;
      c.add(Calendar.DATE, -day_of_week + 1);
      System.out.println(c.getTime());
      return format.format(c.getTime());
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek() {
      Calendar c = Calendar.getInstance();
      int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
      if (day_of_week == 0)
        day_of_week = 7;
      c.add(Calendar.DATE, -day_of_week + 7);
      return format.format(c.getTime());
    }



    public static String getNowDateTime()
    {
      Date today = new Date();
      return dateTimeFormat.format(today);
    }

    public static String getNowTime() {
      Date today = new Date();
      return timeFormat.format(today);
    }

    public static String getNowDateTime4NotSplit() {
      Date today = new Date();
      return dateTimeNotSplitFormat.format(today);
    }

    public static String getNowDate() {
      Date today = new Date();
      return dateFormat.format(today);
    }

    public static long getUnixTime() {
      return System.currentTimeMillis() / 1000L;
    }

    public static String fromUnixTime(int ctime) {
      Date today = new Date(ctime * 1000L);
      return dateTimeFormat.format(today);
    }

    public static String fromUnixTime(long ctime) {
      Date today = new Date(ctime * 1000L);
      return dateTimeFormat.format(today);
    }

    public static String fromUnixTime2Time(long ctime) {
      Date today = new Date(ctime * 1000L);
      return timeFormat.format(today);
    }

    public static String fromUnixTime2Day(long ctime) {
      Date today = new Date(ctime * 1000L);
      return dayFormat.format(today);
    }

    public static String fromUnixTime2YearMonth(long ctime) {
      Date today = new Date(ctime * 1000L);
      return yearMonthFormat.format(today);
    }

    public static String fromUnixTime3Time(long ctime) {
      Date today = new Date(ctime * 1000L);
      return dateFormat.format(today);
    }

    public static String fromUnixTimeNotMiles(long ctime) {
      Date today = new Date(ctime * 1000L);
      return dateTimeFormatNotMiles.format(today);
    }

    public static String fromUnixTime2MMdd(long ctime) {
      Date today = new Date(ctime * 1000L);
      return MMddFormat.format(today);
    }

    public static String fromUnixTime2YYMMdd(long ctime) {
      Date today = new Date(ctime * 1000L);
      return YYMMddFormat.format(today);
    }

    public static String fromUnixTime2YYMMddHHmmssFormatSlash(long ctime) {
      Date today = new Date(ctime * 1000L);
      return YYMMddHHmmssFormatSlash.format(today);
    }

    public static long getStartTime() {
      Calendar todayStart = Calendar.getInstance();
      todayStart.set(11, 0);
      todayStart.set(12, 0);
      todayStart.set(13, 0);
      todayStart.set(14, 0);
      return todayStart.getTime().getTime() / 1000L;
    }

    public static boolean between(String currentTime, String startTime, String endTime) {
      if (startTime.compareTo(endTime) > 0) {
        endTime = String.format("%d%s", new Object[] {
          Integer.valueOf(Integer.valueOf(endTime.substring(0, 2)).intValue() + 24), endTime.substring(2) });
      }

      return (currentTime.compareTo(startTime) >= 0) && (currentTime.compareTo(endTime) <= 0);
    }

    public static int getDayDiff(long starttime, long endtime) {
      starttime = (starttime / 86400000L * 86400000L - TimeZone.getDefault().getRawOffset()) / 1000L;
      endtime = (endtime / 86400000L * 86400000L - TimeZone.getDefault().getRawOffset()) / 1000L;

      Double res = Double.valueOf((endtime - starttime) / 86400L);
      return res.intValue();
    }

    public static boolean isWeekend() {
      String bDate = getNowDate();
      try {
        Date bdate = dateFormat.parse(bDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(bdate);

        return (cal.get(7) == 7) || (cal.get(7) == 1);
      }
      catch (java.text.ParseException localParseException) {}
      return false;
    }

    public static int dayForWeek(String pTime) throws Exception {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      Calendar c = Calendar.getInstance();
      c.setTime(format.parse(pTime));
      int dayForWeek = 0;
      if (c.get(7) == 1) {
        dayForWeek = 7;
      } else {
        dayForWeek = c.get(7) - 1;
      }
      return dayForWeek;
    }









    public static boolean isInDate(Date date, String strDateBegin, String strDateEnd)
      throws Exception
    {
      SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
      String start = strDateBegin;
      String end = strDateEnd;
      String format = sd.format(new Date());
      Date fo = sd.parse(format);
      Date str = sd.parse(start);
      Date en = sd.parse(end);
      if ((fo.getTime() >= str.getTime()) && (fo.getTime() <= en.getTime())) {
        return true;
      }
      return false;
    }
  }

