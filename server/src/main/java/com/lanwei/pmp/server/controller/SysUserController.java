package com.lanwei.pmp.server.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.common.collect.Maps;
import com.lanwei.pmp.common.response.BaseResponse;
import com.lanwei.pmp.common.response.StatusCode;
import com.lanwei.pmp.common.utils.Constant;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.common.utils.ValidatorUtil;
import com.lanwei.pmp.model.entity.SysUserEntity;
import com.lanwei.pmp.server.annotation.LogAnnotation;
import com.lanwei.pmp.server.service.SysUserService;
import com.lanwei.pmp.server.shiro.ShiroUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户管理controller
 * @author lanwei
 * @email 335747949@qq.com
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController{

    @Autowired
    private SysUserService sysUserService;

    //获取当前登录用户的详情
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public BaseResponse currInfo(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap= Maps.newHashMap();
        try {
            resMap.put("user",getUser());

        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        response.setData(resMap);
        return response;
    }

    //修改密码
    @RequestMapping(value = "/password",method = RequestMethod.POST)
    @LogAnnotation("修改登录密码")
    public BaseResponse updatePassword(String password,String newPassword){
        if(StringUtils.isBlank(password) && StringUtils.isBlank(newPassword)){
            return new BaseResponse(StatusCode.PasswordCanNotBlank);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            SysUserEntity entity= getUser();
            String salt = entity.getSalt();
            String oldPwd = ShiroUtil.sha256(password,salt);

            if(!entity.getPassword().equals(oldPwd)){
                return new BaseResponse(StatusCode.OldPasswordNotMatch);
            }

            log.info("~~~旧密码正确，更新新密码~~~");

            //执行更新密码
            String newPwd = ShiroUtil.sha256(newPassword,salt);
            sysUserService.updatePassword(entity.getUserId(),oldPwd,newPwd);

        }catch (Exception e){
            response = new BaseResponse(StatusCode.UpdatePasswordFail);
        }
        return response;
    }

    //分页模糊查询
    @RequestMapping("/list")
    @RequiresPermissions(value = {"sys:user:list","sys:user:info"})
    public BaseResponse list(@RequestParam Map<String ,Object> paramMap){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String ,Object> resMap = Maps.newHashMap();
        try {
            log.info("用户模块~分页模糊查询数据：{}",paramMap);
            PageUtil page = sysUserService.queryPage(paramMap);
            resMap.put("page",page);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    //新增
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiresPermissions("sys:user:save")
    public BaseResponse save(@RequestBody @Validated SysUserEntity entity, BindingResult result){
        String res = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        if(StringUtils.isEmpty(entity.getPassword())){
            return new BaseResponse(StatusCode.PasswordCanNotBlank);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            sysUserService.saveUser(entity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //获取用户详情~角色、岗位关联信息
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:list")
    public BaseResponse info(@PathVariable Long userId){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap= Maps.newHashMap();
        try {
            log.info("获取当前用户详情：{}",userId);

            resMap.put("user",sysUserService.getInfo(userId));

        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        response.setData(resMap);
        return response;
    }

    //修改更新用户信息
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @RequiresPermissions("sys:user:update")
    public BaseResponse update(@RequestBody @Validated SysUserEntity entity, BindingResult result){
        String res = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("修改岗位~接受到的数据：{}",entity);
            sysUserService.updateUser(entity);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;

    }

    //删除
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public BaseResponse delete(@RequestBody Long[] ids ){
        if(ids == null && ids.length<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        //超级管理员~admin不能被删除，当前登录用户不能被删除
        if(ArrayUtils.contains(ids, Constant.SUPER_ADMIN)){
            return new BaseResponse(StatusCode.SysUserCanNotBeDelete);
        }
        if(ArrayUtils.contains(ids,ShiroUtil.getUserId())){
            return  new BaseResponse(StatusCode.CurrUserCanNotBeDelete);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {

            //调用
            sysUserService.deleteUser(ids);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //重置密码
    @RequestMapping("/psd/reset")
    @RequiresPermissions("sys:user:resetPsd")
    public BaseResponse resetPassword(@RequestBody Long[] ids){
        if(ids == null && ids.length<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        //超级管理员~admin不能重置密码；当前登录用户不能重置
        if (ArrayUtils.contains(ids,Constant.SUPER_ADMIN) || ArrayUtils.contains(ids,getUserId())){
            return new BaseResponse(StatusCode.SysUserAndCurrUserCanNotResetPsd);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //调用
            sysUserService.updatePsd(ids);
        }catch (Exception e){
            return new BaseResponse(StatusCode.UpdatePasswordFail);
        }
        return response;
    }
}
