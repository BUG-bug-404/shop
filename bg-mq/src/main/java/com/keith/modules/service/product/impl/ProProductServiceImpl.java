package com.keith.modules.service.product.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.modules.dao.product.ProProductDao;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.service.product.ProProductService;
import org.springframework.stereotype.Service;




@Service("proProductService")
public class ProProductServiceImpl extends ServiceImpl<ProProductDao, ProProduct> implements ProProductService {



}
