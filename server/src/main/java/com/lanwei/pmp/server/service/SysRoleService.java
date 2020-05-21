package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.model.entity.SysRoleEntity;

import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysRoleService extends IService<SysRoleEntity>{

    PageUtil queryPage(Map<String ,Object> params);

    void saveRole(SysRoleEntity role);

    void updateRole(SysRoleEntity role);

    void deleteBatch(Long[] ids);
}
