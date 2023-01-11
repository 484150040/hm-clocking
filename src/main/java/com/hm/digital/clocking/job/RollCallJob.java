package com.hm.digital.clocking.job;

import javax.annotation.PostConstruct;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.hm.digital.clocking.feign.MassageFeignBiz;
import com.hm.digital.common.enums.ConfigEnum;
import com.hm.digital.inface.biz.ConfigsService;
import com.hm.digital.inface.entity.Config;

public class RollCallJob extends BaseJob {

//  @Value("${zh.electronicCall}")
  private static String electronicCall;

  @Autowired
  private MassageFeignBiz massageFeignBiz;

  @Autowired
  public ConfigsService configsServices;
  @PostConstruct
  public void init() {
    electronicCall =  configsServices.getValue(getCofig(ConfigEnum.ZH_ELECTRONICCALL.getKey())).get(0).getValue();
  }

  private Config getCofig(String config) {
    Config configVO = new Config();
    configVO.setType(config);
    configVO.setUniverse("1");
    return configVO;
  }


  @Override
  protected void toTiming(JobExecutionContext context) {
    String prisonId = context.getJobDetail().getKey().toString().split("c")[0].replace(".", "");
    massageFeignBiz.findByPrisonIdAndType(prisonId);
  }


}
