package com.keith.modules.service.ExtProduct.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.modules.dao.ExtProduct.ExtProductDao;
import com.keith.modules.entity.ExtProduct.ExtProduct;
import com.keith.modules.service.ExtProduct.ExtProductService;
import org.springframework.stereotype.Service;

@Service("extProductService")
public class ExtProductServiceImpl extends ServiceImpl<ExtProductDao, ExtProduct> implements ExtProductService {

}
