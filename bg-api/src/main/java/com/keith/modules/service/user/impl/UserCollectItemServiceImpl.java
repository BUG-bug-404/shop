package com.keith.modules.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.modules.dao.product.ProAlbumDao;
import com.keith.modules.dao.product.ProProductDao;
import com.keith.modules.dao.user.UserCollectItemDao;
import com.keith.modules.dao.user.UserMemberDao;
import com.keith.modules.entity.product.ProAlbum;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.user.UserCollectItem;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.user.UserCollectItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("userCollectItemService")
public class UserCollectItemServiceImpl extends ServiceImpl<UserCollectItemDao, UserCollectItem> implements UserCollectItemService {
    @Autowired
    private ProProductDao proProductDao;
    @Autowired
    private UserMemberDao userMemberDao;
    @Autowired
    private ProAlbumDao proAlbumDao;
    @Override
    public Map<String,Object> setColletion(Long productId, Long userMemberId) {
        Map<String, Object> hashMap = new HashMap<>();
        /*先判断是否在收藏夹中，在的话，移除*/
        UserCollectItem collectItem = this.getOne(new QueryWrapper<UserCollectItem>().eq("product_id", productId).eq("user_member_id", userMemberId));
        if(collectItem == null) {
            /*根据商品id获取商品信息*/
            ProProduct proProduct = proProductDao.selectById(productId);
            BigDecimal platformPrice = proProduct.getPlatformPrice();//平台销售价格
            BigDecimal salePrice = proProduct.getSalePrice();//平台批发价格
            String productName = proProduct.getProductName();
//        String subTitle = proProduct.getSubTitle();
            UserMember userMember = userMemberDao.selectById(userMemberId);
            ProAlbum proAlbum = proAlbumDao.selectOne(new QueryWrapper<ProAlbum>().eq("product_id", productId).eq("cover_status", 0));
            UserCollectItem userCollectItem = new UserCollectItem();
            userCollectItem.setProductId(productId);
            userCollectItem.setUserMemberId(userMemberId);
            userCollectItem.setPrice(salePrice);
            userCollectItem.setSalePrice(platformPrice);
            /*主图*/
            userCollectItem.setProductPic(proAlbum.getPic());
            userCollectItem.setProductName(productName);
            userCollectItem.setProductSubTitle("");
            userCollectItem.setMemberUsername(userMember.getUsername());
            userCollectItem.setDeleteStatus(1);
            userCollectItem.setProductCategoryId(proProduct.getProductAttributeCategoryId());//二级分类
            boolean save = this.save(userCollectItem);
            hashMap.put("result","收藏成功！");
            return hashMap;
        }else if(collectItem != null){
            this.removeById(collectItem.getId());
            hashMap.put("result","取消收藏成功！");
            return hashMap;
        }else {
            hashMap.put("result","有误！");
            return hashMap;
        }
    }

    @Override
    public Boolean status(Long productId, Long userMemberId) {
        UserCollectItem collectItem = this.getOne(new QueryWrapper<UserCollectItem>().eq("product_id", productId).eq("user_member_id", userMemberId));
        if(collectItem == null ){
            return false;
        }
        return true;
    }

    @Override
    public PageUtils findAll(Integer currentPage, Integer pageSize,Long userMemberId) {
        QueryWrapper<UserCollectItem> wrapper = new QueryWrapper<UserCollectItem>().eq("user_member_id",userMemberId);
        IPage<UserCollectItem> iPage = new Page<>(currentPage,pageSize);
        if(iPage != null){
            iPage = this.baseMapper.selectPage(iPage,wrapper);
            return new PageUtils(iPage);
        }
        return null;
    }
}
