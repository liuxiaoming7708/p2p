package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.PlatformBankInfo;
import com.payease.p2p.business.qo.PlatformBankInfoQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PlatformBankInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PlatformBankInfo record);

    PlatformBankInfo selectByPrimaryKey(Long id);

    List<PlatformBankInfo> selectAll();

    int updateByPrimaryKey(PlatformBankInfo record);

    int queryForCount(PlatformBankInfoQueryObject qo);

    List<PlatformBankInfo> query(PlatformBankInfoQueryObject qo);
}