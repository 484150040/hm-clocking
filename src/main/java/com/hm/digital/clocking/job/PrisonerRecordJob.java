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
import com.dahuatech.hutool.json.JSONArray;
import com.dahuatech.hutool.json.JSONUtil;
import com.hm.digital.clocking.feign.ConfigsFeignBiz;
import com.hm.digital.common.enums.ConfigEnum;
import com.hm.digital.inface.biz.ConfigsService;
import com.hm.digital.inface.biz.PrisonerRecordService;
import com.hm.digital.inface.entity.Config;
import com.hm.digital.inface.entity.PrisonerRecord;
import com.hm.digital.common.enums.InputParameterEnum;
import com.hm.digital.common.utils.ChineseCharacterUtil;
import com.hm.digital.common.utils.HttpClientUtil;


public class PrisonerRecordJob extends BaseJob {

  @Autowired
  private PrisonerRecordService prisonerRecordService;

//  @Value("${zh.httpGetList}")
  private static String httpGetList;

  @Autowired
  public ConfigsFeignBiz configsFeignBiz;
  @PostConstruct
  public void init() {
    try {
      Config config = configsFeignBiz.configList(getCofig(ConfigEnum.ZH_HTTPGETLIST.getKey())).get(0);
      if (config.getStatus()<2){
        config.setStatus(2);
        configsFeignBiz.save(config);
      }
      httpGetList =  config.getValue();
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

  @Override
  protected void toTiming(JobExecutionContext context) {
    List<PrisonerRecord> list = new ArrayList<>();
    String prisonId = context.getJobDetail().getKey().toString().split("c")[0].replace(".", "");
    //正提讯列表
    getPrisonerRecord(list, prisonId, InputParameterEnum.NOW_ARRAIGN_LIST.getKey(), "5000");
    //正入所列表
    getPrisonerRecord(list, prisonId, InputParameterEnum.NOW_TODAY_ENTRANCE_PRISON_LIST.getKey(), "5000");
    //羁押码红色列表
    getPrisonerRecord(list, prisonId, InputParameterEnum.DETENTION_RED_LIST.getKey(), "5000");
    //羁押码黄色列表
    getPrisonerRecord(list, prisonId, InputParameterEnum.DETENTION_YELLOW_LIST.getKey(), "5000");
    //今日监室违规列表
    getPrisonerRecord(list, prisonId, InputParameterEnum.TODAY_PRISONER_BREAK_RULE_LIST.getKey(), "5000");

    prisonerRecordService.insertAll(list);
  }
  /**
   * 获取综合数据
   *
   * @param list
   * @param schema
   * @param prisonId
   */
  private void getPrisonerRecord(List<PrisonerRecord> list, String prisonId, String... schema) {
    Map<String, String> parametersRed = new HashMap<>();
    parametersRed.put("item", schema[0]);
    parametersRed.put("prisonId", prisonId);
    if (schema.length > 1) {
      parametersRed.put("limit", schema[1]);
    }
    String responseRed = HttpClientUtil.sendGet(httpGetList, parametersRed);
    Map mRed = JSONUtil.parseObj(responseRed);
    JSONArray dRed = (JSONArray) mRed.get("list");
    for (Object o : dRed) {
      Map ms = JSONUtil.parseObj(o);
      Map<String, Object> map = new HashMap<>();
      for (Object key : ms.keySet()) {
        map.put(ChineseCharacterUtil.getFirstSpell(String.valueOf(key), true), ms.get(key));
      }
      map.put("requiredParameter", schema[0]);
      list.add(JSONObject.parseObject(JSONObject.toJSONString(map), PrisonerRecord.class));
    }
  }

}
