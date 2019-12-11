package com.qf.shop_searchservice;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@DubboComponentScan("com.qf.service")
@SpringBootApplication(scanBasePackages = "com.qf")
public class ShopSearchserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopSearchserviceApplication.class, args);
    }

}
