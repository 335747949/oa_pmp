package com.lanwei.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanwei.pmp.model.entity.SysRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Mapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity>{

    //根据角色id列表，批量删除
    void deleteBatch(@Param("roleIds") String  roleIds);

    //根据角色id查询菜单id
    List<Long> queryMenuIdListByRoleId(@PathParam("roleId") Long roleId);
}
