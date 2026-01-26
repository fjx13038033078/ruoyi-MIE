package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallGoods;

/**
 * 商品推荐Service接口
 * 
 * @author ruoyi
 */
public interface IMallRecommendService
{
    /**
     * 基于UserCF算法为用户推荐商品
     * 
     * @param userId 用户ID
     * @return 推荐的商品列表
     */
    public List<MallGoods> recommendGoods(Long userId);

    /**
     * 基于UserCF算法为用户推荐商品（指定数量）
     * 
     * @param userId 用户ID
     * @param count 推荐数量
     * @return 推荐的商品列表
     */
    public List<MallGoods> recommendGoods(Long userId, int count);

    /**
     * 获取热门商品（用于冷启动或无推荐结果时）
     * 
     * @param count 数量
     * @return 热门商品列表
     */
    public List<MallGoods> getHotGoods(int count);
}
