package com.app.service;

import com.app.entity.User;

public interface UserService {

    Integer findPwdByName(String name, String pwd);

    Integer userInsert(User user);

    User getUserInfo(String name);

    Integer submitAdvise(Integer uid, String content);

    Integer Update(Integer uid, String nickname, String phone, String address, Integer sex);
}
