package com.app.controller;

import com.app.entity.User;
import com.app.service.UserService;
import com.app.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

@RestController
@RequestMapping("user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

//    @GetMapping("findPwd")
//    public JsonResult<User> getPwd(String username){
//        User user = userService.findPwdByName(username);
//        System.out.println("username:" + username);
//        return new JsonResult<User>(OK, user);
//    }

    @PostMapping("login")
    public JsonResult<Void> findPwdByName(String username, String password){
        System.out.println("username:" + username);
        System.out.println("password:" + password);
        int code = userService.findPwdByName(username, password);
        return new JsonResult<Void>(code);
    }

    @PostMapping("register")
    public JsonResult<Void> userInsert(String username, String nickname, String phone, String password, String sex){
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setPhone(phone);
        user.setPassword(password);
        user.setSex(Integer.parseInt(sex));

        System.out.println(username);
        System.out.println(nickname);
        System.out.println(phone);
        System.out.println(password);
        System.out.println(sex);

        int code = userService.userInsert(user);
        return new JsonResult<Void>(code);
    }

    @PostMapping("getinfo")
    public User getInfo(String username){
        User user = userService.getUserInfo(username);
        return user;
    }

    @PostMapping("submit")
    public JsonResult<Void> submitAdvise(String uid, String content){
        Integer id = Integer.parseInt(uid);
        int code = userService.submitAdvise(id, content);
        return new JsonResult<Void>(code);
    }

    @PostMapping("updateinfo")
    public JsonResult<Void> UpdateInfo(String uid, String nickname, String phone, String address, String sex){
        Integer id = Integer.parseInt(uid);
        Integer s = Integer.parseInt(sex);
        int code = userService.Update(id, nickname, phone, address, s);
        return new JsonResult<Void>(OK);
    }

}
