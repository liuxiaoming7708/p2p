package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.MoneyWithdraw;
import com.payease.p2p.business.qo.MoneyWithdrawQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyWithdrawMapper {

    int insert(MoneyWithdraw record);

    MoneyWithdraw selectByPrimaryKey(Long id);

    int updateByPrimaryKey(MoneyWithdraw record);

    int queryForCount(MoneyWithdrawQueryObject qo);

    List<MoneyWithdraw> query(MoneyWithdrawQueryObject qo);
}