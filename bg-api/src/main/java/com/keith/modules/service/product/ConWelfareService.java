package com.keith.modules.service.product;


import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.product.ConWelfare;

/**
 * 福利商品
 *
 * @author lzy
 * @email ********
 * @date 2020-06-13 14:55:11
 */
public interface ConWelfareService extends IService<ConWelfare> {

    PageUtils list(Integer currentPage,Integer pageSize);









}

