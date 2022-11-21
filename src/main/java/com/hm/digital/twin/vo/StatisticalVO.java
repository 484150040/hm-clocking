package com.hm.digital.twin.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.hm.digital.twin.entity.PrisonerRecord;
import com.hm.digital.twin.entity.Statistical;
import com.hm.digital.twin.query.BaseQuery;

import lombok.Data;

@Data
public class StatisticalVO extends BaseQuery<Statistical> {

  /**
   * 开始时间
   */
  private Date startTime;

  /**
   * 结束时间
   */
  private Date endrTime;

  @Override
  public Specification<Statistical> toSpec() {

    Specification<Statistical> spec = super.toSpecWithAnd();
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
