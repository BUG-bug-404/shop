package com.keith.modules.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keith.modules.dao.product.ProAlbumDao;
import com.keith.modules.dao.product.ProProductDao;
import com.keith.modules.dao.user.UserCollectItemDao;
import com.keith.modules.entity.product.ProAlbum;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.user.UserCollectItem;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.product.ProProductService;
import com.keith.modules.service.product.ProRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gray
 * @version 1.0
 * @date 2020/6/9 10:57
 */
@Service
public class ProRecommendServiceImpl implements ProRecommendService {

    @Autowired
    private ProAlbumDao albumDao;

    @Autowired
    private UserCollectItemDao userCollectItemDao;

    @Autowired
    private ProProductDao proProductDao;

    @Override
    public Page<ProProduct> findRecommendByCollect(int currentPage, int pageSize, long userId) {
        QueryWrapper<UserCollectItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("product_category_id").eq("user_member_id", userId);
        /**
         * 查询用户的收藏夹的商品的分类
         */
        List<UserCollectItem> list = userCollectItemDao.selectList(queryWrapper);
        List<Long> categoryIds = new ArrayList<>();
        list.stream().forEach(item ->{
            categoryIds.add(item.getProductCategoryId());
        });
        Page<ProProduct> page = new Page<>();
        page.setPages(currentPage);
        page.setSize(pageSize);
        IPage<ProProduct> resultPage = null;
        QueryWrapper<ProProduct> productQueryWrapper = new QueryWrapper<>();
        if (categoryIds.size()>0) {
            /**
             * 有收藏商品  推荐同类型商品
             */
            productQueryWrapper.select("product_name,sale_price,id").in("product_attribute_category_id", categoryIds).isNotNull("parent_id").eq("delete_status", 0).eq("verify_status", 1).eq("publish_status", 1);
            resultPage = proProductDao.selectPage(page,productQueryWrapper);
            resultPage.getRecords().stream().forEach(pro->{
                ProAlbum albumEntity = albumDao.selectCoverStatus(pro.getId());
                pro.setPic(albumEntity == null ? null : albumEntity.getPic());
            });
        }
        else {
            /**
             * 无收藏商品  推荐最新商品
             */
            productQueryWrapper.select("product_name,sale_price,id").isNotNull("parent_id").eq("delete_status", 0).eq("verify_status", 1).eq("publish_status", 1).orderByDesc("create_time");
            resultPage = proProductDao.selectPage(page,productQueryWrapper);
            resultPage.getRecords().stream().forEach(pro->{
                ProAlbum albumEntity = albumDao.selectCoverStatus(pro.getId());
                pro.setPic(albumEntity == null ? null : albumEntity.getPic());
            });
        }
        page.setRecords(resultPage.getRecords());
        page.setTotal(resultPage.getTotal());
        return page;
    }
}
