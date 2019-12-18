package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.CartMapper;
import com.qf.entity.Goods;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加购物车
     * @param shopCart
     * @param user
     * @param cartToken
     * @return
     */
    @Override
    public String insertCart(ShopCart shopCart, User user, String cartToken) {

        Goods goods = goodsService.queryById(shopCart.getGid());
        shopCart.setCartPrice(goods.getPrice().multiply(BigDecimal.valueOf(shopCart.getNumber())));

        if(user != null){

            shopCart.setUid(user.getId());
            cartMapper.insert(shopCart);

        }else {

            cartToken = cartToken != null?cartToken : UUID.randomUUID().toString();
            redisTemplate.opsForList().leftPush(cartToken, shopCart);
        }

        return cartToken;
    }

    @Override
    public List<ShopCart> listCarts(String cartToken, User user) {
        List<ShopCart> shopCarts = null;

        if (user != null){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid", user.getId());
            queryWrapper.orderByDesc("create_time");
            shopCarts = cartMapper.selectList(queryWrapper);

        }else {
            if (cartToken != null){

                shopCarts = redisTemplate.opsForList().range(cartToken,0,redisTemplate.opsForList().size(cartToken));
            }
        }

        for (ShopCart cart : shopCarts) {
            Goods goods = goodsService.queryById(cart.getGid());
            cart.setGoods(goods);
        }
        return shopCarts;
    }




    @Override
    public List<ShopCart> queryCartsByGid(Integer[] gid, User user) {

        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq("uid", user.getId());

        queryWrapper.in("gid", gid);

        List<ShopCart> carts = cartMapper.selectList(queryWrapper);

        for (ShopCart cart : carts) {
            Goods goods = goodsService.queryById(user.getId());

            cart.setGoods(goods);
        }

        return carts;
    }
}
