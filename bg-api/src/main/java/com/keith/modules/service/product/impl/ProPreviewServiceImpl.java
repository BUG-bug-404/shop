package com.keith.modules.service.product.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.modules.dao.product.ProPreviewDao;
import org.springframework.stereotype.Service;


import com.keith.modules.entity.product.ProPreview;
import com.keith.modules.service.product.ProPreviewService;


@Service("proPreviewService")
public class ProPreviewServiceImpl extends ServiceImpl<ProPreviewDao, ProPreview> implements ProPreviewService {



}
