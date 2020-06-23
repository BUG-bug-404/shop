package com.keith.modules.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.SecurityConsts;
import com.keith.config.shiro.security.JwtProperties;
import com.keith.config.shiro.security.JwtToken;
import com.keith.config.shiro.security.JwtUtil;
import com.keith.modules.dao.user.UserMemberDao;
import com.keith.modules.dto.SignInDTO;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.user.UserMemberService;
import com.keith.utils.JedisUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gray
 * @version 1.0
 * @date 2020/6/12 16:23
 */
@Service
public class UserMemberServiceImpl extends ServiceImpl<UserMemberDao, UserMember> implements UserMemberService {


    @Autowired
    private UserMemberDao userMemberDao;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    JedisUtils jedisUtils;
    @Autowired
    SecurityManager securityManager;


    @Override
    public UserMember userMemberLogin(SignInDTO user) {
        UserMember userMember = userMemberDao.selectOne(new LambdaQueryWrapper<UserMember>().eq(UserMember::getMobile,user.getMobile()));
        if (userMember!=null) {
            userMember.setToken(loginSuccess(userMember.getMobile()));
            Subject subject = SecurityUtils.getSubject();
            AuthenticationToken token= new JwtToken(userMember.getToken());
            subject.login(token);
        }
        return userMember;
    }


    private String loginSuccess(String account){
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());

        //生成token
        JSONObject json = new JSONObject();
        String token = JwtUtil.sign(account, currentTimeMillis);
        json.put("token", token);

        //更新RefreshToken缓存的时间戳
        String refreshTokenKey= SecurityConsts.PREFIX_SHIRO_REFRESH_TOKEN + account;
        jedisUtils.saveString(refreshTokenKey, currentTimeMillis, jwtProperties.getTokenExpireTime()*60);

        return token;
    }
}
