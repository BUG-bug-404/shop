package com.keith.modules.service.user;


import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.user.UserCollectItem;

import java.util.List;
import java.util.Map;

/**
 * 收藏表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-03 16:41:56
 */
public interface UserCollectItemService extends IService<UserCollectItem> {

    /**
     * 添加到收藏夹
     * */
    Map<String,Object> setColletion(Long productId, Long userMemberId);

    /**
     * 查看是否在收藏夹*/
    Boolean status(Long productId, Long userMemberId);

    /**
    * 查询收藏夹列表
     * */

    PageUtils findAll(Integer currentPage, Integer pageSize,Long userMemberId);

}

