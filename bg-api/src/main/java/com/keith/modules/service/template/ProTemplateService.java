package com.keith.modules.service.template;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.template.ProTemplate;

import java.util.Map;

/**
 * 小运费模版
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-08 10:58:36
 */
public interface ProTemplateService extends IService<ProTemplate> {

    PageUtils queryPage(Map<String, Object> params);
}

