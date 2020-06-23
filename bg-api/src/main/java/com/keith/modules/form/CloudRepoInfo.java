package com.keith.modules.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Lzy
 * @date 2020/6/9 15:16
 */
@Data
public class CloudRepoInfo {

    @ApiModelProperty(value = "回购区间1开始时间")
    private Date range1StartTime;
    /**
     * 回购区间2开始时间
     */
    @ApiModelProperty(value = "回购区间2开始时间")
    private Date range2StartTime;
    /**
     * 回购区间1结束时间
     */
    @ApiModelProperty(value = "回购区间1结束时间")
    private Date range1EndTime;
    /**
     * 回购区间2结束时间
     */
    @ApiModelProperty(value = "回购区间2结束时间")
    private Date range2EndTime;
    /**
     * 回购区间1的回购折扣
     */
    @ApiModelProperty(value = "回购区间1的回购折扣")
    private BigDecimal range1Discount;
    /**
     * 回购区间2的回购折扣
     */
    @ApiModelProperty(value = "回购区间2的回购折扣")
    private BigDecimal range2Discount;
}
