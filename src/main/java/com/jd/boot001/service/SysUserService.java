package com.jd.boot001.service;

import com.jd.boot001.entity.SysUser;

import java.util.List;

public interface SysUserService {

    /**
     * 查询所有用户信息
     * @return  所有用户信息
     */
    List<SysUser> querySysUserList();

}
