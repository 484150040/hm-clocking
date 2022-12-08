package com.hm.digital.clocking.dto;

import com.hm.digital.clocking.entity.Statistical;

import lombok.Data;

@Data
public class StatisticalDto extends Statistical {

  /**
   * 风险名称
   */
  private String pgzb;
}
