package com.keith.modules.service.record.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.exception.RRException;
import com.keith.common.utils.Query;
import com.keith.modules.dao.record.StoreRecordDao;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.entity.record.PlatformRecord;
import com.keith.modules.entity.record.StoreRecord;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.order.OrderItemService;
import com.keith.modules.service.order.OrderOrderService;
import com.keith.modules.service.order.impl.OrderOrderServiceImpl;
import com.keith.modules.service.product.ProProductService;
import com.keith.modules.service.product.ProSkuStockService;
import com.keith.modules.service.record.PlatformRecordService;
import com.keith.modules.service.record.StoreRecordService;
import com.keith.modules.service.user.UserMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.keith.common.utils.PageUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("storeRecordService")
public class StoreRecordServiceImpl extends ServiceImpl<StoreRecordDao, StoreRecord> implements StoreRecordService {

    @Autowired
    private OrderOrderService orderOrderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProProductService proProductService;
    @Autowired
    private UserMemberService userMemberService;
    @Autowired
    private ProSkuStockService proSkuStockService;
    @Autowired
    private PlatformRecordService platformRecordService;
    @Autowired
    private StoreRecordService storeRecordService;
    @Autowired
    private StoreRecordDao storeRecordDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StoreRecord> page = this.page(
                new Query<StoreRecord>().getPage(params),
                new QueryWrapper<StoreRecord>()
        );

        return new PageUtils(page);
    }

    /**
     * 分销记录
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveRecord(Long orderId){
        BigDecimal earn = new BigDecimal(0);
        BigDecimal allInPrice = new BigDecimal(0);
        BigDecimal allFreightAmount = new BigDecimal(0);//总运费
        ProProduct product = new ProProduct();
        //根据订单号获取商品信息
        OrderOrder orderOrder = orderOrderService.getOne(new QueryWrapper<OrderOrder>().eq("id",orderId));
        //查询用户，根据用户等级计算铺货价
        UserMember userMember = userMemberService.getOne(new QueryWrapper<UserMember>().eq("id",orderOrder.getUserAdminId()));

        allFreightAmount = orderOrder.getFreightAmount();

        List<OrderItem> orderItems = orderItemService.getBaseMapper().selectList(new QueryWrapper<OrderItem>().eq("order_id",orderOrder.getId()));
        for (OrderItem orderItem:orderItems){


             product = proProductService.getOne(new QueryWrapper<ProProduct>().eq("id",orderOrder.getProductId()));
            if(product.getUnifyStatus()==1){//统一规格
                allInPrice = allInPrice.add(product.getSalePrice());
            }else {
                //过规格根据SKUID查询价格
                ProSkuStock proSkuStock = proSkuStockService.getOne(new QueryWrapper<ProSkuStock>().eq("id",orderItem.getProductSkuId()));
                allInPrice = allInPrice.add(proSkuStock.getPlatformPrice());
            }
//            allFreightAmount = allFreightAmount.add(orderItem.getFreightAmount());
        }
        //店家收益 = 实际支付-进货价-运费     进货价=平台进货价*会员等级优惠折扣
        if(userMember.getLevelId()==3){//B级店主铺货9折优惠
            allInPrice = allInPrice.multiply(new BigDecimal(0.9)).setScale(2,BigDecimal.ROUND_HALF_UP);
        }else if (userMember.getLevelId()==4){//C级店主铺货8.5折优惠
            allInPrice = allInPrice.multiply(new BigDecimal(0.85)).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        BigDecimal platform = new BigDecimal(0);//平台收益(订单实际支付金额-商家收益)
        earn = orderOrder.getPayAmount().subtract(allInPrice).subtract(allFreightAmount);


        try {
            //存入分销记录表
            StoreRecord storeRecord = new StoreRecord();
            storeRecord.setCostFee(earn);
            storeRecord.setUserName(userMember.getUsername());
            storeRecord.setUserId(userMember.getId());
            storeRecord.setOrderId(orderId);
            storeRecord.setProductId(product.getId());
            storeRecord.setProductName(product.getProductName());
            storeRecord.setCreateTime(new Date());
            storeRecordService.save(storeRecord);

            //存入平台收益表
            PlatformRecord platformRecord = new PlatformRecord();
            platform = orderOrder.getPayAmount().subtract(earn).subtract(allFreightAmount);
            platformRecord.setCostFee(platform);
            platformRecord.setUserId(userMember.getId());
            platformRecord.setUserName(userMember.getUsername());
            platformRecord.setName(0);
            platformRecord.setOrderId(orderId);
            platformRecord.setUserType(0);
            platformRecord.setRecordType(1);
            platformRecord.setCreateTime(new Date());
            platformRecordService.save(platformRecord);

            //运费记录保存
            PlatformRecord platformRecord1 = new PlatformRecord();
//            platform = orderOrder.getPayAmount().subtract(earn);
            platformRecord1.setCostFee(allFreightAmount);
            platformRecord1.setUserId(userMember.getId());
            platformRecord1.setUserName(userMember.getUsername());
            platformRecord1.setName(1);
            platformRecord1.setOrderId(orderId);
            platformRecord1.setUserType(0);
            platformRecord1.setRecordType(1);
            platformRecord1.setCreateTime(new Date());
            platformRecordService.save(platformRecord1);
        } catch (Exception e) {
            throw new RRException("分销记录保存失败",e);
        }
    }

    /**
     * 获取本周订单收益
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public StoreRecord getMonth(Long userId){
        return storeRecordDao.getMonth(userId);
    }

    /**
     * 获取所有订单收益
     * @return
     */

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public StoreRecord getAll(Long userId){
        return storeRecordDao.getAll(userId);
    }

}
