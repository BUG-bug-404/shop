package com.keith.modules.service.template;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.template.ProFeightTemplate;

import java.util.Map;

/**
 * 大运费模版表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-08 10:58:36
 */
public interface ProFeightTemplateService extends IService<ProFeightTemplate> {

    PageUtils queryPage(Map<String, Object> params);
}

