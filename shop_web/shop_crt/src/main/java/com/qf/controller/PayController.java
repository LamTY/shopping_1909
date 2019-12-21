package com.qf.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qf.entity.Orders;
import com.qf.service.IOrderService;
import com.qf.util.AlipayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/pay")
@Controller
public class PayController {

    private IOrderService orderService;

    @RequestMapping("/alipay")
    @ResponseBody
    public void alipay(String orderid, HttpServletRequest httpRequest,
                       HttpServletResponse httpResponse) throws IOException {

        Orders orders = orderService.QueryByOid(orderid);

        AlipayClient alipayClient = AlipayUtil.getAlipayClient();
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://localhost:8891/orders/list");
        alipayRequest.setNotifyUrl("http://baidu.337337.cc");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+orders.getOrderid()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+orders.getAllprice().doubleValue()+"," +
                "    \"subject\":\""+orders.getOrderDetils().get(0).getSubject()+"...\"," +
                "    \"body\":\"Iphone6 16G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=UTF-8");
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }


    @RequestMapping("/payCallback")
    @ResponseBody
    public String payCallBack(HttpServletRequest request, String out_trade_no, String trade_status) throws AlipayApiException{
        Map<String, String> signParams = new HashMap<>();
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            signParams.put(entry.getKey(), entry.getValue()[0]);
        }
        //验签的过程
        boolean signVerified = AlipaySignature.rsaCheckV1(signParams, AlipayUtil.publicKey, signParams.get("charset"), signParams.get("sign_type")); //调用SDK验证签名
        if(signVerified){
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure

            //验证out_trade_no是否存在
            //判断total_amount和对应的订单总金额是否一致
            //验证seller_id是否匹配
            //验证app_id是否为当前商户的appid

            if(trade_status.equals("TRADE_SUCCESS") || trade_status.equals("TRADE_FINISHED")){
                //支付成功，修改订单状态
                orderService.updateOrderStatus(out_trade_no, 1);
                return "success";
            }
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            System.out.println("支付验证失败！");
        }
        return "failure";


    }
}
