package com.qf.util;

import com.qf.entity.ShopCart;

import java.math.BigDecimal;
import java.util.List;

public class PriceUtil {

    public static double allprice(List<ShopCart> shopCarts){

        BigDecimal allprice = BigDecimal.valueOf(0);

        if (shopCarts != null){
            for (ShopCart cart : shopCarts) {
                allprice = allprice.add(cart.getCartPrice());
            }
        }

        return allprice.doubleValue();
    }
}
