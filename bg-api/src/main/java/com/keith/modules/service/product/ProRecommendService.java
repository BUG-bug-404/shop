package com.keith.modules.service.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keith.modules.entity.product.ProProduct;

/**
 * 用户商品推荐
 *
 * @author gray
 * @version 1.0
 * @date 2020/6/9 10:55
 */
public interface ProRecommendService {

    /**
     * 收藏夹推荐
     * @param currentPage
     * @param pageSize
     * @param userId 用户ID
     * @return
     */
    public Page<ProProduct> findRecommendByCollect(int currentPage,int pageSize,long userId);
}
