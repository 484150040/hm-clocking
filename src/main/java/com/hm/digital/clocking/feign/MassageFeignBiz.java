package com.hm.digital.clocking.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hm.digital.common.utils.ResultData;

@FeignClient(value = "massage-nacos", path = "/massage")
public interface MassageFeignBiz {

  @GetMapping("/findByPrisonIdAndType")
  ResultData findByPrisonIdAndType(@RequestParam(value = "prisonId") String prisonId);

}
