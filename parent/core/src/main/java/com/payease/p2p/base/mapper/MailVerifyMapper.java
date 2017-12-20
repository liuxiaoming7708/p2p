package com.payease.p2p.base.mapper;

import com.payease.p2p.base.domain.MailVerify;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MailVerifyMapper {

    int insert(MailVerify record);

    MailVerify selectByUUID(String uuid);

    List<MailVerify> selectAll();

}