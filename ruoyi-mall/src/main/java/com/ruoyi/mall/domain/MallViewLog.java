package com.ruoyi.mall.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 用户商品浏览记录对象 mall_view_log
 * 
 * @author ruoyi
 */
@Data
public class MallViewLog
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long viewId;

    /** 用户ID */
    private Long userId;

    /** 商品ID */
    private Long goodsId;

    /** 浏览时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date viewTime;

    // ============ 非数据库字段，用于展示 ============

    /** 商品名称 */
    private String goodsName;

    /** 商品封面 */
    private String goodsCover;

    /** 商品价格 */
    private java.math.BigDecimal price;
}
