
package com;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"com.scheduling.dao"})
@Configuration
@EnableTransactionManagement
@ComponentScan(value = {"com.scheduling.*","com.scheduling.common.*","com.scheduling.config","com.scheduling.filter"})
public class Application {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(Application.class, args);
    }
}
