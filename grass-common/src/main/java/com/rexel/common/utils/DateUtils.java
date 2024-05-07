package com.rexel.common.utils;

import cn.hutool.core.date.DateUtil;
import com.rexel.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间工具类
 *
 * @author ids-admin
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final SimpleDateFormat cronSdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
    public static String YYYY = "yyyy";
    public static String YYYY_MM = "yyyy-MM";
    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String HH_MM_SS = "HH:mm:ss";
    public static String MM_DD = "MM-dd";
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 指定日期  相加 天数
     *
     * @param date
     * @param days
     * @return Date
     */
    public static Date getNextDay(Date date, int days) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }


    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 秒数转为 小时分钟秒
     *
     * @param seconds 秒
     * @return
     */
    public static String secondToTime(int seconds) {
        int hour = seconds / 3600;
        int other = seconds % 3600;
        int minute = other / 60;
        int second = other % 60;
        final StringBuilder sb = new StringBuilder();
        if (hour > 0) {
            sb.append(hour);
            sb.append("小时");
        }
        if (minute > 0) {
            sb.append(minute);
            sb.append("分钟");
        }
        if (second > 0) {
            sb.append(second);
            sb.append("秒");
        }
        return sb.toString();
    }

    /**
     * Date转换为String
     *
     * @param date   date
     * @param format format
     * @return String
     */
    public static String dateToStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * 判断字符串是否为合法的日期格式
     *
     * @param dateStr 待判断的字符串
     * @return
     */
    public static boolean isValidDate(String dateStr, String formatData) {
        //判断结果 默认为true
        boolean judgeresult = true;
        //1、首先使用SimpleDateFormat初步进行判断，过滤掉注入 yyyy-01-32 或yyyy-00-0x等格式
        //此处可根据实际需求进行调整，如需判断yyyy/MM/dd格式将参数改掉即可
        SimpleDateFormat format = new SimpleDateFormat(formatData);
        try {
            //增加强判断条件，否则 诸如2022-02-29也可判断出去
            format.setLenient(false);
            Date date = format.parse(dateStr);
        } catch (Exception e) {
            judgeresult = false;
        }
        /*String yearStr = dateStr.split("-")[0];
        if (yearStr.startsWith("0") || yearStr.length() != 4) {
            judgeresult = false;
        }*/
        return judgeresult;
    }


    public static int getMonth(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取月份
     * 月份 < 10,进行补0操作
     * @param date
     * @return
     */
    public static String getMonthAndZero(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int month = instance.get(Calendar.MONTH)+1;
        String  resultMonth="";
        if( month < 10){
            resultMonth ="0"+month;
        }else {
            resultMonth=String.valueOf(month);
        }

        return resultMonth;
    }




    public static Date updateHms(Date date, Time time) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);

        Calendar timeDate = Calendar.getInstance();
        timeDate.setTime(time);

        instance.set(Calendar.HOUR_OF_DAY, timeDate.get(Calendar.HOUR_OF_DAY));
        instance.set(Calendar.MINUTE, timeDate.get(Calendar.MINUTE));
        instance.set(Calendar.SECOND, timeDate.get(Calendar.SECOND));
        return instance.getTime();
    }

    public static int getDay(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.DAY_OF_MONTH);
    }

    public static String dataToCron(Date date) {
        return cronSdf.format(date);
    }

    /**
     * 获取当前时间 加减的天
     *
     * @param date 时间
     * @param days 加减的天数
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    /**
     * 默认获取本年的开始时间
     *
     * @param year 指定年
     * @return
     */
    public static Date getBeginDayOfYear(Integer year) {

        Calendar cal = Calendar.getInstance();
        if (year == null) {
            cal.set(Calendar.YEAR, getNowYear());
        } else {
            cal.set(Calendar.YEAR, year);
        }
        cal.set(Calendar.MONTH, Calendar.JANUARY);

        cal.set(Calendar.DATE, 1);

        return getDayStartTime(cal.getTime());

    }

    /**
     * 默认获取本年的结束时间
     *
     * @param year 指定年
     * @return
     */
    public static Date getEndDayOfYear(Integer year) {

        Calendar cal = Calendar.getInstance();

        if (year == null) {
            cal.set(Calendar.YEAR, getNowYear());
        } else {
            cal.set(Calendar.YEAR, year);
        }
        cal.set(Calendar.MONTH, Calendar.DECEMBER);

        cal.set(Calendar.DATE, 31);

        return getDayEndTime(cal.getTime());

    }

    /**
     * 获取某个日期的开始时间
     *
     * @param d
     * @return
     */
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Timestamp(calendar.getTimeInMillis());

    }

    /**
     * 获取某个日期的结束时间
     *
     * @param d
     * @return
     */
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取今年是哪一年
     *
     * @return
     */
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(1);
    }

    /**
     * 获取本月是哪一月
     *
     * @return
     */
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取指定月的开始时间
     *
     * @param date
     * @return
     */
    public static Date getBeginDayOfMonth(String date) {
        LocalDate date2;
        if (null == date) {
            date2 = LocalDate.now();
        } else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date2 = LocalDate.parse(date, fmt);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(date2.getYear(), date2.getMonthValue() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取本月的开始时间
     *
     * @return
     */
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取本月的结束时间
     *
     * @return
     */
    public static Date getEndDayOfMonth() {

        Calendar calendar = Calendar.getInstance();

        calendar.set(getNowYear(), getNowMonth() - 1, 1);

        int day = calendar.getActualMaximum(5);

        calendar.set(getNowYear(), getNowMonth() - 1, day);

        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取本周的开始时间
     *
     * @return
     */
    public static Date getBeginDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取本周的结束时间
     *
     * @return
     */
    public static Date getEndDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        calendar.add(Calendar.DATE, 7);
        return getDayEndTime(calendar.getTime());
    }

    // 返回第几个月份，不是几月
    // 季度一年四季， 第一季度：2月-4月， 第二季度：5月-7月， 第三季度：8月-10月， 第四季度：11月-1月
    private static int getQuarterInMonth(int month, boolean isQuarterStart) {
        int[] months = {1, 4, 7, 10};
        if (!isQuarterStart) {
            months = new int[]{3, 6, 9, 12};
        }
        if (month >= 2 && month <= 4) {
            return months[0];
        } else if (month >= 5 && month <= 7) {
            return months[1];
        } else if (month >= 8 && month <= 10) {
            return months[2];
        } else {
            return months[3];
        }
    }

    /**
     * 本季度初
     *
     * @return
     */
    @Deprecated
    public static Date getBeginDayOfQuarter() {
        Calendar calendar = Calendar.getInstance();
        int month = getQuarterInMonth(calendar.get(Calendar.MONTH), true);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(DateUtil.formatDateTime(calendar.getTime()));
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取本季度 时间
     *
     * @param isFirst true 开始时间，false结束时间
     * @return
     */
    public static Date getStartOrEndDayOfQuarter(Boolean isFirst) {
        LocalDate today = LocalDate.now();
        Month month = today.getMonth();
        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        if (isFirst) {
            today = LocalDate.of(today.getYear(), firstMonthOfQuarter, 1);
        } else {
            today = LocalDate.of(today.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(today.isLeapYear()));
        }
        return Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 本季度末
     *
     * @return
     */
    @Deprecated
    public static Date getEndDayOfQuarter() {
        Calendar calendar = Calendar.getInstance();
        int month = getQuarterInMonth(calendar.get(Calendar.MONTH), false);
        calendar.set(Calendar.MONTH, month + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 本日 本周 本月 本季 本年
     *
     * @param quickTimeSelection 结束时间
     * @param quickTimeSelection 快速时间选择
     */
    public static Date quickBeginTime(String quickTimeSelection) {
        switch (quickTimeSelection) {
            case "today":
                return DateUtils.getDayStartTime(new Date());
            case "week":
                return DateUtils.getBeginDayOfWeek();
            case "month":
                return DateUtils.getBeginDayOfMonth();
            case "quarter":
                return DateUtils.getStartOrEndDayOfQuarter(true);
            case "year":
                return DateUtils.getBeginDayOfYear(null);
            default:
                log.error("快速时间选择错误:" + quickTimeSelection);
                throw new CustomException("快速时间选择错误");
        }
    }

    public static Date quickEndTime(String quickTimeSelection) {
        switch (quickTimeSelection) {
            case "today":
                return DateUtils.getDayEndTime(new Date());
            case "week":
                return DateUtils.getEndDayOfWeek();
            case "month":
                return DateUtils.getEndDayOfMonth();
            case "quarter":
                return DateUtils.getStartOrEndDayOfQuarter(false);
            case "year":
                return DateUtils.getEndDayOfYear(null);
            default:
                log.error("快速时间选择错误:" + quickTimeSelection);
                throw new CustomException("快速时间选择错误");
        }
    }

    /**
     * 获取某个月份的天数
     *
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static List<LocalDateTime> getIntervalTimeList(Date start, Date end, int interval) {
        List<LocalDateTime> result = new ArrayList<>();
        //格式化成日期类型
        LocalDateTime startDate = LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault());
        LocalDateTime endDate = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());
        //循环按固定间隔，存入集合中
        while (startDate.isBefore(endDate)) {
            result.add(startDate);
            startDate = startDate.plusMonths(interval);
            //startDate = startDate.plusDays(interval);
        }
        return result;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 近7天
     *
     * @return
     */
    public static ArrayList<String> getLast7days() {
       return getLast7days(DateUtils.YYYY_MM_DD);
    }

    /**
     * 近7天
     * @param format 时间格式
     * @return
     */
    public static ArrayList<String> getLast7days(String format) {
        if(StringUtils.isEmpty(format)){
            throw  new RuntimeException("时间格式不能为空");
        }
        ArrayList<String> list = new ArrayList<>();
        Date nowDate = DateUtils.getNowDate();
        for (int i = -6; i <= 0; i++) {
            list.add(DateUtils.dateToStr(DateUtils.addDays(nowDate, i), format));
        }
        return list;
    }

    /**
     * 近半年
     *
     * @return 结果
     */
    public static ArrayList<String> getLast6Months() {
        ArrayList<String> list = new ArrayList<>();
        Date nowDate = DateUtils.getNowDate();
        for (int i = -5; i <= 0; i++) {
            list.add(DateUtils.dateToStr(DateUtils.addMonths(nowDate, i), DateUtils.YYYY_MM));
        }
        return list;
    }

    /**
     * 比较时分秒
     *
     * @param startTime startTime
     * @param endTime endTime
     * @param nowTime nowTime
     * @return 结构
     */
    public static boolean compareHhMmSs(String startTime, String endTime, String nowTime) {
        DateFormat df = new SimpleDateFormat(HH_MM_SS);
        try {
            Date start = df.parse(startTime);
            Date end = df.parse(endTime);
            Date now = df.parse(nowTime);
            if (now.after(start) && now.before(end)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 将 LocalDateTime 转换成 Date
     *
     * @param date LocalDateTime
     * @return LocalDateTime
     */
    public static Date of(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        // 将此日期时间与时区相结合以创建 ZonedDateTime
        ZonedDateTime zonedDateTime = date.atZone(ZoneId.systemDefault());
        // 本地时间线 LocalDateTime 到即时时间线 Instant 时间戳
        Instant instant = zonedDateTime.toInstant();
        // UTC时间(世界协调时间,UTC + 00:00)转北京(北京,UTC + 8:00)时间
        return Date.from(instant);
    }
    /**
     * 将 Date 转换成 LocalDateTime
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime of(Date date) {
        if (date == null) {
            return null;
        }
        // 转为时间戳
        Instant instant = date.toInstant();
        // UTC时间(世界协调时间,UTC + 00:00)转北京(北京,UTC + 8:00)时间
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}

