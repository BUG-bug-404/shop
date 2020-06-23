package com.keith.modules.service.withdraw.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;

import com.keith.modules.dao.withdraw.SetWithdrawDao;
import com.keith.modules.entity.withdraw.SetWithdraw;
import com.keith.modules.service.withdraw.SetWithdrawService;


@Service("setWithdrawService")
public class SetWithdrawServiceImpl extends ServiceImpl<SetWithdrawDao, SetWithdraw> implements SetWithdrawService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SetWithdraw> page = this.page(
                new Query<SetWithdraw>().getPage(params),
                new QueryWrapper<SetWithdraw>()
        );

        return new PageUtils(page);
    }

}
