package com.hm.digital.clocking.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.hm.digital.inface.entity.PrisonerRecord;
import com.hm.digital.common.query.BaseQuery;

import lombok.Data;

@Data
public class PrisonerRecordVO extends BaseQuery<PrisonerRecord> {

  /**
   * 开始时间
   */
  private Date startTime;

  /**
   * 结束时间
   */
  private Date endrTime;

  @Override
  public Specification<PrisonerRecord> toSpec() {

    Specification<PrisonerRecord> spec = super.toSpecWithAnd();
    return ((root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicatesList = new ArrayList<>();
      predicatesList.add(spec.toPredicate(root, criteriaQuery, criteriaBuilder));
      if (startTime != null) {
        predicatesList.add(
            criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(
                    root.get("createTime"), startTime)));
      }
      if (endrTime != null) {
        predicatesList.add(
            criteriaBuilder.and(
                criteriaBuilder.lessThanOrEqualTo(
                    root.get("createTime"), endrTime)));
      }
      return criteriaBuilder.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
    });
  }

}
