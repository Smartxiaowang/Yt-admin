package com.zcloud.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 说明：第一数据源配置
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
@Configuration
@MapperScan(basePackages = MasterDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "masterSqlSessionFactory") //扫描 Mapper 接口并容器管理
public class MasterDataSourceConfig {

    static final String PACKAGE = "com.zcloud.mapper.datasource";								//master 目录
    static final String MAPPER_LOCATION = "classpath:mybatis/datasource/*/*.xml";			//扫描的 xml 目录
    static final String CONFIG_LOCATION = "classpath:mybatis/datasource/mybatis-config.xml"; //自定义的mybatis config 文件位置
    static final String TYPE_ALIASES_PACKAGE = "com.zcloud.entity"; 						//扫描的 实体类 目录
 
    @Value("${datasource.no1.url}")
    private String url;
 
    @Value("${datasource.no1.username}")
    private String user;
 
    @Value("${datasource.no1.password}")
    private String password;
 
    @Value("${datasource.no1.driver-class-name}")
    private String driverClass;
 
    @Bean(name = "masterDataSource")
    @Primary
    public DataSource masterDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
 
    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }
 
    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource)throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MasterDataSourceConfig.MAPPER_LOCATION));
        sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(MasterDataSourceConfig.CONFIG_LOCATION));
        sessionFactory.setTypeAliasesPackage(MasterDataSourceConfig.TYPE_ALIASES_PACKAGE);
        return sessionFactory.getObject();
    }
}
