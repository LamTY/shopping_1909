package com.qf.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("/sso")
public class SsoController {

    @Autowired
    private RedisTemplate redisTemplate;

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

    /**
     * 登录
     */

    @RequestMapping("login")
    public String login(String username, String password, String returnUrl, HttpServletResponse response){
        System.out.println("登录的用户名"+username+"密码"+password);
        User user = userService.queryByUserName(username);
        System.out.println(user);
        if(user != null && password.equals(user.getPassword())){

            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(token , user);
            Cookie loginToken = new Cookie("loginToken", token);

            loginToken.setMaxAge(60 * 60 * 24 * 7);

            loginToken.setPath("/");
            loginToken.setDomain("localhost");
            response.addCookie(loginToken);

            if(returnUrl == null || returnUrl.equals("")){
                returnUrl ="http://localhost";
            }

            return "redirect:"+returnUrl;
        }

        return "redirect:/sso/toLogin";
    }

    /**
     * 判断是否登录
     * jsonp
     */
    @RequestMapping("isLogin")
    @ResponseBody
    public String isLogin(@CookieValue(value = "loginToken", required = false) String loginTokin, String callback){
        System.out.println("判断是否已经登录"+loginTokin);
        ResultData<User> resultData = new ResultData<User>().setCode(ResultData.ResultCodeList.ERROR);
        if(loginTokin != null){

            User user = (User) redisTemplate.opsForValue().get(loginTokin);
            System.out.println("用户是"+user);
            if (user != null){
                resultData.setCode(ResultData.ResultCodeList.OK).setData(user);
            }
        }
        return callback != null ? callback + "(" + JSON.toJSONString(resultData) + ")" : JSON.toJSONString(resultData);
    }

    /**
     * 注销
     */
    @RequestMapping("logout")
    public String logout(@CookieValue(value = "loginToken" , required = false) String loginToken, HttpServletResponse response){

        redisTemplate.delete(loginToken);
        Cookie cookie = new Cookie("loginToken", null);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
        return "redirect:/sso/toLogin";
    }

}
