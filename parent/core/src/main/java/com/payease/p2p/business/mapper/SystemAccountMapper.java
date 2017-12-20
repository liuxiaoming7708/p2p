package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.SystemAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemAccountMapper {

    int insert(SystemAccount record);

    SystemAccount selectCurrent();

    int updateByPrimaryKey(SystemAccount record);
}