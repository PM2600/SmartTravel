package com.app.controller;

import com.app.service.FoodService;
import com.app.service.ViewService;
import com.app.vo.ResultFood;
import com.app.vo.ResultViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("food")
public class FoodController {
    @Autowired
    private FoodService foodService;

    @GetMapping("getfoods")
    public ResultFood getViews(){
        ResultFood result = foodService.getFood();
        System.out.println(result);
        return result;
    }
}
