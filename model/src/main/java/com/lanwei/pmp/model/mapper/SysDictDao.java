package com.lanwei.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanwei.pmp.model.entity.SysDictEntity;
import org.apache.ibatis.annotations.Mapper;

//数据字典
@Mapper
public interface SysDictDao extends BaseMapper<SysDictEntity> {
	
}
