package com.keith.modules.service.ExtProduct.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.exception.RRException;
import com.keith.modules.dao.ExtProduct.ExtProductDao;
import com.keith.modules.dao.ExtProduct.ExtProductSkuDao;
import com.keith.modules.dao.product.ProProductDao;
import com.keith.modules.entity.ExtProduct.ExtProduct;
import com.keith.modules.entity.ExtProduct.ExtProductSku;
import com.keith.modules.entity.product.ProAlbum;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.form.CloudInfoResult;
import com.keith.modules.form.ExtProResult;
import com.keith.modules.form.ExtSkusSelect;
import com.keith.modules.service.ExtProduct.ExtProductService;
import com.keith.modules.service.ExtProduct.ExtProductSkuService;
import com.keith.modules.service.product.CloudManagementService;
import com.keith.modules.service.product.ProAlbumService;
import com.keith.modules.service.product.ProProductService;
import com.keith.modules.service.product.ProSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


@Service("extProductService")
public class ExtProductServiceImpl extends ServiceImpl<ExtProductDao, ExtProduct> implements ExtProductService {

    @Autowired
    private ProAlbumService proAlbumService;
    @Autowired
    private ProSkuStockService proSkuStockService;
    @Autowired
    private ExtProductSkuService extProductSkuService;
    @Autowired
    private ProProductService proProductService;
    @Autowired
    private CloudManagementService cloudManagementService;
    @Autowired
    private ExtProductSkuDao extProductSkuDao;
    @Autowired
    private ProProductDao proProductDao;
    @Autowired
    private ExtProductDao extProductDao;

