package com.keith.modules.service.order.impl;

import com.keith.common.exception.RRException;
import com.keith.modules.dao.address.UserReceiveAddressDao;
import com.keith.modules.dao.order.OrderCartItemDao;
import com.keith.modules.dao.order.OrderOrderDao;
import com.keith.modules.dao.product.CloudManagementDao;
import com.keith.modules.dao.product.ConWelfareDao;
import com.keith.modules.dao.product.ProProductDao;
import com.keith.modules.dao.product.ProSkuStockDao;
import com.keith.modules.dao.template.ProFeightProductDao;
import com.keith.modules.dao.template.ProTemplateDao;
import com.keith.modules.dto.order.FeightDTO;
import com.keith.modules.entity.address.UserReceiveAddress;
import com.keith.modules.entity.order.OrderCartItem;
import com.keith.modules.entity.product.CloudManagement;
import com.keith.modules.entity.product.ConWelfare;
import com.keith.modules.entity.product.ProProduct;
import com.keith.modules.entity.product.ProSkuStock;
import com.keith.modules.entity.template.ProFeightProduct;
import com.keith.modules.entity.template.ProFeightTemplate;
import com.keith.modules.entity.template.ProTemplate;
import com.keith.modules.form.CountOrderPrice;
import com.keith.modules.service.order.OrderOrderService;
import com.keith.modules.service.template.ProFeightTemplateService;
import com.keith.modules.service.template.ProTemplateService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.order.OrderItemDao;
import com.keith.modules.entity.order.OrderItem;
import com.keith.modules.service.order.OrderItemService;


