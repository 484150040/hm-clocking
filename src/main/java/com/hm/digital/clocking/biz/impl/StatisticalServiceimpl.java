package com.hm.digital.clocking.biz.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hm.digital.clocking.biz.StatisticalService;
import com.hm.digital.clocking.entity.Statistical;
import com.hm.digital.clocking.mapper.StatisticalMapper;
import com.hm.digital.clocking.vo.StatisticalVO;

import static com.hm.digital.common.utils.DateUtils.getEndOfYesterday;

@Service
public class StatisticalServiceimpl implements StatisticalService {

  @Autowired
  private StatisticalMapper statisticalMapper;

  @Override
  public void insertAll(List<Statistical> list) {
    StatisticalVO statisticalVO = new StatisticalVO();
    statisticalVO.setStartTime(getEndOfYesterday(new Date()));
    statisticalVO.setEndrTime(new Date());
    Page<Statistical> statisticals = statisticalMapper.findAll(statisticalVO.toSpec(), statisticalVO.toPageable());
    if (statisticals.getTotalPages()>0){
      statisticalMapper.deleteAll(statisticals);
    }
    list.forEach(statistical->{
      statistical.setDeleted(0);
      statistical.setCreateTime(new Date());
      statisticalMapper.save(statistical);
    });
  }
  @Override
  public List<Statistical> selectAll() {
    return statisticalMapper.findAll();
  }
  @Override
  public Page<Statistical> statisticalPage() {
    StatisticalVO statisticalVO = new StatisticalVO();
    statisticalVO.setStartTime(getEndOfYesterday(new Date()));
    statisticalVO.setEndrTime(new Date());
    Page<Statistical> statisticals = statisticalMapper.findAll(statisticalVO.toSpec(), statisticalVO.toPageable());
    return statisticals;
  }
  @Override
  public List<Statistical> findAll(Specification<Statistical> toSpec) {
    return statisticalMapper.findAll(toSpec);
  }
}
