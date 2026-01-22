package com.ruoyi.mall.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 订单主表对象 mall_order
 * 
 * @author ruoyi
 */
@Data
public class MallOrder
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long orderId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 总金额 */
    @Excel(name = "总金额")
    private BigDecimal totalAmount;

    /** 支付状态(0未付 1已付) */
    @Excel(name = "支付状态", readConverterExp = "0=未付,1=已付")
    private String payStatus;

    /** 订单状态(0待发 1已发 2完成) */
    @Excel(name = "订单状态", readConverterExp = "0=待发货,1=已发货,2=已完成")
    private String orderStatus;

    /** 收货人 */
    @Excel(name = "收货人")
    private String receiverName;

    /** 收货电话 */
    @Excel(name = "收货电话")
    private String receiverPhone;

    /** 收货地址 */
    @Excel(name = "收货地址")
    private String receiverAddress;

    /** 下单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下单时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 用户名（非数据库字段，用于展示） */
    private String userName;

    // ========== 下单时的额外参数 ==========
    
    /** 商品ID（下单参数） */
    private Long goodsId;
    
    /** 购买数量（下单参数） */
    private Integer quantity;
}
