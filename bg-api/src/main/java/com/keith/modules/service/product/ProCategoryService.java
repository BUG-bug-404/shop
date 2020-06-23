package com.keith.modules.service.product;


import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.modules.entity.product.ProCategory;

import java.util.List;

/**
 * 商品分类表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-02 13:38:48
 */
public interface ProCategoryService extends IService<ProCategory> {

    /**
     * 查找所有分类*/
    List<ProCategory> findAll();
}