@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItem> implements OrderItemService {


    @Autowired
    private ProProductDao proProductDao;
    @Autowired
    private UserReceiveAddressDao addressDao;

    @Autowired
    private OrderItemDao itemDao;
    @Autowired
    private ProTemplateDao templateDao;
    @Autowired
    private OrderOrderDao orderDao;
    @Autowired
    private ProSkuStockDao skuStockDao;
    @Autowired
    private CloudManagementDao cloudManagementDao;
    @Autowired
    private ConWelfareDao conWelfareDao;
    @Autowired
    private ProFeightProductDao proFeightProductDao;
    @Autowired
    private OrderCartItemDao orderCartItemDao;
    @Autowired
    private ProFeightTemplateService proFeightTemplateService;
    @Autowired
    private ProTemplateService proTemplateService;


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItem> page = this.page(
                new Query<OrderItem>().getPage(params),
                new QueryWrapper<OrderItem>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map<String, Object> CountOrderPrice(List<CountOrderPrice> countOrderPriceList, Long userMemberId) {
        Map<String, Object> map = new HashMap<>();
        if (countOrderPriceList == null || countOrderPriceList.size() <= 0) {
            throw new RRException("传入有误");
        }
        Integer status = countOrderPriceList.get(0).getStatus();
        Integer sum = 0;
        BigDecimal productAmount = new BigDecimal("0");
        List<Map<String, Object>> list = new ArrayList<>();
        for (CountOrderPrice countOrderPrice : countOrderPriceList) {
            Integer count = countOrderPrice.getCount();
            sum = count + sum;
            BigDecimal skuAmount = new BigDecimal("0");
            Integer priceCount = countOrderPrice.getCount();

            if (status != null && status == 0) {

                CloudManagement cloudManagement = cloudManagementDao.selectOne(new QueryWrapper<CloudManagement>().
                        eq("pro_sku_stock_id", countOrderPrice.getSkuId()).eq("product_id", countOrderPrice.getProductId()));
                ProSkuStock proSkuStock = skuStockDao.selectById(countOrderPrice.getSkuId());
                Integer stock = proSkuStock.getStock();
                if(stock <= 0 || stock < priceCount){
                    throw new RRException("库存不足！");
                }
                BigDecimal acitivityPrice = cloudManagement.getAcitivityPrice();
                skuAmount = skuAmount.add(acitivityPrice.multiply(new BigDecimal(priceCount)));
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("sp2", proSkuStock.getSp2());
                hashMap.put("skuId",countOrderPrice.getSkuId());
                hashMap.put("amount", skuAmount);
                list.add(hashMap);
            } else if (status != null && status == 1) {
                ProSkuStock proSkuStock = skuStockDao.selectById(countOrderPrice.getSkuId());
                Integer stock = proSkuStock.getStock();
                if(stock <= 0 || stock < priceCount){
                    throw new RRException("库存不足！");
                }
                ConWelfare conWelfare = conWelfareDao.selectOne(new QueryWrapper<ConWelfare>().
                        eq("pro_sku_stock_id", countOrderPrice.getSkuId()).eq("product_id", countOrderPrice.getProductId()));
                BigDecimal welfarePrice = conWelfare.getWelfarePrice();
                skuAmount = skuAmount.add(welfarePrice.multiply(new BigDecimal(priceCount)));
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("sp2", proSkuStock.getSp2());
                hashMap.put("skuId",countOrderPrice.getSkuId());
                hashMap.put("amount", skuAmount);
                list.add(hashMap);
            } else if(status != null && status == 2) {

                CloudManagement cloudManagement = cloudManagementDao.selectOne(new QueryWrapper<CloudManagement>().
                        eq("pro_sku_stock_id", countOrderPrice.getSkuId()).eq("product_id", countOrderPrice.getProductId()));
                ProSkuStock proSkuStock = skuStockDao.selectById(countOrderPrice.getSkuId());
                Integer stock = proSkuStock.getStock();
                if(stock <= 0 || stock < priceCount){
                    throw new RRException("库存不足！");
                }
                BigDecimal acitivityPrice = cloudManagement.getAcitivityPrice();
                skuAmount = skuAmount.add(acitivityPrice.multiply(new BigDecimal(priceCount)));
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("sp2", proSkuStock.getSp2());
                hashMap.put("skuId",countOrderPrice.getSkuId());
                hashMap.put("amount", skuAmount);
                list.add(hashMap);
            }else {
                ProSkuStock proSkuStock = skuStockDao.selectOne(new QueryWrapper<ProSkuStock>().eq("id", countOrderPrice.getSkuId()).
                        eq("product_id", countOrderPrice.getProductId()));
                BigDecimal platformSalePrice = proSkuStock.getPlatformSalePrice();//平台销售价格
                Integer stock = proSkuStock.getStock();
                if(stock <= 0 || stock < priceCount){
                    throw new RRException("库存不足！");
                }
                skuAmount = skuAmount.add(platformSalePrice.multiply(new BigDecimal(priceCount)));
                BigDecimal platformPrice = proSkuStock.getPlatformPrice();//批发价格
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("sp2", proSkuStock.getSp2());
                hashMap.put("skuId",countOrderPrice.getSkuId());
                hashMap.put("amount", skuAmount);
                list.add(hashMap);
            }
            productAmount = productAmount.add(skuAmount);

        }
        Integer addressId = countOrderPriceList.get(0).getAddressId();
        String addressInfo = countOrderPriceList.get(0).getAddressInfo();
        Integer productId = countOrderPriceList.get(0).getProductId();

        Object feightMoney = null;
        if (status != null && status == 2) {
            feightMoney = null;
        } else {
            if ((addressId != null && !"".equals(addressId)) && (addressInfo == null || "".equals(addressInfo))) {
                /*有地址id--获取邮费*/
                FeightDTO feightDTO = new FeightDTO();
                feightDTO.setAddressId(addressId);
                feightDTO.setNum(sum);
                feightDTO.setProductId(productId);
                Map<String, Object> stringObjectMap = feightMoney(feightDTO, userMemberId);
                feightMoney = stringObjectMap.get("feightMoney");
            } else {
                Map<String, Object> stringObjectMap = feightMoneyAdress(productId, addressInfo, sum);
                feightMoney = stringObjectMap.get("feightMoney");
            }

        }
        /*获取商品单个规格的价格乘以数量*/
        BigDecimal feightAmount = getBigDecimal(feightMoney);
        BigDecimal promotionAmount = new BigDecimal("0");//促销价格
        BigDecimal totalAmount = new BigDecimal("0");//总价格价格
        map.put("feightAmount", feightAmount);
        map.put("payAmount", productAmount);
        map.put("totalAmount", productAmount.add(feightAmount));
        map.put("skuAmount", list);
        map.put("productId",productId);
        return map;
    }

    @Override
    public Map<String, Object> countCartPrice(List<Long> id, Long userMemberId, Long addressId) {
        /*  @ApiModelProperty(value = "商品id")
    private Integer productId;

    @ApiModelProperty(value = "规格id")
    private Long skuId;

    @ApiModelProperty(value = "规格的量")
    private Integer count;

    @ApiModelProperty(value = "{收获地址id}")
    private Integer addressId;

    @ApiModelProperty(value = "{收获地址:示例：浙江省-杭州市-萧山区-详细地址}")
    private String addressInfo;

//    /*预售-代下单-尾款-云仓-福利商品*/
//        @ApiModelProperty(value = "{是0-云仓拿货走人1-福利商品2云仓货物屯仓-不传-现货3-预售无}")
//        private Integer status;*/


        if(id == null || id.size()<=0){
            throw new RRException("有误");
        }
        List<Map<String, Object>> list = new ArrayList<>();
        List<OrderCartItem> orderCartItems = orderCartItemDao.selectBatchIds(id);
        BigDecimal cartfeight = new BigDecimal("0");
        BigDecimal cartAllAmount = new BigDecimal("0");
        for(OrderCartItem orderCartItem : orderCartItems) {
//        orderCartItems.stream().forEach(orderCartItem -> {

            List<CountOrderPrice> arrayList = new ArrayList<>();
            CountOrderPrice countOrderPrice = new CountOrderPrice();

            countOrderPrice.setSkuId(orderCartItem.getProductSkuId());
            countOrderPrice.setAddressId(addressId.intValue());
            countOrderPrice.setProductId(orderCartItem.getProductId().intValue());
            countOrderPrice.setCount(orderCartItem.getQuantity());
            arrayList.add(countOrderPrice);

            Map<String, Object> map = this.CountOrderPrice(arrayList, userMemberId);
            Object totalAmount = map.get("totalAmount");
            Object feightAmount = map.get("feightAmount");
            list.add(map);
            cartAllAmount = cartAllAmount.add(new BigDecimal(totalAmount.toString()));
            cartfeight = cartfeight.add(new BigDecimal(feightAmount.toString()));
//            Integer cartItemType = orderCartItem.getType();
//            countOrderPrice.setStatus()
//        });
        }
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("cartAllAmount",cartAllAmount);
        objectMap.put("cartfeight",cartfeight);
        objectMap.put("result",list);

        return objectMap;

    }

    @Override
    public String countFeight(Long productId) {
        ProProduct proProduct = proProductDao.selectOne(new QueryWrapper<ProProduct>().eq("id", productId));
        ProFeightProduct proFeightProduct = proFeightProductDao.selectOne(new QueryWrapper<ProFeightProduct>().eq("product_id", proProduct.getParentId()));
        Long feightId = proFeightProduct.getFeightId();
        ProFeightTemplate proFeightTemplate = proFeightTemplateService.getById(feightId);
        Integer temTypt = proFeightTemplate.getTemTypt();
        if(temTypt != null && temTypt == 1){
            return "包邮";
        }else {
            ProTemplate proTemplate = proTemplateService.getOne(new QueryWrapper<ProTemplate>().
                    eq("feight_template_id", feightId));

            String str = null;
            Integer chargeType = proFeightTemplate.getChargeType();
            switch (chargeType){
                case 0 :
                    str = "首重"+proTemplate.getFirstFee()+"元";
                    break;
                case 1 :
                    str = "首件"+proTemplate.getFirstFee()+"元";
                    break;
                default:
                    str = "首体积"+proTemplate.getFirstFee()+"元";
            }
            return str;
        }
    }

    /*根据地址id获取邮费*/
    public Map<String, Object> feightMoney(FeightDTO feightDTO, Long userMemberId) {
        Map<String, Object> map = new HashMap<>();

        ProProduct proProduct = proProductDao.selectOne(new QueryWrapper<ProProduct>().eq("id", feightDTO.getProductId()));
        ProFeightProduct proFeightProduct = proFeightProductDao.selectOne(new QueryWrapper<ProFeightProduct>().eq("product_id", proProduct.getParentId()));

        List<ProTemplate> list = templateDao.selectList(new QueryWrapper<ProTemplate>().eq("feight_template_id", proFeightProduct.getFeightId()));

        if (list == null || list.size() <= 0) {
            throw new RRException("暂无运费信息无法配送");
        }
        UserReceiveAddress address = addressDao.selectOne(new QueryWrapper<UserReceiveAddress>().eq("user_member_id", userMemberId).eq("id", feightDTO.getAddressId()));
        if (address == null) {
            throw new RRException("地址信息有误");
        }
        for (ProTemplate template : list) {

            if (template.getProvince() != null && !template.getRegion().equals("true")) {
                String[] key = template.getProvince().split(",");
                String she = "";
                for (int i = 0; i < key.length; i++) {
                    if (address.getProvince().equals(key[i])) {
                        she = key[i];
                        break;
                    }
                }

                if (she.equals(address.getProvince()) || template.getRegion().equals("true")) {//
                    if (feightDTO.getNum() <= template.getFirstUnit().setScale(0, BigDecimal.ROUND_UP).intValue()) {//购买数量小于首数
                        map.put("feightMoney", template.getFirstFee());
                        return map;
                    }
                    if (feightDTO.getNum() > template.getFirstUnit().intValue()) {//购买数量大于首数
                        int num = -(template.getFirstUnit().setScale(0, BigDecimal.ROUND_UP).intValue() - feightDTO.getNum());

                        int a = num / template.getContinueUnit().setScale(0, BigDecimal.ROUND_UP).intValue();
                        int b = num % template.getContinueUnit().setScale(0, BigDecimal.ROUND_UP).intValue();

                        if (b > 0) {
                            b = 1;
                            BigDecimal feight = template.getContinueFee().multiply(new BigDecimal((a + b))).add(template.getFirstFee());
                            map.put("feightMoney", feight);
                            return map;
                        } else {
                            BigDecimal feight = template.getContinueFee().multiply(new BigDecimal(a)).add(template.getFirstFee());
                            map.put("feightMoney", feight);
                            return map;
                        }
                    }
                }

            } else {
                map.put("msg", "msg");
            }

        }

        return map;
    }

    /*根据地址获取邮费*/
    public Map<String, Object> feightMoneyAdress(Integer productId, String addressInfo, Integer sum) {

        Map<String, Object> map = new HashMap<>();

        ProProduct proProduct = proProductDao.selectOne(new QueryWrapper<ProProduct>().eq("id", productId));
        ProFeightProduct proFeightProduct = proFeightProductDao.selectOne(new QueryWrapper<ProFeightProduct>().eq("product_id", proProduct.getParentId()));
        List<ProTemplate> list = templateDao.selectList(new QueryWrapper<ProTemplate>().eq("feight_template_id", proFeightProduct.getFeightId()));
        if (list == null) {
            throw new RRException("暂无运费信息无法配送");
        }
        String[] adress = addressInfo.split("-");
        String province = adress[0];
        for (ProTemplate template : list) {
            String[] key = template.getProvince().split(",");
            String she = "";
            for (int i = 0; i < key.length; i++) {
                if (province.equals(key[i])) {
                    she = key[i];
                    break;
                }
            }

            if (she.equals(province) || template.getRegion().equals("true")) {//
                if (sum <= template.getFirstUnit().setScale(0, BigDecimal.ROUND_UP).intValue()) {//购买数量小于首数
                    map.put("feight", template.getFirstFee());
                    return map;
                }
                if (sum > template.getFirstUnit().intValue()) {//购买数量大于首数
                    int num = -(template.getFirstUnit().setScale(0, BigDecimal.ROUND_UP).intValue() - sum);

                    int a = num / template.getContinueUnit().setScale(0, BigDecimal.ROUND_UP).intValue();
                    int b = num % template.getContinueUnit().setScale(0, BigDecimal.ROUND_UP).intValue();

                    if (b > 0) {
                        b = 1;
                        BigDecimal feight = template.getContinueFee().multiply(new BigDecimal((a + b))).add(template.getFirstFee());
                        map.put("feightMoney", feight);
                    } else {
                        BigDecimal feight = template.getContinueFee().multiply(new BigDecimal(a)).add(template.getFirstFee());
                        map.put("feightMoney", feight);
                    }
                }
            } else {
                map.put("msg", "msg");
            }
        }
        return map;
    }

    /*Object 转换为 BigDecimal*/
    public static BigDecimal getBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
            return ret;
        } else {
            BigDecimal bigDecimal = new BigDecimal("0");
            return bigDecimal;
        }

    }

}
