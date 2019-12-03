package com.qf.controller;

import com.qf.entity.ResultData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @RequestMapping("/list")
    public String list(){

        return "goodslist";
    }

    @RequestMapping("/ajax")
    @ResponseBody
    public ResultData<String> ajax(){
//        System.out.println(1/0);
        return new ResultData<String>().setCode(ResultData.ResultCodeList.OK).setData("MyData");
    }
}
