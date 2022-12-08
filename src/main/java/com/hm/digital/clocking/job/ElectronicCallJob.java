package com.hm.digital.clocking.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.hm.digital.clocking.biz.ElectronicCallService;
import com.hm.digital.clocking.entity.ElectronicCall;
import com.hm.digital.common.utils.HttpClientUtil;

public class ElectronicCallJob extends BaseJob {

  @Value("${zh.electronicCall}")
  private String electronicCall;

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
