package com.lanwei.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanwei.pmp.model.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//角色与用户关联关系
@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {

	//根据角色Id列表，批量删除
	int deleteBatch(@Param("roleIds") String roleIds);

	//根据用户id，查找角色列表
	List<Long> queryRoleList(@Param("userId") Long userId);
}
