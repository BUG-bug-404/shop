package com.keith.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

/**
 * Created with Idea
 * author:SimlerGray
 * author:花花
 * Date:2019/11/22
 * Time:9:23
 *
 * @Description:生成appid，appkey函数
 */
public class Guid {

    public String app_key;

    /**
     * @return
     * @description:随机获取key值
     */
    public String guid() {
        UUID uuid = UUID.randomUUID();
        String key = uuid.toString();
        return key;
    }

    /**
     * 这是其中一个url的参数，是GUID的，全球唯一标志符
     *
     * @param product
     * @return
     */
    public String App_key() {
        Guid q = new Guid();
        String guid = q.guid();
        app_key = guid;
        return app_key;
    }

    /**
     * 根据md5加密
     *
     * @param
     * @return
     */
    public String App_screct() {
        String mw = "key" + app_key;
        String app_sign = DigestUtils.md5Hex(mw).toUpperCase();// 得到以后还要用MD5加密。
        return app_sign;
    }

    /**
     * 生成token
     **/
    public static String createToken() {
//        String str  = "";
//        try {
            String s = UUID.randomUUID().toString().replace("-","");
            return s;
            // 先进行MD5加密
//            MessageDigest md = MessageDigest.getInstance("md5");
//            // 对数据进行加密
//            byte[] bs = md.digest(s.getBytes());
//
//            /*
//             * BASE64Encoder所在包的引入方式（Eclipse）： 在Java Build Path下的
//             * Libraries中拉开JRE，然后点击 第一个选项Access rules（双击），然后点击add，在上面的框中选择
//             * Accessible，下面输入**，保存即可引入相应的包。
//             *
//             * BASE64Encoder底层实现原理：三字节变四字节
//             */
//            // 采用数据指纹进一步加密，拿到数据指纹
//            BASE64Encoder base = new BASE64Encoder();
//            // 进一步加密
//           str = base.encode(bs);
//        }
//        catch (NoSuchAlgorithmException e){
//            e.printStackTrace();
//        }
//        return  str;

    }

}
