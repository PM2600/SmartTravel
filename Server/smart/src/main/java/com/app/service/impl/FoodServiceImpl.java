package com.app.service.impl;

import com.app.entity.Food;
import com.app.entity.Views;
import com.app.mapper.FoodMapper;
import com.app.service.FoodService;
import com.app.vo.ResultFood;
import com.app.vo.ResultViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {
    @Autowired
    private FoodMapper foodMapper;

    @Override
    public ResultFood getFood() {
        List<Food> list = foodMapper.getFood();
        ResultFood result = new ResultFood();
        result.setTotal(list.size());
        result.setState(200);
        result.setRows(list);
        return result;
    }
}
