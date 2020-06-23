

package com.keith.modules.dao.token;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.token.TokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Token
 *
 * @author lijinxiang
 */
@Mapper
public interface TokenDao extends BaseMapper<TokenEntity> {
	
}
