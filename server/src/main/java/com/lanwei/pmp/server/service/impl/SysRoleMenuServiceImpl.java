package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.lanwei.pmp.common.utils.CommonUtil;
import com.lanwei.pmp.model.entity.SysRoleMenuEntity;
import com.lanwei.pmp.model.mapper.SysRoleMenuDao;
import com.lanwei.pmp.server.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao,SysRoleMenuEntity> implements SysRoleMenuService{

    //维护角色~菜单的关系信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        //需要先清除旧的角色~菜单关联数据，再插入新的关联信息
        deleteBatch(Arrays.asList(roleId));

        SysRoleMenuEntity entity;
        if(menuIdList != null && !menuIdList.isEmpty()){
            for(Long mId : menuIdList){
                entity = new SysRoleMenuEntity();
                entity.setRoleId(roleId);
                entity.setMenuId(mId);
                save(entity);
            }
        }
    }

    //根据角色id，获取菜单id数据
    @Override
    public List<Long> queryMenuIdList(Long roleId) {
        return baseMapper.queryMenuIdListByRoleId(roleId);
    }

    //根据角色id批量删除
    @Override
    public void deleteBatch(List<Long> roleIds) {
        if(roleIds != null && !roleIds.isEmpty()){
            String delIds= Joiner.on(",").join(roleIds);
            baseMapper.deleteBatch(CommonUtil.concatStrToInt(delIds,","));
        }
    }
}
