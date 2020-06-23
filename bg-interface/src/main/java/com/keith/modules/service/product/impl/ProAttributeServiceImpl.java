package com.keith.modules.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.product.ProAttributeDao;
import com.keith.modules.entity.product.ProAttribute;
import com.keith.modules.service.product.ProAttributeService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("proAttributeService")
public class ProAttributeServiceImpl extends ServiceImpl<ProAttributeDao, ProAttribute> implements ProAttributeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProAttribute> page = this.page(
                new Query<ProAttribute>().getPage(params),
                new QueryWrapper<ProAttribute>()
        );

        return new PageUtils(page);
    }

}
