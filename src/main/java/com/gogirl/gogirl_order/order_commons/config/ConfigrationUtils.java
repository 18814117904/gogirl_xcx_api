package com.gogirl.gogirl_order.order_commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Created by yinyong on 2018/9/17.
 */
@Configuration
public class ConfigrationUtils {

    @Autowired
    private RestTemplateBuilder builder;

    /**
     * 向容器中创建RestTemplate对象
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
