package com.keith.modules.service.product;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.product.ProProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 商品表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 09:39:17
 */
public interface ProProductService extends IService<ProProduct> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询首页商品
     * @return
     */
    List<ProProduct> findAll(int page, int pageSize);

    /**
     * 查询商品详情
     * @param
     * @return
     */
    ProProduct findById(Long parentId);

    /**
     * 货源查询商品*/
    Page<ProProduct> getProList(BigDecimal lowPrice, BigDecimal highPrice,
                                Long categoryId, Long sortType, Integer currPage, Integer pageSize);

    /**
     * 根据名称查询商品数据
     * @param currentPage  页码
     * @param pageSize 每页数量
     * @param name 名称或其他
     * @return
     */
    Page<ProProduct> findByName(int currentPage,int pageSize,String name);
}

