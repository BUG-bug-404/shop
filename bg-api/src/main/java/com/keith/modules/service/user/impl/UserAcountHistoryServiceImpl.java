package com.keith.modules.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.user.UserAcountHistoryDao;
import com.keith.modules.dto.PageDTO;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.entity.user.UserAcountHistory;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.order.OrderItemService;
import com.keith.modules.service.order.OrderOrderService;
import com.keith.modules.service.product.ProProductService;
import com.keith.modules.service.product.ProSkuStockService;
import com.keith.modules.service.user.UserAcountHistoryService;
import com.keith.modules.service.user.UserMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("userAcountHistoryService")
public class UserAcountHistoryServiceImpl extends ServiceImpl<UserAcountHistoryDao, UserAcountHistory> implements UserAcountHistoryService {

    @Autowired
    private OrderOrderService orderOrderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ProSkuStockService proSkuStockService;

    @Autowired
    private UserMemberService userMemberService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserAcountHistory> page = this.page(
                new Query<UserAcountHistory>().getPage(params),
                new QueryWrapper<UserAcountHistory>()
        );

        return new PageUtils(page);
    }

    /**
     * 计算该订单的收益佣金保存到数据库
     * BigDecimal.ROUND_HALF_UP //四舍五入
     * BigDecimal.ROUND_UP  // 向上取整
     * BigDecimal.ROUND_DOWN  // 向下取整
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void orderEarnings(Long orderId) {

        //总进货价
        BigDecimal allPrice = new BigDecimal(0);
        //总收益
        BigDecimal orderProfits = new BigDecimal(0);
        //订单信息
        OrderOrder orderOrder = orderOrderService.getById(orderId);

        //看该订单是否是用户从店家购买的有效分佣订单，若不是则不进行分佣
    if (!"".equals(orderOrder.getUserAdminId()) && orderOrder.getUserAdminId() != null) {

        //先用订单ID查询出该订单所有商品信息信息
        List<OrderItem> orderItems = orderItemService.list(new QueryWrapper<OrderItem>().eq("order_id", orderId));

        //遍历订单的所有商品去查询对应的进货价，得出每件商品的纯利润收益
        for (OrderItem orderItem : orderItems) {
            //用product_id查询该商品的信息
            ProProduct proProduct = proProductService.getById(orderItem.getProductId());

            //判断该商品是统一规格还是多规格，是多规格的话需要去pro_sku_stock查询对应的商品进货价（是否为统一规格：0->不是；1->是）
            if (proProduct.getUnifyStatus() == 1) {
                allPrice = (proProduct.getSalePrice().multiply(new BigDecimal(orderItem.getProductQuantity())).add(allPrice)).setScale(2, BigDecimal.ROUND_DOWN);
            } else {
                //按照规格ID查询该规格的进货价
                ProSkuStock proSkuStock = proSkuStockService.getById(orderItem.getProductSkuId());
                allPrice = (proSkuStock.getPlatformPrice().multiply(new BigDecimal(orderItem.getProductQuantity())).add(allPrice)).setScale(2, BigDecimal.ROUND_DOWN);
            }
        }
        //该订单收益为 订单应付金额-订单总进货价-运费
        orderProfits = (orderOrder.getPayAmount().subtract(orderOrder.getFreightAmount()).subtract(allPrice)).setScale(2, BigDecimal.ROUND_DOWN);

        //查询该订单店主的上级以及上级的上级是否是A级店长，如是则进行分佣
        UserMember userMember = userMemberService.getParentInfo(orderOrder.getUserAdminId());
        //当上级不为空且是A级店长时就进行分佣
        if (userMember != null) {
            if (userMember.getLevelId() == 2) {
                //将该订单的收益存入数据库
                UserAcountHistory acountHistory = new UserAcountHistory();
                acountHistory.setBalanceType(4);
                acountHistory.setUserMemberId(userMember.getId());
                acountHistory.setNote("订单ID为：" + orderId + ",从用户ID为:" + orderOrder.getUserAdminId() + "得到的A级店长分佣,不存入钱包，只用于统计");
                acountHistory.setOrderId(orderId);
                acountHistory.setBalanceHistory(orderProfits.multiply(new BigDecimal(0.08)).setScale(2, BigDecimal.ROUND_DOWN));
                acountHistory.setCreateTime(new Date());
                this.baseMapper.insert(acountHistory);
            }else if(userMember.getLevelId() == 3){
                //将该订单的收益存入数据库
                UserAcountHistory acountHistory = new UserAcountHistory();
                acountHistory.setBalanceType(4);
                acountHistory.setUserMemberId(userMember.getId());
                acountHistory.setNote("订单ID为：" + orderId + ",从用户ID为:" + orderOrder.getUserAdminId() + "得到的A级店长分佣,不存入钱包，只用于统计");
                acountHistory.setOrderId(orderId);
                acountHistory.setBalanceHistory(orderProfits.multiply(new BigDecimal(0.1)).setScale(2, BigDecimal.ROUND_DOWN));
                acountHistory.setCreateTime(new Date());
            }else if(userMember.getLevelId() == 4){
                //将该订单的收益存入数据库
                UserAcountHistory acountHistory = new UserAcountHistory();
                acountHistory.setBalanceType(4);
                acountHistory.setUserMemberId(userMember.getId());
                acountHistory.setNote("订单ID为：" + orderId + ",从用户ID为:" + orderOrder.getUserAdminId() + "得到的A级店长分佣,不存入钱包，只用于统计");
                acountHistory.setOrderId(orderId);
                acountHistory.setBalanceHistory(orderProfits.multiply(new BigDecimal(0.1)).setScale(2, BigDecimal.ROUND_DOWN));
                acountHistory.setCreateTime(new Date());
            }
            //若订单所属店主的上级用户还存在上级用户
            if (!"".equals(userMember.getParentId()) && userMember.getParentId() != null) {
                //查看上级的上级是否是A级店长，是的话也进行分佣处理
                UserMember userGrandFather = userMemberService.getParentInfo(userMember.getParentId());
                if (userGrandFather.getLevelId() == 2) {
                    //将该订单的收益存入数据库
                    UserAcountHistory acountHistory = new UserAcountHistory();
                    acountHistory.setBalanceType(4);
                    acountHistory.setUserMemberId(userGrandFather.getId());
                    acountHistory.setNote("订单ID为：" + orderId + ",从用户ID为:" + orderOrder.getUserAdminId() + "得到的A级店长分佣,不存入钱包，只用于统计");
                    acountHistory.setOrderId(orderId);
                    acountHistory.setBalanceHistory(orderProfits.multiply(new BigDecimal(0.08)).setScale(2, BigDecimal.ROUND_DOWN));//小数点保留两位，四舍五入
                    acountHistory.setCreateTime(new Date());
                    this.baseMapper.insert(acountHistory);
                }else if(userMember.getLevelId() == 3){
                    //将该订单的收益存入数据库
                    UserAcountHistory acountHistory = new UserAcountHistory();
                    acountHistory.setBalanceType(4);
                    acountHistory.setUserMemberId(userMember.getId());
                    acountHistory.setNote("订单ID为：" + orderId + ",从用户ID为:" + orderOrder.getUserAdminId() + "得到的A级店长分佣,不存入钱包，只用于统计");
                    acountHistory.setOrderId(orderId);
                    acountHistory.setBalanceHistory(orderProfits.multiply(new BigDecimal(0.1)).setScale(2, BigDecimal.ROUND_DOWN));
                    acountHistory.setCreateTime(new Date());
                }else if(userMember.getLevelId() == 4){
                    //将该订单的收益存入数据库
                    UserAcountHistory acountHistory = new UserAcountHistory();
                    acountHistory.setBalanceType(4);
                    acountHistory.setUserMemberId(userMember.getId());
                    acountHistory.setNote("订单ID为：" + orderId + ",从用户ID为:" + orderOrder.getUserAdminId() + "得到的A级店长分佣,不存入钱包，只用于统计");
                    acountHistory.setOrderId(orderId);
                    acountHistory.setBalanceHistory(orderProfits.multiply(new BigDecimal(0.1)).setScale(2, BigDecimal.ROUND_DOWN));
                    acountHistory.setCreateTime(new Date());
                }
            }

        }
        //分佣完成，修改订单的分佣状态
        OrderOrder orderUpdate = new OrderOrder();
        orderUpdate.setId(orderOrder.getId());
        orderUpdate.setCloseAccount(1);
        orderOrderService.updateById(orderUpdate);
    }
    }

    /**
     * 分页获取
     * @return
     */
    @Override
    public PageUtils getPage(PageDTO pageDTO,Long userId){
        IPage<UserAcountHistory> page = new Page<>(pageDTO.getPage(),pageDTO.getPageSize());
        page = this.baseMapper.selectPage(page,new QueryWrapper<UserAcountHistory>().eq("user_member_id",userId));
        return new PageUtils(page);
    }
}
