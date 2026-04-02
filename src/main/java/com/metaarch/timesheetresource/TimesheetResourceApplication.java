package com.metaarch.timesheetresource;

import com.metaarch.timesheetresource.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class TimesheetResourceApplication {

  public static void main(String[] args) {
    SpringApplication.run(TimesheetResourceApplication.class, args);
  }
}
