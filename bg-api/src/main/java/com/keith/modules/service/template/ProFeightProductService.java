package com.keith.modules.service.template;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.template.ProFeightProduct;

import java.util.Map;

/**
 * 
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-08 10:58:50
 */
public interface ProFeightProductService extends IService<ProFeightProduct> {

    PageUtils queryPage(Map<String, Object> params);
}

