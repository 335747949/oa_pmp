package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.lanwei.pmp.model.entity.SysDeptEntity;
import com.lanwei.pmp.model.mapper.SysDeptDao;
import com.lanwei.pmp.server.service.CommonDataService;
import com.lanwei.pmp.server.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao,SysDeptEntity> implements SysDeptService{

    private static final Logger log= LoggerFactory.getLogger(SysDeptServiceImpl.class);

    @Autowired
    private CommonDataService commonDataService;

    //查询所有部门列表 ~ 涉及到 部门数据权限 的控制
    @Override
    public List<SysDeptEntity> queryAll(Map<String, Object> map) {
        //return baseMapper.queryAll(map);
        String deptDataIds = commonDataService.getCurrUserDataDeptIdsStr();
        map.put("deptDataIds", StringUtils.isNotBlank(deptDataIds)? deptDataIds : "");

        return baseMapper.queryListV2(map);
    }

    //根据父级部门id查询子部门id列表
    @Override
    public List<Long> queryDeptIds(Long parentId) {
        return baseMapper.queryDeptIds(parentId);
    }

    //获取当前部门的子部门id列表
    @Override
    public List<Long> getSubDeptIdList(Long deptId) {
        List<Long> depeIdList = Lists.newLinkedList();

        //获取到的第一级部门id列表
        List<Long> subDeptList = baseMapper.queryDeptIds(deptId);
        //调用递归
        getDeptTreeList(subDeptList,depeIdList);

        return depeIdList;
    }

    /**
     * 递归
     * @param subDeptList 第一级部门id列表
     * @param deptIdList 每次递归时循环存储的结果数据Id列表
     */
    private void getDeptTreeList(List<Long> subDeptList,List<Long> deptIdList){
        List<Long> list;
        for(Long subId : subDeptList){
            list = baseMapper.queryDeptIds(subId);
            if(list != null && list.isEmpty()){
                //再次调用递归
                getDeptTreeList(list,deptIdList);
            }

            //当方法执行到这时，表示递归已经执行结束
            deptIdList.add(subId);
        }
    }




}