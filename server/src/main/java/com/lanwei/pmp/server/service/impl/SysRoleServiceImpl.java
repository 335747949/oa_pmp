package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.common.utils.QueryUtil;
import com.lanwei.pmp.model.entity.SysRoleEntity;
import com.lanwei.pmp.model.mapper.SysRoleDao;
import com.lanwei.pmp.server.service.SysRoleDeptService;
import com.lanwei.pmp.server.service.SysRoleMenuService;
import com.lanwei.pmp.server.service.SysRoleService;
import com.lanwei.pmp.server.service.SysUserRoleService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao,SysRoleEntity> implements SysRoleService{

    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String search= (params.get("search") == null)? "": params.get("search").toString() ;

        IPage IPage = new QueryUtil<SysRoleEntity>().getQueryPage(params);
        QueryWrapper wrapper = new QueryWrapper<SysRoleEntity>()
                .like(StringUtils.isNotBlank(search),"role_name",search.trim());

        IPage<SysRoleEntity> resPage = page(IPage,wrapper);
        return new PageUtil(resPage);
    }

    //新增
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRoleEntity role) {
        role.setCreateTime(DateTime.now().toDate());
        save(role);

        //插入角色~菜单关联信息
        sysRoleMenuService.saveOrUpdate(role.getRoleId(),role.getMenuIdList());
        //插入角色~部门关联信息
        sysRoleDeptService.saveOrUpdate(role.getRoleId(),role.getDeptIdList());
    }

    //修改
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRoleEntity role) {
        updateById(role);

        //更新角色~菜单关联信息
        sysRoleMenuService.saveOrUpdate(role.getRoleId(),role.getMenuIdList());
        //更新角色~部门关联信息
        sysRoleDeptService.saveOrUpdate(role.getRoleId(),role.getDeptIdList());
    }

    //删除
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] ids) {
        List<Long> roleIds = Arrays.asList(ids);
        removeByIds(roleIds);

        //删除角色~菜单关联信息
        sysRoleMenuService.deleteBatch(roleIds);
        //删除角色~部门关联信息
        sysRoleDeptService.deleteBatch(roleIds);
        //删除角色~用户关联信息
        sysUserRoleService.deleteBatch(roleIds);
    }
}
