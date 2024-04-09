package com.rexel.quartz.task;

import com.rexel.common.utils.PulseRefreshToken;
import com.rexel.common.utils.StringUtils;
import com.rexel.laocz.service.ILaoczLiquorRuleInfoService;
import com.rexel.quartz.util.ToConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务调度测试
 *
 * @author ids-admin
 */
@Component("ryTask")
@Slf4j
public class RyTask {

    @Autowired
    private PulseRefreshToken pulseRefreshToken;

    @Autowired
    private ToConfiguration toConfiguration;

    @Autowired
    private ILaoczLiquorRuleInfoService laoczLiquorRuleInfoService;


    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }


    public void ryParams(Date params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
//        List<SysUser> sysUsers = userService.selectUserList(new SysUser());
//        System.out.println(sysUsers.size());
//        System.out.println("执行无参方法");
    }

    /**
     * 刷新Pulse token
     */
    public void refreshTokenTime() throws Exception {
        pulseRefreshToken.refreshTokenTime();
    }

    /**
     * 将所有租户电话号传给组态
     */
    public void phoneToConfigure() {
        toConfiguration.phoneToConfiguration();
    }

    /**
     * 将所有租户电话号传给组态
     */
    public void pushLiquorAlarm() {
        laoczLiquorRuleInfoService.pushAlarm();
    }
}
