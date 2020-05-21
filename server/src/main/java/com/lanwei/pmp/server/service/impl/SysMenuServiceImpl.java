package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanwei.pmp.model.entity.SysMenuEntity;
import com.lanwei.pmp.model.mapper.SysMenuDao;
import com.lanwei.pmp.server.service.SysMenuService;
import com.lanwei.pmp.server.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao,SysMenuEntity> implements SysMenuService{

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    //获取不包含按钮的菜单列表
    @Override
    public List<SysMenuEntity> queryNotButtonList() {
        return baseMapper.queryNotButtonList();
    }

    @Override
    public List<SysMenuEntity> queryAll() {
        return baseMapper.queryList();
    }

    //根据父级id，查询子菜单
    @Override
    public List<SysMenuEntity> queryByParentId(Long menuId) {
        return baseMapper.queryListParentId(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long menuId) {
        removeById(menuId);
        sysRoleMenuService.removeById(menuId);
    }
}
