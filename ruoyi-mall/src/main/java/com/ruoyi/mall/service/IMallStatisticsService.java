package com.ruoyi.mall.service;

import java.util.Map;

/**
 * 商城统计Service接口
 * 
 * @author ruoyi
 */
public interface IMallStatisticsService
{
    /**
     * 获取商城概览统计数据
     * 
     * @return 统计数据
     */
    public Map<String, Object> getOverviewStatistics();
}
