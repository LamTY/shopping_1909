package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.IsLogin;
import com.qf.aop.LoginStatus;
import com.qf.entity.Address;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.util.PriceUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("order")
@Controller
public class OrderController {

    @Reference
    private ICartService cartService;

    @Reference
    private IAddressService addressService;


    /**
     * 跳转到订单页面
     * @param gid
     * @param model
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping("toOrdersEdit")
    public String toOrdersEdit(Integer[] gid, Model model){

        User user = LoginStatus.getUser();

        List<ShopCart> shopCarts = cartService.queryCartsByGid(gid, user);

        List<Address> addresses = addressService.queryByUid(user.getId());

        double allprice = PriceUtil.allprice(shopCarts);

        model.addAttribute("carts", shopCarts);
        model.addAttribute("addresses", addresses);
        model.addAttribute("allprice", allprice);

        return "orderdetail";
    }
}
