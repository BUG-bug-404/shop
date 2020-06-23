//package com.keith.modules.service.order.impl;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.keith.common.exception.RRException;
//import com.keith.common.utils.PageUtils;
//import com.keith.modules.dao.order.OrderCartItemDao;
//import com.keith.modules.entity.order.OrderCartItem;
//import com.keith.modules.entity.product.ProAlbum;
//import com.keith.modules.entity.product.ProProduct;
//import com.keith.modules.entity.product.ProSkuStock;
//import com.keith.modules.service.order.OrderCartItemService;
//import com.keith.modules.service.product.ProAlbumService;
//import com.keith.modules.service.product.ProProductService;
//import com.keith.modules.service.product.ProSkuStockService;
//import com.keith.modules.service.user.UserMemberService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//
//@Service("orderCartItemService")
//public class OrderCartItemServiceImpl extends ServiceImpl<OrderCartItemDao, OrderCartItem> implements OrderCartItemService {
//
//    @Autowired
//    private ProProductService proProductService;
//    @Autowired
//    private UserMemberService userMember;
//    @Autowired
//    private ProAlbumService proAlbumService;
//    @Autowired
//    private ProSkuStockService proSkuStockService;
//
//    /**
//     * 加入进货车
//     */
//    @Override
//    public boolean add(Long productId, Long skuId, Integer count, Long userMemberId) {
//        /*判断数量是否大于库存*/
//        /*判断此规格和商品id和用户id*/
//        OrderCartItem cartItem = this.getOne(new QueryWrapper<OrderCartItem>().
//                eq("product_sku_id", skuId).
//                eq("user_member_id", userMemberId).
//                eq("product_id", productId));
//        /*获取规格*/
//        ProSkuStock skuStock = proSkuStockService.getOne(new QueryWrapper<ProSkuStock>().
//                eq("product_id", productId).
//                eq("id", skuId));
//        ProProduct proProduct = proProductService.getOne(new QueryWrapper<ProProduct>().
//                eq("delete_status", 0).
//                eq("verify_status", 1).
//                eq("publish_status", 1).eq("id", productId).isNotNull("parent_id"));
//        if (proProduct == null || skuStock == null) {
//            throw new RRException("商品或规格不存在！");
//        }
//        Integer stock = skuStock.getStock();//获取库存
//        if (count != null && count > stock) {
//            throw new RRException("数量大于库存！");
//        }
//        if (cartItem == null) {
//            /*用户昵称*/
//            String username = userMember.getById(userMemberId).getUsername();
//            /*获取商品图片*/
//            String pic = proAlbumService.getOne(new QueryWrapper<ProAlbum>().eq("product_id", productId).eq("cover_status", 0).eq("pic_owner", 1)).getPic();
//
//            OrderCartItem orderCartItem = new OrderCartItem();
//            orderCartItem.setProductId(productId);
//            orderCartItem.setProductSkuId(skuId);
//            orderCartItem.setUserMemberId(userMemberId);
//            orderCartItem.setMemberUsername(username);
//            orderCartItem.setQuantity(count);
//            /*按数量判断这个规格的价格，ok,平台没有区间价格，只有规格价格*/
//            orderCartItem.setPrice(skuStock.getPlatformPrice());//批发价格
//            orderCartItem.setSalePrice(skuStock.getPlatformSalePrice());//销售价格
//            orderCartItem.setProductPic(pic);
//            orderCartItem.setProductName(proProduct.getProductName());
//            /*商品分类id*/
//            orderCartItem.setProductCategoryId(proProduct.getProductAttributeCategoryId());
//            orderCartItem.setValidStatus(0);//既然是平台的上架的商品那就是有效的进货车
//            orderCartItem.setProductAttr(skuStock.getSp2());
//
//            boolean save = this.save(orderCartItem);
//            return save;
//        } else {
//            Integer quantity = cartItem.getQuantity();
//            Integer sum = null;
//            sum = quantity + count;
//            if (count != null && sum <= stock) {
//                cartItem.setQuantity(sum);
//                cartItem.setPrice(skuStock.getPlatformPrice());//批发价格
//                cartItem.setSalePrice(skuStock.getPlatformSalePrice());//销售价格
//                cartItem.setProductName(proProduct.getProductName());
//                boolean updateById = this.updateById(cartItem);
//                return updateById;
//            }
//            throw new RRException("数量大于库存！");
//        }
//    }
//
//    /**
//     * 删除，传入一个id，count，sku
//     */
//    @Override
//    public boolean deleteCount(Long productId, Long skuId, Integer count, Long userMemberId) {
//
//        OrderCartItem cartItem = this.getOne(new QueryWrapper<OrderCartItem>().
//                eq("product_sku_id", skuId).
//                eq("user_member_id", userMemberId).
//                eq("product_id", productId));
//        /*获取规格*/
//        ProSkuStock skuStock = proSkuStockService.getOne(new QueryWrapper<ProSkuStock>().
//                eq("product_id", productId).
//                eq("id", skuId));
//        ProProduct proProduct = proProductService.getOne(new QueryWrapper<ProProduct>().
//                eq("delete_status", 0).
//                eq("verify_status", 1).
//                eq("publish_status", 1).eq("id", productId));
//        if (cartItem == null || proProduct == null || skuStock == null) {
//            throw new RRException("进货车中不存在此商品或商品、规格不存在或商品已失效！");
//        }
//        Integer stock = null;
//        stock = cartItem.getQuantity();//获取进货车里规格的数量
//        if (count != null && count > stock) {
//            throw new RRException("数量大于进货车里的数量！");
//        }
//        try {
//            Integer sum = null;
//            sum = stock - count;
//            if (sum == 0) {
//                this.removeById(cartItem.getId());
//            }
//            cartItem.setQuantity(sum);
//            cartItem.setPrice(skuStock.getPlatformPrice());//批发价格
//            cartItem.setSalePrice(skuStock.getPlatformSalePrice());//销售价格
//            cartItem.setProductName(proProduct.getProductName());
//            this.updateById(cartItem);
//        } catch (Exception e) {
//            throw new RRException("有问题啊！");
//        }
//        return true;
//
//    }
//
//    /**
//     * 查找当前用户的进货车货物
//     */
//    @Override
//    public PageUtils findAll(Integer currentPage, Integer pageSize, Long userMemberId, Integer status) {
//        if (status != null && status == 0) {
//            IPage<OrderCartItem> page = new Page<>(currentPage, pageSize);
//            QueryWrapper<OrderCartItem> queryWrapper = new QueryWrapper<OrderCartItem>().
//                    eq("user_member_id", userMemberId)
//                    .eq("valid_status", 0);//0是未失效
//            page = this.baseMapper.selectPage(page, queryWrapper);
//            return new PageUtils(page);
//        } else {
//            IPage<OrderCartItem> page = new Page<>(currentPage, pageSize);
//            QueryWrapper<OrderCartItem> queryWrapper = new QueryWrapper<OrderCartItem>().
//                    eq("user_member_id", userMemberId)
//                    .eq("valid_status", 1);//1是已失效
//            page = this.baseMapper.selectPage(page, queryWrapper);
//            return new PageUtils(page);
//        }
//
//    }
//
//    /**
//     * 批量删除
//     */
//    @Override
//    public boolean deleteByIds(List<Long> ids, Long userMemberId) {
//        try {
//            for (Long id : ids) {
//                this.remove(new QueryWrapper<OrderCartItem>().eq("id", id).eq("user_member_id", userMemberId));
//            }
//            return true;
//        } catch (Exception e) {
//            throw new RRException(e.getMessage());
//        }
//    }
//
//    /**
//     * 修改进货车里的商品规格
//     */
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public boolean updateSku(Long id, Long productId, Long skuId, Long userMemberId, Long reSkuId, Integer countchange) {
//
//        if (id == null && skuId == null) {
//            throw new RRException("传入参数为空！");
//        }
//        ProSkuStock proSkuStock = proSkuStockService.getOne(new QueryWrapper<ProSkuStock>().
//                eq("product_id", productId).eq("id", reSkuId));
//        if (proSkuStock == null) {
//            throw new RRException("选择的该商品规格不存在！");
//        }
//        if (skuId.equals(reSkuId)) {
//            OrderCartItem cartItem = this.getOne(new QueryWrapper<OrderCartItem>().
//                    eq("user_member_id", userMemberId).eq("product_sku_id", skuId).eq("id", id));
//            if (countchange > proSkuStock.getStock()) {
//                throw new RRException("数量大于规格总库存！");
//            }
//            cartItem.setQuantity(countchange);
//            this.updateById(cartItem);
//            return true;
//        } else {
//
//            OrderCartItem cartItem = this.getOne(new QueryWrapper<OrderCartItem>().
//                    eq("user_member_id", userMemberId).eq("product_sku_id", skuId).eq("id", id));
//            if (cartItem == null || cartItem.getValidStatus() == 1) {
//                throw new RRException("您的进货车不存在此商品规格或者商品已失效！");
//            }
//
//            /*判断要更改的规格在不在*/
//            OrderCartItem cartItems = this.getOne(new QueryWrapper<OrderCartItem>().
//                    eq("user_member_id", userMemberId).eq("product_sku_id", reSkuId));
//            if (cartItems != null) {
////                Integer count = null;
////                Integer counts = null;
////                Integer sum = null;
////                /*获取原规格的数量*/
////                count = cartItem.getQuantity();
////                /*获取要更改的数量*/
////                counts = cartItems.getQuantity();
////                sum = count + counts;
//                if (countchange > proSkuStock.getStock()) {
//                    throw new RRException("数量大于规格总库存！");
//                }
//                cartItems.setQuantity(countchange);
//                cartItems.setProductAttr(proSkuStock.getSp2());
//                this.updateById(cartItems);
//                this.remove(new QueryWrapper<OrderCartItem>().
//                        eq("user_member_id", userMemberId).eq("product_sku_id", skuId));
//            } else {
//                cartItem.setProductSkuId(reSkuId);
//                cartItem.setProductAttr(proSkuStock.getSp2());
//                this.updateById(cartItem);
//            }
//            return true;
//        }
//
//    }
//
//
//}
