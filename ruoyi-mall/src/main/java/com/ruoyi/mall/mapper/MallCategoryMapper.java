package com.ruoyi.mall.mapper;

import java.util.List;
import com.ruoyi.mall.domain.MallCategory;

/**
 * 商品分类Mapper接口
 * 
 * @author ruoyi
 */
public interface MallCategoryMapper
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
     * 查询所有商品分类
     * 
     * @return 商品分类集合
     */
    public List<MallCategory> selectMallCategoryAll();

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
     * 删除商品分类
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    public int deleteMallCategoryById(Long categoryId);

    /**
     * 批量删除商品分类
     * 
     * @param categoryIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteMallCategoryByIds(Long[] categoryIds);

    /**
     * 查询是否存在子分类
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    public int hasChildByParentId(Long categoryId);
}
