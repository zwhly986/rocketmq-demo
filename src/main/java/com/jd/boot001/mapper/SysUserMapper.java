package com.jd.boot001.mapper;

import com.jd.boot001.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {

    /**
     * 查询所有用户信息
     * @return  所有用户信息
     */
    List<SysUser> querySysUserList();
}
