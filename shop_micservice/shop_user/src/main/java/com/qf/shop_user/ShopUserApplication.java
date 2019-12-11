package com.qf.shop_user;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@DubboComponentScan("com.qf.serviceimpl")
@MapperScan("com.qf.dao")
@EnableTransactionManagement
public class ShopUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopUserApplication.class, args);
    }

}
