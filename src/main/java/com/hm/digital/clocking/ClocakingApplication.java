package com.hm.digital.clocking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class ClocakingApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClocakingApplication.class, args);
  }
}

