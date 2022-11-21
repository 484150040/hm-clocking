package com.hm.digital.twin.biz.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hm.digital.twin.biz.ElectronicCallService;
import com.hm.digital.twin.entity.ElectronicCall;
import com.hm.digital.twin.mapper.ElectronicCallMapper;

@Service
public class ElectronicCallServiceimpl implements ElectronicCallService {

  @Autowired
  private ElectronicCallMapper electronicCallMapper;

  @Override
  public List<ElectronicCall> findAll(Specification<ElectronicCall> toSpec) {
    return electronicCallMapper.findAll(toSpec);
  }

  @Override
  public List<ElectronicCall> insertAll(List<ElectronicCall> list) {
    electronicCallMapper.deleteAll();
    list.forEach(electronicCall -> {
      electronicCall.setCreateTime(new Date());
      electronicCall.setDeleted(0);
    });
    return electronicCallMapper.saveAll(list);
  }
}
