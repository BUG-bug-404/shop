package com.keith.modules.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.exception.RRException;
import com.keith.modules.dao.user.UserLevelChangeDao;
import com.keith.modules.entity.ExtProduct.ExtProduct;
import com.keith.modules.entity.ExtProduct.ExtProductSku;
import com.keith.modules.entity.order.Order;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.product.CloudManagement;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.entity.user.*;
import com.keith.modules.service.ExtProduct.ExtProductService;
import com.keith.modules.service.ExtProduct.ExtProductSkuService;
import com.keith.modules.service.order.OrderItemService;
import com.keith.modules.service.order.OrderOrderService;
import com.keith.modules.service.product.CloudManagementService;
import com.keith.modules.service.product.ProProductService;
import com.keith.modules.service.product.ProSkuStockService;
import com.keith.modules.service.user.UserAcountHistoryService;
import com.keith.modules.service.user.UserBanlanceService;
import com.keith.modules.service.user.UserLevelChangeService;
import com.keith.modules.service.user.UserMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("userLevelChangeService")
public class UserLevelChangeServiceImpl extends ServiceImpl<UserLevelChangeDao, UserLevelChange> implements UserLevelChangeService {

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private UserLevelChangeService userLevelChangeService;

    @Autowired
    private  UserBanlanceService userBanlanceService;

    @Autowired
    private UserAcountHistoryService userAcountHistoryService;

    @Autowired
    private OrderOrderService orderOrderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ExtProductService extProductService;

    @Autowired
    private ExtProductSkuService extProductSkuService;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ProSkuStockService proSkuStockService;

    @Autowired
    private CloudManagementService cloudManagementService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserLevelChange> page = this.page(
                new Query<UserLevelChange>().getPage(params),
                new QueryWrapper<UserLevelChange>()
        );

