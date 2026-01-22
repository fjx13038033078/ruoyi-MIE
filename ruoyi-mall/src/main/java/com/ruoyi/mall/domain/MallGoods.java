package com.ruoyi.mall.domain;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品信息对象 mall_goods
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MallGoods extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品ID */
    private Long goodsId;

    /** 分类ID */
    @Excel(name = "分类ID")
    private Long categoryId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String goodsName;

    /** 商品简介 */
    @Excel(name = "商品简介")
    private String goodsIntro;

    /** 商品封面图 */
    @Excel(name = "商品封面图")
    private String goodsCover;

    /** 销售价格 */
    @Excel(name = "销售价格")
    private BigDecimal price;

    /** 原价 */
    @Excel(name = "原价")
    private BigDecimal originalPrice;

    /** 库存 */
    @Excel(name = "库存")
    private Integer stock;

    /** 虚拟销量 */
    @Excel(name = "虚拟销量")
    private Integer sales;

    /** 状态（0上架 1下架） */
    @Excel(name = "状态", readConverterExp = "0=上架,1=下架")
    private String status;

    /** 适用年龄(如:0-6M, 1-3Y) */
    @Excel(name = "适用年龄")
    private String ageStage;

    /** 商品详情(富文本) */
    private String detailContent;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 分类名称（非数据库字段，用于展示） */
    private String categoryName;
}
