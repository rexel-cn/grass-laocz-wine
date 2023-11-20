package com.rexel.nsq.rule;

import com.github.brainlag.nsq.NSQConfig;
import com.github.brainlag.nsq.NSQConsumer;
import com.github.brainlag.nsq.lookup.DefaultNSQLookup;
import com.github.brainlag.nsq.lookup.NSQLookup;
import com.rexel.nsq.rule.config.NsqConfig;
import com.rexel.nsq.rule.config.NsqConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ClassName NsqAsync
 * Description
 *
 * @Author mengkaitong
 * Date 2022/4/24
 **/
@Component
@Slf4j
public class ConsumerAlarmRocketNsq {
    private NSQConsumer consumer;
    private NsqConfig config;
    private NsqConfigUtil nsqConfigUtil;
    private NsqMessageProcess nsqMessageProcess;

    @Autowired
    public void setConfig(NsqConfig config) {
        this.config = config;
    }

    @Autowired
    public void setNsqConfigUtil(NsqConfigUtil nsqConfigUtil) {
        this.nsqConfigUtil = nsqConfigUtil;
    }

    @Autowired
    public void setNsqMessageProcess(NsqMessageProcess nsqMessageProcess) {
        this.nsqMessageProcess = nsqMessageProcess;
    }

    @Async("threadPoolTaskExecutor")
    public void start() {
        if (!nsqConfigUtil.status) {
            return;
        }
        //ip
        String namesrvAddr = nsqConfigUtil.namesrvAddr;
        //topic
        String topic = config.getTopic();
        //端口
        int port = nsqConfigUtil.port;
        //channel
        String channel = config.getChannel();
        NSQLookup lookup = new DefaultNSQLookup();
        lookup.addLookupAddress(namesrvAddr, port);
        NSQConfig nsqConfig = new NSQConfig();
        nsqConfig.setMaxInFlight(nsqConfigUtil.maxInFlight);
        consumer = new NSQConsumer(
                lookup, topic,
                channel,
                nsqMessageProcess,
                nsqConfig);
        consumer.start();
        log.info("[Nsq启动日志：================]---namesrvAddr:{},port:{},topic:{},channel:{}", namesrvAddr, port, topic, channel);
        log.info("[---Nsq消费者启动---]topic=" + topic + ", channel=" + channel);
    }

    public void stop() {
        if (!nsqConfigUtil.status) {
            return;
        }
        try {
            consumer.close();
        } catch (IOException e) {
            log.error("[---Nsq消费者停止异常---]" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        log.info("[---Nsq消费者停止---] NsqConsumer");
    }

}
