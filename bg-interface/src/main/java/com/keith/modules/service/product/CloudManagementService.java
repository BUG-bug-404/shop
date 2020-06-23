package com.keith.modules.service.product;


import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.modules.dto.cloud.CloudRepoCount;
import com.keith.modules.dto.cloud.CloudUserRepoInfo;
import com.keith.modules.entity.product.CloudManagement;
import com.keith.modules.form.BuyCloudProSelect;
import com.keith.modules.form.CloudForResult;
import com.keith.modules.form.CloudInfoResult;
import com.keith.modules.form.GetGoodsSelect;

import java.util.List;
import java.util.Map;


/**
 * 云仓SKU数据表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-04 19:25:40
 */
public interface CloudManagementService extends IService<CloudManagement> {

    /**查询云仓里的商品*/
    Map<String, Object> findAll(Integer currentPage, Integer pageSize);

    /**根据穿过来的id查云仓这个商品的规格们*/
    CloudInfoResult getByProduct(Long productId);

    /**查询这个用户下单的云仓东东*/
    Map<String, Object> getUserAll(Long userMemberId,Integer currentPage, Integer pageSize);

    /**查询云仓所有活动的商品以及规格*/
    Map<String, Object> getAll(Integer currentPage, Integer pageSize);

    /**进入到代发货页面*/
    CloudForResult getInfo(Long userMemberId, Long productId, Long skuId);


//    void buyCloudPro(Long userMemberId);
//    void buyCloud(Long userMemberId,List<BuyCloudProSelect> products);

    /**代下单----提货----付邮费*/
    Map<String, Object> getGoods(GetGoodsSelect getGoodsSelect, Long userMemberId);
    /**计算该商品该规格回收价格---*/
    Map<String, Object> countRepoPrice(CloudRepoCount cloudRepoCount, Long userMemberId);
    /**生成订单*/
    Map<String, Object> createCloudOrder(BuyCloudProSelect buyCloudProSelect,Long userMemberId);

    /*回收*/
    Map<String, Object> createRepoOrder(List<CloudUserRepoInfo> cloudUserRepoInfos, Long userMemberId);

    /*查看回收详情*/
    List<CloudUserRepoInfo> findRepoInfo(Long userMemberId,Long repoSn);




}

