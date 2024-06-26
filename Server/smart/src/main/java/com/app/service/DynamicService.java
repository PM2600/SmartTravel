package com.app.service;

import com.app.vo.ResultDynamic;

public interface DynamicService {

    ResultDynamic getDynamic();

    Integer releaseDynamic(String uid, String dytext);
}
