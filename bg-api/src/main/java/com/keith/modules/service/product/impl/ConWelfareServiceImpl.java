package com.keith.modules.service.product.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.modules.dao.product.ConWelfareDao;
import com.keith.modules.dao.product.ProSkuStockDao;
import com.keith.modules.entity.product.ConWelfare;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.service.product.ConWelfareService;
import com.keith.modules.service.product.ProProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("conWelfareService")
public class ConWelfareServiceImpl extends ServiceImpl<ConWelfareDao, ConWelfare> implements ConWelfareService {
    @Autowired
    private ProProductService proProductService;
    @Autowired
    private ProSkuStockDao proSkuStockDao;

    @Override
    public PageUtils list(Integer currentPage,Integer pageSize) {

        QueryWrapper<ConWelfare> wrapper = new QueryWrapper<>();
        wrapper.eq("publish_status",1);
        IPage<ConWelfare> page = new Page<>(currentPage,pageSize);
        page = this.baseMapper.selectPage(page, wrapper);
        page.getRecords().stream().forEach(con->{
            ProSkuStock proSku = proSkuStockDao.selectById(con.getProSkuStockId());
            con.setProSkuStock(proSku);
        });
        return new PageUtils(page);
    }






}
