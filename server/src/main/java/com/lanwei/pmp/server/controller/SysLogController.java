package com.lanwei.pmp.server.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Maps;
import com.lanwei.pmp.common.response.BaseResponse;
import com.lanwei.pmp.common.response.StatusCode;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.server.service.SysLogService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController extends AbstractController{

    @Autowired
    private SysLogService sysLogService;

    @RequestMapping("/list")
    public BaseResponse list(@RequestParam Map<String , Object> paramsMap){
        BaseResponse response  = new BaseResponse(StatusCode.Success);
        Map<String ,Object> resMap = Maps.newHashMap();
        try {
            log.info("日志分页查询：{}",paramsMap);
            PageUtil page = sysLogService.queryPage(paramsMap);
            resMap.put("page",page);

        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    //清除系统日志
    @RequestMapping("/truncate")
    @RequiresPermissions("sys:log:truncate")
    public BaseResponse truncate(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("清除系统日志~");
            sysLogService.truncate();
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}
