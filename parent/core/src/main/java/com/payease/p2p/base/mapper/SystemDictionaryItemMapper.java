package com.payease.p2p.base.mapper;

import com.payease.p2p.base.domain.SystemDictionaryItem;
import com.payease.p2p.base.query.SystemDictionaryQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SystemDictionaryItemMapper {

    int insert(SystemDictionaryItem record);

    SystemDictionaryItem selectByPrimaryKey(Long id);

    List<SystemDictionaryItem> selectByParentSn(String sn);

    int updateByPrimaryKey(SystemDictionaryItem record);

    int queryForCount(SystemDictionaryQueryObject qo);

    List<SystemDictionaryItem> query(SystemDictionaryQueryObject qo);
}