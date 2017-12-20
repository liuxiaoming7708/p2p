package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.PaymentScheduleDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentScheduleDetailMapper {

    int insert(PaymentScheduleDetail record);

    PaymentScheduleDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKey(PaymentScheduleDetail record);
}