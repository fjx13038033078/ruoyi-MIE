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
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.mall.domain.MallGoods;
import com.ruoyi.mall.service.IMallGoodsService;

/**
 * 商品信息Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/mall/goods")
public class MallGoodsController extends BaseController
{
    @Autowired
    private IMallGoodsService mallGoodsService;

    /**
     * 查询商品信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MallGoods mallGoods)
    {
        startPage();
        List<MallGoods> list = mallGoodsService.selectMallGoodsList(mallGoods);
        return getDataTable(list);
    }

    /**
     * 导出商品信息列表
     */
    @Log(title = "商品信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MallGoods mallGoods)
    {
        List<MallGoods> list = mallGoodsService.selectMallGoodsList(mallGoods);
        ExcelUtil<MallGoods> util = new ExcelUtil<MallGoods>(MallGoods.class);
        util.exportExcel(response, list, "商品信息数据");
    }

    /**
     * 获取商品信息详细信息
     */
    @GetMapping(value = "/{goodsId}")
    public AjaxResult getInfo(@PathVariable("goodsId") Long goodsId)
    {
        return success(mallGoodsService.selectMallGoodsById(goodsId));
    }

    /**
     * 新增商品信息
     */
    @Log(title = "商品信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MallGoods mallGoods)
    {
        mallGoods.setCreateBy(getUsername());
        return toAjax(mallGoodsService.insertMallGoods(mallGoods));
    }

    /**
     * 修改商品信息
     */
    @Log(title = "商品信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MallGoods mallGoods)
    {
        mallGoods.setUpdateBy(getUsername());
        return toAjax(mallGoodsService.updateMallGoods(mallGoods));
    }

    /**
     * 删除商品信息
     */
    @Log(title = "商品信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{goodsIds}")
    public AjaxResult remove(@PathVariable Long[] goodsIds)
    {
        return toAjax(mallGoodsService.deleteMallGoodsByIds(goodsIds));
    }
}
