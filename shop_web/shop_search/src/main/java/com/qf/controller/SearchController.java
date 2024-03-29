package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Reference
    private ISearchService searchService;

    @RequestMapping("searchByKeyword")
    public String searchByKeyword(String keyword, Model model){
        System.out.println("关键字"+keyword);
        List<Goods> goods=searchService.querySolr(keyword);
        model.addAttribute("goods",goods);
        return "searchlist";
    }
}
