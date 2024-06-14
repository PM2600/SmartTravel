package com.app.service.impl;

import com.app.entity.Love;
import com.app.entity.Product;
import com.app.entity.User;
import com.app.mapper.ProductMapper;
import com.app.mapper.UserMapper;
import com.app.service.ProductService;
import com.app.service.ex.DeleteException;
import com.app.service.ex.InsertException;
import com.app.service.ex.ProductNotFoundException;
import com.app.service.ex.UpdateException;
import com.app.vo.Detail;
import com.app.vo.Display;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    

    @Autowired
    private UserMapper userMapper;

    @Override
    @Async
    public void proInsert(Product pro) {
        int row = productMapper.proInsert(pro);
        if(row != 1){
            throw new InsertException("录入商品数据时出现未知错误");
        }
    }

    @Override
    public List<Display> findByType(Integer type) {
        List<Display> list = productMapper.findByType(type);
        return list;
    }



    @Override
    public List<Display> userPro(String phone) {
        List<Display> list = productMapper.findByPhone(phone);
        return list;
    }

    @Override
    public Detail show(Integer pid) {
        Detail de = productMapper.show(pid);
        if (de == null) {
            throw new ProductNotFoundException("尝试访问的商品数据不存在");
        }
        return de;
    }

    @Override
    public void proDelete(Integer pid) {
        Integer row = productMapper.proDelete(pid);
        if(row != 1){
            throw new DeleteException("删除商品产生未知异常");
        }
    }

    @Override
    public void proOff(Integer pid) {
        Integer row = productMapper.proOff(pid);
        if(row != 1){
            throw new DeleteException("下架商品产生未知异常");
        }
    }

    @Override
    public Product findById(Integer pid, Integer status) {
        Product product = productMapper.findById(pid, status);
        return product;
    }

    @Override
    public Product findImage(Integer pid) {
        Product pro = productMapper.findImage(pid);
        return pro;
    }

    @Override
    public Product findPhone(Integer pid) {
        Product pro = productMapper.findPhone(pid);
        return pro;
    }

    @Override
    public void giveLike(Integer pid, String phone) {
        Love result = productMapper.findLove(pid, phone);

        if (result == null) {
            // 表中无记录，插入数据
            Integer row1 = productMapper.intoLove(pid, phone);
            if (row1 != 1) {
                throw new InsertException("点赞产生未知异常");
            }

            Integer row2 = productMapper.giveLike(pid);
            if (row2 != 1) {
                throw new UpdateException("点赞产生未知异常");
            }
        }else {
            // 表中有数据，检查是否已经点赞
            if (result.getIslove() == 1) {
                Integer islove = 0;
                Integer r = productMapper.updateLove(result.getLid(), islove);
                if (r != 1) {
                    throw new UpdateException("点赞产生未知异常");
                }
                // 已点赞
                productMapper.canLike(pid);
            } else {
                Integer islove = 1;
                Integer r = productMapper.updateLove(result.getLid(), islove);
                if (r != 1) {
                    throw new UpdateException("点赞产生未知异常");
                }
                // 未点赞
                productMapper.giveLike(pid);
            }
        }
    }


    @Override
    public void clickPro(Integer pid) {
        productMapper.clickPro(pid);
    }

    @Override
    public List<Display> clickChart() {
        List<Display> list = productMapper.clickChart();
        return list;
    }

    @Override
    public List<Display> likeChart() {
        List<Display> list = productMapper.likeChart();
        return list;
    }

    @Override
    public List<Display> heatChart() {
        List<Display> list = productMapper.heatChart();
        return list;
    }

    @Override
    public String getAddr(String url){
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> respons1 = template.getForEntity(url, String.class);
        return respons1.getBody();
    }

}
