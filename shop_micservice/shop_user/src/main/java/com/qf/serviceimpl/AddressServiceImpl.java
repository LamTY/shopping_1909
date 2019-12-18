package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.AddressMapper;
import com.qf.entity.Address;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> queryByUid(Integer uid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", uid);
        return addressMapper.selectList(queryWrapper);
    }

    @Override
    public Address queryById(Integer id) {
        return addressMapper.selectById(id);
    }


    public int insertAddress(Address address, User user) {

        address.setUid(user.getId());
        if(address.getIsdefault() == 1){

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid", user.getId());
            queryWrapper.eq("isdefault", 1);
            Address defaultAddress = addressMapper.selectOne(queryWrapper);
            defaultAddress.setIsdefault(0);
            addressMapper.updateById(defaultAddress);
        }
        return addressMapper.insert(address);
    }
}
