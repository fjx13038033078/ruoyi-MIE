package com.ruoyi.web.controller.mall;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.mall.domain.MallCategory;
import com.ruoyi.mall.service.IMallCategoryService;

/**
 * 商品分类Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/mall/category")
public class MallCategoryController extends BaseController
{
    @Autowired
    private IMallCategoryService mallCategoryService;

    /**
     * 查询商品分类列表
     */
    @GetMapping("/list")
    public AjaxResult list(MallCategory mallCategory)
    {
        List<MallCategory> list = mallCategoryService.selectMallCategoryList(mallCategory);
        return success(list);
    }

    /**
     * 查询商品分类列表（树形结构）
     */
    @GetMapping("/treeList")
    public AjaxResult treeList(MallCategory mallCategory)
    {
        List<MallCategory> list = mallCategoryService.selectMallCategoryList(mallCategory);
        return success(mallCategoryService.buildCategoryTree(list));
    }

    /**
     * 获取所有分类（用于下拉选择框）
     */
    @GetMapping("/selectAll")
    public AjaxResult selectAll()
    {
        List<MallCategory> list = mallCategoryService.selectMallCategoryAll();
        return success(list);
    }

    /**
     * 导出商品分类列表
     */
    @Log(title = "商品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MallCategory mallCategory)
    {
        List<MallCategory> list = mallCategoryService.selectMallCategoryList(mallCategory);
        ExcelUtil<MallCategory> util = new ExcelUtil<MallCategory>(MallCategory.class);
        util.exportExcel(response, list, "商品分类数据");
    }

    /**
     * 获取商品分类详细信息
     */
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") Long categoryId)
    {
        return success(mallCategoryService.selectMallCategoryById(categoryId));
    }

    /**
     * 新增商品分类
     */
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MallCategory mallCategory)
    {
        mallCategory.setCreateBy(getUsername());
        return toAjax(mallCategoryService.insertMallCategory(mallCategory));
    }

    /**
     * 修改商品分类
     */
    @Log(title = "商品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MallCategory mallCategory)
    {
        mallCategory.setUpdateBy(getUsername());
        return toAjax(mallCategoryService.updateMallCategory(mallCategory));
    }

    /**
     * 删除商品分类
     */
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryId}")
    public AjaxResult remove(@PathVariable Long categoryId)
    {
        if (mallCategoryService.hasChildByParentId(categoryId))
        {
            return error("存在下级分类，不允许删除");
        }
        return toAjax(mallCategoryService.deleteMallCategoryById(categoryId));
    }
}
