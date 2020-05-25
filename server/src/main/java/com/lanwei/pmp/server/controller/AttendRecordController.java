package com.lanwei.pmp.server.controller;

import com.google.common.collect.Maps;
import com.lanwei.pmp.common.response.BaseResponse;
import com.lanwei.pmp.common.response.StatusCode;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.server.service.AttendRecordService;
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
@RequestMapping("/attend/record")
public class AttendRecordController extends AbstractController{

    @Autowired
    private AttendRecordService attendRecordService;

    //列表分页查询
    @RequestMapping("/list")
    public BaseResponse list(@RequestParam Map<String,Object> params){

        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap = Maps.newHashMap();
        try {
            log.info("---考勤列表----");
            PageUtil page = attendRecordService.queryPage(params);
            resMap.put("page",page);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }
}
