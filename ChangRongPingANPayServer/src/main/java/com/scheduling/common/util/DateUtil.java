  package com.scheduling.common.util;

  import java.text.ParseException;
  import java.text.SimpleDateFormat;
  import java.util.ArrayList;
  import java.util.Calendar;
  import java.util.Date;
  import java.util.List;
  import java.util.Locale;
  import org.joda.time.DateTime;
  import org.joda.time.DateTimeZone;
  import org.joda.time.Days;
  import org.joda.time.format.ISODateTimeFormat;


  public class DateUtil
  {
    public static String toIso(Date date)
    {
      return new DateTime(date).toString(ISODateTimeFormat.dateTimeNoMillis());
    }

    public static Date fromIso(String date) {
      return ISODateTimeFormat.dateOptionalTimeParser().parseDateTime(date).toDate();
    }

    public static String getDateAsKey(Date date) {
      return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    public static String getDateTimeAsKey(Date date) {
      return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
    }


    public static String getMillisecondAsKey(Date date)
    {
      return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
    }

    public static Date getDateWithoutTime(Date source) {
      return getDate(getString(source, "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    public static Date getPast() {
      return fromIso("1970-01-01T00:00:00+08:00");
    }

    public static String getStringOfNow(String pattern) {
      return new SimpleDateFormat(pattern).format(getNow());
    }

    public static Date getNow() {
      return Calendar.getInstance().getTime();
    }

    public static int getDay() {
      return Calendar.getInstance().get(5);
    }

    public static int getDay(Date date) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(date);
      return instance.get(5);
    }

    public static int getMonth() {
      return Calendar.getInstance().get(2) + 1;
    }

    public static int getMonth(Date date) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(date);
      return instance.get(2) + 1;
    }

    public static int getYear() {
      return Calendar.getInstance().get(1);
    }

    public static int getYear(Date date) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(date);
      return instance.get(1);
    }

    private static int getDayOfWeek(Date date) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(date);
      return instance.get(7);
    }

    public static int getDayOfWeekFromSunday0(Date date) {
      int dayOfWeekFromSunday = getDayOfWeek(date);
      int dayOfWeekFromMonday = dayOfWeekFromSunday - 1;
      if (dayOfWeekFromMonday == 0) {
        dayOfWeekFromMonday = 7;
      }
      return dayOfWeekFromMonday;
    }

    public static int getDayOfWeekFromSunday1(Date date) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(date);
      return instance.get(7);
    }

    public static int getDayOfWeekFromMonday1(Date date) {
      int dayOfWeekFromSunday = getDayOfWeek(date);
      int dayOfWeekFromMonday = dayOfWeekFromSunday - 1;
      if (dayOfWeekFromMonday == 0) {
        dayOfWeekFromMonday = 7;
      }
      return dayOfWeekFromMonday;
    }

    public static int getDayOfMonth() {
      return Calendar.getInstance().get(5);
    }

    public static int getDayOfMonth(Date date) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(date);
      return instance.get(5);
    }

    public static Date getDate(String source, String pattern) {
      try {
        return new SimpleDateFormat(pattern).parse(source);
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
    }

    public static Date getDate(String source, String pattern, Locale locale) {
      Locale dl = Locale.getDefault();
      try {
        return new SimpleDateFormat(pattern, locale).parse(source);
      } catch (ParseException e) {
        throw new RuntimeException(e);
      } finally {
        Locale.setDefault(dl);
      }
    }

    public static String getString(Date source, String pattern, Locale locale) {
      Locale dl = Locale.getDefault();
      try {
        return new SimpleDateFormat(pattern, locale).format(source);
      } finally {
        Locale.setDefault(dl);
      }
    }

    public static String getString(Date source, String pattern) {
      return new SimpleDateFormat(pattern).format(source);
    }

    public static boolean isWeekend(Date date) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(date);
      int dayOfWeek = instance.get(7);
      return (dayOfWeek == 1) || (dayOfWeek == 7);
    }

    public static List<Integer> splitDays(String days) {
      String[] dayArray = days.split(",");
      List<Integer> dayList = new ArrayList();
      for (String day : dayArray) {
        dayList.add(Integer.valueOf(day.trim()));
      }
      return dayList;
    }

    public static String toUtc(Date date, String pattern) {
      return new DateTime(date, DateTimeZone.UTC).toString(pattern);
    }


    public static int getOverdueDays(Date dueDate, Date byDate)
    {
      int dueDays = Days.daysBetween(new DateTime(getDateWithoutTime(dueDate).getTime()), new DateTime(getDateWithoutTime(byDate))).getDays();
      return dueDays < 0 ? 0 : dueDays;
    }

    public static int getDays(Date checkInDate, Date checkOutDate) {
      return Days.daysBetween(new DateTime(checkInDate), new DateTime(checkOutDate)).getDays();
    }

    public static Date getFirstDateOfThisYear(DateTime dateTime) {
      return DateTime.parse(dateTime.toString("yyyy-01-01")).toDate();
    }

    public static Date getFirstDateOfNextYear(DateTime dateTime) {
      return DateTime.parse(dateTime.toString("yyyy-01-01")).plusYears(1).toDate();
    }

    public static Date getFirstDateOfThisMonth(DateTime dateTime) {
      return dateTime.dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
    }

    public static Date getFirstDateOfNextMonth(DateTime dateTime) {
      return dateTime.plusMonths(1).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
    }

    public static Date getLastNDaysStartDate(DateTime now, int n) {
      return now.minusDays(n).withTimeAtStartOfDay().toDate();
    }

    public static Date getLastNWeeksStartDate(DateTime now, int n) {
      return now.minusWeeks(n).withTimeAtStartOfDay().toDate();
    }

    public static Date getLastNMonthsStartDate(DateTime now, int n) {
      return now.minusMonths(n).withTimeAtStartOfDay().toDate();
    }

    public static Date getDateTime(Date date, String time) {
      return getDate(getString(date, "yyyy-MM-dd") + " " + time, "yyyy-MM-dd H:m");
    }

    public static Date stampToDate(String stamp) {
      long lt = new Long(stamp).longValue();
      return new Date(lt);
    }
  }

