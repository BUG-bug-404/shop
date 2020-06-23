package com.keith.modules.service.product.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.keith.common.exception.RRException;
import com.keith.modules.dao.product.CloudManagementDao;
import com.keith.modules.dto.cloud.CloudRepoCount;
import com.keith.modules.dto.cloud.CloudUserRepoInfo;
import com.keith.modules.dto.cloud.CloudUserSkuInfo;
import com.keith.modules.entity.ExtProduct.ExtProduct;
import com.keith.modules.entity.ExtProduct.ExtProductSku;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.product.*;
import com.keith.modules.form.*;
import com.keith.modules.service.ExtProduct.ExtProductService;
import com.keith.modules.service.ExtProduct.ExtProductSkuService;
import com.keith.modules.service.order.OrderItemService;
import com.keith.modules.service.order.OrderOrderService;
import com.keith.modules.service.product.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Service("cloudManagementService")
public class CloudManagementServiceImpl extends ServiceImpl<CloudManagementDao, CloudManagement> implements CloudManagementService {

    @Autowired
    private ProProductService proProductService;
    @Autowired
    private ProAlbumService proAlbumService;
    @Autowired
    private ProSkuStockService proSkuStockService;
    @Autowired
    private ProAttributeService proAttributeService;
    @Autowired
    private ProCategoryService proCategoryService;
    @Autowired
    private ExtProductService extProductService;
    @Autowired
    private ExtProductSkuService extProductSkuService;
    @Autowired
    private OrderOrderService orderOrderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CloudRepoService cloudRepoService;
    @Autowired
    private CloudManagementDao cloudManagementDao;


    Map<String, Object> map = new HashMap<>();
    private static Lock lock = new ReentrantLock(true);

    /*查云仓的全部*/
    @Override
    public Map<String, Object> findAll(Integer currentPage, Integer pageSize) {
        List<CloudResult> arrayList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<CloudManagement> cloudManagements = this.baseMapper.selectList(new QueryWrapper<CloudManagement>()
                .eq("publish_status", 1).groupBy("product_id"));
        if (cloudManagements != null) {
            for (CloudManagement cloudManagement : cloudManagements) {
                CloudResult cloudResult = new CloudResult();
                String productName = proProductService.getById(cloudManagement.getProductId()).getProductName();
                cloudResult.setProductName(productName);
                cloudResult.setProductId(cloudManagement.getProductId());
                ProAlbum proAlbum = proAlbumService.getOne(new QueryWrapper<ProAlbum>().
                        eq("product_id", cloudManagement.getProductId()).
                        eq("cover_status", 0).
                        eq("pic_owner", 1));
                cloudResult.setPic(proAlbum.getPic());
                Date range2EndTime = cloudManagement.getRange2EndTime();
                cloudResult.setEndTime(range2EndTime);
                arrayList.add(cloudResult);
            }
        }

            List<Object> currentPageList = new ArrayList<>();
            if (arrayList != null && arrayList.size() > 0) {
                int currIdx = (currentPage > 1 ? (currentPage - 1) * pageSize : 0);
                for (int i = 0; i < pageSize && i < arrayList.size() - currIdx; i++) {
                    Object data = arrayList.get(currIdx + i);
                    currentPageList.add(data);
                }
            }

        map.put("total",arrayList.size());
        map.put("result",currentPageList);
        return map;
    }

