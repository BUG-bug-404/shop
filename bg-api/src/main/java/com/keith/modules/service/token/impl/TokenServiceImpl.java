

package com.keith.modules.service.token.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.utils.DateUtils;
import com.keith.modules.dao.token.TokenDao;
import com.keith.modules.entity.token.TokenEntity;
import com.keith.modules.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service("tokenService")
public class TokenServiceImpl extends ServiceImpl<TokenDao, TokenEntity> implements TokenService {
	/**
	 * 3600 = 一个小时
	 * 4380个小时后过期
	 */
//	private final static int EXPIRE = 3600 * 4380;
	private final static int EXPIRE = 6;

	//@Autowired
	//private JedisUtil jedisUtil;

	@Override
	public TokenEntity queryByToken(String token) {
		return this.getOne(new QueryWrapper<TokenEntity>().eq("token", token));
	}

	@Override
	public TokenEntity createToken(long userId) {
		//当前时间
		Date now = new Date();
		//过期时间
//		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
		Date expireTime= DateUtils.addDateMonths(now,EXPIRE);
		//生成token
		String token = generateToken();
		//jedisUtil.set(token.toString(), token.toString(), Constant.Redis.EXPIRE_TIME_MINUTE);

		//保存或更新用户token
		TokenEntity tokenEntity = new TokenEntity();
		tokenEntity.setUserId(userId);
		tokenEntity.setToken(token);
		tokenEntity.setUpdateTime(now);
		tokenEntity.setExpireTime(expireTime);
		this.saveOrUpdate(tokenEntity);

		return tokenEntity;
	}

	@Override
	public void expireToken(long userId){
		Date now = new Date();

		TokenEntity tokenEntity = new TokenEntity();
		tokenEntity.setUserId(userId);
		tokenEntity.setUpdateTime(now);
		tokenEntity.setExpireTime(now);
		this.saveOrUpdate(tokenEntity);
	}

	private String generateToken(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
