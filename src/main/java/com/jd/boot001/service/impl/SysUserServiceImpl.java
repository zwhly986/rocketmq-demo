package com.jd.boot001.service.impl;

import com.jd.boot001.entity.SysUser;
import com.jd.boot001.mapper.SysUserMapper;
import com.jd.boot001.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper userMapper;

    @Override
    public List<SysUser> querySysUserList() {
        return userMapper.querySysUserList();
    }
}
