package com.keith.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * @author huahua
 * @create 2018-12-25 19:58
 **/
public class SignUtil {
    private static final String DEFAULT_SECRET = "1qaz@WSX#$%&";

    /**
     * hmac256签名 用于通用的签名  针对通用客户
     * @param secret
     * @param body
     * @param params
     * @param paths
     * @return
     */
    public static String sign(String secret,String body, Map<String, String[]> params, String[] paths) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(body)) {
            Map<String,Object> map = JSON.parseObject(body,Map.class);
            map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(key->{

                sb.append(key.getKey()).append("=").append(key.getValue()).append('&');

            });
            sb.deleteCharAt(sb.length()-1);
        }

        if (!CollectionUtils.isEmpty(params)) {
            params.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(paramEntry -> {
                        String paramValue = String.join(",", Arrays.stream(paramEntry.getValue()).sorted().toArray(String[]::new));
                        sb.append(paramEntry.getKey()).append("=").append(paramValue).append('#');
                    });
        }

        if (ArrayUtils.isNotEmpty(paths)) {
            String pathValues = String.join(",", Arrays.stream(paths).sorted().toArray(String[]::new));
            sb.append(pathValues);
        }
        String createSign = HmacUtils.hmacMd5Hex(secret, sb.toString());
        System.out.println("JSON:"+body);
        System.out.println("sb:"+sb);
        System.out.println("sign:"+createSign);
        return createSign;
    }

    /**
     * 使用md5签名加密，暂时只用于一家客户
     * @param secret
     * @param body
     * @param params
     * @param paths
     * @return
     */
    public static String signMD5(String secret,String body, Map<String, String[]> params, String[] paths) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(body)) {
            Map<String,Object> map = JSON.parseObject(body,Map.class);
            map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(key->{

                sb.append(key.getKey()).append("=").append(key.getValue()).append('&');

            });
            sb.deleteCharAt(sb.length()-1);
        }

        if (!CollectionUtils.isEmpty(params)) {
            params.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(paramEntry -> {
                        String paramValue = String.join(",", Arrays.stream(paramEntry.getValue()).sorted().toArray(String[]::new));
                        sb.append(paramEntry.getKey()).append("=").append(paramValue).append('#');
                    });
        }

        if (ArrayUtils.isNotEmpty(paths)) {
            String pathValues = String.join(",", Arrays.stream(paths).sorted().toArray(String[]::new));
            sb.append(pathValues);
        }
        sb.append("&secret=");
        sb.append(secret);
        String createSign = DigestUtils.md5Hex(sb.toString());
        System.out.println("JSON:"+body);
        System.out.println("sb:"+sb);
        System.out.println("sign:"+createSign);
        return createSign;
    }

}