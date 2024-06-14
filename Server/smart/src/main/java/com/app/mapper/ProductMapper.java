package com.app.mapper;

import com.app.entity.Love;
import com.app.entity.Product;
import com.app.entity.User;
import com.app.vo.Detail;
import com.app.vo.Display;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;

public interface ProductMapper {

    /**
     * 用户上传商品
     * @param pro 商品对象
     * @return 影响的行数
     */
    Integer proInsert(Product pro);

    /**
     * 显示销售商品列表或交换商品列表
     * @param type 商品类别
     * @return 该类中全部商品
     */
    List<Display> findByType(Integer type);

    /**
     * 根据用户电话查找商品
     * @param phone 用户电话
     * @return 商品对象
     */
    List<Display> findByPhone(String phone);
    //List<Display> findById(Integer uid);

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
     * 显示具体的商品
     * @param pid 商品id
     * @return 返回商品详细信息
     */
    Detail show(Integer pid);

    /**
     * 删除商品
     * @param pid 商品id
     * @return 影响的行数
     */
    Integer proDelete(Integer pid);

    /**
     * 下架商品
     * @param pid 商品id
     * @return 影响的行数
     */
    Integer proOff(Integer pid);

    /**
     * 查找某种状态商品（是否存在）
     * @param pid 商品id
     * @param status 商品状态
     * @return 商品信息
     */
    Product findById(Integer pid, Integer status);

    /**
     * 给商品点赞
     * @param pid 商品id
     * @return 影响的行数
     */
    Integer giveLike(Integer pid);

    /**
     * 取消点赞
     * @param pid 商品id
     * @return 影响的行数
     */
    Integer canLike(Integer pid);

    /**
     * 点赞表新添数据
     * @param pid 商品id
     * @param phone 用户电话
     * @return 影响的行数
     */
    Integer intoLove(Integer pid, String phone);

    /**
     * 查找点赞表中是否有数据
     * @param pid 商品id
     * @param phone 用户电话
     * @return Love实体
     */
    Love findLove(Integer pid, String phone);

    /**
     * 更新点赞状态
     * @param lid 点赞记录id
     * @param islove 状态
     * @return 影响的行数
     */
    Integer updateLove(Integer lid, Integer islove);

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

}