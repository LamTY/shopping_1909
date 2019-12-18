package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.IsLogin;
import com.qf.aop.LoginStatus;
import com.qf.entity.Address;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Reference
    private IAddressService addressService;



    /**
     * 添加地址
     * @param address
     * @return
     */
    @RequestMapping("insert")
    @IsLogin
    public ResultData<String> insert(Address address){

        User user = LoginStatus.getUser();
        addressService.insertAddress(address,user);

        return new ResultData<String>().setCode(ResultData.ResultCodeList.OK).setMsg("添加成功！");
    }

}
