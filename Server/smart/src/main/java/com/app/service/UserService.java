package com.app.service;

import com.app.entity.User;

public interface UserService {

    Integer findPwdByName(String name, String pwd);

    Integer userInsert(User user);
}
