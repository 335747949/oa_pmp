package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.model.entity.SysUserEntity;

import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysUserService extends IService<SysUserEntity>{

    // 修改密码
    boolean updatePassword(Long userId,String oldPassword,String newPassword );

    //分页模糊查询
    PageUtil queryPage(Map<String ,Object> map);

    //保存
    void saveUser(SysUserEntity entity);

    //获取详情
    SysUserEntity getInfo(Long userId);

    //修改更新用户详情
    void updateUser(SysUserEntity entity);

    //删除
    void deleteUser(Long[] ids);

    //重置密码
    void updatePsd(Long[] ids);
}
