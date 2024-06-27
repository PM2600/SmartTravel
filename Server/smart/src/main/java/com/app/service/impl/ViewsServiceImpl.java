package com.app.service.impl;

import com.app.entity.Views;
import com.app.mapper.DynamicMapper;
import com.app.mapper.ViewsMapper;
import com.app.service.ViewService;
import com.app.vo.DynamicView;
import com.app.vo.ResultDynamic;
import com.app.vo.ResultViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewsServiceImpl implements ViewService {
    @Autowired
    private ViewsMapper viewsMapper;

    @Override
    public ResultViews getViews() {
        List<Views> list = viewsMapper.getViews();
        ResultViews result = new ResultViews();
        result.setTotal(list.size());
        result.setState(200);
        result.setRows(list);
        return result;
    }
}
