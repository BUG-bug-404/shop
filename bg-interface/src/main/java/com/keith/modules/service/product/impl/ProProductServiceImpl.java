package com.keith.modules.service.product.impl;


import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.keith.common.exception.RRException;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.product.ProAlbumDao;
import com.keith.modules.dao.product.ProProductDao;
import com.keith.modules.dao.product.ProSkuStockDao;
import com.keith.modules.dao.template.ProFeightProductDao;
import com.keith.modules.dao.template.ProFeightTemplateDao;
import com.keith.modules.dao.template.ProTemplateDao;
import com.keith.modules.entity.product.ProAlbum;
import com.keith.modules.entity.product.ProAttribute;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.entity.template.ProFeightProduct;
import com.keith.modules.entity.template.ProFeightTemplate;
import com.keith.modules.entity.template.ProTemplate;
import com.keith.modules.service.product.ProAlbumService;
import com.keith.modules.service.product.ProAttributeService;
import com.keith.modules.service.product.ProProductService;
import com.keith.modules.service.product.ProSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service("proProductService")
public class ProProductServiceImpl extends ServiceImpl<ProProductDao, ProProduct> implements ProProductService {

    @Autowired
    private ProAlbumDao albumDao;
    @Autowired
    private ProAlbumService albumService;

    @Autowired
    private ProAttributeService attributeService;
    @Autowired
    private ProProductDao proProductDao;

    @Autowired
    private ProSkuStockService skuStockService;

    @Autowired
    private ProSkuStockDao proSkuStockDao;

    @Autowired
    private ProFeightProductDao feightProductDao;
    @Autowired
    private ProFeightTemplateDao feightTemplateDao;
    @Autowired
    private ProTemplateDao templateDao;

    JSONObject json = new JSONObject();

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProProduct> page = this.page(
                new Query<ProProduct>().getPage(params),
                new QueryWrapper<ProProduct>()
        );

        return new PageUtils(page);
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<ProProduct> findAll(int page, int pageSize) {

        List<Long> id = new ArrayList<>();
        int pa = (page - 1) * pageSize;

        List<ProProduct> productEntityList = this.list(new QueryWrapper<ProProduct>()
                .isNotNull("parent_id").eq("delete_status", 0).eq("verify_status", 1).eq("publish_status", 1));



        for (ProProduct proProductEntity : productEntityList) {
            ProProduct proProduct = this.getOne(new QueryWrapper<ProProduct>().eq("id", proProductEntity.getParentId()).eq("publish_status", 1));




            if (proProduct != null) {
                id.add(proProduct.getId());
            }
        }

        /*List<ProProductEntity> productList=this.list(new QueryWrapper<ProProductEntity>()
                .isNotNull("parent_id").eq("delete_status",0).eq("verify_status",1)
                .eq("publish_status",1).in("parent_id",id));*/
        List<ProProduct> productList = proProductDao.selectAll(pa, pageSize);
        for (ProProduct productEntity : productList) {

            ProProduct proProduct = this.getOne(new QueryWrapper<ProProduct>().eq("id", productEntity.getId()).eq("publish_status", 1));

            if(proProduct!=null){
                List<ProSkuStock> list=proSkuStockDao.selectList(new QueryWrapper<ProSkuStock>().eq("product_id",proProduct.getId()));
                for (ProSkuStock skuStock:list){
                    redisTemplate.opsForValue().set(skuStock.getId()+"",skuStock.getStock()+"");
                }
            }
            ProAlbum albumEntity = albumDao.selectCoverStatus(productEntity.getId());
            productEntity.setPic(albumEntity == null ? null : albumEntity.getPic());
        }

        return productList;
    }

