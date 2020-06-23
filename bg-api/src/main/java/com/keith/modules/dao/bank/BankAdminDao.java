package com.keith.modules.dao.bank;

import com.keith.modules.entity.bank.BankAdmin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 银行卡表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-18 11:25:22
 */
@Mapper
public interface BankAdminDao extends BaseMapper<BankAdmin> {
	
}
