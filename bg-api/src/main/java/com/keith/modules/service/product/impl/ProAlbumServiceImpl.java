package com.keith.modules.service.product.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.product.ProAlbumDao;
import com.keith.modules.entity.product.ProAlbum;
import com.keith.modules.service.product.ProAlbumService;


@Service("proAlbumService")
public class ProAlbumServiceImpl extends ServiceImpl<ProAlbumDao, ProAlbum> implements ProAlbumService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProAlbum> page = this.page(
                new Query<ProAlbum>().getPage(params),
                new QueryWrapper<ProAlbum>()
        );

        return new PageUtils(page);
    }

}
