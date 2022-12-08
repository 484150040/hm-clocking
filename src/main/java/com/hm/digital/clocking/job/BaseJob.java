package com.hm.digital.clocking.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseJob extends QuartzJobBean {



  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    log.info("定时任务开始");
    toTiming(context);
    log.info("定时任务结束");
  }
  /**
   * 执行定时业务逻辑.
   *
   * @param context 定时任务上下文
   */
  protected abstract void toTiming(JobExecutionContext context);
}
