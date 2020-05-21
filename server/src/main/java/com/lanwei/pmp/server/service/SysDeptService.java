package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.model.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysDeptService extends IService<SysDeptEntity>{

    List<SysDeptEntity> queryAll(Map<String ,Object> params);

    List<Long> queryDeptIds(Long parentId);

    List<Long> getSubDeptIdList(Long deptId);

}
