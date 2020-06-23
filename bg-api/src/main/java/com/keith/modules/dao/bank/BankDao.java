package com.keith.modules.dao.bank;

import com.keith.modules.entity.bank.Bank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 银行卡表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 10:56:12
 */
@Mapper
public interface BankDao extends BaseMapper<Bank> {
	
}
