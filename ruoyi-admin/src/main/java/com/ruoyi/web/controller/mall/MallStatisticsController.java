package com.ruoyi.web.controller.mall;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.mall.service.IMallStatisticsService;

/**
 * 商城统计Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/mall/statistics")
public class MallStatisticsController extends BaseController
{
    @Autowired
    private IMallStatisticsService mallStatisticsService;

    /**
     * 获取商城概览统计数据
     * 管理员首页统计，登录即可访问
     */
    @GetMapping("/overview")
    public AjaxResult getOverview()
    {
        Map<String, Object> data = mallStatisticsService.getOverviewStatistics();
        return success(data);
    }
}
