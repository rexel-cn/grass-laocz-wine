package com.rexel.nsq.rule;

import com.rexel.common.constant.Constants;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.send.factory.PushFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @ClassName SendMsgAsync
 * @Description 异步多线程发送告警通知
 * @Author 董海
 * @Date 2020/6/19 8:52
 */
@Slf4j
@Component
public class EarlyWarningSendMsgAsync {
    @Autowired
    private PushFactory pushFactory;

    @Async("sendMessageExecutor")
    public void sendMsgAsync(List<String> sendType, List<SysUser> userList, String noticeContent) {
        try {
            pushFactory.startSend(userList, sendType, noticeContent, Constants.ALARM_EARLY_WARN);
        } catch (Exception e) {
            log.error("[预警规则通知异常.发送内容:{}]", noticeContent);
        }
    }
}