package com.hm.digital.clocking.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.hm.digital.clocking.feign.ConfigsFeignBiz;
import com.hm.digital.common.enums.ConfigEnum;
import com.hm.digital.inface.biz.ConfigsService;
import com.hm.digital.inface.biz.ElectronicCallService;
import com.hm.digital.inface.entity.Config;
import com.hm.digital.inface.entity.ElectronicCall;
import com.hm.digital.common.utils.HttpClientUtil;

public class ElectronicCallJob extends BaseJob {

  //  @Value("${zh.electronicCall}")
  private static String electronicCall;

  @Autowired
  public ConfigsFeignBiz configsFeight;
  @PostConstruct
  public void init() {
    try {
      Config config = configsFeight.configList(getCofig(ConfigEnum.ZH_ELECTRONICCALL.getKey())).get(0);
      if (config.getStatus()<2){
        config.setStatus(2);
        configsFeight.save(config);
      }
      electronicCall = config.getValue();
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  private Config getCofig(String config) {
    Config configVO = new Config();
    configVO.setType(config);
    configVO.setUniverse("1");
    return configVO;
  }


  @Autowired
  private ElectronicCallService electronicCallService;

  @Override
  protected void toTiming(JobExecutionContext context) {
    List<ElectronicCall> list = new ArrayList<>();
    String prisonId = context.getJobDetail().getKey().toString().split("c")[0].replace(".", "");
    getElectronicCallJob(list, prisonId, "即时");
    electronicCallService.insertAll(list);
  }


  /**
   * 获取综合数据
   *
   * @param list
   * @param schema
   * @param prisonId
   */
  private void getElectronicCallJob(List<ElectronicCall> list, String prisonId, String... schema) {
    Map<String, String> parametersRed = new HashMap<>();
    parametersRed.put("type", schema[0]);
    parametersRed.put("prisonId", prisonId);
    String responseRed = HttpClientUtil.sendGet(electronicCall, parametersRed);
    List<ElectronicCall> lists = JSONObject.parseArray(responseRed, ElectronicCall.class);
    list.addAll(lists);
  }
}
