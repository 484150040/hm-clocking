package com.hm.digital.clocking.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hm.digital.inface.entity.Config;

@FeignClient(value = "digital-nacos", path = "/digital")
public interface ConfigsFeignBiz {

  @RequestMapping("/configList")
  List<Config> configList(@RequestBody Config config);

  @GetMapping(value = "/save")
  void save(@RequestBody Config config);
}