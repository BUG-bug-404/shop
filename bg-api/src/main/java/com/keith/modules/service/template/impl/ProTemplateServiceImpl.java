package com.keith.modules.service.template.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.template.ProTemplateDao;
import com.keith.modules.entity.template.ProTemplate;
import com.keith.modules.service.template.ProTemplateService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("proTemplateService")
public class ProTemplateServiceImpl extends ServiceImpl<ProTemplateDao, ProTemplate> implements ProTemplateService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProTemplate> page = this.page(
                new Query<ProTemplate>().getPage(params),
                new QueryWrapper<ProTemplate>()
        );

        return new PageUtils(page);
    }

}
