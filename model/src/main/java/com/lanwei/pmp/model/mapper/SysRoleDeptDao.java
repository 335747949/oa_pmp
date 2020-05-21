package com.lanwei.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanwei.pmp.model.entity.SysRoleDeptEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.util.List;

//角色与部门关联关系
@Mapper
public interface SysRoleDeptDao extends BaseMapper<SysRoleDeptEntity> {

    //根据角色id列表，批量删除
    int deleteBatch(@Param("roleIds") String roleIds);

    //根据角色Id，获取部门Id列表
    List<Long> queryDeptIdListByRoleId(@PathParam("roleId") Long roleId);
}
