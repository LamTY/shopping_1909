package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.GoodsLIstMapper;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.entity.GoodsImages;
import com.qf.service.IGoodsService;
import com.qf.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsService implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsLIstMapper goodsLIstMapper;

    @Reference
    private ISearchService searchService;

    @Override
    @Transactional
    public void insert(Goods goods) {
        goodsMapper.insert(goods);
        GoodsImages gi1=new GoodsImages();
        gi1.setGid(goods.getId())
        .setIsfengmian(1)
        .setUrl(goods.getFmurl());
        goodsLIstMapper.insert(gi1);

        for (String otherurl : goods.getOtherurls()) {
            System.out.println("fds"+otherurl);
            GoodsImages gi2=new GoodsImages();
            gi2.setGid(goods.getId())
                    .setIsfengmian(0)
                    .setUrl(otherurl);
            System.out.println(gi2);
            goodsLIstMapper.insert(gi2);
        }

        searchService.insertSolr(goods);

    }
    @Override
    public List<Goods> list() {

        return goodsMapper.queryList();
    }
}
