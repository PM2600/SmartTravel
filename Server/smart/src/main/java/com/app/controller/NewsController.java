package com.app.controller;

import com.app.entity.News;
import com.app.service.NewsService;
import com.app.service.impl.NewsServiceImpl;
import com.app.util.JsonResult;
import com.app.vo.ResultNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("news")
public class NewsController extends BaseController{
    @Autowired
    private NewsService newsService;

    @GetMapping("getnews")
    public ResultNews getNews(){
        ResultNews result = newsService.getNews();
        //System.out.println(result);
        return result;
    }

//    @GetMapping("getnews")
//    public List<News> getNews(){
//        List<News> result = newsService.getNews();
//        //System.out.println(result);
//        return result;
//    }
}
