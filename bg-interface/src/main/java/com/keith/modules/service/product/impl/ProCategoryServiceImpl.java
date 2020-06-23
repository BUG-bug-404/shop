package com.keith.modules.service.product.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.modules.dao.product.ProCategoryDao;
import com.keith.modules.entity.product.ProCategory;
import com.keith.modules.service.product.ProCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProCategoryServiceImpl extends ServiceImpl<ProCategoryDao, ProCategory> implements ProCategoryService {

    @Autowired
    private ProCategoryDao proCategoryDao;


    @Override
    public List<ProCategory> findAll() {
        /*一级分类*/
        List<ProCategory> level0 = proCategoryDao.selectList(new QueryWrapper<ProCategory>().eq("level", 0));
        if(level0 == null && level0.size() <= 0){
            return null;
        }
        List<ProCategory> level1 = null;
        for(ProCategory proCategory : level0){
            level1 = proCategoryDao.selectList(new QueryWrapper<ProCategory>().eq("parent_id", proCategory.getId()));
            proCategory.setProCategories(level1);
        }
        return level0;
    }
}
