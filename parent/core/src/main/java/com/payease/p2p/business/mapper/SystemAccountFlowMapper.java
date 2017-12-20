package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.SystemAccountFlow;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemAccountFlowMapper {

    int insert(SystemAccountFlow record);

    SystemAccountFlow selectByPrimaryKey(Long id);

}