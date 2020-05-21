package com.lanwei.pmp.server.controller;

import com.lanwei.pmp.model.entity.SysUserEntity;
import com.lanwei.pmp.server.shiro.ShiroUtil;
import org.apache.catalina.User;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;


/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Controller
public abstract class AbstractController {

    //日志
    protected Logger log= LoggerFactory.getLogger(getClass());

    //获取当前登录用户的详情
    protected SysUserEntity getUser(){
        SysUserEntity user= (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    //获取当前登录用户的id
    protected Long getUserId(){
        return getUser().getUserId();
    }

    //获取当前登录用户的姓名
    protected String getUsername(){
        return getUser().getUsername();
    }

    //获取当前登录用的部门id
    protected Long getDeptId(){
        return getUser().getDeptId();
    }
}