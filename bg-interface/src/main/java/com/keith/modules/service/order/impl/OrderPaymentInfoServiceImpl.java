package com.keith.modules.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.order.OrderPaymentInfoDao;
import com.keith.modules.entity.order.OrderPaymentInfo;
import com.keith.modules.service.order.OrderPaymentInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("orderPaymentInfoService")
public class OrderPaymentInfoServiceImpl extends ServiceImpl<OrderPaymentInfoDao, OrderPaymentInfo> implements OrderPaymentInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderPaymentInfo> page = this.page(
                new Query<OrderPaymentInfo>().getPage(params),
                new QueryWrapper<OrderPaymentInfo>()
        );

        return new PageUtils(page);
    }

}
