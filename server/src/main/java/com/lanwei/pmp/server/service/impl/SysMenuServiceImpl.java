package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.lanwei.pmp.common.utils.Constant;
import com.lanwei.pmp.model.entity.SysMenuEntity;
import com.lanwei.pmp.model.mapper.SysMenuDao;
import com.lanwei.pmp.model.mapper.SysUserDao;
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
    @Autowired
    private SysUserDao sysUserDao;

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

    //动态获取首页左侧导航菜单栏
    @Override
    public List<SysMenuEntity> getUserMenuList(Long currUserId) {
        List<SysMenuEntity> list = Lists.newLinkedList();
        if(currUserId == Constant.SUPER_ADMIN){
            //超级管理员默认可以看到所有菜单
            list = getAllMenuList(null);
        }else{
            //非超级管理员~根据分配给用户的角色~菜单关联信息来获取菜单列表
            List<Long> menuIdList = sysUserDao.queryAllMenuId(currUserId);
            list = getAllMenuList(menuIdList);
        }
        return list;
    }

    //获取所有菜单列表
    private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
        List<SysMenuEntity> menuList = queryListByParentId(0L,menuIdList);

        //递归获取一级菜单的子菜单
        getMenuTrees(menuList,menuIdList);

        return menuList;
    }

    /**
     * 根据父id查询子菜单列表，找出一级菜单列表（找出类型为目录的菜单）
     * @param parentId
     * @param menuIdList 针对于非超级管理员的处理 ~ 获取分配的角色下的菜单id列表
     * @return
     */
    private List<SysMenuEntity> queryListByParentId(Long parentId,List<Long> menuIdList){
        List<SysMenuEntity> menuList = baseMapper.queryListParentId(parentId);
        if(menuIdList == null || menuIdList.isEmpty()){
            return menuList;
        }

        //在所有的一级菜单列表中 找出存在于“用户分配的菜单列表”
        List<SysMenuEntity> userMenuList = Lists.newLinkedList();
        for (SysMenuEntity entity : menuList){
            if(menuIdList.contains(entity.getMenuId())){
                userMenuList.add(entity);
            }
        }
        return userMenuList;
    }

    /**
     * 递归思想：何时开始，何时结束，做什么事情
     * @param menuList
     * @param menuIdList
     * @return
     */
    private List<SysMenuEntity> getMenuTrees(List<SysMenuEntity> menuList,List<Long> menuIdList){
        List<SysMenuEntity> subMenuList = Lists.newLinkedList();

        List<SysMenuEntity> tempList;
        for(SysMenuEntity entity : menuList){

            //当前菜单类型为目录（一级菜单），即进行遍历；（递归终止：不是目录，或者子菜单列表为空）
            if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
                tempList = queryListByParentId(entity.getMenuId(),menuIdList);
                entity.setList(getMenuTrees(tempList,menuIdList));
            }

            subMenuList.add(entity);
        }
        return subMenuList;
    }
}
