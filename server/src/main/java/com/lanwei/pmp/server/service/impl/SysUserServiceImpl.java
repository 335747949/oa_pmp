package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanwei.pmp.common.response.BaseResponse;
import com.lanwei.pmp.common.response.StatusCode;
import com.lanwei.pmp.common.utils.Constant;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.common.utils.QueryUtil;
import com.lanwei.pmp.model.entity.*;
import com.lanwei.pmp.model.mapper.SysUserDao;
import com.lanwei.pmp.server.service.*;
import com.lanwei.pmp.server.shiro.ShiroUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao,SysUserEntity> implements SysUserService {

    @Autowired
    private SysUserPostservice sysUserPostservice;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    //更新密码 ~ 借助于mybatis-plus的方法来实现
    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        SysUserEntity entity = new SysUserEntity();
        entity.setPassword(newPassword);
        QueryWrapper<SysUserEntity> wrapper = new QueryWrapper<SysUserEntity>();
        Boolean res=this.update(entity,wrapper.eq("user_id",userId).eq("password",oldPassword));
        return res;
    }

    //列表分页模糊查询
    @Override
    public PageUtil queryPage(Map<String, Object> map) {
        String search = (map.get("username")!=null)? (String)map.get("username").toString() : "";

        IPage iPage = new QueryUtil<SysUserEntity>().getQueryPage(map);

        QueryWrapper wrapper = new QueryWrapper<SysUserEntity>()
                .like(StringUtils.isNotBlank(search.trim()),"username",search.trim())
                .or(StringUtils.isNotBlank(search.trim()))
                .like(StringUtils.isNotBlank(search.trim()),"name",search.trim());

        IPage<SysUserEntity> resPage = page(iPage,wrapper);

        //获取用户的部门和岗位
        SysDeptEntity dept;
        for(SysUserEntity user : resPage.getRecords()){
            try {
                dept = sysDeptService.getById(user.getDeptId());
                //设置用户的部门
                user.setDeptName((dept !=null && StringUtils.isNotBlank(dept.getName()))? dept.getName() : "");
                //设置用户的岗位
                String postName = sysUserPostservice.getPostNameByUserId(user.getUserId());
                user.setPostName(postName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new PageUtil(resPage);
    }

    //新增用户
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserEntity entity) {

        //校验用户名
        QueryWrapper wrapper = new QueryWrapper<SysUserEntity>()
                .eq("username",entity.getUsername());
        if(getOne(wrapper) != null){
            throw new RuntimeException("用户名已存在！");
        }
        //加密密码
        entity.setCreateTime(DateTime.now().toDate());
        String salt = RandomStringUtils.randomAlphanumeric(20);
        String password = ShiroUtil.sha256(entity.getPassword(),salt);
        //更新用户数据
        entity.setPassword(password);
        entity.setSalt(salt);
        save(entity);
        //维护好用户~角色的关联关系
        sysUserRoleService.saveOrUpdate(entity.getUserId(),entity.getRoleIdList());

        //维护好用户~岗位的关联关系
        sysUserPostservice.saveOrUpdate(entity.getUserId(),entity.getPostIdList());

    }

    //获取当前用户的详情~包括用户和角色、岗位之间的关联信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUserEntity getInfo(Long userId) {
        SysUserEntity entity = getById(userId);

        //获取用户~角色的关联信息
        List<Long> roleIds = sysUserRoleService.queryRoleList(userId);
        entity.setRoleIdList(roleIds);
        //获取用户~岗位的关联信息
        List<Long> postIds = sysUserPostservice.queryPostList(userId);
        entity.setPostIdList(postIds);

        return entity;
    }

    //修改用户详情
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserEntity entity) {
        SysUserEntity old = getById(entity.getUserId());
        if(old == null){
            return;
        }
        if(!old.getUsername().equals(entity.getUsername())){
            //校验用户名
            QueryWrapper wrapper = new QueryWrapper<SysUserEntity>()
                    .eq("username",entity.getUsername());
            if(getOne(wrapper) != null){
                throw new RuntimeException("修改的用户名已存在！");
            }
        }
        if(StringUtils.isNotBlank(entity.getPassword())){
            //加密密码
            String password = ShiroUtil.sha256(entity.getPassword(),old.getSalt());
            entity.setPassword(password);
        }
        updateById(entity);

        //修改更新用户~角色关联信息
        sysUserRoleService.saveOrUpdate(entity.getUserId(),entity.getRoleIdList());
        //修改更新用户~岗位关联信息
        sysUserPostservice.saveOrUpdate(entity.getUserId(),entity.getPostIdList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long[] ids) {
        if(ids != null && ids.length>0){
            List<Long> userIdList = Arrays.asList(ids);
            removeByIds(userIdList);
//            for(Long uId : ids){
//                //删除用户~角色关联关系
//                sysUserRoleService.remove(new QueryWrapper<SysUserRoleEntity>().eq("user_id",uId));
//                //删除用户~岗位关联关系
//                sysUserPostservice.remove(new QueryWrapper<SysUserPostEntity>().eq("user_id",uId));
//            }
            //java 8 的写法
            userIdList.stream().forEach(uId -> sysUserRoleService.remove(new QueryWrapper<SysUserRoleEntity>().eq("user_id",uId)));
            userIdList.stream().forEach(uId -> sysUserPostservice.remove(new QueryWrapper<SysUserPostEntity>().eq("user_id",uId)));
        }
    }

    //重置密码
    @Override
    public void updatePsd(Long[] ids) {
        if(ids != null && ids.length>0){
            SysUserEntity entity;
            for(Long uId : ids){
                entity = new SysUserEntity();

                String salt = RandomStringUtils.randomAlphanumeric(20);
                String newPsd = ShiroUtil.sha256(Constant.DefaultPassword,salt);
                entity.setSalt(salt);
                entity.setPassword(newPsd);
                update(entity,new QueryWrapper<SysUserEntity>().eq("user_id",uId));
            }
        }
    }

}
