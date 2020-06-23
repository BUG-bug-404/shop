package com.keith.modules.service.ExtProduct.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.modules.dao.ExtProduct.ExtProductSkuDao;
import com.keith.modules.entity.ExtProduct.ExtProductSku;
import com.keith.modules.service.ExtProduct.ExtProductSkuService;
import org.springframework.stereotype.Service;


@Service
public class ExtProductSkuServiceImpl extends ServiceImpl<ExtProductSkuDao, ExtProductSku> implements ExtProductSkuService {

}
