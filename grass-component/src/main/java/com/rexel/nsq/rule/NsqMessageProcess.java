package com.rexel.nsq.rule;

import com.github.brainlag.nsq.NSQMessage;
import com.github.brainlag.nsq.callbacks.NSQMessageCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName ids-service
 * Description
 * Author 孟开通
 * Date 2022/4/24
 **/
@Component
@Slf4j
public class NsqMessageProcess implements NSQMessageCallback {
    private NsqSendMsgAsync nsqSendMsgAsync;

    @Autowired
    public void setNsqSendMsgAsync(NsqSendMsgAsync nsqSendMsgAsync) {
        this.nsqSendMsgAsync = nsqSendMsgAsync;
    }

    @Override
    public void message(NSQMessage message) {
        nsqSendMsgAsync.sendMsgAsync(message);
    }

}
