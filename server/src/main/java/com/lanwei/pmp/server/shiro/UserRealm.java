package com.lanwei.pmp.server.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanwei.pmp.common.utils.Constant;
import com.lanwei.pmp.model.entity.SysMenuEntity;
import com.lanwei.pmp.model.entity.SysUserEntity;
import com.lanwei.pmp.model.mapper.SysUserDao;
import com.lanwei.pmp.server.service.SysMenuService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * shiro用于用户认证~授权
 * @author lanwei
 * @email 335747949@qq.com
 */
@Component
public class UserRealm extends AuthorizingRealm {

    private static final Logger log= LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 资源-权限分配 ~ 授权 ~ 需要将分配给当前用户的权限列表塞给shiro的权限字段中去
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //获取当前登录用户（主体）
        SysUserEntity user= (SysUserEntity) principals.getPrimaryPrincipal();
        Long userId=user.getUserId();
        List<String> perms= Lists.newLinkedList();

        //系统超级管理员拥有最高的权限，不需要发出sql的查询，直接拥有所有权限；否则，则需要根据当前用户id去查询权限列表
        if (userId == Constant.SUPER_ADMIN){
            List<SysMenuEntity> list=sysMenuService.list();
            if (list!=null && !list.isEmpty()){
                perms=list.stream().map(SysMenuEntity::getPerms).collect(Collectors.toList());
            }
        }else{
            perms=sysUserDao.queryAllPerms(userId);
        }

        //对于每一个授权编码进行 , 的解析拆分
        Set<String> stringPermissions= Sets.newHashSet();
        if (perms!=null && !perms.isEmpty()){
            for (String p:perms){
                if (StringUtils.isNotBlank(p)){
                    stringPermissions.addAll(Arrays.asList(StringUtils.split(p.trim(),",")));
                }
            }
        }

        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.setStringPermissions(stringPermissions);
        return info;

    }

    /**
     * 用户认证~认证登录
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        final String userName = token.getUsername();
        final String password = String.valueOf(token.getPassword());
        log.info("用户名：{} 密码：{} ",userName,password);

        SysUserEntity entity=sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("username",userName));
        //SysUserEntity entity = sysUserDao.selectByUserName(userName);

        //账户不存在
        if(entity == null){
            throw new UnknownAccountException("账号不存在！");
        }
        //账号被禁用
        if(0 == entity.getStatus()){
            throw new DisabledAccountException("账号已被封禁，请联系管理员！");
        }

        String realPassword = ShiroUtil.sha256(password,entity.getSalt());
        if(StringUtils.isBlank(realPassword) || !realPassword.equals(entity.getPassword())){
            throw new IncorrectCredentialsException("密码不正确！");
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(entity,password,getName());
        return info;
    }
}