    @Override
    public CloudInfoResult getByProduct(Long productId) {
        List<CloudManagement> cloudManagements = this.baseMapper.selectList(new QueryWrapper<CloudManagement>().
                eq("product_id", productId).eq("publish_status", 1));
        if (cloudManagements == null || cloudManagements.size() <= 0) {
            throw new RRException("这个商品没参加云仓活动！或活动已结束！");
        }
        CloudInfoResult infoResult = new CloudInfoResult();
        ProProduct proProduct = proProductService.getOne(new QueryWrapper<ProProduct>().
                eq("id", productId).eq("delete_status", 0).
                eq("verify_status", 1).eq("publish_status", 1));
        if (proProduct == null) {
            throw new RRException("没了，该商品夭折了！");
        }
        List<ProAlbum> proAlbums = proAlbumService.getBaseMapper().selectList(new QueryWrapper<ProAlbum>().
                eq("pic_owner", 1).
                eq("product_id", productId));
        if (proAlbums == null || proAlbums.size() <= 0) {
            throw new RRException("没了，该商品居然没有一张图片！");
        }

        /*参加云仓活动的规格们*/
        List<Long> skus = new ArrayList<>();
        List<CloudSkuInfo> infos = new ArrayList<>();
        Integer count = 0;
        for (CloudManagement cloudManagement : cloudManagements) {
            skus.add(cloudManagement.getProSkuStockId());
            CloudSkuInfo skuInfo = new CloudSkuInfo();
            skuInfo.setActCount(cloudManagement.getActivityStock());
            skuInfo.setActPrice(cloudManagement.getAcitivityPrice());
            skuInfo.setSkuId(cloudManagement.getProSkuStockId());
            ProSkuStock skuStock = proSkuStockService.getOne(new QueryWrapper<ProSkuStock>().
                    eq("id", cloudManagement.getProSkuStockId()).
                    eq("product_id", productId));
            if (skuStock != null) {
                skuInfo.setSkuValue(skuStock.getSp2());
                skuInfo.setStock(skuStock.getStock());
            }
            count = count + cloudManagement.getStock();
            infos.add(skuInfo);

        }
        CloudManagement cloudManagement = cloudManagements.get(0);
        CloudRepoInfo repoInfo = new CloudRepoInfo();
        repoInfo.setRange1Discount(cloudManagement.getRange1Discount());
        repoInfo.setRange1StartTime(cloudManagement.getRange1StartTime());
        repoInfo.setRange1EndTime(cloudManagement.getRange1EndTime());
        repoInfo.setRange2Discount(cloudManagement.getRange2Discount());
        repoInfo.setRange2StartTime(cloudManagement.getRange2StartTime());
        repoInfo.setRange2EndTime(cloudManagement.getRange2EndTime());

        infoResult.setRepoDetail(repoInfo);//回购详情
        infoResult.setCloudSkuInfos(infos);
        infoResult.setStock(count);

        /*判断是否统一规格*/
        Integer unifyStatus = proProduct.getUnifyStatus();
        //0不是统一规格
        if(unifyStatus != null && !"".equals(unifyStatus) && unifyStatus == 0){
            List<String> sp1s = new ArrayList<>();
            List<String> sp2s = new ArrayList<>();
            for (Long id : skus) {
                ProSkuStock skuStock = proSkuStockService.getOne(new QueryWrapper<ProSkuStock>().eq("id", id).eq("product_id", productId));
                String sp2 = skuStock.getSp2();
                String sp1 = skuStock.getSp1();
                sp1s.add(sp1);
                sp2s.add(sp2);
            }
            Map<String, Set<String>> map = Maps.newHashMap();
            for (int i = 0; i < sp1s.size(); i++) {
                String key = sp1s.get(i);
                String value = sp2s.get(i);
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
            infoResult.setSkus(result);
        }else {
            infoResult.setSkus(null);
        }

        List<String> list = new ArrayList<>();
        for (ProAlbum proAlbum : proAlbums) {
            list.add(proAlbum.getPic());
        }
        infoResult.setProductId(productId);
        infoResult.setProductName(proProduct.getProductName());
        infoResult.setProductCode(proProduct.getProductCode());//货号，暂时没得
        infoResult.setPic(list);
        Long templateId = proProduct.getTemplateId();
        if(templateId != null && !"".equals(templateId)){
            infoResult.setTemplateId(templateId);//待运算
        }
        infoResult.setArea(proProduct.getProductAddress());
        Long productAttributeCategoryId = proProduct.getProductAttributeCategoryId();//二级分类id
        Long parentId = proCategoryService.getOne(new QueryWrapper<ProCategory>().eq("id", productAttributeCategoryId)).getParentId();
        String name = proCategoryService.getById(parentId).getName();
        String name1 = proCategoryService.getById(productAttributeCategoryId).getName();
        infoResult.setCategory(name+">"+name1);//这是分类
//        infoResult.setCount(1);//已出售
        List<ProAttribute> attributes = proAttributeService.getBaseMapper().selectList(new QueryWrapper<ProAttribute>().
                eq("type", 1).eq("product_id", productId));
        List<Map<String, String>> objects = new ArrayList<>();
        if (attributes != null && attributes.size() > 0) {
            for (ProAttribute proAttribute : attributes) {
                Map<String, String> map = new HashMap<>();
                map.put("name",proAttribute.getName());
                map.put("value",proAttribute.getInputList());
                objects.add(map);
            }
        }
        infoResult.setAttr(objects);
        infoResult.setDetail(proProduct.getDescription());
        return infoResult;
    }

    @Override
    public Map<String, Object> getUserAll(Long userMemberId, Integer currentPage, Integer pageSize) {
        List<ExtProduct> extProducts = extProductService.getBaseMapper().selectList(new QueryWrapper<ExtProduct>().
                eq("user_member_id", userMemberId).eq("cloud_status", 1));
        Map<String, Object> m = new HashMap<>();
        List<Object> objects1 = new ArrayList<>();
        if (extProducts != null && extProducts.size() > 0) {
            for (ExtProduct extProduct : extProducts) {
                Map<String, Object> hashMap = new HashMap<>();
                Long productId = extProduct.getProductId();
                ProAlbum proAlbum = proAlbumService.getOne(new QueryWrapper<ProAlbum>().
                        eq("product_id", productId).
                        eq("cover_status", 0).
                        eq("pic_owner", 1));
                String productName = proProductService.getOne(new QueryWrapper<ProProduct>().eq("id", productId)).getProductName();

                hashMap.put("productId", productId);
                hashMap.put("productName", productName);
                hashMap.put("pic", proAlbum.getPic());
                hashMap.put("keyWords", "自营");
                Long extProductId = extProduct.getId();
                List<ExtProductSku> extProductSkus = extProductSkuService.getBaseMapper().selectList(new QueryWrapper<ExtProductSku>().
                        eq("product_id", productId).
                        eq("ext_product_id", extProductId).eq("delete_status", 0));
                if (extProductSkus != null && extProductSkus.size() > 0) {
                    List<Object> objects = new ArrayList<>();
                    for (ExtProductSku extProductSku : extProductSkus) {
                        Map<String, Object> map = new HashMap<>();
                        Long skuStockId = extProductSku.getSkuStockId();
                        Long stock = extProductSku.getStock();
                        ProSkuStock proSkuStock = proSkuStockService.getById(skuStockId);
                        BigDecimal platformPrice = proSkuStock.getPlatformPrice();//平台进货价格
                        BigDecimal platformSalePrice = proSkuStock.getPlatformSalePrice();//这是销售价格
                        map.put("skuStockId", skuStockId);
                        map.put("stock", stock);
                        map.put("platformPrice",platformPrice);
                        map.put("platformSalePrice",platformSalePrice);
                        String sp2 = proSkuStockService.getOne(new QueryWrapper<ProSkuStock>().
                                eq("id", skuStockId).
                                eq("product_id", productId)).getSp2();
                        map.put("skuValue", sp2);
                        CloudManagement cloudManagement = this.getOne(new QueryWrapper<CloudManagement>().
                                eq("product_id", productId).
                                eq("pro_sku_stock_id", skuStockId).eq("publish_status", 1));
                        if (cloudManagement != null) {
                            BigDecimal acitivityPrice = cloudManagement.getAcitivityPrice();
                            map.put("acitivityPrice", acitivityPrice);
                        }
                        objects.add(map);
                    }
                    hashMap.put("skus", objects);
                }
                objects1.add(hashMap);
            }
            List<Object> currentPageList = new ArrayList<>();
            if (objects1 != null && objects1.size() > 0) {
                int currIdx = (currentPage > 1 ? (currentPage - 1) * pageSize : 0);
                for (int i = 0; i < pageSize && i < objects1.size() - currIdx; i++) {
                    Object data = objects1.get(currIdx + i);
                    currentPageList.add(data);
                }
            }
            m.put("total", objects1.size());
            m.put("result", currentPageList);

        }
        return m;
    }

    @Override
    public Map<String, Object> getAll(Integer currentPage, Integer pageSize) {
        /*获取云仓里所有的商品id遍历商品id，获取云仓表里这个商品的所有规格*/
        List<CloudManagement> cloudManagements = this.baseMapper.selectList(new QueryWrapper<CloudManagement>()
                .select("DISTINCT product_id")
                .eq("publish_status",1));
        /*商品id*/
        Map<String, Object> map1 = new HashMap<>();
        List<Object> objects1 = new ArrayList<>();
        for(CloudManagement cloudManagement : cloudManagements){
            Map<Object, Object> hashMap = new HashMap<>();
            Long productId = cloudManagement.getProductId();
            /*图片，标题*/
            ProAlbum proAlbum = proAlbumService.getOne(new QueryWrapper<ProAlbum>().
                    eq("product_id",productId ).
                    eq("cover_status", 0).
                    eq("pic_owner", 1));
            String productName = proProductService.getOne(new QueryWrapper<ProProduct>().eq("id",productId)).getProductName();
            hashMap.put("productId",productId);
            hashMap.put("productName",productName);
            hashMap.put("pic",proAlbum.getPic());
            hashMap.put("keyWords","自营");
            /*查询这个商品id在云仓表里的所有规格*/
            List<CloudManagement> managements = this.baseMapper.selectList(new QueryWrapper<CloudManagement>()
                    .eq("product_id", productId).eq("publish_status",1));

            ArrayList<Object> objects = new ArrayList<>();
            for(CloudManagement cloudManagement1 : managements){
                Map<Object, Object> map = new HashMap<>();
                /*获取规格id*/
                Long stockId = cloudManagement1.getProSkuStockId();
                /*获取规格，以及规格附带的批发价格以及销售价格*/
                ProSkuStock proSkuStock = proSkuStockService.getById(stockId);
                String sp2 = proSkuStock.getSp2();//这是规格
                BigDecimal platformPrice = proSkuStock.getPlatformPrice();//平台进货价格
                BigDecimal platformSalePrice = proSkuStock.getPlatformSalePrice();//这是销售价格
                Integer stock = proSkuStock.getStock();//这是库存
                map.put("skuValue ",sp2);
                map.put("platformPrice",platformPrice);
                map.put("platformSalePrice",platformSalePrice);
                map.put("skuStock ",stock);
                map.put("acitivityPrice",cloudManagement1.getAcitivityPrice());
//                map.put("repo_time",cloudManagement1.getRepoTime());//无时间
//                map.put("repo_price",cloudManagement1.getRepoPrice());
                map.put("cloudId",cloudManagement1.getId());
                map.put("skuId",stockId);
                objects.add(map);
            }
            hashMap.put("skus",objects);
            objects1.add(hashMap);
        }
        List<Object> currentPageList = new ArrayList<>();
        if(objects1 != null && objects1.size()>0){
            int currIdx = (currentPage > 1 ? (currentPage - 1) * pageSize : 0);
            for (int i = 0; i < pageSize && i < objects1.size() - currIdx; i++) {
                Object data = objects1.get(currIdx + i);
                currentPageList.add(data);
            }
        }
        map1.put("total",objects1.size());
        map1.put("result",currentPageList);
        return map1;
    }

    @Override
    public CloudForResult getInfo(Long userMemberId, Long productId, Long skuId) {
        if (productId == null || skuId == null) {
            throw new RRException("商品id或规格id为空！");
        }
        CloudForResult cloudForResult = new CloudForResult();
        ProAlbum proAlbum = proAlbumService.getOne(new QueryWrapper<ProAlbum>().
                eq("product_id", productId).
                eq("cover_status", 0).
                eq("pic_owner", 1));
        cloudForResult.setProductId(productId);
        cloudForResult.setPic(proAlbum.getPic());
        ProProduct proProduct = proProductService.getOne(new QueryWrapper<ProProduct>().eq("id", productId));
        String productName = proProduct.getProductName();
        Long templateId = proProduct.getTemplateId();
        cloudForResult.setProductName(productName);
        ProSkuStock proSkuStock = proSkuStockService.getById(skuId);
        cloudForResult.setPlatformPrice(proSkuStock.getPlatformPrice());
        cloudForResult.setPlatformSalePrice(proSkuStock.getPlatformSalePrice());
//        cloudForResult.setSkus();
        cloudForResult.setTemplateId(templateId);
        cloudForResult.setSkuValue(proSkuStock.getSp2());
//        cloudForResult.setStock();
        //        cloudForResult.setAcitivityPrice();
        CloudManagement cloudManagement = this.getOne(new QueryWrapper<CloudManagement>().
                eq("product_id", productId).
                eq("pro_sku_stock_id", skuId));
        cloudForResult.setAcitivityPrice(cloudManagement.getAcitivityPrice());
        Long id = extProductService.getOne(new QueryWrapper<ExtProduct>().
                eq("user_member_id", userMemberId).
                eq("product_id", productId).eq("cloud_status", 1)).getId();
        ExtProductSku skuServiceOne = extProductSkuService.getOne(new QueryWrapper<ExtProductSku>().
                eq("ext_product_id",id).
                eq("sku_stock_id", skuId).
                eq("product_id", productId));
        cloudForResult.setStock(skuServiceOne.getStock());

        return cloudForResult;
    }

    /*生成云仓订单---订单完成还得铺到铺货去*/
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> createCloudOrder(BuyCloudProSelect buyCloudProSelect,Long userMemberId) {
        lock.lock();
        if(buyCloudProSelect == null){
            throw new RRException("有误！");
        }
        ProProduct proProduct = proProductService.getBaseMapper().selectOne(new QueryWrapper<ProProduct>().
                eq("id", buyCloudProSelect.getProductId()).
                eq("delete_status",0).eq("verify_status",1).eq("publish_status",1));
        if(proProduct == null){
            throw new RRException("该商品已失效！");
        }
        List<BuyCloudSelect> skus = buyCloudProSelect.getSkus();
        if(skus == null || skus.size()<=0){
            throw new RRException("该商品规格选择有误！");
        }
        try {
            Date date = new Date();
            String uuId = getOrderIdByUUId();
            OrderOrder orderOrder = new OrderOrder();
//            orderOrder.setProductId(proProduct.getId());//订单无商品id
            orderOrder.setOrderSn(uuId);
//            orderOrder.setUserAdminId(productEntity.getUserAdminId());//云仓无店家id
            orderOrder.setUserMemberId(userMemberId);//店家id
            orderOrder.setCreateTime(date);
            orderOrder.setStatus(0);
            orderOrder.setOrderType(2);
            orderOrder.setReceiveAddressId(buyCloudProSelect.getAddressId());//收货地址id
            orderOrder.setTotalAmount(buyCloudProSelect.getTotalAmount());
            orderOrder.setPayAmount(buyCloudProSelect.getPayAmount());//实付款
            orderOrder.setFreightAmount(buyCloudProSelect.getFreightAmount());
            orderOrderService.save(orderOrder);
            //库存得减啊
            for(BuyCloudSelect buyCloudSelect : skus){
                Long skuId = buyCloudSelect.getSkuId();
                CloudManagement cloudManagement = this.getOne(new QueryWrapper<CloudManagement>().
                        eq("buyCloudSelect", buyCloudProSelect.getProductId()).eq("pro_sku_stock_id", skuId).eq("publish_status",1));
                if(cloudManagement == null){
                    throw new RRException("选择的规格已不参加云仓活动！");
                }
                //创建订单详情
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderOrder.getId());
                orderItem.setOrderSn(uuId);
                orderItem.setCreateTime(date);
                orderItem.setProductId(proProduct.getId());
                orderItem.setProductSkuId(skuId);
//                orderItem.setUserAdminId();
                orderItem.setProductName(proProduct.getProductName());
                orderItem.setProductQuantity(buyCloudSelect.getCount());
                orderItem.setUserAdminId(proProduct.getUserAdminId());//这是供应商id
                //规格运费金额
                orderItemService.save(orderItem);
            }
            map.put("orderSn",orderOrder.getOrderSn());
            map.put("msg", "创建订单成功！");
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> createRepoOrder(List<CloudUserRepoInfo> cloudUserRepoInfos, Long userMemberId) {
        if(cloudUserRepoInfos == null || cloudUserRepoInfos.size()<= 0){
            throw new RRException("有误！");
        }
        /*判断数量是否正确*/
        String orderIdByUUId = getOrderIdByUUId();
        Date date = new Date();
        for(CloudUserRepoInfo cloudUserRepoInfo : cloudUserRepoInfos){
            String pic = cloudUserRepoInfo.getPic();
            Long productId = cloudUserRepoInfo.getProductId();
            String productName = cloudUserRepoInfo.getProductName();
            BigDecimal sumPrice = cloudUserRepoInfo.getSumPrice();
            Integer allCount = cloudUserRepoInfo.getAllCount();
            List<CloudUserSkuInfo> skus = cloudUserRepoInfo.getSkus();
            if(skus == null || skus.size() <= 0){
                throw new RRException("有误！");
            }
            for(CloudUserSkuInfo cloudUserSkuInfo : skus){
                CloudRepo cloudRepo = new CloudRepo();
                cloudRepo.setRepoSn(orderIdByUUId);
                cloudRepo.setCount(cloudUserSkuInfo.getSkuCount());
                cloudRepo.setCreateTime(date);
                cloudRepo.setProductId(productId);
                cloudRepo.setPic(pic);
                cloudRepo.setProductName(productName);
                cloudRepo.setUserMemberId(userMemberId);
                cloudRepo.setProSkuStockId(cloudUserSkuInfo.getSkuId());
                cloudRepo.setProSkuValue(cloudUserSkuInfo.getSkuValue());
                cloudRepo.setRepoPrice(cloudUserSkuInfo.getSkuPrice()); //规格回收时的价格
                cloudRepo.setRepoDiscount(cloudUserSkuInfo.getSkuDisCount());//规格回收时的折扣
                cloudRepo.setAllCount(allCount);
                cloudRepo.setSumPrice(sumPrice);
                cloudRepo.setOriginalPrice(cloudUserSkuInfo.getActivityPrice());
                cloudRepo.setStatus(1);//申请中
              cloudRepoService.save(cloudRepo);
            }
        }
        map.put("repoSn",orderIdByUUId);
        return map;
    }

    @Override
    public List<CloudUserRepoInfo> findRepoInfo(Long userMemberId, Long repoSn) {
        List<CloudRepo> cloudRepos = cloudRepoService.getBaseMapper().selectList(new QueryWrapper<CloudRepo>().
                eq("user_member_id", userMemberId).eq("repo_sn", repoSn));
        if(cloudRepos == null || cloudRepos.size() <= 0){
            throw new RRException("暂无详情信息！");
        }
        List<CloudRepo> cloudRepoList = cloudRepoService.getBaseMapper().selectList(new QueryWrapper<CloudRepo>().
                eq("user_member_id", userMemberId).eq("repo_sn", repoSn).select("DISTINCT product_id"));
        List<Long> objects = new ArrayList<>();
        for(CloudRepo cloudRepo : cloudRepoList){
            Long productId = cloudRepo.getProductId();
            objects.add(productId);
        }
        List<CloudUserRepoInfo> list = new ArrayList<>();
        CloudUserRepoInfo cloudUserRepoInfo = new CloudUserRepoInfo();
        for(Long productId : objects){
            List<CloudRepo> repoList = cloudRepoService.getBaseMapper().selectList(new QueryWrapper<CloudRepo>().
                    eq("user_member_id", userMemberId).eq("repo_sn", repoSn).eq("product_id", productId));
            List<CloudUserSkuInfo> arrayList = new ArrayList<>();
            for(CloudRepo cloudRepo : repoList){
                CloudUserSkuInfo cloudUserSkuInfo = new CloudUserSkuInfo();
                cloudUserSkuInfo.setSkuCount(cloudRepo.getCount());
                cloudUserSkuInfo.setSkuValue(cloudRepo.getProSkuValue());
                cloudUserSkuInfo.setSkuId(cloudRepo.getProSkuStockId());
                cloudUserSkuInfo.setSkuDisCount(cloudRepo.getRepoDiscount());
                cloudUserSkuInfo.setSkuPrice(cloudRepo.getRepoPrice());
                cloudUserSkuInfo.setActivityPrice(cloudRepo.getOriginalPrice());
                arrayList.add(cloudUserSkuInfo);
            }
            cloudUserRepoInfo.setAllCount(repoList.get(0).getAllCount());
            cloudUserRepoInfo.setPic(repoList.get(0).getPic());
            cloudUserRepoInfo.setProductId(repoList.get(0).getProductId());
            cloudUserRepoInfo.setProductName(repoList.get(0).getProductName());
            cloudUserRepoInfo.setSkus(arrayList);
            cloudUserRepoInfo.setSumPrice(repoList.get(0).getSumPrice());
        }
        list.add(cloudUserRepoInfo);
        return list;
    }


    /*代发货------订单*/
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> getGoods(GetGoodsSelect getGoodsSelect, Long userMemberId) {
        /*减云仓一个库存*/
        lock.lock();
        if(getGoodsSelect == null){
            throw new RRException("选择错误！");
        }
        ProProduct proProduct = proProductService.getOne(new QueryWrapper<ProProduct>().eq("id", getGoodsSelect.getProductId()));
        try{
            Date date = new Date();
            String uuId = getOrderIdByUUId();
            OrderOrder orderOrder = new OrderOrder();
            orderOrder.setOrderSn(uuId);
            orderOrder.setUserMemberId(userMemberId);//用户是店家id
            orderOrder.setStatus(0);
            orderOrder.setOrderType(2);
            orderOrder.setReceiveAddressId(getGoodsSelect.getAddressId());//收货地址id
            orderOrder.setTotalAmount(getGoodsSelect.getFreightAmount());
            orderOrder.setPayAmount(getGoodsSelect.getFreightAmount());//实付款
            orderOrder.setFreightAmount(getGoodsSelect.getFreightAmount());
            orderOrderService.save(orderOrder);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderOrder.getId());
            orderItem.setOrderSn(uuId);
            orderItem.setCreateTime(date);
            orderItem.setProductId(getGoodsSelect.getProductId());
            orderItem.setProductSkuId(getGoodsSelect.getSkuId());
            orderItem.setProductName(proProduct.getProductName());
            orderItem.setProductQuantity(getGoodsSelect.getCount());
            orderItem.setUserAdminId(proProduct.getUserAdminId());//这是供应商id
            orderItemService.save(orderItem);
            map.put("orderSn",orderOrder.getOrderSn());
            map.put("msg", "创建订单成功！");
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return map;
    }

    @Override
    public Map<String, Object> countRepoPrice(CloudRepoCount cloudRepoCount, Long userMemberId) {
        if(cloudRepoCount == null){
            throw new RRException("有误！");
        }
        CloudManagement cloudManagement = this.getOne(new QueryWrapper<CloudManagement>().
                eq("product_id", cloudRepoCount.getProductId()).
                eq("pro_sku_stock_id", cloudRepoCount.getSkuId()));

        Date nowTime = new Date();
        BigDecimal acitivityPrice = cloudManagement.getAcitivityPrice();

        BigDecimal range1Discount = cloudManagement.getRange1Discount();
        Date range1StartTime = cloudManagement.getRange1StartTime();
        Date range1EndTime = cloudManagement.getRange1EndTime();

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(range1StartTime);

        if(begin.after(date)){
            throw new RRException("未到回购开始时间！");
        }
        BigDecimal range2Discount = cloudManagement.getRange2Discount();
        Date range2StartTime = cloudManagement.getRange2StartTime();
        Date range2EndTime = cloudManagement.getRange2EndTime();

        BigDecimal bigDecimal = new BigDecimal("100");
        boolean effectiveDate1 = isEffectiveDate(nowTime, range1StartTime, range1EndTime);
        if(effectiveDate1){
            BigDecimal divide = range1Discount.divide(bigDecimal, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal multiply = acitivityPrice.multiply(divide.setScale(2,BigDecimal.ROUND_HALF_UP));
            map.put("rePrice",multiply);
            map.put("discount",range1Discount);
            return map;
        }else {
            boolean effectiveDate2 = isEffectiveDate(nowTime, range2StartTime, range2EndTime);
            if(effectiveDate2){
                BigDecimal divide = range2Discount.divide(bigDecimal, 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal multiply = acitivityPrice.multiply(divide.setScale(2,BigDecimal.ROUND_HALF_UP));
                map.put("rePrice",multiply);
                map.put("discount",range2Discount);
                return map;
            }else {
                throw new RRException("已过回收截止时间！");
            }
        }
    }
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void buyCloud(Long userMemberId, List<BuyCloudProSelect> products) {
//        if(products == null && products.size() <= 0){
//            throw new RRException("选择有误！");
//        }
//        for( BuyCloudProSelect buyCloudProSelect : products) {
//            Long productId = buyCloudProSelect.getProductId();
//            List<BuyCloudSelect> skus = buyCloudProSelect.getSkus();
//            if (skus == null && skus.size() <= 0) {
//                throw new RRException("规格选择有误！");
//            }
//                for (BuyCloudSelect buyCloudSelect : skus) {
//                    Long count = buyCloudSelect.getCount();//选择的数量
//                    if (count == null || "".equals(count)) {
//                        throw new RRException("您没有选择数量！");
//                    }
//                    Long skuId = buyCloudSelect.getSkuId();//选择的规格
//                    CloudManagement cloudManagement = cloudManagementService.getOne(new QueryWrapper<CloudManagement>().
//                            eq("product_id", productId).eq("pro_sku_stock_id", skuId).
//                            eq("publish_status", 1));
//                    if (cloudManagement == null) {
//                        throw new RRException("选择的规格已不参加云仓活动！");
//                    }
//                    Integer activityStock = cloudManagement.getActivityStock();//这是该选择活动的数量
//                    Integer skuStock = cloudManagement.getSkuStock();//库存
//                    /*判断选择的数量有没有毛病*/
//                    if (count > skuStock) {
//                        throw new RRException("选择的规格库存大于商品规格库存！");
//                    } else if (count % activityStock != 0) {
//                        throw new RRException("选择的数量不是活动数量的倍数！");
//                    }
//
//                }
//
//        }
//    }

    /**
     * 创建订单编号
     *
     * @return
     */
    public static String getOrderIdByUUId() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String time = format.format(date);
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return time + String.format("%011d", hashCodeV);
    }

    /**判断时间是否在此间隔内*/
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

}
