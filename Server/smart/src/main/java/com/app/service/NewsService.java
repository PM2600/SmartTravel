package com.app.service;

import com.app.entity.News;
import com.app.vo.ResultNews;

import java.util.List;

public interface NewsService {
    ResultNews getNews();

    //List<News> getNews();
}
