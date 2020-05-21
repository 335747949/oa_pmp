package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.model.entity.SysRoleMenuEntity;

import java.util.List;


/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity>{

    void deleteBatch(List<Long> roleIds);

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    List<Long> queryMenuIdList(Long roleId);
}
