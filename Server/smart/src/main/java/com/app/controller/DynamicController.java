package com.app.controller;

import com.app.service.DynamicService;
import com.app.util.JsonResult;
import com.app.vo.ResultDynamic;
import com.app.vo.ResultNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dynamic")
public class DynamicController {
    @Autowired
    private DynamicService dynamicService;

    @GetMapping("getdynamic")
    public ResultDynamic getDynamic(){
        ResultDynamic result = dynamicService.getDynamic();
        System.out.println(result);
        return result;
    }

    @PostMapping("release")
    public JsonResult<Void> releaseDynamic(String uid, String dytext){
        dynamicService.releaseDynamic(uid, dytext);
        return new JsonResult<Void>(200);
    }
}
