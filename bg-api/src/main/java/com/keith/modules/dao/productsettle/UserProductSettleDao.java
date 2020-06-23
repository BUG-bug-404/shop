package com.keith.modules.dao.productsettle;

import com.keith.modules.entity.productsettle.UserProductSettle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 供应商，商品结算表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 18:03:34
 */
@Mapper
@Repository
public interface UserProductSettleDao extends BaseMapper<UserProductSettle> {
	
}
