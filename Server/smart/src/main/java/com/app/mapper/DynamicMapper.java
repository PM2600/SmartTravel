package com.app.mapper;

import com.app.entity.Dynamic;
import com.app.entity.News;
import com.app.vo.DynamicView;

import java.util.List;

public interface DynamicMapper {
    List<DynamicView> getDynamic();

    Integer releaseDynamic(Dynamic dynamic);
}
