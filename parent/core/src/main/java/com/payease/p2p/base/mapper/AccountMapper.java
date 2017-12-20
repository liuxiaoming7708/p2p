package com.payease.p2p.base.mapper;

import com.payease.p2p.base.domain.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMapper {

    int insert(Account record);

    Account selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Account record);
}