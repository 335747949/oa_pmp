package com.lanwei.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanwei.pmp.model.entity.SysDeptEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 部门管理
 * @author lanwei
 * @email 335747949@qq.com
 */
@Mapper
public interface SysDeptDao extends BaseMapper<SysDeptEntity>{

    List<SysDeptEntity> queryAll(Map<String, Object> params);

    List<SysDeptEntity> queryListV2(Map<String ,Object> params);

    //根据父级部门id查询子部门id列表
    List<Long> queryDeptIds(@Param("parentId") Long parentId);

    //查询所有部门id
    Set<Long> queryAllDeptIds();
}
