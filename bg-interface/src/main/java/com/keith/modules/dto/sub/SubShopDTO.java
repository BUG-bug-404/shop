package com.keith.modules.dto.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("添加分账用户")
public class SubShopDTO {
    @ApiModelProperty(value = "{类型  MERCHANT_ID：商户ID PERSONAL_WECHATID：个人微信号 PERSONAL_OPENID：个人openid（由父商户APPID转换得到） PERSONAL_SUB_OPENID: 个人sub_openid（由子商户APPID转换得到）}")
    private String type;
    @ApiModelProperty(value = "{类型是MERCHANT_ID时，是商户ID 类型是PERSONAL_WECHATID时，是个人微信号 类型是PERSONAL_OPENID时，是个人openid 类型是PERSONAL_SUB_OPENID时，是个人sub_openid}")
    private String account;
    @ApiModelProperty(value = "{分账接收方类型是MERCHANT_ID时，是商户全称（必传） 分账接收方类型是PERSONAL_NAME 时，是个人姓名（必传） 分账接收方类型是PERSONAL_OPENID时，是个人姓名（选传，传则校验） 分账接收方类型是PERSONAL_SUB_OPENID时，是个人姓名（选传，传则校验）}")
    private String name;
    @ApiModelProperty(value = "{子商户与接收方的关系。 本字段值为枚举：\n" +
            "                         SERVICE_PROVIDER：服务商\n" +
            "                         STORE：门店\n" +
            "                          STAFF：员工\n" +
            "                          STORE_OWNER：店主\n" +
            "                          PARTNER：合作伙伴\n" +
            "                         HEADQUARTER：总部\n" +
            "                          BRAND：品牌方\n" +
            "                          DISTRIBUTOR：分销商\n" +
            "                         USER：用户\n" +
            "                          SUPPLIER：供应商\n" +
            "                         CUSTOM：自定义}")
    private String relationType;
    @ApiModelProperty(value = "{子商户与接收方具体的关系，本字段最多10个字。\n" +
            "                          当字段relation_type的值为CUSTOM时，本字段必填\n" +
            "                         当字段relation_type的值不为CUSTOM时，本字段无需}")
    private String customRelation;
    /**
     * 商家id
     */
    @ApiModelProperty(value ="商家id")
    private Integer shopId;
}
