package com.ruoyi.web.controller.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.mall.service.IMallViewLogService;

/**
 * 用户商品浏览记录Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/mall/viewlog")
public class MallViewLogController extends BaseController
{
    @Autowired
    private IMallViewLogService mallViewLogService;

    /**
     * 记录用户浏览商品（用户端调用）
     * 当用户进入商品详情页时自动调用
     */
    @PostMapping("/record/{goodsId}")
    public AjaxResult recordView(@PathVariable("goodsId") Long goodsId)
    {
        Long userId = SecurityUtils.getUserId();
        mallViewLogService.recordViewLog(userId, goodsId);
        return success();
    }
}
