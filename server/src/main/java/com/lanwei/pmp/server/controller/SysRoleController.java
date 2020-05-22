package com.lanwei.pmp.server.controller;

import com.google.common.collect.Maps;
import com.lanwei.pmp.common.response.BaseResponse;
import com.lanwei.pmp.common.response.StatusCode;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.common.utils.ValidatorUtil;
import com.lanwei.pmp.model.entity.SysRoleEntity;
import com.lanwei.pmp.server.annotation.LogAnnotation;
import com.lanwei.pmp.server.service.SysRoleDeptService;
import com.lanwei.pmp.server.service.SysRoleMenuService;
import com.lanwei.pmp.server.service.SysRoleService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController{

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    //分页模糊查询
    @RequestMapping("/list")
    @RequiresPermissions("sys:role:list")
    public BaseResponse list(@RequestParam Map<String,Object> paramMap) {
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap= Maps.newHashMap();
        try {
            PageUtil page=sysRoleService.queryPage(paramMap);
            resMap.put("page",page);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    //新增
    @LogAnnotation("新增角色")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiresPermissions("sys:role:save")
    public BaseResponse save(@RequestBody @Validated SysRoleEntity entity, BindingResult result){
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("新增角色~接收到数据：{}",entity);

            sysRoleService.saveRole(entity);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    //获取详情
    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    @RequiresPermissions("sys:role:info")
    public BaseResponse info(@PathVariable Long id){
        if(id == null || id <=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String ,Object> resMap = Maps.newHashMap();
        try {
            //根据id获取角色信息
            SysRoleEntity role = sysRoleService.getById(id);

            //获取角色对应的菜单列表
            List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(id);
            role.setMenuIdList(menuIdList);
            //获取角色对应的部门列表
            List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(id);
            role.setDeptIdList(deptIdList);
            resMap.put("role",role);
            log.info("角色详情~接受到的数据{}",resMap);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    //修改
    @LogAnnotation("修改角色")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @RequiresPermissions("sys:role:update")
    public BaseResponse update(@RequestBody @Validated SysRoleEntity entity, BindingResult result) {
        String res=ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("修改角色~接收到数据：{}",entity);

            sysRoleService.updateRole(entity);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    //删除
    @LogAnnotation("删除角色")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @RequiresPermissions("sys:role:delete")
    public BaseResponse delete(@RequestBody Long[] ids){
        if(ids ==  null || ids.length <=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            log.info("删除角色~接收到数据：{}", ids);

            sysRoleService.deleteBatch(ids);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //查询所有角色
    @RequestMapping("/select")
    public BaseResponse select(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("查询所有角色>>>>");
            Map<String ,Object> resMap = Maps.newHashMap();
            resMap.put("list",sysRoleService.list());
            response.setData(resMap);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}
