package com.qf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    /**
     * 加入购物车
     * @return

    @RequestMapping("insert")
    @ResponseBody
    @IsLogin(mustLogin = true)
    public Object  insert(Integer id , Integer number, String callback, HttpServletRequest request){
        System.out.println("购物车");
        System.out.println(id+" ------>>>>"+number);
        User user = LoginStatus.getUser();
        ResultData resultData=null;
        if(user==null){
            String returnUrl =  "http://localhost:8891/cart/insert?" +request.getQueryString();
            try {
                returnUrl=URLEncoder.encode(returnUrl,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            resultData = new ResultData<>().setCode(ResultData.ResultCodeList.ERROR).setMsg(returnUrl);

        }else {
            resultData = new ResultData<>().setCode(ResultData.ResultCodeList.OK).setMsg("添加成功");
        }
        String header = request.getHeader("X-Requested-With");
        System.out.println(header);
        if("XMLHttpRequest".equals(header)){
            return callback + "("+ JSON.toJSONString(resultData)+")";
        }else {
            return new ModelAndView("ok") ;
        }


    }
     */


}
