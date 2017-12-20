package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.UserBankinfo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBankinfoMapper {

    int insert(UserBankinfo record);

    UserBankinfo selectByUser(Long userid);

}