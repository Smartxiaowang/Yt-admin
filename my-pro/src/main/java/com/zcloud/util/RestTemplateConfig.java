package com.zcloud.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	 //启动时需要实例化该类的一个实例
    @Autowired
    private RestTemplateBuilder builder;
    //使用RestTemplateBuilder来实例化RestTemplate对象,Spring已经默认注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate(){
        return builder.build();
    }
}
