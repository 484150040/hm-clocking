package com.hm.digital.clocking.controller;


import java.util.HashMap;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hm.digital.clocking.jobdata.JobDetails;
import com.hm.digital.clocking.manager.QuartzManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 定时任务
 *
 * @author pdai
 */
@RestController
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
  @PostMapping(value = "/addjob")
  public void addjob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName,
      @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
    qtzManager.addOrUpdateJob(getClass(jobClassName), jobClassName, jobGroupName, cronExpression);
  }

  /**
   * 暂停定时任务
   *
   * @param jobClassName
   * @param jobGroupName
   * @throws Exception
   */
  @PostMapping(value = "/pausejob")
  public void pausejob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
    qtzManager.pauseJob(jobClassName, jobGroupName);
  }

  /**
   * 重新开始定时任务
   *
   * @param jobClassName
   * @param jobGroupName
   * @throws Exception
   */
  @PostMapping(value = "/resumejob")
  public void resumejob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
    qtzManager.resumeJob(jobClassName, jobGroupName);
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
  public void rescheduleJob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName,
      @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
    qtzManager.addOrUpdateJob(getClass(jobClassName), jobClassName, jobGroupName, cronExpression);
  }

  /**
   * 删除定时任务
   *
   * @param jobClassName
   * @param jobGroupName
   * @throws Exception
   */
  @PostMapping(value = "/deletejob")
  public void deletejob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
    qtzManager.deleteJob(jobClassName, jobGroupName);
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
