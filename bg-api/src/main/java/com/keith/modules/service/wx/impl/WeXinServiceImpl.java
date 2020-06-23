package com.keith.modules.service.wx.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keith.common.exception.RRException;
import com.keith.modules.dao.user.UserMemberDao;
import com.keith.modules.entity.token.TokenEntity;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.token.TokenService;
import com.keith.modules.service.user.UserLoginLogService;
import com.keith.modules.service.wx.OpenIdJson;
import com.keith.modules.service.wx.WeXinService;
import com.keith.modules.service.wx.WxUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("weXinService")
public class WeXinServiceImpl  implements WeXinService {
    Map<String, Object> map=new HashMap<>();

    @Value("${wxxcx.appID}")
    private String appID;

    @Value("${wxxcx.appSecret}")
    private String appSecret;

    @Autowired
    private UserMemberDao userMemberDao;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserLoginLogService userLoginLogService;

    @Override
    public Map<String, Object> wxLogin(String code) throws IOException {
        String result = "";
        try{//请求微信服务器，用code换取openid。HttpUtil是工具类，后面会给出实现，Configure类是小程序配置信息，后面会给出代码
            result = HttpUtil.doGet(
                    "https://api.weixin.qq.com/sns/jscode2session?appid="
                            + this.appID + "&secret="
                            + this.appSecret + "&js_code="
                            + code
                            + "&grant_type=authorization_code", null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSON.parseObject(result);
        ObjectMapper mapper = new ObjectMapper();
        OpenIdJson openIdJson = mapper.readValue(result, OpenIdJson.class);
        map.put("openId",openIdJson.getOpenid());
        //查询邀请人
//        UserMember inviter = userMemberDao.selectOne(new QueryWrapper<UserMember>().eq("invite_code",inviteCode));

        UserMember userEntity  = userMemberDao.selectOne(new QueryWrapper<UserMember>().eq("open_id", openIdJson.getOpenid()));
        if ( userEntity==null) {
            UserMember user=new UserMember();
            user.setOpenId(openIdJson.getOpenid());
            user.setWxSessionKey(openIdJson.getSession_key());
            user.setCreateDate(new Date());
            user.setType(1);
//            if (inviter != null) {
//                user.setParentId(inviter.getId());
//            }
//            user.setPic();//头像
            int a=1;
            long  b= (long) a;
            user.setLevelId(b);
            userMemberDao.insert(user);
            TokenEntity tokenEntity=tokenService.createToken(Long.valueOf(user.getId()));
            userLoginLogService.save(Long.valueOf(user.getId()));
            map.put("userType",user.getType());
            map.put("token",tokenEntity.getToken());
        }if(userEntity!=null){
            TokenEntity tokenEntity=tokenService.createToken(Long.valueOf(userEntity.getId()));
            userLoginLogService.save(Long.valueOf(userEntity.getId()));
            //更新微信秘钥
            UserMember userMember = new UserMember();
            userMember.setId(tokenEntity.getUserId());
            userMember.setWxSessionKey(openIdJson.getSession_key());
            userEntity.setWxSessionKey(openIdJson.getSession_key());
            userMemberDao.updateById(userEntity);
            map.put("userType",userEntity.getType());
            map.put("token",tokenEntity.getToken());
        }

        System.out.println(result.toString());
        System.out.println(openIdJson.getOpenid());
        System.out.println(openIdJson.getSession_key());

        return map;
    }

    @Override
    public boolean saveUserInfo(WxUserInfo wxUserInfo,Long usermemberId) {
        if(wxUserInfo == null ){
            throw new RRException("有误！");
        }
        UserMember userMember = userMemberDao.selectById(usermemberId);
        if(userMember != null){
            userMember.setPic(wxUserInfo.getPic());
            userMember.setUsername(wxUserInfo.getUsername());
            userMember.setMobile(wxUserInfo.getPhone());
            userMember.setGender(wxUserInfo.getGender());
            userMember.setCity(wxUserInfo.getAddress());
            userMemberDao.updateById(userMember);
            return true;
        }
        return false;
    }

}
