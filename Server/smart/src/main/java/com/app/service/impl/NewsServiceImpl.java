package com.app.service.impl;

import com.app.entity.News;
import com.app.mapper.NewsMapper;
import com.app.mapper.UserMapper;
import com.app.service.NewsService;
import com.app.vo.ResultNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsMapper newsMapper;

    @Override
    public ResultNews getNews() {
        List<News> list = newsMapper.getNews();
        ResultNews result = new ResultNews();
        result.setTotal(list.size());
        result.setState(200);
        result.setRows(list);
        return result;
    }

//    @Override
//    public List<News> getNews() {
//        List<News> list = newsMapper.getNews();
//        return list;
//    }
}
