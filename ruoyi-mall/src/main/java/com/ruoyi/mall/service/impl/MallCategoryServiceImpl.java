package com.ruoyi.mall.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mall.mapper.MallCategoryMapper;
import com.ruoyi.mall.domain.MallCategory;
import com.ruoyi.mall.service.IMallCategoryService;

/**
 * 商品分类Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class MallCategoryServiceImpl implements IMallCategoryService
{
    @Autowired
    private MallCategoryMapper mallCategoryMapper;

    /**
     * 查询商品分类
     * 
     * @param categoryId 分类ID
     * @return 商品分类
     */
    @Override
    public MallCategory selectMallCategoryById(Long categoryId)
    {
        return mallCategoryMapper.selectMallCategoryById(categoryId);
    }

    /**
     * 查询商品分类列表
     * 
     * @param mallCategory 商品分类
     * @return 商品分类
     */
    @Override
    public List<MallCategory> selectMallCategoryList(MallCategory mallCategory)
    {
        return mallCategoryMapper.selectMallCategoryList(mallCategory);
    }

    /**
     * 查询所有分类
     * 
     * @return 商品分类集合
     */
    @Override
    public List<MallCategory> selectMallCategoryAll()
    {
        return mallCategoryMapper.selectMallCategoryAll();
    }

    /**
     * 构建前端所需要的树结构
     * 
     * @param categories 分类列表
     * @return 树结构列表
     */
    @Override
    public List<MallCategory> buildCategoryTree(List<MallCategory> categories)
    {
        List<MallCategory> returnList = new ArrayList<>();
        List<Long> tempList = categories.stream().map(MallCategory::getCategoryId).collect(Collectors.toList());
        for (MallCategory category : categories)
        {
            // 如果是顶级节点，遍历该父节点的所有子节点
            if (!tempList.contains(category.getParentId()))
            {
                recursionFn(categories, category);
                returnList.add(category);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = categories;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<MallCategory> list, MallCategory t)
    {
        // 得到子节点列表
        List<MallCategory> childList = getChildList(list, t);
        t.setChildren(childList);
        for (MallCategory tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<MallCategory> getChildList(List<MallCategory> list, MallCategory t)
    {
        List<MallCategory> tlist = new ArrayList<>();
        Iterator<MallCategory> it = list.iterator();
        while (it.hasNext())
        {
            MallCategory n = it.next();
            if (n.getParentId() != null && n.getParentId().longValue() == t.getCategoryId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<MallCategory> list, MallCategory t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 新增商品分类
     * 
     * @param mallCategory 商品分类
     * @return 结果
     */
    @Override
    public int insertMallCategory(MallCategory mallCategory)
    {
        mallCategory.setCreateTime(DateUtils.getNowDate());
        return mallCategoryMapper.insertMallCategory(mallCategory);
    }

    /**
     * 修改商品分类
     * 
     * @param mallCategory 商品分类
     * @return 结果
     */
    @Override
    public int updateMallCategory(MallCategory mallCategory)
    {
        mallCategory.setUpdateTime(DateUtils.getNowDate());
        return mallCategoryMapper.updateMallCategory(mallCategory);
    }

    /**
     * 批量删除商品分类
     * 
     * @param categoryIds 需要删除的分类ID
     * @return 结果
     */
    @Override
    public int deleteMallCategoryByIds(Long[] categoryIds)
    {
        return mallCategoryMapper.deleteMallCategoryByIds(categoryIds);
    }

    /**
     * 删除商品分类
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    @Override
    public int deleteMallCategoryById(Long categoryId)
    {
        return mallCategoryMapper.deleteMallCategoryById(categoryId);
    }

    /**
     * 查询是否存在子分类
     * 
     * @param categoryId 分类ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean hasChildByParentId(Long categoryId)
    {
        int result = mallCategoryMapper.hasChildByParentId(categoryId);
        return result > 0;
    }
}
