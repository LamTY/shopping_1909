package com.qf.controller;

import com.qf.entity.ResultData;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private String uploadPath = "G:/idea_workspace/imgs";

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

    @RequestMapping("/uploader")
    @ResponseBody
    public ResultData<String> uploader(MultipartFile file){
        String filename= UUID.randomUUID().toString();
        String path=uploadPath+"/"+filename;
        try (
                InputStream inputStream = file.getInputStream();
                OutputStream OutputStream = new FileOutputStream(path);
             ){
            IOUtils.copy(inputStream,OutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultData<String>().setCode(ResultData.ResultCodeList.OK).setData(path);
    }

    @RequestMapping("showimg")
    public void showimg(String imgPath, HttpServletResponse response){
        try (
                InputStream inputStream = new FileInputStream(imgPath);
                ServletOutputStream outputStream = response.getOutputStream();
                ){
            IOUtils.copy(inputStream,outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
