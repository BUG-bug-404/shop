

package com.keith.modules.service.token;


import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.modules.entity.token.TokenEntity;

/**
 * 用户Token
 *
 * @author JohnSon
 */
public interface TokenService extends IService<TokenEntity> {

	TokenEntity queryByToken(String token);

	/**
	 * 生成token
	 * @param userId  用户ID
	 * @return        返回token信息
	 */
	TokenEntity createToken(long userId);

	/**
	 * 设置token过期
	 * @param userId 用户ID
	 */
	void expireToken(long userId);

}
