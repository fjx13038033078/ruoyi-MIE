package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallCategory;

/**
 * 商品分类Service接口
 * 
 * @author ruoyi
 */
public interface IMallCategoryService
{
    /**
     * 查询商品分类
     * 
     * @param categoryId 分类ID
     * @return 商品分类
     */
    public MallCategory selectMallCategoryById(Long categoryId);

    /**
     * 查询商品分类列表
     * 
     * @param mallCategory 商品分类
     * @return 商品分类集合
     */
    public List<MallCategory> selectMallCategoryList(MallCategory mallCategory);

    /**
     * 查询所有分类（用于下拉选择）
     * 
     * @return 商品分类集合
     */
    public List<MallCategory> selectMallCategoryAll();

    /**
     * 构建前端所需要的树结构
     * 
     * @param categories 分类列表
     * @return 树结构列表
     */
    public List<MallCategory> buildCategoryTree(List<MallCategory> categories);

    /**
     * 新增商品分类
     * 
     * @param mallCategory 商品分类
     * @return 结果
     */
    public int insertMallCategory(MallCategory mallCategory);

    /**
     * 修改商品分类
     * 
     * @param mallCategory 商品分类
     * @return 结果
     */
    public int updateMallCategory(MallCategory mallCategory);

    /**
     * 批量删除商品分类
     * 
     * @param categoryIds 需要删除的分类ID
     * @return 结果
     */
    public int deleteMallCategoryByIds(Long[] categoryIds);

    /**
     * 删除商品分类
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    public int deleteMallCategoryById(Long categoryId);

    /**
     * 查询是否存在子分类
     * 
     * @param categoryId 分类ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean hasChildByParentId(Long categoryId);
}
