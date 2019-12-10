package com.qf.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.UserMapper;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public int register(User user) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",user.getUsername());
        int result=userMapper.selectCount(queryWrapper);
        if(result >0){
            return 0;
        }else {
            return userMapper.insert(user);
        }

    }
}
