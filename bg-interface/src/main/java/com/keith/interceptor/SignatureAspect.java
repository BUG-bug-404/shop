package com.keith.interceptor;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.keith.modules.dao.app.UserMemberAppDao;
import com.keith.modules.entity.app.UserMemberApp;
import com.keith.utils.BufferedHttpServletRequest;
import com.keith.utils.SignUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Aspect
@Component
public class SignatureAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

    @Autowired
    private UserMemberAppDao userMemberAppDao;


    @Around("execution(* com..controller..*.*(..)) " +
            "&& @annotation(com.keith.annotation.Sign) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping))"
    )
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        try {
            this.checkSign();
            return pjp.proceed();
        } catch (Throwable e) {
            LOGGER.error("SignatureAspect>>>>>>>>", e);
            throw e;
        }
    }

    private void checkSign() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String oldSign = request.getHeader("X-SIGN");
        if (StringUtils.isBlank(oldSign)) {
            throw new RuntimeException("签名参数不能为空");

        }


        //获取body（对应@RequestBody）
        String body = null;
        if (request instanceof BufferedHttpServletRequest) {
            body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        }

        //获取parameters（对应@RequestParam）
        Map<String, String[]> params = null;
        if (!CollectionUtils.isEmpty(request.getParameterMap())) {
            params = request.getParameterMap();
        }

        //获取path variable（对应@PathVariable）
        String[] paths = null;
        ServletWebRequest webRequest = new ServletWebRequest(request, null);
        Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (!CollectionUtils.isEmpty(uriTemplateVars)) {
            paths = uriTemplateVars.values().toArray(new String[]{});
        }
        Map<String, Object> map = JSON.parseObject(body, Map.class);
        System.out.println("body:" + JSON.toJSONString(body));
        String appId = String.valueOf(map.get("userAppId"));

        UserMemberApp user = userMemberAppDao.selectOne(new LambdaQueryWrapper<UserMemberApp>().eq(UserMemberApp::getAppId, appId));
        if (user != null) {
            String newSign = SignUtil.signMD5(user.getAppSecret(), body, params, paths);
            System.out.println(newSign);
            if (!newSign.equals(oldSign)) {
                throw new RuntimeException("签名不一致");
            }
        } else {
            throw new RuntimeException("无效AppId");
        }


    }
}