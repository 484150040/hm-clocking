package com.hm.digital.clocking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.hm.digital.inface.mapper")
@EntityScan(basePackages = "com.hm.digital.inface.entity")
public class ClocakingApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClocakingApplication.class, args);
  }
}

