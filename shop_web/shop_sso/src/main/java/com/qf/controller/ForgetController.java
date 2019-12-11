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
                .setContext("点击<a href='"+url+"'>这里</a>找回密码")
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
}
