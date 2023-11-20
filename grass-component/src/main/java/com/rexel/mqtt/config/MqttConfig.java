package com.rexel.mqtt.config;

import com.rexel.mqtt.source.core.ClientManager;
import com.rexel.mqtt.source.core.Mqttv3ClientManager;
import com.rexel.mqtt.source.inbound.MqttPahoMessageDrivenChannelAdapter;
import com.rexel.mqtt.source.outbound.MqttPahoMessageHandler;
import com.rexel.mqtt.source.support.DefaultPahoMessageConverter;
import lombok.Data;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;

/**
 * @ClassName MqttConfig
 * @Description mqtt配置类
 * @Author 孟开通
 * @Date 2023/8/31 11:01
 **/
@Data
@Configuration
@EnableIntegration
@IntegrationComponentScan
public class MqttConfig {
    @Resource
    private MqttProperties mqttProperties;

    /**
     * 客户端连接配置
     *
     * @return MqttConnectOptions
     */
    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        PropertyMapper mapper = PropertyMapper.get();
        mapper.from(mqttProperties::getServerUrls).whenNonNull().to(options::setServerURIs);
        mapper.from(mqttProperties::getUserName).whenNonNull().to(options::setUserName);
        mapper.from(mqttProperties::getPassWord).whenNonNull().as(String::toCharArray).to(options::setPassword);
        mapper.from(mqttProperties::getKeepAliveInterval).whenNonNull().to(options::setKeepAliveInterval);
        mapper.from(mqttProperties::getAutomaticReconnect).whenNonNull().to(options::setAutomaticReconnect);
        mapper.from(mqttProperties::getCleanSession).whenNonNull().to(options::setCleanSession);
        mapper.from(mqttProperties::getCompletionTimeout).whenNonNull().to(options::setConnectionTimeout);
        return options;
    }

    /**
     * mqtt v3 客户端管理器
     *
     * @param mqttConnectOptions mqttConnectOptions
     * @return 结果
     */
    @Bean
    public ClientManager<IMqttAsyncClient, MqttConnectOptions> mqttv3ClientManager(MqttConnectOptions mqttConnectOptions) {
        return new Mqttv3ClientManager(mqttConnectOptions, mqttProperties.getClientId());
    }

    /**
     * mqtt v3 入站通道适配器，使用客户端管理器进行统一管理客户端
     *
     * @param clientManager clientManager
     * @return 结果
     */
    @Bean
    public MqttPahoMessageDrivenChannelAdapter adapter(ClientManager<IMqttAsyncClient, MqttConnectOptions> clientManager) {
        return new MqttPahoMessageDrivenChannelAdapter(clientManager, mqttProperties.getTopics());
    }

    /**
     * mqtt v3 入站
     *
     * @param adapter adapter
     * @return 结果
     */
    @Bean
    public MessageProducer mqttInbound(MqttPahoMessageDrivenChannelAdapter adapter) {
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        // 入站投递的通道
        adapter.setOutputChannel(mqttInboundChannel());
        adapter.setErrorChannel(errorChannel());
        adapter.setQos(mqttProperties.getQos());
        return adapter;
    }

    /**
     * mqtt v3 出站
     *
     * @param clientManager clientManager
     * @return 结果
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(ClientManager<IMqttAsyncClient, MqttConnectOptions> clientManager) {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientManager);
        handler.setAsync(true);
        return handler;
    }

    /**
     * mqtt v3 出站通道
     *
     * @return 结果
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * mqtt v3 入站通道
     *
     * @return 结果
     */
    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    /**
     * 错误通道
     *
     * @return 结果
     */
    @Bean
    public MessageChannel errorChannel() {
        return new DirectChannel();
    }
}
