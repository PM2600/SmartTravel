package com.app.service.impl;

import com.app.entity.Dynamic;
import com.app.mapper.DynamicMapper;
import com.app.mapper.NewsMapper;
import com.app.service.DynamicService;
import com.app.vo.DynamicView;
import com.app.vo.ResultDynamic;
import com.app.vo.ResultNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DynamicServiceImpl implements DynamicService {

    @Autowired
    private DynamicMapper dynamicMapper;

    @Override
    public ResultDynamic getDynamic() {
        List<DynamicView> list = dynamicMapper.getDynamic();
        ResultDynamic result = new ResultDynamic();
        result.setTotal(list.size());
        result.setState(200);
        result.setRows(list);
        return result;
    }
}
