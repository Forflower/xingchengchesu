package com.hisense.springboot.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class EveryDayQuatz extends QuartzJobBean {


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
       // System.err.println("第二个任务正在运行 is running…………………………………… ");
    }
}
