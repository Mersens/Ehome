package com.zzu.ehome.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/13.
 */
public class DateUtils {

    /**
     * 日期
     */
    public static final String TODAY = "今天";
    public static final String YESTERDAY = "昨天";
    public static final String TOMORROW = "明天";
    public static final String BEFORE_YESTERDAY = "前天";
    public static final String AFTER_TOMORROW = "后天";
    public static final String SUNDAY = "周日";
    public static final String MONDAY = "周一";
    public static final String TUESDAY = "周二";
    public static final String WEDNESDAY = "周三";
    public static final String THURSDAY = "周四";
    public static final String FRIDAY = "周五";
    public static final String SATURDAY = "周六";

    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
                    return 29;
                }else{
                    return 28;
                }
            default:
                return  -1;
        }
    }
    /**
     * 返回当前月份1号位于周几
     * @param year
     * 		年份
     * @param month
     * 		月份，传入系统获取的，不需要正常的
     * @return
     * 	日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        Log.d("DateView", "DateView:First:" + calendar.getFirstDayOfWeek());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    public static String getTodayTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


    /**
     * 获取当前日期的后几天
     * @param d 当前日期
     * @param day 后几天
     * @return
     */
    public static String getDateAfter(Date d, int day) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return df.format(now.getTime());
    }
    //=---------------
    public static String getFormatTime(){
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date=new Date();

        return  dateFormater.format(date);
    }
    public static boolean Compare_date(String time, String nowtime ) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date dt1 = df.parse(time);
            Date dt2 = df.parse(nowtime);
            if (dt1.getTime() > dt2.getTime()) {
                return false;
            } else if (dt1.getTime() <= dt2.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
    /**
     * 根据当前日期判断星期几
     * @param pTime 当前日期
     * @return
     */
    public static String dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return stringForWeek(dayForWeek);
    }
    /**
     * 根据当前日期判断星期几
     * @param pTime 当前日期
     * @return
     */
    public static int dayIntForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    public static String stringForWeek(int n) {
        String str = null;
        switch (n) {
            case 1:
                str = "星期一";
                break;
            case 2:
                str = "星期二";
                break;
            case 3:
                str = "星期三";
                break;
            case 4:
                str = "星期四";
                break;
            case 5:
                str = "星期五";
                break;
            case 6:
                str = "星期六";
                break;
            case 7:
                str = "星期日";
                break;
        }
        return str;

    }
    public static String getCurrMonth(){

        Calendar now=Calendar.getInstance();
        int year=now.get(Calendar.YEAR);
        int month=now.get(Calendar.MONTH)+1;
return year+"-"+month+"-"+"01";
    }
    /**
     * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日").
     * @param date String 想要格式化的日期
     * @param oldPattern String 想要格式化的日期的现有格式
     * @param newPattern String 想要格式化成什么格式
     * @return String
     */
    public static String StringPattern(String date, String oldPattern, String newPattern) {
        if (date == null || oldPattern == null || newPattern == null)
            return "";
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern) ;        // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern) ;        // 实例化模板对象
        Date d = null ;
        try{
            d = sdf1.parse(date) ;   // 将给定的字符串中的日期提取出来
        }catch(Exception e){            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace() ;       // 打印异常信息
        }
        return sdf2.format(d);
    }
    public static String getDateDetail(String date){
        Calendar today = Calendar.getInstance();
        Calendar target = Calendar.getInstance();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            today.setTime(df.parse(df.format(new Date())));
            today.set(Calendar.HOUR, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            target.setTime(df.parse(date));
            target.set(Calendar.HOUR, 0);
            target.set(Calendar.MINUTE, 0);
            target.set(Calendar.SECOND, 0);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        long intervalMilli = target.getTimeInMillis() - today.getTimeInMillis();
        int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
        return showDateDetail(xcts,target);

    }
    /**
     * 将日期差显示为日期或者星期
     * @param xcts
     * @param target
     * @return
     */
    private static String showDateDetail(int xcts, Calendar target){
        switch(xcts){
            case 0:
                return TODAY;
            case 1:
                return TOMORROW;
            case 2:
                return AFTER_TOMORROW;
            case -1:
                return YESTERDAY;
            case -2:
                return BEFORE_YESTERDAY;
            default:
                int dayForWeek = 0;
                dayForWeek = target.get(Calendar.DAY_OF_WEEK);
                switch(dayForWeek){
                    case 1: return SUNDAY;
                    case 2: return MONDAY;
                    case 3: return TUESDAY;
                    case 4: return WEDNESDAY;
                    case 5: return THURSDAY;
                    case 6: return FRIDAY;
                    case 7: return SATURDAY;
                    default:return null;
                }

        }
    }
    public static Date getDateCount(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.DATE,day);

        return now.getTime();
    }

}
