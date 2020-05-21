package com.lanwei.pmp.server.service.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.lanwei.pmp.common.utils.CommonUtil;
import com.lanwei.pmp.common.utils.Constant;
import com.lanwei.pmp.model.entity.SysUserEntity;
import com.lanwei.pmp.model.mapper.SysDeptDao;
import com.lanwei.pmp.model.mapper.SysUserDao;
import com.lanwei.pmp.server.service.CommonDataService;
import com.lanwei.pmp.server.service.SysDeptService;
import com.lanwei.pmp.server.shiro.ShiroUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 通用化的部门数据权限控制service
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("commonDataService")
public class CommonDataServiceImpl implements CommonDataService{

    private static final Logger log= LoggerFactory.getLogger(CommonDataServiceImpl.class);

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysDeptDao sysDeptDao;
    @Autowired
    private SysDeptService sysDeptService;

    //获取当前登录用户的部门数据Id列表
    @Override
    public Set<Long> getCurrUserDataDeptIds() {
        Set<Long> dataIds = Sets.newHashSet();
        SysUserEntity user = ShiroUtil.getUserEntity();

        //超级管理员默认拥有所有数据权限
        if(Constant.SUPER_ADMIN == user.getUserId()){
            dataIds = sysDeptDao.queryAllDeptIds();
        }else {
            //分配给用户的部门数据权限id列表
            Set<Long> userDeptDataIds= sysUserDao.querytDeptIdsByUserId(user.getUserId());
            if(dataIds != null && dataIds.isEmpty()){
                dataIds.addAll(userDeptDataIds);
            }
            //用户所在的部门及其子部门Id列表 ~ 递归实现
            dataIds.add(user.getDeptId());

            List<Long> subDeptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
            dataIds.addAll(Sets.newHashSet(subDeptIdList));
        }

        return dataIds;
    }

    //将 部门数据Id列表 转化为 id拼接 的字符串
    @Override
    public String getCurrUserDataDeptIdsStr() {

        String result=null;

        Set<Long> dataSet=this.getCurrUserDataDeptIds();
        if (dataSet!=null && !dataSet.isEmpty()){
            result= CommonUtil.concatStrToInt(Joiner.on(",").join(dataSet),",");
        }

        return result;
    }
}
