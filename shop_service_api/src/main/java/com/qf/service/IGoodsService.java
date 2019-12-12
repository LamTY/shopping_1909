package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface IGoodsService {


    void insert(Goods goods);

    List<Goods> list();

    Goods queryById(Integer id);
}
