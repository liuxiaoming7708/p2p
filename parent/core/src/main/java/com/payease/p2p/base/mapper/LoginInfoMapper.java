package com.payease.p2p.base.mapper;

import com.payease.p2p.base.domain.LoginInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LoginInfoMapper {
    //注册
    int insert(LoginInfo record);

    LoginInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(LoginInfo record);
    //查询用户名有多少个
    int countUserByName(String username);
    //用户登陆
    LoginInfo login(@Param("username") String username,@Param("password") String encode,@Param("userType")int userType);
    //按照用户类型统计
    int countByUserType(int userType);
    //用户自动补全
    List<Map<String,Object>> autoComplate(String keyword);
}