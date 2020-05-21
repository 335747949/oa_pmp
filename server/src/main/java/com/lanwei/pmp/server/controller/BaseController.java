package com.lanwei.pmp.server.controller;

import com.lanwei.pmp.common.response.BaseResponse;
import com.lanwei.pmp.common.response.StatusCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Controller
@RequestMapping("/base")
public class BaseController {

    /**
     * 第一个案例-json格式响应体交互
     * @param name
     * @return
     */
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse info(String name){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        if(StringUtils.isBlank(name)){
            name = "权限管理平台";
        }
        response.setData(name);
        return response;
    }

    /**
     * 第二个案例：页面跳转-塞值
     * @param name
     * @return
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public String page(String name, ModelMap modelMap){
        if(StringUtils.isBlank(name)){
            name = "权限管理平台";
        }
        modelMap.put("name",name);
        modelMap.put("app","你好呀");
        modelMap.put("age",28);
        return "pageOne";
    }

}
