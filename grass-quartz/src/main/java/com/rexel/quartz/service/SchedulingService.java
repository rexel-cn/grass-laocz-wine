package com.rexel.quartz.service;/**
 * @Author 董海
 * @Date 2022/11/11 15:25
 * @version 1.0
 */

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;

/**
 *@ClassName donghai
 *@Description 排班定时
 *@Author Hai.Dong
 *@Date 2022/11/11 15:25
 **/
public interface SchedulingService {

    void generatePlcPlanToday(Date date) throws ParseException;

    void plcSetZero(Time time, Date date);

}
