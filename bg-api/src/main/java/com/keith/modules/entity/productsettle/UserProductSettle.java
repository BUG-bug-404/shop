package com.keith.modules.entity.productsettle;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 供应商，商品结算表
 *
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-15 18:03:34
 */
@Data
@TableName("user_product_settle")
public class UserProductSettle implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ApiModelProperty(value = "{}")
    @TableId
    private Long id;
    /**
     * 供应商id
     */
    @ApiModelProperty(value = "{供应商id}")
    private Long userAdminId;
    /**
     *
     */
    @ApiModelProperty(value = "{}")
    private Long productId;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "{商品名称}")
    private String productName;
    /**
     * 商品图片
     */
    @ApiModelProperty(value = "{商品图片}")
    private String productImg;
    /**
     * 商品规格
     */
    @ApiModelProperty(value = "{商品规格}")
    private String productSpce;
    /**
     * 规格类型，1单规格，2：多规格
     */
    @ApiModelProperty(value = "{规格类型，1单规格，2：多规格}")
    private Integer productSpceStatus;
    /**
     * 结算天数
     */
    @ApiModelProperty(value = "{结算天数}")
    private Integer setDate;
    /**
     * 总收入
     */
    @ApiModelProperty(value = "{总收入}")
    private BigDecimal account;
    /**
     * 结算状态，1：已结算，2未结算
     */
    @ApiModelProperty(value = "{结算状态，1：已结算，2未结算}")
    private Integer setStatus;
    /**
     * 结算时间
     */
    @ApiModelProperty(value = "{结算时间}")
    private Date setTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "{创建时间}")
    private Date createTime;
    /**
     * 流水
     */
    @ApiModelProperty(value = "{流水}")
    private String uuid;

}
