package com.keith.modules.service.order.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.exception.RRException;
import com.keith.common.utils.PageUtils;
import com.keith.modules.dao.order.OrderCartItemDao;
import com.keith.modules.entity.order.OrderCartItem;
import com.keith.modules.service.order.OrderCartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("orderCartItemService")
public class OrderCartItemServiceImpl extends ServiceImpl<OrderCartItemDao, OrderCartItem> implements OrderCartItemService {


}
