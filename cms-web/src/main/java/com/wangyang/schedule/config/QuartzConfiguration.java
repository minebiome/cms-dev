package com.wangyang.schedule.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

     @Bean
      public Scheduler scheduler(){
          Scheduler scheduler =null;
          try {
              scheduler = StdSchedulerFactory.getDefaultScheduler();
          } catch (SchedulerException e) {
              e.printStackTrace();
          }
          return scheduler;
      }
}
