package com.core.util;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.core.utils.HttpUtils;
import com.google.common.collect.Maps;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public class AliyunUtils {

    /**
     *
     * @param cardNo 银行卡号
     * @param idNo 身份证号码
     * @param name 姓名
     * @param phoneNo 手机号码
     * @return
     */
    public static String bankAuthenticate4(String cardNo,String idNo,String name,String phoneNo){

        String host = "https://yunyidata.market.alicloudapi.com";
        String path = "/bankAuthenticate4";
        String method = "POST";
        String appcode = "5a292b33187447939faa12aeb5bb7516";
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = Maps.newHashMap();
        Map<String, String> bodys = Maps.newHashMap();
        bodys.put("ReturnBankInfo", "YES");
        bodys.put("cardNo", cardNo);
        bodys.put("idNo",idNo );
        bodys.put("name", name);
        bodys.put("phoneNo", phoneNo);
        /**
         * 0000	结果匹配	信息准确，四个元素信息一致
         * 0001	开户名不能为空	开户名不能为空
         * 0002	银行卡号格式错误	银行卡号格式错误
         * 0003	身份证号格式错误	身份证号格式错误
         * 0004	手机号不能为空	手机号不能为空
         * 0005	手机号格式错误	手机号格式错误
         * 0006	银行卡号不能为空	银行卡号不能为空
         * 0007	身份证号不能为空	身份证号不能为空
         * 0008	信息不匹配	    信息不匹配
         */
        String jsonStr="";
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            jsonStr= EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }




    public static void main(String[] args) {
        String jsonStr=   AliyunUtils.bankAuthenticate4("6215591309001001898","342921198803084410","檀龙群","15156577957");
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        jsonObject.get("respCode");
        jsonObject.get("respMessage");
        System.out.println(jsonObject.toString());
        System.out.println(jsonObject.get("respCode"));
        System.out.println(jsonObject.get("bankName"));
        System.out.println(jsonObject.get("respMessage"));
    }

}
