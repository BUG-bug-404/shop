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
import com.keith.modules.service.ExtProduct.ExtProductService;
import com.keith.modules.service.ExtProduct.ExtProductSkuService;
import com.keith.modules.service.product.CloudManagementService;
import com.keith.modules.service.product.ProAlbumService;
import com.keith.modules.service.product.ProProductService;
import com.keith.modules.service.product.ProSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


@Service
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
    @Lazy(true)
    private CloudManagementService cloudManagementService;
    @Autowired
    private ExtProductSkuDao extProductSkuDao;
    @Autowired
    private ProProductDao proProductDao;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ExtProResult extProductNormal(List<Long> productIds, long userMemberId, int type){
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
                ExtProduct extProduct1 = new ExtProduct();
                extProduct1.setUserMemberId(userMemberId);
                extProduct1.setProductId(productId);
                extProduct1.setDeleteStatus(0);//添加的是未删除
                extProduct1.setCloudStatus(0);//0不是云仓过来的
                extProduct1.setPublishStatus(0);//默认下架状态
                extProduct1.setSale(0);//铺货过去的销量定义初始值
                extProduct1.setExtType(type);
                List<ProAlbum> proAlbums = proAlbumService.getBaseMapper().selectList(new QueryWrapper<ProAlbum>()
                        .eq("product_id",productId)
                        .eq("pic_owner", 1));//1是平台的
                if( proAlbums != null && proAlbums.size()>0){
                    List<String> arrayList = new ArrayList<>();
                    for(ProAlbum proAlbum : proAlbums){
                        arrayList.add(proAlbum.getPic());
                    }
                    extProduct1.setPic(arrayList.toString());//铺货的图片们，默认则用平台的图片
                }
                this.save(extProduct1);
                if (type ==2){
                    List<ExtProductSku> extSkuList = new LinkedList<>();
                    CloudInfoResult cloudResult = cloudManagementService.getByProduct(productId);
                    cloudResult.getCloudSkuInfos().stream().forEach(cloudSku->{
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
                }
                else {
                    List<ProSkuStock> skuStock = proSkuStockService.list(new LambdaQueryWrapper<ProSkuStock>().eq(ProSkuStock::getProductId,productId));
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
            IPage<ExtProduct> result = this.page(page, new LambdaQueryWrapper<ExtProduct>().eq(ExtProduct::getUserMemberId, userId).eq(ExtProduct::getPublishStatus, status));
            result.getRecords().stream().forEach(extProduct -> {
                List<ExtProductSku> extProductSku = extProductSkuDao.findSkuByName(extProduct.getId());
                ProProduct proProduct = proProductService.getById(extProduct.getProductId());

                ExtProResult proResult = new ExtProResult();
                proResult.setExSkus(extProductSku);
                proResult.setProductName(proProduct.getProductName());
                proResult.setDetail(proProduct.getDescription());//这是图文详情
                proResult.setProductId(extProduct.getProductId());
                proResult.setPic(Arrays.asList(extProduct.getPic().replace("[","").replace("]","").split(",")));
                proResult.setCloudStatus(extProduct.getCloudStatus());
                extPros.add(proResult);

            });
            Page<ExtProResult> resultPage = new Page<>();
            resultPage.setRecords(extPros);
            resultPage.setTotal(result.getTotal());
            resultPage.setCurrent(page.getCurrent());
            resultPage.setSize(page.getPages());
            return resultPage;
        }
        else if (type ==1){ //仓库中
            IPage<ExtProduct> result = this.page(page, new LambdaQueryWrapper<ExtProduct>().eq(ExtProduct::getUserMemberId, userId) .eq(ExtProduct::getExtType,3));

            result.getRecords().stream().forEach(extProduct -> {
                List<ExtProductSku> extProductSku = extProductSkuDao.findSkuByName(extProduct.getId());
                ProProduct proProduct = proProductService.getById(extProduct.getProductId());

                ExtProResult proResult = new ExtProResult();
                proResult.setExSkus(extProductSku);
                proResult.setProductName(proProduct.getProductName());
                proResult.setDetail(proProduct.getDescription());//这是图文详情
                proResult.setProductId(extProduct.getProductId());
                proResult.setCloudStatus(extProduct.getCloudStatus());
                proResult.setPic(Arrays.asList(extProduct.getPic().replace("[","").replace("]","").split(",")));
                extPros.add(proResult);

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
                    List<ExtProductSku> extProductSku = extProductSkuDao.findSkuByName(extProduct.getId());

                    ExtProResult proResult = new ExtProResult();
                    proResult.setExSkus(extProductSku);
                    proResult.setProductName(extProduct.getProductName());
                    proResult.setDetail(extProduct.getDescription());//这是图文详情
                    proResult.setProductId(extProduct.getId());
                    proResult.setPic(Arrays.asList(extProduct.getPic().replace("[","").replace("]","").split(",")));
                    extPros.add(proResult);

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
        ExtProduct extProduct = new ExtProduct();
        extProduct.setPic(JSONObject.toJSONString(extProResult.getPic()));
        extProduct.setPublishStatus(extProResult.getPublisstatus());
        this.updateById(extProduct);

        extProResult.getExSkus().stream().forEach(extSku->{
            ProSkuStock skuStock = proSkuStockService.getById(extSku.getSkuStockId());
            if (extSku.getSupplierPrice().compareTo(skuStock.getPlatformSalePrice())>=0) {
                extProductSkuDao.updateById(extSku);
            }
        });
        return true;
    }
}
