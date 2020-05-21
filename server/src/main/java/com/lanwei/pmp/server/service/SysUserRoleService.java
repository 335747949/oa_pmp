package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.model.entity.SysUserRoleEntity;

import java.util.List;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity>{

    void deleteBatch(List<Long> roleIds);

    void saveOrUpdate(Long userId,List<Long> roleIds);

    List<Long> queryRoleList(Long userId);

}
