package com.rexel.mqtt;

import com.rexel.mqtt.source.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MqttServiceImpl implements MqttService {
    MqttPahoMessageDrivenChannelAdapter adapter;

    @Autowired
    public MqttServiceImpl(MqttPahoMessageDrivenChannelAdapter adapter) {
        this.adapter = adapter;
    }


    @Override
    public void addTopic(String topic) {
        String[] topics = adapter.getTopic();
        if (!Arrays.asList(topics).contains(topic)) {
            adapter.addTopic(topic);
        }
    }

    @Override
    public void removeTopic(String topic) {
        adapter.removeTopic(topic);
    }
}
