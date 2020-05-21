package com.lanwei.pmp.server.controller;

import com.lanwei.pmp.server.shiro.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Controller
public class SysPageController {


    @RequestMapping("modules/{module}/{page}.html")
    public String page(@PathVariable String module, @PathVariable String page){
        return "modules/"+module+"/"+page;
    }


    @RequestMapping(value = {"index.html","/"} )
    public String index(){
        return "index";
    }

    @RequestMapping("login.html")
    public String login(){
        if (SecurityUtils.getSubject().isAuthenticated()){
            return "redirect:index.html";
        }
        return "login";
    }

    @RequestMapping("main.html")
    public String main(){
        return "main";
    }

    @RequestMapping("404.html")
    public String notFound(){
        return "404";
    }
}