    @Override
    public ProProduct findById(Long productId) {

        ProProduct productEntity = this.getOne(new QueryWrapper<ProProduct>().eq("id", productId)
                .eq("delete_status", 0).eq("verify_status", 1)
                .eq("publish_status", 1));
        if (productEntity == null) {
            throw new RRException("商品信息有误！");
        }
        /**
         * 查询图片*/
        List<ProAlbum> albumEntityList = albumService.list(new QueryWrapper<ProAlbum>().eq("pic_owner", 1));
        productEntity.setAlbumEntityList(albumEntityList);

        //规格属性
        List<ProAttribute> attributeEntityList = attributeService.list(new QueryWrapper<ProAttribute>().eq("product_id", productEntity.getId()));
        //规格
        List<ProSkuStock> skuStockEntityList = skuStockService.list(new QueryWrapper<ProSkuStock>().eq("product_id", productEntity.getId()));
        Integer inventory=0;
        for (ProSkuStock skuStock:skuStockEntityList){
            inventory=inventory+skuStock.getStock();
        }

        if (productEntity.getUnifyStatus() == 0) {
            List<String> sp1 = proSkuStockDao.selectSp1(productId);
            List<String> sp2 = proSkuStockDao.selectSp2(productId);
            Map<String, Set<String>> map = Maps.newHashMap();
            for (int i = 0; i < sp1.size(); i++) {
                String key = sp1.get(i);
                String value = sp2.get(i);
                String[] splKey = key.split(",");
                String[] splValue = value.split(",");
                for (int k = 0; k < splKey.length; k++) {
                    Set<String> set = map.get(splKey[k]);
                    if (set == null || set.isEmpty()) {
                        set = Sets.newHashSet();
                    }
                    set.add(splValue[k]);
                    map.put(splKey[k], set);
                }

            }
            List<Map<String, Object>> result = Lists.newArrayList();
            Map<String, Object> one = null;
            for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
                one = Maps.newHashMap();
                one.put("name", entry.getKey());
                one.put("list", entry.getValue());
                result.add(one);
            }
            System.out.println(result);
            productEntity.setSku(result);
        }
        ProFeightProduct feightProduct = feightProductDao.selectOne(new QueryWrapper<ProFeightProduct>().eq("product_id", productEntity.getParentId()));
        if (feightProduct == null) {
            throw new RRException("未找到运费模板信息！");
        }
        ProFeightTemplate feightTemplate = feightTemplateDao.selectOne(new QueryWrapper<ProFeightTemplate>().eq("id", feightProduct.getFeightId()));
        if (feightTemplate == null) {
            throw new RRException("未找到运费模板信息！");
        }
        List<ProTemplate> template = templateDao.selectList(new QueryWrapper<ProTemplate>().eq("feight_template_id", feightTemplate.getId()));
        if (template == null) {
            throw new RRException("未找到运费模板信息！");
        }

        productEntity.setTotalInventory(inventory);
        productEntity.setTemplate(template);
        productEntity.setAttributeEntityList(attributeEntityList);
        productEntity.setSkuStockEntityList(skuStockEntityList);

        return productEntity;
    }


    /**
     * 货源
     */
    @Override
    public Page<ProProduct> getProList(BigDecimal lowPrice, BigDecimal highPrice, Long categoryId, Long sortType, Integer currPage, Integer pageSize) {
        Page<ProProduct> page = new Page<ProProduct>(currPage, pageSize);
        List<ProProduct> List = proProductDao.getProList(lowPrice, highPrice, categoryId, sortType, null);
        List<ProProduct> proList = proProductDao.getProList(lowPrice, highPrice, categoryId, sortType, page);
        if (proList != null && proList.size() > 0) {
            page.setTotal(List.size());
        }
        page.setRecords(proList);
        return page;
    }


    @Override
    public Page<ProProduct> findByName(int currentPage, int pageSize, String name) {
        Page<ProProduct> page = new Page<ProProduct>(currentPage,pageSize);
        IPage<ProProduct> list = proProductDao.selectByName(page,name);
        list.getRecords().stream().forEach(pro->{
            ProAlbum albumEntity = albumDao.selectCoverStatus(pro.getId());
            pro.setPic(albumEntity == null ? null : albumEntity.getPic());
        });
        page.setTotal(list.getTotal());
        page.setRecords(list.getRecords());
        return page;
    }
}
