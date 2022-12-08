package com.hm.digital.clocking.jobdata;

import java.util.Date;

import lombok.Data;

/**
 * @author pdai
 *
 */
@Data
public class JobDetails {

  /**
   * cron表达式
   */
  private String cronExpression;

  /**
   * 工作名称
   */
  private String jobClassName;

  /**
   * 组名
   */
  private String triggerGroupName;

  /**
   * 触发器名称
   */
  private String triggerName;

  /**
   * 工作组名称
   */
  private String jobGroupName;

  /**
   * 工作名称
   */
  private String jobName;

  /**
   * 下次执行日期
   */
  private Date nextFireTime;

  /**
   * 之前执行日期
   */
  private Date previousFireTime;

  /**
   * 开始时间
   */
  private Date startTime;

  /**
   * 时间周期
   */
  private String timeZone;

  /**
   * 状态
   */
  private String status;
}