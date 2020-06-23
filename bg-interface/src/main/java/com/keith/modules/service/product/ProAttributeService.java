package com.keith.modules.service.product;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.product.ProAttribute;

import java.util.Map;

/**
 * 商品属性表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 14:05:23
 */
public interface ProAttributeService extends IService<ProAttribute> {

    PageUtils queryPage(Map<String, Object> params);
}

