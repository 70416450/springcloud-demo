package com.tzy.testribbon;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
 
import cn.hutool.core.util.NetUtil;

@SpringBootApplication
@EnableEurekaClient
//@EnableDiscoveryClient表示用于发现eureka 注册中心的微服务。
@EnableDiscoveryClient
public class ProductViewServiceRibbonApplication {
 
    public static void main(String[] args) {
        int port = 8010;
        if(!NetUtil.isUsableLocalPort(port)) {
            System.err.printf("端口%d被占用了，无法启动%n", port );
            System.exit(1);
        }
        new SpringApplicationBuilder(ProductViewServiceRibbonApplication.class).run(args);
 
    }
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
     
}