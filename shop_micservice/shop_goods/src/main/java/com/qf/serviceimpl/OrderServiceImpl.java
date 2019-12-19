package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.dao.OrderDetilsMapper;
import com.qf.dao.OrdersMapper;
import com.qf.entity.*;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.service.IOrderService;
import com.qf.util.PriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderServiceImpl implements IOrderService {

    @Reference
    private IAddressService addressService;

    @Reference
    private ICartService cartService;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetilsMapper orderDetilsMapper;




    /**
     * 添加订单详情
     */
    @Override
    @Transactional
    public Orders insertOrder(Integer[] cids, Integer aid, User user) {

        Address address = addressService.queryById(aid);

        List<ShopCart> shopCarts = cartService.queryCartsByGid(cids, user);

        double allprice = PriceUtil.allprice(shopCarts);

        Orders orders = new Orders()
                .setOrderid(UUID.randomUUID().toString())
                .setUid(user.getId())
                .setAllprice(BigDecimal.valueOf(allprice))
                .setPhone(address.getPhone())
                .setCode(address.getCode())
                .setAddress(address.getAddress())
                .setPerson(address.getPerson());

        ordersMapper.insert(orders);

        for (ShopCart shopCart : shopCarts) {

            OrderDetils orderDetils = new OrderDetils()
                    .setGid(shopCart.getGid())
                    .setDetilsPrice(shopCart.getCartPrice())
                    .setNumber(shopCart.getNumber())
                    .setPrice(shopCart.getGoods().getPrice())
                    .setSubject(shopCart.getGoods().getSubject())
                    .setOid(orders.getId())
                    .setFmurl(shopCart.getGoods().getFmurl());

            orderDetilsMapper.insert(orderDetils);
        }

        cartService.deleteByCids(cids);

        return orders;
    }

    @Override
    public List<Orders> queryByUid(Integer uid) {
        return null;
    }

    @Override
    public Orders queryById(Integer id) {
        return null;
    }

    @Override
    public Orders QueryByOid(String oid) {
        return null;
    }

    @Override
    public int updateOrderStatus(String orderid, Integer status) {
        return 0;
    }
}
