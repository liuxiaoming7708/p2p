package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.AccountFlow;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountFlowMapper {

    int insert(AccountFlow record);
}