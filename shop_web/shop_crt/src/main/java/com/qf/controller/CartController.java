package com.qf.controller;

import com.alibaba.fastjson.JSON;
import com.qf.aop.IsLogin;
import com.qf.aop.LoginStatus;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cart")
public class CartController {

    /**
     * 加入购物车
     * @return
     */
    @RequestMapping("insert")
    @ResponseBody
    @IsLogin(mustLogin = true)
    public String insert(Integer id , Integer number,String callback){
        System.out.println("购物车");
        System.out.println(id+" ------>>>>"+number);
        User user = LoginStatus.getUser();
        ResultData resultData=null;
        if(user==null){
            resultData = new ResultData<>().setCode(ResultData.ResultCodeList.ERROR);
        }else {
            resultData = new ResultData<>().setCode(ResultData.ResultCodeList.OK).setMsg("添加成功");
        }
        return callback + "("+ JSON.toJSONString(resultData)+")";
    }
}
