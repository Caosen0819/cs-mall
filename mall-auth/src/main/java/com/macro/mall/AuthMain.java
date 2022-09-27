package com.macro.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author Caosen
 * @Date 2022/9/7 16:07
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = "com.macro.mall")
@EnableFeignClients
@EnableDiscoveryClient
public class AuthMain {

    public static void main(String[] args) {
        SpringApplication.run(AuthMain.class, args);
    }
}
