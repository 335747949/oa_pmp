package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.lanwei.pmp.common.utils.CommonUtil;
import com.lanwei.pmp.model.entity.SysUserRoleEntity;
import com.lanwei.pmp.model.mapper.SysUserRoleDao;
import com.lanwei.pmp.server.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao,SysUserRoleEntity> implements SysUserRoleService{

    @Override
    public void deleteBatch(List<Long> roleIds) {
        if (roleIds!=null && !roleIds.isEmpty()){
            String delIds= Joiner.on(",").join(roleIds);
            baseMapper.deleteBatch(CommonUtil.concatStrToInt(delIds,","));
        }
    }

    //维护用户~角色关联关系
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long userId, List<Long> roleIds) {

        //需要先清除旧的关联数据，再插入新的关联信息
        remove(new QueryWrapper<SysUserRoleEntity>()
                .eq("user_id",userId));


        if(roleIds != null && !roleIds.isEmpty()){
            SysUserRoleEntity entity;
            for(Long rId : roleIds){
                entity = new SysUserRoleEntity();
                entity.setUserId(userId);
                entity.setRoleId(rId);
                save(entity);
            }
        }
    }

    @Override
    public List<Long> queryRoleList(Long userId) {
        return baseMapper.queryRoleList(userId);
    }
}
