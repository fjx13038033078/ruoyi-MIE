package com.ruoyi.web.controller.mall;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.mall.domain.MallGoods;
import com.ruoyi.mall.service.IMallRecommendService;

/**
 * 商品推荐Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/mall/recommend")
public class MallRecommendController extends BaseController
{
    @Autowired
    private IMallRecommendService mallRecommendService;

    /**
     * 获取推荐商品列表（猜你喜欢）
     * 基于UserCF协同过滤算法
     */
    @GetMapping("/list")
    public AjaxResult recommendList(@RequestParam(value = "count", defaultValue = "10") Integer count)
    {
        Long userId = SecurityUtils.getUserId();
        List<MallGoods> list = mallRecommendService.recommendGoods(userId, count);
        return success(list);
    }

    /**
     * 获取热门商品列表
     * 用于未登录用户或冷启动场景
     */
    @GetMapping("/hot")
    public AjaxResult hotList(@RequestParam(value = "count", defaultValue = "10") Integer count)
    {
        List<MallGoods> list = mallRecommendService.getHotGoods(count);
        return success(list);
    }
}
