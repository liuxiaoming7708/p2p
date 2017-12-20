package com.payease.p2p.base.mapper;

import com.payease.p2p.base.domain.UserFile;
import com.payease.p2p.base.query.UserFileQueryObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFileMapper {

    int insert(UserFile record);

    UserFile selectByPrimaryKey(Long id);

    int updateByPrimaryKey(UserFile record);

    /**
     * 列出指定用户没有选择类型的风控材料
     * @param loginInfoId
     * @return
     */
    List<UserFile> listUserFilesByHasSelectType(@Param("loginInfoId") Long loginInfoId, @Param("hasTypes") boolean hasTypes);

    int queryForCount(UserFileQueryObject qo);

    List<UserFile> query(UserFileQueryObject qo);
}