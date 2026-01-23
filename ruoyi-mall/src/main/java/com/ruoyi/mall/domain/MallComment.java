package com.ruoyi.mall.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品评价对象 mall_comment
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MallComment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private Long commentId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 商品ID */
    @Excel(name = "商品ID")
    private Long goodsId;

    /** 评分(1-5) */
    @Excel(name = "评分")
    private Integer star;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 评论时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "评论时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // ============ 非数据库字段，用于展示 ============

    /** 用户昵称 */
    private String userName;

    /** 用户头像 */
    private String avatar;

    /** 商品名称 */
    private String goodsName;

    /** 商品封面图 */
    private String goodsCover;
}
