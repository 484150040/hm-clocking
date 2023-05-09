package com.hm.digital.clocking.controller;


import java.util.HashMap;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hm.digital.clocking.jobdata.JobDetails;
import com.hm.digital.clocking.manager.QuartzManager;

import com.hm.digital.common.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.bind.annotation.*;


/**
 * 定时任务
 *
 * @author pdai
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/job")
public class JobController {

  @Autowired
  private QuartzManager qtzManager;

  @SuppressWarnings("unchecked")
  private static Class<? extends QuartzJobBean> getClass(String classname) throws Exception {
    Class<?> class1 = Class.forName(classname);
    return (Class<? extends QuartzJobBean>) class1;
  }

  /**
   * 增加定时任务
   *
   * @param jobClassName
   * @param jobGroupName
   * @param cronExpression
   * @throws Exception
   */
  @RequestMapping(value = "/addjob")
  public ResultData addjob(@RequestParam(value = "jobClassName") String jobClassName,
                           @RequestParam(value = "jobGroupName") String jobGroupName,
                           @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
    try{
      qtzManager.addOrUpdateJob(getClass(jobClassName), jobClassName, jobGroupName, cronExpression);
    }catch (Exception e){
      e.printStackTrace();
      return ResultData.error(500,"系统停止请检查!");
    }
    return ResultData.success();
  }

  /**
   * 暂停定时任务
   *
   * @param jobClassName
   * @param jobGroupName
   * @throws Exception
   */
  @PostMapping(value = "/pausejob")
  public ResultData pausejob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName) {
    try{
      qtzManager.pauseJob(jobClassName, jobGroupName);
    }catch (Exception e){
      e.printStackTrace();
      return ResultData.error(500,"系统停止请检查!");
    }
    return ResultData.success();
  }

  /**
   * 重新开始定时任务
   *
   * @param jobClassName
   * @param jobGroupName
   * @throws Exception
   */
  @PostMapping(value = "/resumejob")
  public ResultData resumejob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName) {
    try{
      qtzManager.resumeJob(jobClassName, jobGroupName);
    }catch (Exception e){
      e.printStackTrace();
      return ResultData.error(500,"系统停止请检查");
    }
    return ResultData.success();
  }

  /**
   * 重新安排定时任务
   *
   * @param jobClassName
   * @param jobGroupName
   * @param cronExpression
   * @throws Exception
   */
  @PostMapping(value = "/reschedulejob")
  public ResultData rescheduleJob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName,
      @RequestParam(value = "cronExpression") String cronExpression){
    try {
      qtzManager.addOrUpdateJob(getClass(jobClassName), jobClassName, jobGroupName, cronExpression);
    } catch (Exception e) {
      e.printStackTrace();
      return ResultData.error(500,"系统停止请检查！");
    }
    return ResultData.success();
  }

  /**
   * 删除定时任务
   *
   * @param jobClassName
   * @param jobGroupName
   * @throws Exception
   */
  @RequestMapping(value = "/deletejob")
  public ResultData deletejob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName) {
    try {
      qtzManager.deleteJob(jobClassName, jobGroupName);
    } catch (Exception e) {
      e.printStackTrace();
      return ResultData.error(500,"系统停止请检查！");
    }
    return ResultData.success();
  }

  /**
   * 查询定时任务
   *
   * @param pageNum
   * @param pageSize
   * @return
   */
  @GetMapping(value = "/queryjob")
  public Map<String, Object> queryjob(@RequestParam(value = "pageNum") Integer pageNum,
      @RequestParam(value = "pageSize") Integer pageSize) {
    PageInfo<JobDetails> jobAndTrigger = qtzManager.queryAllJobBean(pageNum, pageSize);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("JobAndTrigger", jobAndTrigger);
    map.put("number", jobAndTrigger.getTotal());
    return map;
  }
}
