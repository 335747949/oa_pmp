package com.lanwei.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanwei.pmp.model.entity.SysUserEntity;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Set;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysUserDao extends BaseMapper<SysUserEntity> {

    SysUserEntity selectByUserName(@Param("userName") String userName);

    List<String> queryAllPerms(@Param("userId") Long userId);

    //根据用户id获取部门数据Id列表 ~ 数据权限
    Set<Long> querytDeptIdsByUserId(@Param("userId") Long userId);

}
