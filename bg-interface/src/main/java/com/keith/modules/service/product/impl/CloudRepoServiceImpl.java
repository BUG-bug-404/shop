package com.keith.modules.service.product.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.modules.dao.product.CloudRepoDao;
import com.keith.modules.entity.product.CloudRepo;
import com.keith.modules.service.product.CloudRepoService;
import org.springframework.stereotype.Service;


@Service("cloudRepoService")
public class CloudRepoServiceImpl extends ServiceImpl<CloudRepoDao, CloudRepo> implements CloudRepoService {


}
