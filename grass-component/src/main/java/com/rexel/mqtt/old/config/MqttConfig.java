//package com.rexel.mqtt.old.config;
//
//import com.rexel.mqtt.old.factory.DefaultMqttPahoClientFactory;
//import com.rexel.mqtt.old.factory.MqttPahoClientFactory;
//import lombok.Data;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConfigurationProperties(prefix = "mqtt")
//@Data
//public class MqttConfig {
//
//    private String[] serverUrl;
//    private String username;
//    private char[] password;
//    private int keepAliveInterval;
//    private int connectionTimeout;
//    private int maxInFlight;
//
//    @Bean
//    public MqttPahoClientFactory mqttClientFactory() {
//        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setServerURIs(serverUrl);
//        options.setUserName(username);
//        options.setPassword(password);
//        options.setCleanSession(true);
//        options.setAutomaticReconnect(true);
//        options.setConnectionTimeout(connectionTimeout);
//        options.setKeepAliveInterval(keepAliveInterval);
//        options.setMaxInflight(maxInFlight);
//        factory.setConnectionOptions(options);
//        return factory;
//    }
//}
