package com.keith.modules.service.ExtProduct;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.modules.entity.ExtProduct.ExtProduct;
import com.keith.modules.form.ExtProResult;

import java.util.List;

/**
 * 铺货商品表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-05 14:52:58
 */
public interface ExtProductService extends IService<ExtProduct> {


    /**
     * 铺货接口
     * @param productIds 上传的产品ID
     * @param userMemberId 用户ID
     * @param type 铺货类型   1-现售铺货  2-云仓铺货  3-预售铺货
     * @return
     */
    ExtProResult extProductNormal(List<Long> productIds, long userMemberId, int type);

    Page<ExtProResult> findProductNormal(Integer currentPage,Integer size,long userId,int status,int type);

    boolean updateExtProduct(ExtProResult extProResult,Long userMemberId);
}

