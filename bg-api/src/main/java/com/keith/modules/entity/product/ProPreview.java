package com.keith.modules.entity.product;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 预售设置表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-17 15:01:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("pro_preview")
public class ProPreview implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "预售id")
    private Long id;
    /**
     *
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;
    /**
     *
     */
    @ApiModelProperty(value = "库存id-不用")
    private Long skuStockId;
    /**
     *
     */
    @ApiModelProperty(value = "供应商id")
    private Long userAdminId;
    /**
     * 0全款预售/1比例定金预售2/固定金定金预售
     */
    @ApiModelProperty(value = "0全款预售/1比例定金预售2/固定金定金预售")
    private Integer style;
    /**
     * 预售开始时间
     */
    @ApiModelProperty(value = "预售开始时间")
    private Date preSaleStarttime;
    /**
     * 预售结束时间
     */
    @ApiModelProperty(value = "预售结束时间")
    private Date preSaleStoptime;
    /**
     * 尾款支付时间
     */
    @ApiModelProperty(value = "尾款支付时间")
    private Date payStart;
    /**
     * 尾款支付结束时间
     */
    @ApiModelProperty(value = "尾款支付结束时间")
    private Date payEnd;
    /**
     * 尾款支付固定期间
     */
    @ApiModelProperty(value = "尾款支付固定期间")
    private String defaultPayTime;
    /**
     * 预售到期操作0/关闭预警1/自动下架
     */
    @ApiModelProperty(value = "预售到期操作0/关闭预警1/自动下架")
    private Integer terminate;
    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    private String deliveryTime;
    /**
     * 预售说明
     */
    @ApiModelProperty(value = "预售说明")
    private String preDeclare;
    /**
     * 添加人
     */
    @ApiModelProperty(value = "添加人")
    private String createPerson;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private Date createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String changePerson;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date changeTime;
    /**
     * 预售金额
     */
    @ApiModelProperty(value = "预售金额")
    private BigDecimal preMoney;
    /**
     * 预售比例
     */
    @ApiModelProperty(value = "预售比例")
    private Integer preRatio;

}