    /**
     * 这是铺货-----默认,别忘了加事务
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void extProduct(Long productId, List<Long> skuId, Long userMemberId) {
        if (productId == null || skuId == null || skuId.size() <= 0) {
            throw new RRException("参数错误！");
        }
        ProProduct proProduct = proProductService.getOne(new QueryWrapper<ProProduct>().
                eq("delete_status", 0).
                eq("verify_status", 1).
                eq("publish_status", 1).eq("id", productId).isNotNull("parent_id"));
        if (proProduct == null) {
            throw new RRException("选择的商品不存在！");
        }
        /*/*查看选择铺货的商品和规格是否存在 */
        ExtProduct extProduct1 = this.getOne(new QueryWrapper<ExtProduct>().
                eq("user_member_id", userMemberId).
                eq("product_id", productId));
        if (extProduct1 == null) {
            ExtProduct extProduct = new ExtProduct();
            extProduct.setUserMemberId(userMemberId);
            extProduct.setProductId(productId);
            extProduct.setDeleteStatus(0);//添加的是未删除
            extProduct.setCloudStatus(0);//0不是云仓过来的
            extProduct.setPublishStatus(0);//默认下架状态
            extProduct.setSale(0);//铺货过去的销量定义初始值
//        extProduct.setDescription("");//图文详情
            List<ProAlbum> proAlbums = proAlbumService.getBaseMapper().selectList(new QueryWrapper<ProAlbum>()
                    .eq("product_id", productId)
                    .eq("pic_owner", 1));//1是平台的
            if( proAlbums != null && proAlbums.size()>0){
                List<String> arrayList = new ArrayList<>();
                for(ProAlbum proAlbum : proAlbums){
                    arrayList.add(proAlbum.getPic());
                }
                extProduct1.setPic(arrayList.toString());//铺货的图片们，默认则用平台的图片
            }
            this.save(extProduct);
            /*这是传过来的商品id*/
            for (Long id : skuId) {
                ProSkuStock proSkuStock = proSkuStockService.getOne(new QueryWrapper<ProSkuStock>().
                        eq("product_id", productId).
                        eq("id", id));
                ExtProductSku extProductSku = new ExtProductSku();
                extProductSku.setProductId(productId);
                extProductSku.setExtProductId(extProduct.getId());//这是铺货的id)
                extProductSku.setSkuStockId(id);//商品规格id
                extProductSku.setDeleteStatus(0);//
                extProductSku.setSupplierPrice(proSkuStock.getPlatformSalePrice());//平台的销售价格
                extProductSkuService.save(extProductSku);
            }
        } else {
            for (Long id : skuId) {
                /*铺货表里有这个商品*/
                ExtProductSku extProductSku1 = extProductSkuService.getOne(new QueryWrapper<ExtProductSku>()
                        .eq("ext_product_id", extProduct1.getId())
                        .eq("sku_stock_id", id)
                        .eq("product_id", productId));
                ProSkuStock proSkuStock = proSkuStockService.getOne(new QueryWrapper<ProSkuStock>().
                        eq("product_id", productId).
                        eq("id", id));
                if(extProductSku1 == null){
                    /*不存在就存进去*/
                    ExtProductSku extProductSku = new ExtProductSku();
                    extProductSku.setProductId(productId);
                    extProductSku.setExtProductId(extProduct1.getId());//这是铺货的id)
                    extProductSku.setSkuStockId(id);//商品规格id
                    extProductSku.setDeleteStatus(0);//
                    extProductSku.setSupplierPrice(proSkuStock.getPlatformSalePrice());//平台的销售价格
                    extProductSkuService.save(extProductSku);
                }else {
                   throw  new RRException("您选择的规格"+proSkuStock.getSp2()+"已存在铺货！请重新选择！");
                }
            }
        }
    }

    @Override
    public ExtProResult findProInfo(Long productId) {
        if( productId == null ){
            throw new RRException("商品id有误！");
        }
        ExtProResult extProResult = new ExtProResult();
        /**查询此商品的所有图片,图片需是平台的*/
        List<ProAlbum> proAlbums = proAlbumService.getBaseMapper().selectList(new QueryWrapper<ProAlbum>()
                .eq("product_id", productId)
                .eq("pic_owner", 1));
        List<String> objects = new ArrayList<>();
        for(ProAlbum proAlbum : proAlbums){
            objects.add(proAlbum.getPic());
        }
        extProResult.setPic(objects);
        ProProduct proProduct = proProductService.getById(productId);
        extProResult.setProductName(proProduct.getProductName());
        extProResult.setDetail(proProduct.getDescription());//这是图文详情
        extProResult.setProductId(productId);
        /*获取这个商品的所有规格以及规格对应的库存以及平台的批发价格*/
        List<ProSkuStock> skuStocks = proSkuStockService.getBaseMapper().selectList(new QueryWrapper<ProSkuStock>().eq("product_id", productId));
        ArrayList<ExtSkusSelect> arrayList = new ArrayList<>();
        for(ProSkuStock proSkuStock : skuStocks){
            ExtSkusSelect select = new ExtSkusSelect();
            proSkuStock.getSp1();
            select.setPlatPrice(proSkuStock.getPlatformPrice());//;//平台进货价格
            select.setSkuId(proSkuStock.getId());//规格id
            select.setSkuInfo(proSkuStock.getSp2());
            select.setStock(proSkuStock.getStock());//库存
            arrayList.add(select);
        }
        extProResult.setSkus(arrayList);//这是查出来的规格们
        return extProResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean extProductChoose(ExtProResult extProResult,Long userMemberId) {
        if(extProResult == null){
            throw new RRException("有误!");
        }
        ProProduct proProduct = proProductService.getOne(new QueryWrapper<ProProduct>().
                eq("delete_status", 0).
                eq("verify_status", 1).
                eq("publish_status", 1).eq("id", extProResult.getProductId()).isNotNull("parent_id"));
        if(proProduct == null){
            throw new RRException("选择的商品已失效！");
        }
        ExtProduct extProduct = this.getOne(new QueryWrapper<ExtProduct>()
                .eq("user_member_id", userMemberId)
                .eq("product_id", extProResult.getProductId()));

        /**
         * 加入铺货商品已存在
         */
        if(extProduct != null){
            extProduct.setDescription(extProResult.getDetail());//修改详情
            List<String> resultPic = extProResult.getPic();//获取图片地址
            if(resultPic.size()>0 && resultPic != null){
                extProduct.setPic(resultPic.toString());//设置铺货图片
            }
            this.updateById(extProduct);
            /*接着查看规格*/
            /**查询铺货商品对应的规格*/
            List<ExtProductSku> extSkuList = extProductSkuService.listObjs(new LambdaQueryWrapper<ExtProductSku>().eq(ExtProductSku::getExtProductId,extProduct.getId()),obj->{
                ExtProductSku extSku = JSONObject.parseObject(JSONObject.toJSONString(obj),ExtProductSku.class);
                return extSku;
            });
            extProResult.getSkus().stream().forEach(resSku->{
                if (resSku.getSkuIds().size()>1){
                    extSkuList.stream().forEach(sku->{
                        resSku.getSkuIds().stream().forEach(id->{
                            if (id==sku.getId()){
                                sku.setSupplierPrice(resSku.getPrice());
                            }
                        });
                    });
                }
                else {
                    extSkuList.stream().forEach(sku->{
                        if (resSku.getSkuIds().get(0)==sku.getId()) {
                            sku.setSupplierPrice(resSku.getPrice());
                        }
                    });
                }
            });
            extProductSkuService.updateBatchById(extSkuList);
            return true;
        }else{
            ExtProduct extProduct1 = new ExtProduct();
            extProduct1.setUserMemberId(userMemberId);
            extProduct1.setProductId(extProResult.getProductId());
            extProduct1.setDeleteStatus(0);//添加的是未删除
            extProduct1.setCloudStatus(0);//0不是云仓过来的
            extProduct1.setPublishStatus(0);//默认下架状态
            extProduct1.setSale(0);//铺货过去的销量定义初始值
//        extProduct.setDescription("");//图文详情
            List<ProAlbum> proAlbums = proAlbumService.getBaseMapper().selectList(new QueryWrapper<ProAlbum>()
                    .eq("product_id", extProResult.getProductId())
                    .eq("pic_owner", 1));//1是平台的
            if( proAlbums != null && proAlbums.size()>0){
                List<String> arrayList = new ArrayList<>();
                for(ProAlbum proAlbum : proAlbums){
                    arrayList.add(proAlbum.getPic());
                }
                extProduct1.setPic(arrayList.toString());//铺货的图片们，默认则用平台的图片
            }
            this.save(extProduct1);

            /*接着存规格*/
            List<ExtProductSku> extSkuList = new LinkedList<>();
            extProResult.getSkus().stream().forEach(extSkus->{
                ExtProductSku extSku = new ExtProductSku();
                extSku.setSupplierPrice(extSkus.getPrice());
                extSku.setDeleteStatus(0);
                extSku.setSkuStockId(extSkus.getSkuId());
                extSku.setProductId(extProResult.getProductId());
                if (extSkus.getSkuIds().size()>1){

                    extSkus.getSkuIds().stream().forEach(ids->{
                        extSku.setExtProductId(ids);
                    });

                }
                else {
                    extSku.setExtProductId(extSkus.getSkuIds().get(0));
                }
                extSkuList.add(extSku);
            });
            extProductSkuService.saveBatch(extSkuList);
            return true;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ExtProResult extProductNormal(List<Long> productIds, long userMemberId){
        if(productIds == null || productIds.size()==0){
            throw new RRException("有误!");
        }
        List<Long> failProIds = new LinkedList<>();
        List<Long> successProIds = new LinkedList<>();
        productIds.stream().forEach(productId->{
            ProProduct proProduct = proProductService.getOne(new QueryWrapper<ProProduct>().
                    eq("delete_status", 0).
                    eq("verify_status", 1).
                    eq("publish_status", 1).eq("id", productId).isNotNull("parent_id"));

            ExtProduct extProduct = this.getOne(new QueryWrapper<ExtProduct>()
                    .eq("user_member_id", userMemberId)
                    .eq("product_id",productId));
            if (proProduct == null) {
                failProIds.add(productId);
            }
            else if (extProduct !=null){
                failProIds.add(productId);
            }
            else {
                int type = 0;
                if (proProduct.getPreviewStatus() == 0) {
                    type = 1;

                    ExtProduct extProduct1 = new ExtProduct();
                    extProduct1.setUserMemberId(userMemberId);
                    extProduct1.setProductId(productId);
                    extProduct1.setDeleteStatus(0);//添加的是未删除
                    extProduct1.setCloudStatus(0);//0不是云仓过来的
                    extProduct1.setPublishStatus(0);//默认下架状态
                    extProduct1.setSale(0);//铺货过去的销量定义初始值
                    extProduct1.setProductName(proProduct.getProductName());
                    extProduct1.setExtType(type);

                    List<ProAlbum> proAlbums = proAlbumService.getBaseMapper().selectList(new QueryWrapper<ProAlbum>()
                            .eq("product_id", productId)
                            .eq("pic_owner", 1));//1是平台的
                    if (proAlbums != null && proAlbums.size() > 0) {
                        List<String> arrayList = new ArrayList<>();
                        for (ProAlbum proAlbum : proAlbums) {
                            arrayList.add(proAlbum.getPic());
                        }
                        extProduct1.setPic(arrayList.toString());//铺货的图片们，默认则用平台的图片
                    }
                    proProductDao.updateByProductId(extProduct1.getProductId());
                    this.save(extProduct1);
                    if (type == 2) {
                        List<ExtProductSku> extSkuList = new LinkedList<>();
                        CloudInfoResult cloudResult = cloudManagementService.getByProduct(productId);
                        cloudResult.getSkuStockEntityLists().stream().forEach(cloudSku -> {
                            ExtProductSku sku = new ExtProductSku();
                            sku.setSupplierPrice(cloudSku.getActPrice());
                            sku.setDeleteStatus(0);
                            sku.setStock(cloudSku.getStock().longValue());
                            sku.setSkuStockId(cloudSku.getSkuId());
                            sku.setExtProductId(extProduct1.getId());
                            sku.setProductId(extProduct1.getProductId());
                            extSkuList.add(sku);
                        });
                        extProductSkuService.saveBatch(extSkuList);
                    } else {
                        List<ProSkuStock> skuStock = proSkuStockService.list(new LambdaQueryWrapper<ProSkuStock>().eq(ProSkuStock::getProductId, productId));
                        List<ExtProductSku> extSkuList = new LinkedList<>();
                        skuStock.stream().forEach(proSkuStock -> {
                            ExtProductSku sku = new ExtProductSku();
                            sku.setSupplierPrice(proSkuStock.getPlatformSalePrice());
                            sku.setDeleteStatus(0);
                            sku.setStock(proSkuStock.getStock().longValue());
                            sku.setSkuStockId(proSkuStock.getId());
                            sku.setExtProductId(extProduct1.getId());
                            sku.setProductId(extProduct1.getProductId());
                            extSkuList.add(sku);
                        });
                        extProductSkuService.saveBatch(extSkuList);
                    }
                    successProIds.add(productId);
                } else {
                    failProIds.add(productId);
                }
            }
        });
        ExtProResult result = new ExtProResult();
        result.setFailProIds(failProIds);
        result.setSuccessProIds(successProIds);
        return result;
    }

    @Override
    public Page<ExtProResult> findProductNormal(Integer currentPage,Integer size,long userId,int status,int type) {
        Page<ExtProduct> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(size);
        List<ExtProResult> extPros = new LinkedList<>();
        if (type ==2) { //铺货中/已下架
            if (status!=2) {
                IPage<ExtProduct> result = this.page(page, new LambdaQueryWrapper<ExtProduct>().eq(ExtProduct::getUserMemberId, userId).eq(ExtProduct::getPublishStatus, status).eq(ExtProduct::getDeleteStatus,0));
                result.getRecords().stream().forEach(extProduct -> {
                    List<ExtProductSku> extProductSku = extProductSkuDao.findSkuByName(extProduct.getId());
                    ProProduct proProduct = proProductService.getById(extProduct.getProductId());
                    if (proProduct != null) {
                        ExtProResult proResult = new ExtProResult();
                        proResult.setExSkus(extProductSku);
                        proResult.setProductName(extProduct.getProductName());
                        proResult.setDetail(extProduct.getDescription());//这是图文详情
                        proResult.setProductId(extProduct.getProductId());
                        proResult.setPic(Arrays.asList(extProduct.getPic().split(",")));
                        extPros.add(proResult);
                    }

                });
                Page<ExtProResult> resultPage = new Page<>();
                resultPage.setRecords(extPros);
                resultPage.setTotal(result.getTotal());
                resultPage.setCurrent(page.getCurrent());
                resultPage.setSize(page.getPages());
                return resultPage;
            }else {
                IPage<ExtProduct> result = this.page(page, new LambdaQueryWrapper<ExtProduct>().eq(ExtProduct::getUserMemberId, userId).eq(ExtProduct::getDeleteStatus,0));
                result.getRecords().stream().forEach(extProduct -> {
                    List<ExtProductSku> extProductSku = extProductSkuDao.findSkuByName(extProduct.getId());

                    ProProduct proProduct = proProductService.getById(extProduct.getProductId());
                    if (proProduct!=null) {
                        ExtProResult proResult = new ExtProResult();
                        proResult.setExSkus(extProductSku);
                        proResult.setProductName(extProduct.getProductName());
                        proResult.setDetail(extProduct.getDescription());//这是图文详情
                        proResult.setProductId(extProduct.getProductId());
                        proResult.setPic(Arrays.asList(extProduct.getPic().replace("[","").replace("]","").split(",")));
                        extPros.add(proResult);
                    }

                });
                Page<ExtProResult> resultPage = new Page<>();
                resultPage.setRecords(extPros);
                resultPage.setTotal(result.getTotal());
                resultPage.setCurrent(page.getCurrent());
                resultPage.setSize(page.getPages());
                return resultPage;
            }
        }
        else if (type ==1){ //仓库中
            IPage<ExtProduct> result = this.page(page, new LambdaQueryWrapper<ExtProduct>().eq(ExtProduct::getUserMemberId, userId) .eq(ExtProduct::getExtType,3).eq(ExtProduct::getDeleteStatus,0));

            result.getRecords().stream().forEach(extProduct -> {
                List<ExtProductSku> extProductSku = extProductSkuDao.findSkuByName(extProduct.getId());
                ProProduct proProduct = proProductService.getById(extProduct.getProductId());
                if (proProduct!=null) {
                    ExtProResult proResult = new ExtProResult();
                    proResult.setExSkus(extProductSku);
                    proResult.setProductName(extProduct.getProductName());
                    proResult.setDetail(extProduct.getDescription());//这是图文详情
                    proResult.setProductId(extProduct.getProductId());
                    proResult.setPic(Arrays.asList(extProduct.getPic().replace("[","").replace("]","").split(",")));
                    extPros.add(proResult);
                }

            });
            Page<ExtProResult> resultPage = new Page<>();
            resultPage.setRecords(extPros);
            resultPage.setTotal(result.getTotal());
            resultPage.setCurrent(page.getCurrent());
            resultPage.setSize(page.getPages());
            return resultPage;
        }
        else {  //已售空
            Page<ProProduct> page1 = new Page<>(page.getCurrent(),page.getSize());
            IPage<ProProduct> result = proProductDao.selectByUserId(page1,userId);
            if (result!=null) {
                result.getRecords().stream().forEach(extProduct -> {
                    ExtProduct e = extProductDao.selectOne(new LambdaQueryWrapper<ExtProduct>().eq(ExtProduct::getProductId,extProduct.getId()).eq(ExtProduct::getUserMemberId,userId).eq(ExtProduct::getPublishStatus,0));

                    if (e!=null) {
                        List<ExtProductSku> extProductSku = extProductSkuDao.findSkuByName(extProduct.getId());

                        ExtProResult proResult = new ExtProResult();
                        proResult.setExSkus(extProductSku);
                        proResult.setProductName(extProduct.getProductName());
                        proResult.setDetail(extProduct.getDescription());//这是图文详情
                        proResult.setProductId(extProduct.getId());
                        proResult.setPic(Arrays.asList(extProduct.getPic().replace("[", "").replace("]", "").split(",")));
                        extPros.add(proResult);
                    }

                });
                Page<ExtProResult> resultPage = new Page<>();
                resultPage.setRecords(extPros);
                resultPage.setTotal(result.getTotal());
                resultPage.setCurrent(page.getCurrent());
                resultPage.setSize(page.getPages());
                return resultPage;
            }
           else {
               throw new RRException("暂无数据");
            }

        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateExtProduct(ExtProResult extProResult, Long userMemberId) {
        if (extProResult==null) {
            throw new RRException("错误!");
        }
        ExtProduct extProduct = this.getOne(new LambdaQueryWrapper<ExtProduct>().eq(ExtProduct::getProductId,extProResult.getProductId()).eq(ExtProduct::getUserMemberId,userMemberId));
        extProduct.setPic(JSONObject.toJSONString(extProResult.getPic()));
        extProduct.setPublishStatus(extProResult.getPublisstatus());
        extProduct.setProductName(extProduct.getProductName());

//        this.updateById(extProduct);
        extProductDao.updateById(extProduct);

        if (extProResult.getExSkus()!=null) {
            extProResult.getExSkus().stream().forEach(extSku -> {
                ProSkuStock skuStock = proSkuStockService.getById(extSku.getSkuStockId());
                if (extSku.getSupplierPrice().compareTo(skuStock.getPlatformSalePrice()) >= 0) {
                    extProductSkuDao.updateById(extSku);
                }
            });
        }
        return true;
    }
}
