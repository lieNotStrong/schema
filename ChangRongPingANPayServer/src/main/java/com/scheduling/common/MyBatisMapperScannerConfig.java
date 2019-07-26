package com.scheduling.common;


import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Configuration;


@Configuration
@org.springframework.boot.autoconfigure.AutoConfigureAfter({MyBatisConfig.class})
public class MyBatisMapperScannerConfig {
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfig = new MapperScannerConfigurer();

        mapperScannerConfig.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return mapperScannerConfig;
    }
}

