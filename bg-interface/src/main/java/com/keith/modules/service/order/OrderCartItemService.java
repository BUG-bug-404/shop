package com.keith.modules.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.order.OrderCartItem;

import java.util.List;


/**
 * 进货车表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-03 16:26:54
 */
public interface OrderCartItemService extends IService<OrderCartItem> {

    /**加入进货车*/
    boolean add(Long productId,Long skuId,Integer count,Long userMemberId);

    /**从进货车减数量*/
    boolean deleteCount(Long productId, Long skuId, Integer count, Long userMemberId);

    /**查询进货车里的商品，查看商品是否失效，失效为供应商已删除、下架，或者平台将商品删除、下架*/
    PageUtils findAll(Integer currentPage, Integer pageSize,Long userMemberId,Integer status);

    /**
     * 从进货车中批量删除*/
    boolean deleteByIds(List<Long> ids,Long userMemberId);

    /**
     * 根据进货车里的商品推荐商品*/
    /**直接用货源的查找分类的*/

    /**修改规格*/
    boolean updateSku(Long id,Long skuId,Long productId,Long userMemberId,Long reSkuId,Integer count);


}

