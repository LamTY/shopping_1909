package com.qf.exception;

import com.qf.entity.ResultData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    @ResponseBody
    public Object exceptiongHandler(HttpServletRequest request,Exception e){
        String header = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(header)){
            return new ResultData<>().setCode(ResultData.ResultCodeList.ERROR).setMsg("嘿嘿，没想到吧，又是我");
        }else {
            return new ModelAndView("myError") ;
        }

    }

}
