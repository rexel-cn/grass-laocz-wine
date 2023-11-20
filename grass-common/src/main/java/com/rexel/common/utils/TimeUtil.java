package com.rexel.common.utils;/**
 * @Author 董海
 * @Date 2022/11/7 11:24
 * @version 1.0
 */

import com.rexel.common.exception.UtilException;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;

/**
 * @ClassName TimeUtil
 * @Description 时分秒 相关工具类
 * @Author Hai.Dong
 * @Date 2022/11/7 11:24
 **/
public class TimeUtil {

    /**
     * 一天毫秒
     */
    public static final Long MILLISECONDS_DAY_SECOND = 86400000L;
    public static final Long MONTH_SECOND = 2592000000L;
    /**
     * 24小时 对应的 秒数
     * 86400 s = 24 * 60 * 60
     */
    private static final Long TWENTY_FOUR_HOURS_SECONDS = 86400L;

    /**
     * 比较两个 time 间相差的 秒数
     * seconds = toTime-fromTime
     *
     * @param fromTime 时间1
     * @param toTime   时间2
     * @return 秒数:注意 差值为正负数
     */
    public static Long diffSeconds(Time fromTime, Time toTime) {
        if (ObjectUtils.isEmpty(fromTime) || ObjectUtils.isEmpty(toTime)) {
            throw new UtilException("比较的时间为空，请进行检查");
        }
        LocalTime localTimeFrom = fromTime.toLocalTime();
        LocalTime localTimeTo = toTime.toLocalTime();
        long seconds = Duration.between(localTimeFrom, localTimeTo).getSeconds();
        return seconds;
    }

    /**
     * 比较两个 time 间相差的 秒数
     * <p>
     * 计算方式：endTime-startTime
     *
     * @param startTime 开始
     * @param endTime   结束
     * @return boolean  true:之后 ； false:之前
     */
    public static boolean conpareTime(Time startTime, Time endTime) {
        if (ObjectUtils.isEmpty(startTime) || ObjectUtils.isEmpty(endTime)) {
            throw new UtilException("比较的时间为空，请进行检查");
        }
        LocalTime localTimeFrom = startTime.toLocalTime();
        LocalTime localTimeTo = endTime.toLocalTime();
        long seconds = Duration.between(localTimeFrom, localTimeTo).getSeconds();
        if (seconds > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是24小时
     *
     * @param seconds 秒数
     * @return 返回结果
     */
    public static boolean isTwentyFourHours(Long seconds) {
        if (seconds.equals(TWENTY_FOUR_HOURS_SECONDS)) {
            return true;
        }
        return false;
    }
}
