package com.lanwei.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanwei.pmp.model.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


//菜单管理
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {

    //根据父级Id，查询子菜单
    List<SysMenuEntity> queryListParentId(Long parentId);

    //获取不包含按钮的菜单按钮
    List<SysMenuEntity> queryNotButtonList();

    //获取所有菜单
    List<SysMenuEntity> queryList();
}
