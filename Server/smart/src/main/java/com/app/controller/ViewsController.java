package com.app.controller;

import com.app.service.ViewService;
import com.app.vo.ResultDynamic;
import com.app.vo.ResultViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("view")
public class ViewsController {
    @Autowired
    private ViewService viewService;

    @GetMapping("getviews")
    public ResultViews getViews(){
        ResultViews result = viewService.getViews();
        System.out.println(result);
        return result;
    }
}
