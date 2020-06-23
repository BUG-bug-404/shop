package com.keith.modules.service.template.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.template.ProFeightProductDao;
import com.keith.modules.entity.template.ProFeightProduct;
import com.keith.modules.service.template.ProFeightProductService;


@Service("proFeightProductService")
public class ProFeightProductServiceImpl extends ServiceImpl<ProFeightProductDao, ProFeightProduct> implements ProFeightProductService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProFeightProduct> page = this.page(
                new Query<ProFeightProduct>().getPage(params),
                new QueryWrapper<ProFeightProduct>()
        );

        return new PageUtils(page);
    }

}
