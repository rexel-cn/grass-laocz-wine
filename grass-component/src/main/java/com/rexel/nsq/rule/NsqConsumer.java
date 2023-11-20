package com.rexel.nsq.rule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Component
@Slf4j
public class NsqConsumer {
    private ConsumerAlarmRocketNsq consumerAlarmRocketNsq;

    @Autowired
    public void setNsqAsync(ConsumerAlarmRocketNsq consumerAlarmRocketNsq) {
        this.consumerAlarmRocketNsq = consumerAlarmRocketNsq;
    }

    @PostConstruct
    public void start() {
        consumerAlarmRocketNsq.start();
    }

    @PreDestroy
    public void shutdown() {
        consumerAlarmRocketNsq.stop();
    }


}
