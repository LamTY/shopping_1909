package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.entity.Goods;
import com.qf.entity.ResultData;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private IGoodsService goodsService;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;


    @RequestMapping("/list")
    public String list(Model model){
        List<Goods> goodsList=goodsService.list();
        model.addAttribute("goodsList",goodsList);
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
//        String filename= UUID.randomUUID().toString();
//        String path=uploadPath+"/"+filename;
//        try (
//                InputStream inputStream = file.getInputStream();
//                OutputStream OutputStream = new FileOutputStream(path);
//             ){
//            IOUtils.copy(inputStream,OutputStream);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        System.out.println("file是"+file);
        String path=null;
        try {
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                    file.getInputStream(),
                    file.getSize(),
                    "JPG",
                    null

            );
            System.out.println("dsads21321321");
            path=storePath.getFullPath();
            System.out.println("path"+path);
        } catch (Exception e) {
            e.printStackTrace();

        }

        System.out.println("这个傻逼玩意");
        return new ResultData<String>().setCode(ResultData.ResultCodeList.OK).setData("http://www.img.com:8080/"+path);
    }

/*    @RequestMapping("/showimg")
    public void showimg(String imgPath, HttpServletResponse response){
        System.out.println("路径"+imgPath);
        try (
                InputStream inputStream = new FileInputStream(imgPath);
                ServletOutputStream outputStream = response.getOutputStream();
                ){
            IOUtils.copy(inputStream,outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/

    @RequestMapping("insert")
    public String insert(Goods goods){
        System.out.println(goods);
        goodsService.insert(goods);
        return "redirect:/goods/list";
    }
}
