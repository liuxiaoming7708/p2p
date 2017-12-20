package com.payease.p2p.base.mapper;

import com.payease.p2p.base.domain.Userinfo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserinfoMapper {

    int insert(Userinfo record);

    Userinfo selectByPrimaryKey(Long id);


    int updateByPrimaryKey(Userinfo record);
}