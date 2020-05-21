package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.lanwei.pmp.common.utils.CommonUtil;
import com.lanwei.pmp.model.entity.SysRoleDeptEntity;
import com.lanwei.pmp.model.mapper.SysRoleDeptDao;
import com.lanwei.pmp.server.service.SysRoleDeptService;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("sysRoleDeptService")
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptDao,SysRoleDeptEntity> implements SysRoleDeptService{

    //维护角色~部门关联关系
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> deptIdList) {
        //需要先清除旧的角色~部门关联数据，再插入新的关联信息
        deleteBatch(Arrays.asList(roleId));

        SysRoleDeptEntity entity;
        if(deptIdList != null && !deptIdList.isEmpty()){
            for(Long dId : deptIdList){
                entity = new SysRoleDeptEntity();
                entity.setRoleId(roleId);
                entity.setDeptId(dId);
                save(entity);
            }
        }
    }

    //根据角色id，获取部门id数据
    @Override
    public List<Long> queryDeptIdList(Long roleId) {
        return baseMapper.queryDeptIdListByRoleId(roleId);
    }

    //根据角色id，批量删除
    @Override
    public void deleteBatch(List<Long> roleIds) {
        if (roleIds!=null && !roleIds.isEmpty()){
            String delIds= Joiner.on(",").join(roleIds);
            baseMapper.deleteBatch(CommonUtil.concatStrToInt(delIds,","));
        }
    }
}
