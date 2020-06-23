package com.keith.common.deliverQuery;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lzy
 * @date 2020/5/19 10:03
 */
public class DeliverQuery {
    public Object deliverQuery(String number, String type, String phone) throws Exception {
        String appcode = "a4e80403f80e40d5b3ccdbb1053e6bc1";
        String host = "https://jisukdcx.market.alicloudapi.com";
        String path = "/express/query";
//        String method = "GET";

        Map<String, Object> returnMap = new HashMap<String, Object>();
        System.out.println("物流单号" + number + "手机号" + phone + "type" + type);
        if (StringUtils.isEmpty(number)) {
            throw new Exception("请输入物流单号！");
        }
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode); //格式为:Authorization:APPCODE 83359fd73fe11248385f570e3c139xxx
        Map<String, String> querys = new HashMap<String, String>();
        System.out.println("headers" + headers);

        querys.put("mobile", phone);
        querys.put("number", number);
        if (StringUtils.isEmpty(type)) {
            querys.put("type", "auto");
        } else {
            querys.put("type", type);
        }
//        method
        String returnStr = "";
        HttpResponse response = HttpUtils.doGet(host, path, headers, querys);
        System.out.println("response" + response);

        returnStr = EntityUtils.toString(response.getEntity()); //输出json
        System.out.println("returnStr" + returnStr);
        if (StringUtils.isEmpty(returnStr)) {
            throw new Exception("查询物流失败！");
        }
        return returnStr;
/*      返回示例
        201	Express a single number is empty	快递单号为空
        202	Express company is empty	快递公司为空
        203	Courier company does not exist	快递公司不存在
        204	Courier companies identify failure	快递公司识别失败
        205	No information	没有信息
        208	Odd numbers do not have information.	单号没有信息（扣次数）
        deliverystatus:物流状态1在途中2派件中3已签收4派送失败（拒签等）
        type(快递公司)
*//*     查询成功   {result={\"status\":0,\"msg\":\"ok\",\"result\":{\"number\":\"773037010983711\",\"type\":\"sto\",\"typename\":\"申通快递\",\"logo\":\"https:\\/\\/api.jisuapi.com\\/express\\/static\\/images\\/logo\\/80\\/sto.png\",\"list\":[{\"time\":\"2020-05-18 20:32:30\",\"status\":\"快件由【天津转运中心】发往【浙江杭州转运中心】\"},{\"time\":\"2020-05-18 20:22:19\",\"status\":\"快件由【天津转运中心】发往【浙江杭州转运中心】\"},{\"time\":\"2020-05-18 20:19:04\",\"status\":\"快件已到达【天津转运中心】扫描员是【装车2号扫描员】\"},{\"time\":\"2020-05-18 20:11:23\",\"status\":\"快件已到达【天津转运中心】扫描员是【天津交叉分拣流水线】\"},{\"time\":\"2020-05-18 13:00:18\",\"status\":\"快件由【天津项目客户部】发往【天津转运中心】\"},{\"time\":\"2020-05-18 13:00:17\",\"status\":\"【天津项目客户部】的收件员【FMC】已收件\"}],\"deliverystatus\":1,\"issign\":0}}, QueryTime=1589870689070}\n";
          失败  * {result={"status":"203","msg":"快递公司不存在","result":""}, QueryTime=1589872660794}*/
    }

}
