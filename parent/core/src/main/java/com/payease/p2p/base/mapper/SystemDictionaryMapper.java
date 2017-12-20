package com.payease.p2p.base.mapper;

import com.payease.p2p.base.domain.SystemDictionary;
import com.payease.p2p.base.query.SystemDictionaryQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SystemDictionaryMapper {

    int insert(SystemDictionary record);

    SystemDictionary selectByPrimaryKey(Long id);

    List<SystemDictionary> selectAll();

    int queryForCount(SystemDictionaryQueryObject qo);

    List<SystemDictionary> query(SystemDictionaryQueryObject qo);

    int updateByPrimaryKey(SystemDictionary record);
}