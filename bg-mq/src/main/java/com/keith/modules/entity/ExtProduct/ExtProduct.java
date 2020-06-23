package com.keith.modules.entity.ExtProduct;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 铺货商品表
 *
 * @author lzy
 * @email ********
 * @date 2020-06-05 14:52:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("ext_product")
public class ExtProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 铺货过来，属性还是原来商品的属性，
     */
    @TableId
    @ApiModelProperty(value = "铺货过来，属性还是原来商品的属性，")
    private Long id;
    /**
     * 店家id
     */
    @ApiModelProperty(value = "店家id")
    private Long userMemberId;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;
    /**
     * 删除状态：0->未删除；1->已删除【供应商或者平台将商品下架啊删除状态为1】铺货要下架
     */
    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除【供应商或者平台将商品下架啊删除状态为1】铺货要下架")
    private Integer deleteStatus;
    /**
     * 铺货上架状态：0->下架；1->上架
     */
    @ApiModelProperty(value = "铺货上架状态：0->下架；1->上架")
    private Integer publishStatus;
    /**
     * 铺货后的销量
     */
    @ApiModelProperty(value = "铺货后的销量")
    private Integer sale;
    /**
     * 是否在云仓该商品是否从云仓铺过来的
     */
    @ApiModelProperty(value = "是否在云仓该商品是否从云仓铺过来的")
    private Integer cloudStatus;
    /**
     * 云仓id
     */
    @ApiModelProperty(value = "云仓id")
    private Integer cloudId;
    /**
     * 图文详情
     */
    @ApiModelProperty(value = "图文详情")
    private String description;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;
    /**
     * 移动端网页详情
     */
    @ApiModelProperty(value = "移动端网页详情")
    private String detailMobileHtml;
    /**
     * 铺货的图片们，逗号隔开
     */
    @ApiModelProperty(value = "铺货的图片们，逗号隔开")
    private String pic;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT )
    private LocalDateTime createTime;


    @ApiModelProperty(value = "从云仓购买锁定的一个库存")
    private Integer cloudStock;

    @ApiModelProperty(value = "铺货类型 1:现售 2:云仓 3:预售")
    private Integer extType;
    @ApiModelProperty(value = "商品名称")
    private String productName;

}
