package com.zcloud.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 说明：字符编码
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
@Configuration
public class EncodingFilterConfig {
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> filterRegistrationBean() {
        FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<CharacterEncodingFilter>();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("UTF-8");
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }
}
