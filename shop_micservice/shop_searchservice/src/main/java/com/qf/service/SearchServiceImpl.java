package com.qf.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements ISearchService{

    @Autowired
    private SolrClient solrClient;

    @Override
    public int insertSolr(Goods goods) {
        SolrInputDocument document=new SolrInputDocument();
        document.addField("id",goods.getId());
        document.addField("subject", goods.getSubject());
        document.addField("info", goods.getInfo());
        document.addField("price", goods.getPrice().doubleValue());
        document.addField("save", goods.getSave());
        document.addField("image", goods.getFmurl());

        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Goods> querySolr(String keyword) {
        SolrQuery query=new SolrQuery();
        if(keyword != null && !keyword.equals("")){
            query.setQuery("subject:" + keyword + " || info:" + keyword);
        } else {
            query.setQuery("*:*");
        }
        try {
            QueryResponse response = solrClient.query(query);
            SolrDocumentList results = response.getResults();
            List<Goods> goodsList = new ArrayList<>();
            for (SolrDocument result : results) {
                Goods goods = new Goods();
                goods.setId(Integer.parseInt((String)result.get("id")));
                goods.setSubject(result.get("subject") + "")
                        .setSave((int)result.get("save"))
                        .setPrice(BigDecimal.valueOf((double)result.get("price")))
                        .setFmurl(result.get("image") + "");
                goodsList.add(goods);
            }
            return goodsList;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
