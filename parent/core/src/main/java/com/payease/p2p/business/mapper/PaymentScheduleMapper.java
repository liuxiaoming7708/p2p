package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.PaymentSchedule;
import com.payease.p2p.business.qo.PaymentScheduleQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentScheduleMapper {

    int insert(PaymentSchedule record);

    PaymentSchedule selectByPrimaryKey(Long id);

    int updateByPrimaryKey(PaymentSchedule record);

    int queryForCount(PaymentScheduleQueryObject qo);

    List<PaymentSchedule> query(PaymentScheduleQueryObject qo);
}