package com.hm.digital.clocking.job;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.hm.digital.clocking.feign.MassageFeignBiz;

public class RollCallJob extends BaseJob {

  @Value("${zh.electronicCall}")
  private String electronicCall;

  @Autowired
  private MassageFeignBiz massageFeignBiz;

  @Override
  protected void toTiming(JobExecutionContext context) {
    String prisonId = context.getJobDetail().getKey().toString().split("c")[0].replace(".", "");
    massageFeignBiz.findByPrisonIdAndType(prisonId);
  }


}
