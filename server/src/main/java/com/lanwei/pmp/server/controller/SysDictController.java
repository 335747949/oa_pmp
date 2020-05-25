package com.lanwei.pmp.server.controller;

import com.google.common.collect.Maps;
import com.lanwei.pmp.common.response.BaseResponse;
import com.lanwei.pmp.common.response.StatusCode;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.common.utils.ValidatorUtil;
import com.lanwei.pmp.model.entity.SysDictEntity;
import com.lanwei.pmp.server.annotation.LogAnnotation;
import com.lanwei.pmp.server.service.SysDictService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@RestController
@RequestMapping("/sys/dict")
public class SysDictController extends AbstractController{

    @Autowired
    private SysDictService sysDictService;

    //分页模糊查询
    @RequestMapping("/list")
    public BaseResponse list(@RequestParam Map<String ,Object> map){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String ,Object> resmap = Maps.newHashMap();
        try {
            log.info("字典分页查询：{}",map);
            PageUtil page = sysDictService.queryPage(map);
            resmap.put("page",page);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resmap);
        return response;
    }

    //获取详情
    @RequestMapping("/info/{id}")
    public BaseResponse info(@PathVariable Long id){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap = Maps.newHashMap();
        try {
            log.info("获取到的id：{}",id);
            SysDictEntity entity = sysDictService.getById(id);
            resMap.put("dict",entity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    //新增
    @LogAnnotation("字典新增")
    @RequestMapping("/save")
    @RequiresPermissions("sys:dict:save")
    public BaseResponse save(@RequestBody @Validated SysDictEntity dict, BindingResult result){
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            sysDictService.save(dict);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //修改
    @LogAnnotation("字典修改")
    @RequestMapping("/update")
    public BaseResponse update(@RequestBody @Validated SysDictEntity dict, BindingResult result){
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            //调用
            sysDictService.updateById(dict);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

     //删除
    @LogAnnotation("字典删除")
    @RequestMapping("/delete")
    public BaseResponse delete(@RequestBody Long[] ids){
        if(ids == null && ids.length<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            sysDictService.removeByIds(Arrays.asList(ids));
        }catch (Exception e){
            return  new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}
