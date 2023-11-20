/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rexel.mqtt.source.xml;

import org.springframework.integration.config.xml.AbstractIntegrationNamespaceHandler;

/**
 * The namespace handler for the MqttAdapter namespace.
 *
 * @author Gary Russell
 * @since 4.0
 */
public class MqttNamespaceHandler extends AbstractIntegrationNamespaceHandler {

    @Override
    public void init() {
        this.registerBeanDefinitionParser("message-driven-channel-adapter", new MqttMessageDrivenChannelAdapterParser());
        this.registerBeanDefinitionParser("outbound-channel-adapter", new MqttOutboundChannelAdapterParser());
    }

}
