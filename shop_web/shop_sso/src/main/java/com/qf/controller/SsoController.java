package com.qf.controller;

import com.qf.entity.User;
import com.qf.service.IUserService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sso")
public class SsoController {

    @Reference
    private IUserService userService;

    @RequestMapping("/toregister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/register")
    public String register(User user, Model model) {

        int result =userService.register(user);
        if(result > 0){
            return "login";
        }else if(result == 0){
            model.addAttribute("error","用户已存在，请重新申请");

        }else {
            model.addAttribute("error", "注册失败！");
        }
        return "register";
    }

}
