package com.app.service;

import com.app.entity.Love;
import com.app.entity.Product;
import com.app.vo.Detail;
import com.app.vo.Display;

import java.util.List;

public interface ProductService {
    /**
     * 用户上传商品
     * @param pro 商品对象
     * @return 影响的行数
     */
    void proInsert(Product pro);

    /**
     * 显示销售商品列表或交换商品列表
     * @param type 商品类别
     * @return 该类全部商品
     */
    List<Display> findByType(Integer type);

    /**
     * 根据用户手机查询该用户发布的商品
     * @param phone 用户手机号
     * @return 商品列表
     */
    List<Display> userPro(String phone);

    /**
     * 显示具体的商品
     * @param pid 商品id
     * @return 返回商品详细信息
     */
    Detail show(Integer pid);

    /**
     * 删除商品
     * @param pid 商品id
     */
    void proDelete(Integer pid);

    /**
     * 下架商品
     * @param pid 商品id
     */
    void proOff(Integer pid);

    /**
     * 查找某种状态商品（是否存在）
     * @param pid 商品id
     * @param status 商品状态
     * @return 商品信息
     */
    Product findById(Integer pid, Integer status);

    /**
     * 根据pid查找商品图片路径
     * @param pid 商品id
     * @return 商品对象
     */
    Product findImage(Integer pid);

    /**
     * 根据pid查找用户电话
     * @param pid 商品id
     * @return 对象
     */
    Product findPhone(Integer pid);

    /**
     * 给商品点赞
     * @param pid 商品id
     * @param phone 用户电话
     */
    void giveLike(Integer pid, String phone);

    /**
     * 商品点击量加 1
     * @param pid 商品id
     */
    void clickPro(Integer pid);

    /**
     * 点击排行榜
     * @return 商品列表（前10）
     */
    List<Display> clickChart();

    /**
     * 点赞排行榜
     * @return 商品列表（前10）
     */
    List<Display> likeChart();

    /**
     * 热度排行榜
     * @return 商品列表（前10）
     */
    List<Display> heatChart();


    String getAddr(String url);
}
