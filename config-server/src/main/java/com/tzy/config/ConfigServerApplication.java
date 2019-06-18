package com.tzy.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
 
import cn.hutool.core.util.NetUtil;
 
@SpringBootApplication
@EnableEurekaClient
//@EnableConfigServer 配置服务。
@EnableConfigServer
//@EnableDiscoveryClient表示用于发现eureka 注册中心的微服务。
@EnableDiscoveryClient
public class ConfigServerApplication {
    public static void main(String[] args) {
        int port = 8030;
        if(!NetUtil.isUsableLocalPort(port)) {
            System.err.printf("端口%d被占用了，无法启动%n", port );
            System.exit(1);
        }
        new SpringApplicationBuilder(ConfigServerApplication.class).run(args);
     
    }
}