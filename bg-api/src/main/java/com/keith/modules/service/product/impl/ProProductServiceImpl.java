package com.keith.modules.service.product.impl;


import cn.hutool.json.JSONObject;
import com.aliyuncs.regions.ProductDomain;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.keith.common.exception.RRException;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.product.*;
import com.keith.modules.dao.template.ProFeightProductDao;
import com.keith.modules.dao.template.ProFeightTemplateDao;
import com.keith.modules.dao.template.ProTemplateDao;
import com.keith.modules.dao.user.UserCollectItemDao;
import com.keith.modules.entity.product.*;
import com.keith.modules.entity.template.ProFeightProduct;
import com.keith.modules.entity.template.ProFeightTemplate;
import com.keith.modules.entity.template.ProTemplate;
import com.keith.modules.entity.user.UserCollectItem;
import com.keith.modules.service.order.OrderItemService;
import com.keith.modules.service.product.ProAlbumService;
import com.keith.modules.service.product.ProAttributeService;
import com.keith.modules.service.product.ProSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.keith.modules.service.product.ProProductService;


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
    @Autowired
    private ProPreviewDao proPreviewDao;
    @Autowired
    private UserCollectItemDao userCollectItemDao;
    @Autowired
            private OrderItemService orderItemService;

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

    @Autowired
    ProCategoryDao proCategoryDao;

    @Override
    public ProProduct findById(Long productId) {
        ProProduct productEntity = this.getOne(new QueryWrapper<ProProduct>().eq("id", productId)
                .eq("delete_status", 0).eq("verify_status", 1)
                .eq("publish_status", 1).isNotNull("parent_id"));


        if (productEntity == null) {
            throw new RRException("商品信息有误！");
        }

        /**
         * 查询图片*/
        List<ProAlbum> albumEntityList = albumService.list(new QueryWrapper<ProAlbum>().
                eq("pic_owner", 1).
                eq("product_id",productId));
        productEntity.setAlbumEntityList(albumEntityList);

        //规格属性
        List<ProAttribute> attributeEntityList = attributeService.list(new QueryWrapper<ProAttribute>().eq("product_id", productEntity.getId()));
        //规格
        List<ProSkuStock> skuStockEntityList = skuStockService.list(new QueryWrapper<ProSkuStock>().eq("product_id", productEntity.getId()));
        Integer inventory=0;
        for (ProSkuStock skuStock:skuStockEntityList){
            inventory=inventory+skuStock.getStock();
        }

        /*
            0不是统一规格
         */
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

        ProCategory proCategory=proCategoryDao.selectOne(new QueryWrapper<ProCategory>().eq("id",productEntity.getProductAttributeCategoryId()));
        if(proCategory!=null){
            if(proCategory.getLevel()==0){
                String  string=proCategory.getName();
                productEntity.setType(string);
            }
            else if(proCategory.getLevel()==1){
                ProCategory category=proCategoryDao.selectOne(new QueryWrapper<ProCategory>().eq("id",proCategory.getParentId()));
                if(category!=null){
                    String  string=category.getName()+">"+proCategory.getName();
                    productEntity.setType(string);
                }
            }
        }


        productEntity.setTotalInventory(inventory);
        productEntity.setTemplate(template);
        productEntity.setAttributeEntityList(attributeEntityList);
        productEntity.setSkuStockEntityList(skuStockEntityList);
        /**判断此商品是否为预售*/
        Integer previewStatus = productEntity.getPreviewStatus();
        if(previewStatus != null && previewStatus == 1){
            ProPreview proPreview = proPreviewDao.selectOne(new QueryWrapper<ProPreview>().eq("product_id", productId));
            productEntity.setProPreview(proPreview);
        }
        String feight = orderItemService.countFeight(productId);
        productEntity.setFeightAmount(feight);
        return productEntity;
    }


    /**
     * 货源
     */
    @Override
    public Page<ProProduct> getProList(BigDecimal lowPrice, BigDecimal highPrice, Long categoryId, Long sortType, Integer currPage, Integer pageSize) {
        Page<ProProduct> page = new Page<ProProduct>(currPage, pageSize);
        List<ProProduct> proList = proProductDao.getProList(lowPrice, highPrice, categoryId, sortType);
        List<ProProduct> areaVo = proList.stream().skip(pageSize * (currPage - 1)).limit(pageSize).collect(Collectors.toList());
        areaVo.stream().forEach(skr->{
            Long id = skr.getId();
            Integer count = userCollectItemDao.selectCount(new QueryWrapper<UserCollectItem>().eq("product_id", id));
            skr.setLikeCount(count.toString());
        });
        if (proList != null && proList.size() > 0) {
            page.setTotal(proList.size());
        }
        page.setRecords(areaVo);
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
