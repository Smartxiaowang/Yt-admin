package com.zcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 说明：启动类 
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
@SpringBootApplication//去除冲突 
@MapperScan(value = {"com.zcloud.mapper","com.zcloud.newsbt"})
@EnableCaching
@EnableScheduling
public class FHmainApplication {

	public static void main(String[] args) {
		SpringApplication.run(FHmainApplication.class, args);
	}

}