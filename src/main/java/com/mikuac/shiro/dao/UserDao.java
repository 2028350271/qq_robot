package com.mikuac.shiro.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mikuac.shiro.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {

}

