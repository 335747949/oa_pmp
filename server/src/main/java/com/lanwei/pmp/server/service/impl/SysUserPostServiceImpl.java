package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.lanwei.pmp.model.entity.SysUserPostEntity;
import com.lanwei.pmp.model.mapper.SysUserPostDao;
import com.lanwei.pmp.server.service.SysUserPostservice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户~岗位服务service
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("sysUserPostService")
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostDao,SysUserPostEntity> implements SysUserPostservice{

    //根据用户id获取岗位:如果有多个，则采用 , 拼接
    @Override
    public String getPostNameByUserId(Long userId) {

//        //第一种写法   只获取postName
//        Set<String> set = baseMapper.getPostNamesByUserId(userId);
//        if(set != null && !set.isEmpty()){
//            return Joiner.on(",").join(set);
//        }else {
//            return "";
//        }


        //第二种写法  获取到的是实体对象sysUserPostEntity
        String result="";
        List<SysUserPostEntity> list=baseMapper.getByUserId(userId);
        if (list!=null && !list.isEmpty()){
            //java8的stream api
            Set<String> set=list.stream().map(SysUserPostEntity::getPostName).collect(Collectors.toSet());
            //Google Guava提供了Joiner类专门用来连接String
            result=Joiner.on(",").join(set);
        }
        return result;
    }

    //更新用户~岗位的关联关系
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long userId, List<Long> postId) {
        //需要先清除旧的关联数据，再插入新的关联信息
        remove(new QueryWrapper<SysUserPostEntity>()
                .eq("user_id",userId));

        if(postId != null && !postId.isEmpty()){
            SysUserPostEntity entity;
            for (Long pId : postId){
                entity = new SysUserPostEntity();
                entity.setUserId(userId);
                entity.setPostId(pId);
                save(entity);
            }
        }
    }

    @Override
    public List<Long> queryPostList(Long userId) {
        return baseMapper.queryPostList(userId);
    }
}
