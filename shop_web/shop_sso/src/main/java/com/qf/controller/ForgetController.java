package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Email;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/forget")
public class ForgetController {

    @Reference
    private IUserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/toForgetPassword")
    public String toForgetPassword(){
        return "forgetpassword";
    }

    /**
     * 忘记密码
     */
    @RequestMapping("/sendmail")
    @ResponseBody
    public ResultData<Map<String, String>> sendmail(String username){
        User user=userService.queryByUserName(username);
        if(user == null){
            return new ResultData<Map<String, String>>()
                    .setCode(ResultData.ResultCodeList.ERROR)
                    .setMsg("用户不存在");
        }
        String uuid= UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("uuid",username);
        redisTemplate.expire(uuid,5, TimeUnit.MINUTES);

        String url="http://localhost:8890/forget/toUpdatePassword?token=" + uuid;

        Email email=new Email()
                .setTo(user.getEmail())
                .setSubject("找回通往世界的钥匙")
                .setContext("a href='"+url+"'>骚年，你渴望力量吗</a>")
                .setSendTime(new Date());

        rabbitTemplate.convertAndSend("mail_exchange","",email);

        String emailStr=user.getEmail();
        String emailSub=emailStr.substring(3,emailStr.indexOf("@"));
        String emailEnd=emailStr.replace(emailSub,"*****");
        String emailTo="mail" + emailStr.substring(emailStr.lastIndexOf("@") + 1);
        Map<String , String> emailMap=new HashMap<>();
        emailMap.put("emailEnd",emailEnd);
        emailMap.put("emailTo",emailTo);


        return new ResultData<Map<String, String>>()
                .setCode(ResultData.ResultCodeList.OK)
                .setMsg("发送成功")
                .setData(emailMap);
    }

    @RequestMapping("toUpdatePassword")
    public String toUpdatePassword(){
        return "updatapassword";
    }

    @RequestMapping("updatePassword")
    public String updatePassword(String newpassword,String token){
        System.out.println("修改密码");
        String username = redisTemplate.opsForValue().get("token");
        if(username == null){
            return "updatapassworderror";
        }
        userService.updataPassword(username,newpassword);

        return "login";
    }
}