        return new PageUtils(page);
    }

    /**
     * 用户花199升级成为A级店长,同时更新上级店长邀请人数，判断是否触发升级条件
     */

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatelevelA(Long userId){
        UserMember user = userMemberService.getInfo(userId);
        if(user == null){
            throw  new RRException("用户资源获取失败，无法升级！");
        }
        //更新用户列表的店家等级和用户等级表的信息
        try {

            user.setLevelId(2L);
            userMemberService.updateById(user);
            UserLevelChange userLevelChange = new UserLevelChange();
            userLevelChange.setUserMemberId(user.getId());
            userLevelChange.setCurrentLevelId(2L);
            userLevelChange.setWelfareStatus(0);
            userLevelChange.setNote("用户花199升级成为A级店长");
            userLevelChange.setCreateTime(new Date());
            userLevelChangeService.save(userLevelChange);

            //同时更新邀请人的A级店长直邀人数
            if(!"".equals(user.getParentId()) && user.getParentId() != null){
                //判断上级店家是否满足店家升级条件
                UserMember userMember = userMemberService.getOne(new QueryWrapper<UserMember>().eq("id",user.getParentId()));

                    userMemberService.updateCount(user.getParentId());

                    //直邀店主的奖励
                    if(userMember.getLevelId()==2){
                        //A级店主享受新人直邀孵化奖69元
                        UserAcountHistory userAcountHistory = new UserAcountHistory();
                        userAcountHistory.setUserMemberId(userMember.getId());
                        userAcountHistory.setNote("直邀店主孵化奖励");
                        userAcountHistory.setBalanceType(0);
                        userAcountHistory.setBalanceHistory(new BigDecimal(69));
                        userAcountHistory.setCreateTime(new Date());
                        userAcountHistoryService.save(userAcountHistory);
                        userBanlanceService.updateBanlance(userMember.getId(),new BigDecimal(69));

                    }

                if(userMember.getInviteCount()>3){
                    if(userMember.getInviteCount()<10){
                        List<Order> list = orderOrderService.getCountForUser(userMember.getId());
                        if(list.size()>=3){
                            //可以升级A级店主
                            if(userMember.getLevelId()<2) {
                                userMember.setLevelId(2L);
                                userMemberService.updateById(userMember);
                                //更新等级信息
                                UserLevelChange level = new UserLevelChange();
                                level.setCreateTime(new Date());
                                level.setUserMemberId(userMember.getId());
                                level.setCurrentLevelId(2L);
                                level.setNote("直邀A级店主大于3且成功分享3单，升级为A级店主！");
                                userLevelChangeService.save(level);
                            }
                        }
                    }else if (userMember.getInviteCount()<20){
                        List<UserMember> list = userMemberService.getChildNum(userMember.getId());
                        if(list.size()>20){
                            //满足升级B级店主条件
                            if(userMember.getLevelId()<3) {
                                userMember.setLevelId(3L);
                                userMemberService.updateById(userMember);
                                //更新等级信息
                                UserLevelChange level = new UserLevelChange();
                                level.setCreateTime(new Date());
                                level.setUserMemberId(userMember.getId());
                                level.setCurrentLevelId(3L);
                                level.setNote("直邀A级店主大于10且社群总数大于20人，升级为B级店主！");
                                userLevelChangeService.save(level);
                            }

                        }
                    }else if (userMember.getInviteCount()>20){
                        List<UserMember> list = userMemberService.getChildNum(userMember.getId());
                        if(list.size()>200){
                            List<UserMember> userMemberList = userMemberService.list(new QueryWrapper<UserMember>().eq("parent_id",userMember.getId()));
                            Integer num = 0;
                            for(UserMember userMember1:userMemberList){
                                if(userMember1.getLevelId()==3){
                                    num+=1;
                                }
                            }
                            if (num>4){
                                //满足升级C级店主条件
                                userMember.setLevelId(4L);
                                userMemberService.updateById(userMember);
                                //更新等级信息
                                UserLevelChange level = new UserLevelChange();
                                level.setCreateTime(new Date());
                                level.setUserMemberId(userMember.getId());
                                level.setCurrentLevelId(4L);
                                level.setNote("直邀A级店主大于20且社群总数大于200人直属B级团长大于4人，升级为C级店主！");
                                userLevelChangeService.save(level);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RRException("用户等级更新失败！");
        }

    }
    /**
     * 用户花388升级成为A级店长,同时更新上级店长邀请人数，判断是否触发升级条件
     */

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatelevelB(Long userId) {
        UserMember user = userMemberService.getInfo(userId);
        if (user == null) {
            throw new RRException("用户资源获取失败，无法升级！");
        }
        try {
            user.setLevelId(3L);
            userMemberService.updateById(user);
            UserLevelChange userLevelChange = new UserLevelChange();
            userLevelChange.setUserMemberId(user.getId());
            userLevelChange.setCurrentLevelId(3L);
            userLevelChange.setWelfareStatus(0);
            userLevelChange.setNote("用户花388升级成为B级店长");
            userLevelChange.setCreateTime(new Date());
            userLevelChangeService.save(userLevelChange);
        } catch (Exception e) {
            throw new RRException("升级失败！",e);
        }
    }
    /**
     * 用户花588升级成为A级店长,同时更新上级店长邀请人数，判断是否触发升级条件
     */

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatelevelC(Long userId) {
        UserMember user = userMemberService.getInfo(userId);
        if (user == null) {
            throw new RRException("用户资源获取失败，无法升级！");
        }
        try {
            user.setLevelId(4L);
            userMemberService.updateById(user);
            UserLevelChange userLevelChange = new UserLevelChange();
            userLevelChange.setUserMemberId(user.getId());
            userLevelChange.setCurrentLevelId(3L);
            userLevelChange.setWelfareStatus(0);
            userLevelChange.setNote("用户花588升级成为C级店长");
            userLevelChange.setCreateTime(new Date());
            userLevelChangeService.save(userLevelChange);
        } catch (Exception e) {
            throw new RRException("升级失败！",e);
        }
    }


    @Override
    public void updateLevelByOrder(Long orderId){
        //根据订单ID查询订单信息
        OrderOrder orderOrder = orderOrderService.getOne(new QueryWrapper<OrderOrder>().eq("id",orderId));
//        OrderItem orderItem = orderItemService.getOne(new QueryWrapper<OrderItem>().eq("order_id",orderId));
        Long userId = orderOrder.getUserAdminId();
        BigDecimal extAll = new BigDecimal(0);//铺货总价
        //查询所有该店家已完成的订单
        List<OrderOrder> orderOrderList = orderOrderService.list(
                new QueryWrapper<OrderOrder>().eq("user_admin_id",userId).eq("status",4));
        for (OrderOrder orderOrder1:orderOrderList){
                List<OrderItem> orderItems = orderItemService.list(new QueryWrapper<OrderItem>().eq("order_id",orderOrder1.getId()));
                //去商品表查询该商品的铺货价格
            for (OrderItem orderItem:orderItems){

                //查看是否是来自云仓商品
                ExtProduct extProduct = extProductService.getOne(new QueryWrapper<ExtProduct>().eq("product_id",orderItem.getProductId()));
                if (extProduct.getCloudStatus()==1){
                    CloudManagement cloudManagement = cloudManagementService.getOne(
                            new QueryWrapper<CloudManagement>().eq("id",extProduct.getCloudId())
                                    .eq("product_id",extProduct.getProductId()).eq("pro_sku_stock_id",orderItem.getProductSkuId()));
                    extAll = extAll.add(cloudManagement.getAcitivityPrice());
                }else {
                    ProProduct product = proProductService.getOne(new QueryWrapper<ProProduct>().eq("id", orderItem.getProductId()));
                    //如果是统一规格
                    if (product.getUnifyStatus() == 1) {
                        extAll = extAll.add(product.getSalePrice());
                    } else {
                        ProSkuStock proSkuStock = proSkuStockService.getOne(
                                new QueryWrapper<ProSkuStock>().eq("product_id",orderItem.getProductId()).eq("id",orderItem.getProductSkuId()));
                        extAll = extAll.add(proSkuStock.getPlatformPrice());
                    }
                }
            }
        }
        UserMember userMember = userMemberService.getOne(new QueryWrapper<UserMember>().eq("id",userId));

        if (extAll.compareTo(new BigDecimal(30000)) == -1) {
            if (extAll.compareTo(new BigDecimal(5000)) == 0 || extAll.compareTo(new BigDecimal(5000)) == 1) {
                //可以升级为B级
                if(userMember.getLevelId()<3) {
                    UserMember user = new UserMember();
                    user.setLevelId(3L);
                    user.setId(userId);
                    userMemberService.updateById(user);
                    UserLevelChange userLevelChange = new UserLevelChange();
                    userLevelChange.setUserMemberId(user.getId());
                    userLevelChange.setCurrentLevelId(3L);
                    userLevelChange.setWelfareStatus(0);
                    userLevelChange.setNote("个人店铺累计销售额大于或等于5000，升级为B级店长！");
                    userLevelChange.setCreateTime(new Date());
                    userLevelChangeService.save(userLevelChange);
                }
            }
        }else if (extAll.compareTo(new BigDecimal(30000)) == 0 || extAll.compareTo(new BigDecimal(30000)) == 1){
            //可以升级为C级
            if(userMember.getLevelId()<3) {
                UserMember user = new UserMember();
                user.setLevelId(4L);
                user.setId(userId);
                userMemberService.updateById(user);
                UserLevelChange userLevelChange = new UserLevelChange();
                userLevelChange.setUserMemberId(user.getId());
                userLevelChange.setCurrentLevelId(4L);
                userLevelChange.setWelfareStatus(0);
                userLevelChange.setNote("个人店铺累计销售额大于或等于30000，升级为C级店长！");
                userLevelChange.setCreateTime(new Date());
                userLevelChangeService.save(userLevelChange);
            }
        }


    }
}
