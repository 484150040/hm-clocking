package com.hm.digital.clocking.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.hm.digital.clocking.feign.ConfigsFeignBiz;
import com.hm.digital.common.enums.ConfigEnum;
import com.hm.digital.inface.biz.ConfigsService;
import com.hm.digital.inface.biz.StatisticalService;
import com.hm.digital.clocking.dto.StatisticalDto;
import com.hm.digital.inface.entity.Config;
import com.hm.digital.inface.entity.Statistical;
import com.hm.digital.common.enums.InputParameterEnum;
import com.hm.digital.common.utils.DateUtils;
import com.hm.digital.common.utils.HttpClientUtil;

import static com.hm.digital.common.utils.DateUtils.getEndOfYesterday;

public class StatisticalJob extends BaseJob{

  @Autowired
  private StatisticalService statisticalService;

//  @Value("${zh.httpGetChart}")
  private static String httpGetChart;

//  @Value("${zh.alarmOrder}")
  private static String alarmOrder;
  
//  @Value("${zh.startTime}")
  private static String startTime;

//  @Value("${zh.httpGetChartOrecle}")
  private static String httpGetChartOrecle;


  @Autowired
  public ConfigsFeignBiz configsFeignBiz;
  @PostConstruct
  public void init() {
    try {
      Config config = configsFeignBiz.configList(getCofig(ConfigEnum.ZH_HTTPGETCHART.getKey())).get(0);
      Config config1 = configsFeignBiz.configList(getCofig(ConfigEnum.ZH_ALARMORDER.getKey())).get(0);
      Config config2 = configsFeignBiz.configList(getCofig(ConfigEnum.ZH_STARTTIME.getKey())).get(0);
      Config config3 = configsFeignBiz.configList(getCofig(ConfigEnum.ZH_HTTPGETCHARTORECLE.getKey())).get(0);
      if (config.getStatus()<2){
        config.setStatus(2);
        configsFeignBiz.save(config);
      }
      if (config1.getStatus()<2){
        config1.setStatus(2);
        configsFeignBiz.save(config1);
      }
      if (config2.getStatus()<2){
        config2.setStatus(2);
        configsFeignBiz.save(config2);
      }
      if (config3.getStatus()<2){
        config3.setStatus(2);
        configsFeignBiz.save(config3);
      }
      httpGetChart =  config.getValue();
      alarmOrder =  config1.getValue();
      startTime =  config2.getValue();
      httpGetChartOrecle =  config3.getValue();
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
    List<Statistical> list = new ArrayList<>();
    String prisonId = context.getJobDetail().getKey().toString().split("c")[0].replace(".", "");
    List<Statistical> isList = statisticalService.selectAll();
    if (CollectionUtils.isEmpty(isList)){
      addStatisticalList(list,prisonId,startTime);
    }else {
      addStatisticalList(list,prisonId,DateUtils.format(getEndOfYesterday(new Date()), DateUtils.DATE_FORMAT));
    }
    statisticalService.insertAll(list);
  }
  /**
   * 封装数据存入list集合中
   *
   * @param list
   * @param prisonId
   * @param time
   */
  private void addStatisticalList(List<Statistical> list, String prisonId, String time) {
    //每日民警进监情况
    getStatisticalJob(httpGetChart,list,prisonId, InputParameterEnum.TODAY_CIVILIAN_POLICE_IN_PRISONB_CHAR.getKey(),time);
    //每月民警进监情况
    getStatisticalJob(httpGetChart,list,prisonId,InputParameterEnum.MONTH_CIVILIAN_POLICE_IN_PRISONB_CHAR.getKey(),time);
    //每季度民警进监情况
    getStatisticalJob(httpGetChart,list,prisonId,InputParameterEnum.QUARTER_CIVILIAN_POLICE_IN_PRISONB_CHAR.getKey(),time);
    //每日各巡视点巡视时长
    getStatisticalJob(httpGetChart,list,prisonId,InputParameterEnum.TODAY_INSPECTION_DURATION_CHAR.getKey(),time);
    //每月各巡视点巡视时长
    getStatisticalJob(httpGetChart,list,prisonId,InputParameterEnum.MONTH_INSPECTION_DURATION_CHAR.getKey(),time);
    //每季度各巡视点巡视时长
    getStatisticalJob(httpGetChart,list,prisonId,InputParameterEnum.QUARTER_INSPECTION_DURATION_CHAR.getKey(),time);
    //每日医疗预约
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.TODAY_MEDICAL_APPOINTMENT.getKey(),time);
    //每月医疗预约
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.MONTH_MEDICAL_APPOINTMENT.getKey(),time);
    //每季度医疗预约
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.QUARTER_MEDICAL_APPOINTMENT.getKey(),time);
    //每日押量
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.TODAY_DORM_CODE_IN_PRISON.getKey(),time);
    //每月押量
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.MONTH_DORM_CODE_IN_PRISON.getKey(),time);
    //每季度押量
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.QUARTER_DORM_CODE_IN_PRISON.getKey(),time);
    //设备报警数量分析季度
    getStatisticalJob(alarmOrder,list,prisonId,"1",time);
    //设备报警数量分析月
    getStatisticalJob(alarmOrder,list,prisonId,"2",time);
    //设备报警数量分析日
    getStatisticalJob(alarmOrder,list,prisonId,"3",time);
    //监室交互

    //每日民警进监情况
    getStatisticalJob(httpGetChart,list,prisonId, InputParameterEnum.DORMCODE_TODAY_CIVILIAN_POLICE_IN_PRISONB_CHAR.getKey(),time);
    //每月民警进监情况
    getStatisticalJob(httpGetChart,list,prisonId,InputParameterEnum.DORMCODE_MONTH_CIVILIAN_POLICE_IN_PRISONB_CHAR.getKey(),time);
    //每季度民警进监情况
    getStatisticalJob(httpGetChart,list,prisonId,InputParameterEnum.DORMCODE_QUARTER_CIVILIAN_POLICE_IN_PRISONB_CHAR.getKey(),time);
    //每日医疗预约
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMCODE_TODAY_MEDICAL_APPOINTMENT.getKey(),time);
    //每月医疗预约
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMCODE_MONTH_MEDICAL_APPOINTMENT.getKey(),time);
    //每季度医疗预约
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMCODE_QUARTER_MEDICAL_APPOINTMENT.getKey(),time);
    //每日押量
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMCODE_TODAY_DORM_CODE_IN_PRISON.getKey(),time);
    //每月押量
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMCODE_MONTH_DORM_CODE_IN_PRISON.getKey(),time);
    //每季度押量
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMCODE_QUARTER_DORM_CODE_IN_PRISON.getKey(),time);

    //监区交互

    //每日民警进监情况
    getStatisticalJob(httpGetChart,list,prisonId, InputParameterEnum.DORMAREA_TODAY_CIVILIAN_POLICE_IN_PRISONB_CHAR.getKey(),time);
    //每月民警进监情况
    getStatisticalJob(httpGetChart,list,prisonId,InputParameterEnum.DORMAREA_MONTH_CIVILIAN_POLICE_IN_PRISONB_CHAR.getKey(),time);
    //每季度民警进监情况
    getStatisticalJob(httpGetChart,list,prisonId,InputParameterEnum.DORMAREA_QUARTER_CIVILIAN_POLICE_IN_PRISONB_CHAR.getKey(),time);
    //每日医疗预约
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMAREA_TODAY_MEDICAL_APPOINTMENT.getKey(),time);
    //每月医疗预约
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMAREA_MONTH_MEDICAL_APPOINTMENT.getKey(),time);
    //每季度医疗预约
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMAREA_QUARTER_MEDICAL_APPOINTMENT.getKey(),time);
    //每日押量
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMAREA_TODAY_DORM_CODE_IN_PRISON.getKey(),time);
    //每月押量
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMAREA_MONTH_DORM_CODE_IN_PRISON.getKey(),time);
    //每季度押量
    getStatisticalJob(httpGetChartOrecle,list,prisonId,InputParameterEnum.DORMAREA_QUARTER_DORM_CODE_IN_PRISON.getKey(),time);

    //重点人员- 风险人员
    getStatisticalJob(httpGetChart,list,prisonId,InputParameterEnum.RISKLEVEL_PRISONER_CHART.getKey(),time);
  }
  /**
   * 获取综合数据
   *
   * @param list
   * @param schema
   * @param prisonId
   */
  private void getStatisticalJob(String httpGetCharts,List<Statistical> list, String prisonId, String... schema) {
    Map<String, String> parametersRed = new HashMap<>();
    if (schema[0].equals("1")||schema[0].equals("2")||schema[0].equals("3")){
      parametersRed.put("type", schema[0]);
    }else {
      parametersRed.put("item", schema[0]);
    }
    parametersRed.put("prisonId", prisonId);
    parametersRed.put("endTime", DateUtils.format(new Date(),DateUtils.DATE_FORMAT));
    if (schema.length > 1) {
      parametersRed.put("startTime", schema[1]);
    }
    String responseRed = HttpClientUtil.sendGet(httpGetCharts, parametersRed);
    List<StatisticalDto> lists = JSONObject.parseArray(responseRed,StatisticalDto.class);
    lists.forEach(statistical->{
      if (!StringUtils.isEmpty(statistical.getPgzb())){
        statistical.setNAME(statistical.getPgzb());
      }
      statistical.setPrisonId(prisonId);
      statistical.setRequiredParameter(schema[0]);
    });
    list.addAll(lists);
  }
}
