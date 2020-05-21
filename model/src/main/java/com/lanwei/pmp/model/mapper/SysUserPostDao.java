package com.lanwei.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanwei.pmp.model.entity.SysUserPostEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

//用户与岗位关联关系
@Mapper
public interface SysUserPostDao extends BaseMapper<SysUserPostEntity> {


	//根据用户id获取用户-岗位详情
	Set<String> getPostNamesByUserId(@Param("userId") Long userId);

	//根据用户id获取用户-岗位详情
	List<SysUserPostEntity> getByUserId(@Param("userId") Long userId);

	//根据用户id，获取岗位列表
	List<Long> queryPostList(@Param("userId") Long userId);
}
