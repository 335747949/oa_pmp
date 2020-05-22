package com.lanwei.pmp.server.controller;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanwei.pmp.common.response.BaseResponse;
import com.lanwei.pmp.common.response.StatusCode;
import com.lanwei.pmp.common.utils.Constant;
import com.lanwei.pmp.common.utils.ValidatorUtil;
import com.lanwei.pmp.model.entity.SysDeptEntity;
import com.lanwei.pmp.server.annotation.LogAnnotation;
import com.lanwei.pmp.server.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController{

    @Autowired
    private SysDeptService sysDeptService;

    @RequestMapping(value = "/list")
    @RequiresPermissions("sys:dept:list")
    public List<SysDeptEntity> list(){
        return sysDeptService.queryAll(Maps.newHashMap());
    }

    //获取一级部门/顶级部门的deptId
    @RequestMapping(value = "/info")
    public BaseResponse info(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String , Object> resMap = Maps.newHashMap();
        try {
            //数据视野决定的顶级部门id可能不是0
            if(getUserId() != Constant.SUPER_ADMIN){
                //假设登录的账号不是超级管理员
                //涉及到数据视野的问题
            }

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        resMap.put("deptId",0L);
        response.setData(resMap);
        return response;
    }

    @RequestMapping("/select")
    public BaseResponse select(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String , Object> resMap = Maps.newHashMap();
        List<SysDeptEntity> deptList = Lists.newLinkedList();
        try {
            deptList = sysDeptService.queryAll(Maps.newHashMap());
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        resMap.put("deptList",deptList);
        response.setData(resMap);
        return response;
    }

    //新增
    @LogAnnotation("新增部门")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiresPermissions("sys:dept:save")
    public BaseResponse save(@RequestBody @Validated SysDeptEntity entity, BindingResult result){
        String res = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("新增部门~接受到的数据：{}",entity);
            sysDeptService.save(entity);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //获取部门详情
    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    @RequiresPermissions("sys:dept:info")
    public BaseResponse info(@PathVariable Long id){
        if(id == null || id <= 0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String , Object> resMap = Maps.newHashMap();
        try {
            log.info("部门详情~接受到的数据：{}",sysDeptService.getById(id));
            resMap.put("dept",sysDeptService.getById(id));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    //修改
    @LogAnnotation("修改部门")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @RequiresPermissions("sys:dept:update")
    public BaseResponse update(@RequestBody @Validated SysDeptEntity entity, BindingResult result){
        String res = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        if(entity.getDeptId() == null || entity.getDeptId() <=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("修改部门~接受到的数据：{}",entity);
            sysDeptService.updateById(entity);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;

    }

    //删除
    @LogAnnotation("删除部门")
    @RequestMapping(value = "/delete")
    @RequiresPermissions("sys:dept:delete")
    public BaseResponse delete(Long deptId){
        if (deptId==null || deptId<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("删除部门~接收到数据：{}",deptId);

            //如果当前部门有子部门，则需要要求先删除下面的所有子部门，再删除当前部门
            List<Long> subIds=sysDeptService.queryDeptIds(deptId);
            if (subIds!=null && !subIds.isEmpty()){
                return new BaseResponse(StatusCode.DeptHasSubDeptCanNotBeDelete);
            }
            sysDeptService.removeById(deptId);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;

    }
}
