package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.model.entity.SysRoleDeptEntity;

import java.util.List;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysRoleDeptService extends IService<SysRoleDeptEntity>{

        void deleteBatch(List<Long> roleIds);

        void saveOrUpdate(Long roleId, List<Long> deptIdList);

        List<Long> queryDeptIdList(Long roleId);
}
