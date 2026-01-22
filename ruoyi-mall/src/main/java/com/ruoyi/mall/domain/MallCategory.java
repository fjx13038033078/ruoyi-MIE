package com.ruoyi.mall.domain;

import java.util.ArrayList;
import java.util.List;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类对象 mall_category
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MallCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String categoryName;

    /** 父分类ID */
    @Excel(name = "父分类ID")
    private Long parentId;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 父分类名称（非数据库字段，用于展示） */
    private String parentName;

    /** 子分类列表（非数据库字段，用于树形结构） */
    private List<MallCategory> children = new ArrayList<>();
}
