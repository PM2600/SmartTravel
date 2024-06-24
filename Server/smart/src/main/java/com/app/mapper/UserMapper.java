package com.app.mapper;

import com.app.entity.Advise;
import com.app.entity.User;
import org.apache.ibatis.annotations.Param;

import java.net.Inet4Address;
import java.util.Date;

public interface UserMapper {

    User findPwdByName(String username);

    Integer userInsert(User user);

    User getUserInfo(String name);

    Integer submitAdvise(Advise advise);

    Integer Update(Integer uid, String nickname, String phone, String address, Integer sex);
}
