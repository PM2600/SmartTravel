package com.app.service.impl;

import com.app.entity.Advise;
import com.app.entity.User;
import com.app.mapper.UserMapper;
import com.app.service.UserService;
import com.app.service.ex.InsertException;
import com.app.service.ex.UpdateException;
import com.app.service.ex.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    @Override
    public User getUserInfo(String name) {
        User user = userMapper.getUserInfo(name);
        return user;
    }

    @Override
    public Integer submitAdvise(Integer uid, String content) {
        Advise advise = new Advise();
        advise.setUid(uid);
        advise.setContent(content);
        Date date = new Date();
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        Timestamp time = Timestamp.valueOf(nowTime);
        advise.setCreateTime(time);
        int row = userMapper.submitAdvise(advise);
        if(row != 1){
            throw new InsertException("提交建议时出现未知错误");
        }
        return 200;
    }

    @Override
    public Integer Update(Integer uid, String nickname, String phone, String address, Integer sex) {
//        User user = new User();
//        user.setNickname(nickname);
//        user.setPhone(phone);
//        user.setSex(sex);
        int row = userMapper.Update(uid, nickname, phone, address, sex);
        if(row != 1){
            throw new InsertException("更新用户信息时出现未知错误");
        }
        return 200;
    }

}
