package com.app.mapper;

import com.app.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface UserMapper {

    User findPwdByName(String username);

    Integer userInsert(User user);
}
