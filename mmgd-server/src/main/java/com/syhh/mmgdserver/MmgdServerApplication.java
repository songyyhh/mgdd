package com.syhh.mmgdserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MmgdServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(MmgdServerApplication.class, args);
  }

}
