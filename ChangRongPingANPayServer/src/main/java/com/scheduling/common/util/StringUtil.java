package com.scheduling.common.util;

import com.alibaba.fastjson.JSONObject;
import com.scheduling.common.HttpClientUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class StringUtil {
    /**
     * 判断字符串是否为空白，空白包括：null,""," "
     *
     * @param str
     * @return
     */
    public static boolean isBlank(CharSequence str) {
        if (isEmpty(str)) {
            return true;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; ++i) {
            if (!(Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否为空，包括：null,"";
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return !((str != null) && (str.length() > 0));
    }

    /**
     * 校验参数
     *
     * @param params 传入参数map集合
     * @param strs   传入要校验的参数数组
     * @return
     */
    public static Map<String, Object> checkParamIsBank(Map<String, Object> params, String[] strs) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (String str : strs) {
            if (isBlank((String) params.get(str))) {
                result.put("code", "500");
                result.put("msg", str + " is required!");
                result.put("msg_cn", str + " 字段不能为空!");
                return result;
            }
        }
        return null;
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

    // 获得昨天0点时间
    public static Date getYesterdaymorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTimesmorning().getTime() - 3600 * 24 * 1000);
        return cal.getTime();
    }

    // 获得当天近7天时间
    public static Date getWeekFromNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTimesmorning().getTime() - 3600 * 24 * 1000 * 7);
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

    // 获得本周一0点时间
    public static Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
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
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    // 获得本月最后一天24点时间
    public static Date getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
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
    public static Date getCurrentQuarterEndTime11() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentQuarterStartTime());
        cal.add(Calendar.MONTH, 3);
        return cal.getTime();
    }


    public static Date getCurrentYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
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

    public static boolean isEmpty(String str) {
        if (str == null)
            return true;
        String tempStr = str.trim();
        if (tempStr.length() == 0)
            return true;
        if (tempStr.equals("null"))
            return true;
        return false;
    }

    /**
     * 得到本周周五日期
     *
     * @return
     */
    public static String getbenWeekFriday() {
        SimpleDateFormat formater = new SimpleDateFormat("yyMMdd");
        Calendar cal = new GregorianCalendar();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 4);
        Date last = cal.getTime();
        return formater.format(last);
    }

    /**
     * 得到本周周五日期
     *
     * @return
     */
    public static String getbennyrWeekFriday(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 4);
        Date last = cal.getTime();
        return formater.format(last);
    }

    // 获得当前日期与本周日相差的天数
    public static int getMondayPlus(Date gmtCreate) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(gmtCreate);
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    // 获得下周星期五的日期
    public static String getNextFriday() {
        SimpleDateFormat formater = new SimpleDateFormat("yyMMdd");
        int mondayPlus = getMondayPlus(new Date());
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 11);
        Date last = currentDate.getTime();
        return formater.format(last);
    }

    // 获得下周星期五的日期
    public static String getNextnyrFriday(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        int mondayPlus = getMondayPlus(date);
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 11);
        Date last = currentDate.getTime();
        return formater.format(last);
    }

    /**
     * 获取 当前年、半年、季度、月、日、小时 开始结束时间
     */

    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
    ;
    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ;

    /**
     * 当前季度的结束时间
     *
     * @return
     */
    public static String getCurrentQuarterEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat formater = new SimpleDateFormat("M");
        return formater.format(now);
    }

    /**
     * 本季度最后一个周五
     *
     * @return
     */
    public static String getjiduFriday(Date date) {
        /*Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat formater = new SimpleDateFormat("M");
        String s = "2018-" + formater.format(now);
        String str = s + "-05";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        sdf1.setLenient(false);
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = null;
        try {
            Date parse = sdf.parse(str);
            Calendar lastDate = Calendar.getInstance();
            lastDate.setTime(parse);
            lastDate.set(Calendar.DATE, 1);
            lastDate.add(Calendar.MONTH, 1);
            lastDate.add(Calendar.DATE, -1);
            format = sdf.format(lastDate.getTime());
//	            System.out.println(format);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            try {
                Date date = sdf1.parse(s + "-" + i);
                if (sdf2.format(date).equals("星期五")) {
                    //System.out.println(sdf1.format(date) + " : " + sdf2.format(date));
                    list.add(sdf1.format(date));
                }
            } catch (ParseException e) {
                //do nothing
            }
        }
        String strqw = "";
        if (null != list && null != list.get(list.size() - 1)) {
            strqw = list.get(list.size() - 1);
        }*/
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int month = calendar.get(Calendar.MONTH)+1;
        return lastFRIDAY(month);
    }

    /**
     * 实体对象转成Map
     *
     * @param obj 实体对象
     * @return
     */
    public static Map<String, Object> object2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Map转成实体对象
     *
     * @param map   map实体对象包含属性
     * @param clazz 实体对象类型
     * @return
     */
    public static Object map2Object(Map<String, Object> map, Class<?> clazz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();


            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 指定年月的最后个星期五
     *
     * @param month
     */
    public static String lastFRIDAY(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1); //防止getInstance()返回今天是2月29号，被认为是3月了
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //每个月的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            cal.set(Calendar.DAY_OF_MONTH, --lastDay);
        }
        Date lastMonday = cal.getTime();
        String dtStr = new SimpleDateFormat("yyyy-MM-dd").format(lastMonday);
        return dtStr;
    }
    public static Date getCurrentQuarterStartTimess() {
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
     * 当前季度的结束时间。即2012-03-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentQuarterEndTimess() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentQuarterStartTimess());
        cal.add(Calendar.MONTH, 3);
        return cal.getTime();
    }
    public static String getWeekFriday(){
        SimpleDateFormat formater=new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal=new GregorianCalendar();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 4);
        Date last=cal.getTime();
        return  formater.format(last);
    }
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }
    public static Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }
    /*public static void main(String[] args) {



        System.out.println("当天24点时间：" + getTimesnight().toLocaleString());
        System.out.println("当前时间：" + new Date().toLocaleString());
        System.out.println("当天0点时间：" + getTimesmorning().toLocaleString());
        System.out.println("昨天0点时间：" + getYesterdaymorning().toLocaleString());
        System.out.println("近7天时间：" + getWeekFromNow().toLocaleString());
        System.out.println("本周周一0点时间：" + getTimesWeekmorning().toLocaleString());
        System.out.println("本周周日24点时间：" + getTimesWeeknight().toLocaleString());
        System.out.println("本月初0点时间：" + getTimesMonthmorning().toLocaleString());
        System.out.println("本月未24点时间：" + getTimesMonthnight().toLocaleString());
        System.out.println("上月初0点时间：" + getLastMonthStartMorning().toLocaleString());
        System.out.println("本季度开始点时间：" + getCurrentQuarterStartTime().toLocaleString());
        System.out.println("本季度结束点时间：" + getCurrentQuarterEndTime11().toLocaleString());
        System.out.println("本年开始点时间：" + getCurrentYearStartTime().toLocaleString());
        System.out.println("本年结束点时间：" + getCurrentYearEndTime().toLocaleString());
        System.out.println("上年开始点时间：" + getLastYearStartTime().toLocaleString());
       try {
           SimpleDateFormat longSdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           Date ddate=longSdf1.parse((getbennyrWeekFriday(new Date())+" 16:00:00"));
           Date xdate=new Date();
           if (ddate.getTime()<=new Date().getTime()){
               xdate=getNextWeekMonday(new Date());
           }
           System.out.println("当周·"+getbennyrWeekFriday(xdate));
           System.out.println("次周·"+getNextnyrFriday(xdate));
           System.out.println("季度·"+getjiduFriday(xdate));
       }catch (Exception e){

       }
        String ethjson = HttpClientUtils.get("https://mifengcha.com/api/v1/alerts/list");
        JSONObject objeth = JSONObject.parseObject(ethjson);
        System.out.println(objeth);
    }*/
    /**
     * 读取一个文本 一行一行读取
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static List<String> readFile02(String path) throws Exception {
        // 使用一个字符串集合来存储文本中的路径 ，也可用String []数组
        List<String> list = new ArrayList<String>();
        FileInputStream fis = new FileInputStream(path);
        // 防止路径乱码   如果utf-8 乱码  改GBK     eclipse里创建的txt  用UTF-8，在电脑上自己创建的txt  用GBK
        InputStreamReader isr = new InputStreamReader(fis, "GBK");
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {
            // 如果 t x t文件里的路径 不包含---字符串       这里是对里面的内容进行一个筛选
            if (line.lastIndexOf("---") < 0) {
                list.add(line);
            }
        }
        br.close();
        isr.close();
        fis.close();
        return list;
    }
}
