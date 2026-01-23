package com.ruoyi.web.controller.mall;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.mall.domain.MallComment;
import com.ruoyi.mall.service.IMallCommentService;

/**
 * 商品评价Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/mall/comment")
public class MallCommentController extends BaseController
{
    @Autowired
    private IMallCommentService mallCommentService;

    // ==================== 管理端接口 ====================

    /**
     * 查询商品评价列表（管理端）
     */
    @PreAuthorize("@ss.hasPermi('mall:comment:list')")
    @GetMapping("/list")
    public TableDataInfo list(MallComment mallComment)
    {
        startPage();
        List<MallComment> list = mallCommentService.selectMallCommentList(mallComment);
        return getDataTable(list);
    }

    /**
     * 导出商品评价列表
     */
    @PreAuthorize("@ss.hasPermi('mall:comment:export')")
    @Log(title = "商品评价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MallComment mallComment)
    {
        List<MallComment> list = mallCommentService.selectMallCommentList(mallComment);
        ExcelUtil<MallComment> util = new ExcelUtil<MallComment>(MallComment.class);
        util.exportExcel(response, list, "商品评价数据");
    }

    /**
     * 获取商品评价详细信息
     */
    @PreAuthorize("@ss.hasPermi('mall:comment:query')")
    @GetMapping(value = "/{commentId}")
    public AjaxResult getInfo(@PathVariable("commentId") Long commentId)
    {
        return success(mallCommentService.selectMallCommentById(commentId));
    }

    /**
     * 删除商品评价（管理端）
     */
    @PreAuthorize("@ss.hasPermi('mall:comment:remove')")
    @Log(title = "商品评价", businessType = BusinessType.DELETE)
    @DeleteMapping("/{commentIds}")
    public AjaxResult remove(@PathVariable Long[] commentIds)
    {
        return toAjax(mallCommentService.deleteMallCommentByIds(commentIds));
    }

    // ==================== 用户端接口 ====================

    /**
     * 获取商品的评价列表（用户端，无需权限）
     */
    @GetMapping("/goods/{goodsId}")
    public AjaxResult listByGoodsId(@PathVariable("goodsId") Long goodsId)
    {
        List<MallComment> list = mallCommentService.selectMallCommentByGoodsId(goodsId);
        return success(list);
    }

    /**
     * 获取商品的评价统计信息
     */
    @GetMapping("/stats/{goodsId}")
    public AjaxResult getStats(@PathVariable("goodsId") Long goodsId)
    {
        Double avgStar = mallCommentService.getAvgStarByGoodsId(goodsId);
        int count = mallCommentService.getCountByGoodsId(goodsId);
        return success()
                .put("avgStar", avgStar)
                .put("count", count);
    }

    /**
     * 用户添加评价
     */
    @Log(title = "添加评价", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addComment(@RequestBody MallComment mallComment)
    {
        // 设置当前登录用户ID
        mallComment.setUserId(SecurityUtils.getUserId());
        return toAjax(mallCommentService.insertMallComment(mallComment));
    }

    /**
     * 用户删除自己的评价
     */
    @Log(title = "删除评价", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/{commentId}")
    public AjaxResult deleteMyComment(@PathVariable("commentId") Long commentId)
    {
        Long userId = SecurityUtils.getUserId();
        return toAjax(mallCommentService.deleteMyComment(commentId, userId));
    }

    /**
     * 获取当前用户的评价列表
     */
    @GetMapping("/my")
    public TableDataInfo myComments()
    {
        startPage();
        Long userId = SecurityUtils.getUserId();
        List<MallComment> list = mallCommentService.selectMallCommentByUserId(userId);
        return getDataTable(list);
    }
}
