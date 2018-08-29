package com.hisense.springboot.config;

import com.hisense.springboot.job.EveryDayQuatz;
import com.hisense.springboot.job.TestQuartz;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    //号牌统计
    @Bean
    public JobDetail testQuartzDetail(){
        return JobBuilder.newJob(TestQuartz.class).withIdentity("testQuartz").storeDurably().build();
    }
    //每日运行job
    @Bean
    public JobDetail everyDayQuartzDetail(){
        return JobBuilder.newJob(EveryDayQuatz.class).withIdentity("everyDayQuartz").storeDurably().build();
    }

    @Bean
    public Trigger testQuartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(1)  //设置时间周期单位分钟
                //.withIntervalInSeconds(5)
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(testQuartzDetail())
                .withIdentity("testQuartz")
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public  Trigger everyDayQuartz() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                //.withIntervalInMinutes(10)  //设置时间周期单位分钟
                .withIntervalInHours(24)//设置为一天
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(everyDayQuartzDetail())
                .withIdentity("everyDayQuartz")
                .withSchedule(scheduleBuilder)
                .build();
    }

}