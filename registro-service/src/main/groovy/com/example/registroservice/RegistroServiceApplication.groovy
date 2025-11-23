package com.example.registroservice

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
class RegistroServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(RegistroServiceApplication.class, args)
    }

}
