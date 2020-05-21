package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.model.entity.SysMenuEntity;

import java.util.List;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysMenuService extends IService<SysMenuEntity>{

    //获取不包含按钮的菜单列表
    List<SysMenuEntity> queryNotButtonList();

    //获取所有菜单列表
    List<SysMenuEntity> queryAll();

    //根据父级id，查询子菜单
    List<SysMenuEntity> queryByParentId(Long menuId);

    void delete(Long menuId);

    List<SysMenuEntity> getUserMenuList(Long currUserId);
}
