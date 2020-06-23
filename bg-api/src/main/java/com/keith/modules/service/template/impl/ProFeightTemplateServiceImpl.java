package com.keith.modules.service.template.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.template.ProFeightTemplateDao;
import com.keith.modules.entity.template.ProFeightTemplate;
import com.keith.modules.service.template.ProFeightTemplateService;


@Service("proFeightTemplateService")
public class ProFeightTemplateServiceImpl extends ServiceImpl<ProFeightTemplateDao, ProFeightTemplate> implements ProFeightTemplateService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProFeightTemplate> page = this.page(
                new Query<ProFeightTemplate>().getPage(params),
                new QueryWrapper<ProFeightTemplate>()
        );

        return new PageUtils(page);
    }

}
