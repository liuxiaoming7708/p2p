package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.RechargeOffline;
import com.payease.p2p.business.qo.RechargeOfflineQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargeOfflineMapper {

    int insert(RechargeOffline record);

    RechargeOffline selectByPrimaryKey(Long id);

    int updateByPrimaryKey(RechargeOffline record);


    int queryForCount(RechargeOfflineQueryObject qo);

    List<RechargeOffline> query(RechargeOfflineQueryObject qo);
}