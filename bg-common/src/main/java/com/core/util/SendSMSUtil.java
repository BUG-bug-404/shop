package com.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SendSMSUtil {
	
	private static final Logger logger= LoggerFactory.getLogger(SendSMSUtil.class);
	
	//发送验证码的请求路径URL
    private static final String  SERVER_URL="https://api.netease.im/sms/sendcode.action";
    //短信模板ID   语音验证码（默认）
    private static final String TEMPLATEID="9404198";
    //验证码长度，范围4～10，默认为4
    private static final String CODELEN="6";
    
    public static JSONObject sendSMSCode(String mobile){
    	 try {
			 DefaultHttpClient httpClient = new DefaultHttpClient();
			 HttpPost httpPost = new HttpPost(SERVER_URL);
			 String curTime = String.valueOf((new Date()).getTime() / 1000L);
			 String appKey = "91d3789dbcc7410a47b59a052c40d3d6";
			 String appSecret = "ac81abb5248c";
			 String nonce= UUID.randomUUID().toString();
			 String checkSum = getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

			 // 设置请求的header
			 httpPost.addHeader("AppKey", appKey);
			 httpPost.addHeader("Nonce", nonce);
			 httpPost.addHeader("CurTime", curTime);
			 httpPost.addHeader("CheckSum", checkSum);
			 httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			 // 设置请求的的参数，requestBody参数
			 List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			 /*
			  * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
			  * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
			  * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
			  */
			 nvps.add(new BasicNameValuePair("templateid", TEMPLATEID));
			 nvps.add(new BasicNameValuePair("mobile", mobile));
			 nvps.add(new BasicNameValuePair("codeLen", CODELEN));

			 httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			 // 执行请求
			 HttpResponse response = httpClient.execute(httpPost);
			 /*
			  * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
			  * 2.具体的code有问题的可以参考官网的Code状态表
			  */
			 String result= EntityUtils.toString(response.getEntity(), "utf-8");
			 // 打印执行结果
			 logger.debug("返回参数："+result);
			 if(result==null){
				 return null;
			 }
			 return JSON.parseObject(result);
		} catch (IOException e) {
			 logger.error("调用网易云发送短信接口-验证码，发生异常 msg={}","原因",e);
		}
    	return null;
    }



    // 计算并获取CheckSum
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };



}
