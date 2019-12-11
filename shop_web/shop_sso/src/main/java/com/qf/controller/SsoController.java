package com.qf.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sso")
public class SsoController {

    @Reference
    private IUserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }


    @RequestMapping("/toRegister")
    public String toRegister(){

        return "register";
    }

    /**
     * 注册
     * @param user
     * @param model
     * @return
     */
    @RequestMapping("/register")
    public String register(User user, Model model) {
        System.out.println("需要添加的用户是"+user);
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
