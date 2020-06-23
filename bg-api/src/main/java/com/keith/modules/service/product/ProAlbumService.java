package com.keith.modules.service.product;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.entity.product.ProAlbum;

import java.util.Map;

/**
 * 商品图集
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-02 11:05:20
 */
public interface ProAlbumService extends IService<ProAlbum> {

    PageUtils queryPage(Map<String, Object> params);
}

