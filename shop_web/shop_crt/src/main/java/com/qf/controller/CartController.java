package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.aop.IsLogin;
import com.qf.aop.LoginStatus;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 加入购物车
     * @return

    @RequestMapping("insert")
    @ResponseBody
    @IsLogin(mustLogin = true)
    public Object  insert(Integer id , Integer number, String callback, HttpServletRequest request){
        System.out.println("购物车");
        System.out.println(id+" ------>>>>"+number);
        User user = LoginStatus.getUser();
        ResultData resultData=null;
        if(user==null){
            String returnUrl =  "http://localhost:8891/cart/insert?" +request.getQueryString();
            try {
                returnUrl=URLEncoder.encode(returnUrl,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            resultData = new ResultData<>().setCode(ResultData.ResultCodeList.ERROR).setMsg(returnUrl);

        }else {
            resultData = new ResultData<>().setCode(ResultData.ResultCodeList.OK).setMsg("添加成功");
        }

        if(callback != null){
            return callback + "("+ JSON.toJSONString(resultData)+")";
        }else {
            return "redirect:/item/showById?id= "+id;
        }


    }
     */




    @IsLogin(mustLogin = true)
    @RequestMapping("/insert")
    public String insert(Integer id, Integer number, @CookieValue(value = "cartToken",required = false) String cartToken, HttpServletResponse response){
        System.out.println("加入购物车：" + id + "---" + number);

        User user = LoginStatus.getUser();
        System.out.println("当前的登录信息：" + user);

        //调用购物车服务添加购物车

        ShopCart shopCart = new ShopCart().setGid(id).setNumber(number);
        cartService.insertCart(shopCart, user, cartToken);
        Cookie cookie = new Cookie("cartToken", cartToken);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "insertok";
    }

    @RequestMapping("list")
    @IsLogin(mustLogin = true)
    public  String list(@CookieValue(value = "cartToken",required = false) String cartToken,String callback){

        User user = LoginStatus.getUser();
        List<ShopCart> shopCarts = cartService.listCarts(cartToken, user);
        return callback!=null ? callback + "("+ JSON.toJSONString(shopCarts) +")" : JSON.toJSONString(shopCarts);
    }

    @RequestMapping("merge")
    @IsLogin(mustLogin = true)
    public String merge(@CookieValue(value = "cartToken",required = false) String cartToken, String returnUrl, HttpServletResponse response){

        if(cartToken != null){
            List<ShopCart> cartList = redisTemplate.opsForList().range("cartToken", 0, redisTemplate.opsForList().size("cartToken"));

            User user = LoginStatus.getUser();
            for (ShopCart cart : cartList) {
                cartService.insertCart(cart, user ,cartToken);
            }

            redisTemplate.delete(cartToken);

            Cookie cookie = new Cookie("cartToken", null);

            cookie.setMaxAge(0);

            cookie.setPath("/");

            response.addCookie(cookie);
        }

        return "redirect" + returnUrl;
    }
}
