package com.jd.boot001.controller;

import com.jd.boot001.common.R;
import com.jd.boot001.common.exception.BusinessException;
import com.jd.boot001.entity.SysUser;
import com.jd.boot001.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysUser")
@Slf4j // 日志使用：log.info(debug)
public class SysUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询所有用户信息
     *
     * @return
     */
    @RequestMapping(value = "/querySysUser", method = RequestMethod.GET)
    public List<SysUser> querySysUser() {
        LOGGER.info("querySysUser|用户列表查询,userId={}", "10000");
        return sysUserService.querySysUserList();
    }

    @RequestMapping("/getBusinessException")
    public R getBusinessException() {
        LOGGER.debug("getBusinessException|抛出异常测试");
        if (true) {
            throw new BusinessException("400", "我出错了");
        }
        return R.success();
    }

    @RequestMapping("/getException")
    public R getException() {
        LOGGER.debug("Exception|抛出异常测试");
        int a = 1 / 0;
        return R.success();
    }

}
