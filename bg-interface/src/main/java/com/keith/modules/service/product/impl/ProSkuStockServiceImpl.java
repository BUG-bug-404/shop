package com.keith.modules.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.product.ProSkuStockDao;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.service.product.ProSkuStockService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("proSkuStockService")
public class ProSkuStockServiceImpl extends ServiceImpl<ProSkuStockDao, ProSkuStock> implements ProSkuStockService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProSkuStock> page = this.page(
                new Query<ProSkuStock>().getPage(params),
                new QueryWrapper<ProSkuStock>()
        );

        return new PageUtils(page);
    }

}
