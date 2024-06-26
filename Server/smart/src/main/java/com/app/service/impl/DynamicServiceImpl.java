package com.app.service.impl;

import com.app.entity.Dynamic;
import com.app.mapper.DynamicMapper;
import com.app.mapper.NewsMapper;
import com.app.service.DynamicService;
import com.app.service.ex.InsertException;
import com.app.vo.DynamicView;
import com.app.vo.ResultDynamic;
import com.app.vo.ResultNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Override
    public Integer releaseDynamic(String uid, String dytext) {
        Integer id = Integer.parseInt(uid);
        Dynamic dynamic = new Dynamic();
        dynamic.setUid(id);
        dynamic.setDytext(dytext);
        dynamic.setDylike(0);
        Date date = new Date();
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        System.out.println(nowTime);
        dynamic.setDycommit_time(nowTime);
        int row = dynamicMapper.releaseDynamic(dynamic);
        if(row != 1){
            throw new InsertException("发布动态时出现未知错误");
        }
        return row;
    }
}
