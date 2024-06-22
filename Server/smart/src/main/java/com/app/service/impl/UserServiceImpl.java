package com.app.service.impl;

import com.app.entity.User;
import com.app.mapper.UserMapper;
import com.app.service.UserService;
import com.app.service.ex.InsertException;
import com.app.service.ex.UpdateException;
import com.app.service.ex.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer findPwdByName(String name, String pwd) {
        User user = userMapper.findPwdByName(name);
        if(user == null){
            return 401;
        }
        user.setUsername(name);
        System.out.println("select name:" + user.getUsername());
        System.out.println("select pwd:" + user.getPassword());

        if(name.equals(user.getUsername()) && pwd.equals(user.getPassword())){
            return 200;
        }
        return 401;
    }

    @Override
    public Integer userInsert(User user) {
        User u = userMapper.findPwdByName(user.getUsername());
        if(u != null){
            //用户名已存在
            return 301;
        }
        int row = userMapper.userInsert(user);
        if(row != 1){
            //插入失败
            return 401;
        }
        return 200;
    }
}
