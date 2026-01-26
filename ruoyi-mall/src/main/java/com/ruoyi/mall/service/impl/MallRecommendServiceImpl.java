package com.ruoyi.mall.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mall.domain.MallGoods;
import com.ruoyi.mall.mapper.MallCommentMapper;
import com.ruoyi.mall.mapper.MallGoodsMapper;
import com.ruoyi.mall.mapper.MallViewLogMapper;
import com.ruoyi.mall.recommend.UserCFRecommender;
import com.ruoyi.mall.service.IMallRecommendService;

/**
 * 商品推荐Service业务层处理
 * 基于UserCF（用户协同过滤）算法实现
 * 
 * @author ruoyi
 */
@Service
public class MallRecommendServiceImpl implements IMallRecommendService
{
    private static final Logger log = LoggerFactory.getLogger(MallRecommendServiceImpl.class);

    @Autowired
    private MallViewLogMapper mallViewLogMapper;

    @Autowired
    private MallCommentMapper mallCommentMapper;

    @Autowired
    private MallGoodsMapper mallGoodsMapper;

    /**
     * 基于UserCF算法为用户推荐商品
     * 
     * @param userId 用户ID
     * @return 推荐的商品列表
     */
    @Override
    public List<MallGoods> recommendGoods(Long userId)
    {
        return recommendGoods(userId, UserCFRecommender.getDefaultRecommendCount());
    }

    /**
     * 基于UserCF算法为用户推荐商品（指定数量）
     * 
     * @param userId 用户ID
     * @param count 推荐数量
     * @return 推荐的商品列表
     */
    @Override
    public List<MallGoods> recommendGoods(Long userId, int count)
    {
        try
        {
            // 1. 获取所有浏览记录
            List<long[]> viewLogs = convertViewLogs(mallViewLogMapper.selectAllViewLogsForCF());
            
            // 2. 获取所有评论记录
            List<Object[]> comments = convertComments(mallCommentMapper.selectAllCommentsForCF());
            
            // 3. 执行UserCF推荐算法
            List<Long> recommendGoodsIds = UserCFRecommender.recommend(userId, viewLogs, comments, count);
            
            // 4. 如果推荐结果为空（冷启动），返回热门商品
            if (recommendGoodsIds.isEmpty())
            {
                log.info("用户[{}]触发冷启动，返回热门商品", userId);
                return getHotGoods(count);
            }
            
            // 5. 根据商品ID查询商品详情
            List<MallGoods> result = new ArrayList<>();
            for (Long goodsId : recommendGoodsIds)
            {
                MallGoods goods = mallGoodsMapper.selectMallGoodsById(goodsId);
                if (goods != null && "0".equals(goods.getStatus()))
                {
                    // 只返回上架的商品
                    result.add(goods);
                }
            }
            
            // 6. 如果推荐结果不足，用热门商品补充
            if (result.size() < count)
            {
                List<MallGoods> hotGoods = getHotGoods(count - result.size());
                // 过滤掉已推荐的商品
                List<Long> existIds = result.stream().map(MallGoods::getGoodsId).collect(Collectors.toList());
                for (MallGoods hot : hotGoods)
                {
                    if (!existIds.contains(hot.getGoodsId()))
                    {
                        result.add(hot);
                        if (result.size() >= count) break;
                    }
                }
            }
            
            return result;
        }
        catch (Exception e)
        {
            log.error("推荐算法执行异常，返回热门商品", e);
            return getHotGoods(count);
        }
    }

    /**
     * 获取热门商品（用于冷启动或无推荐结果时）
     * 按销量降序排列
     * 
     * @param count 数量
     * @return 热门商品列表
     */
    @Override
    public List<MallGoods> getHotGoods(int count)
    {
        MallGoods query = new MallGoods();
        query.setStatus("0"); // 只查询上架商品
        List<MallGoods> allGoods = mallGoodsMapper.selectMallGoodsList(query);
        
        // 按销量降序排序，取前N个
        return allGoods.stream()
                .sorted((a, b) -> (b.getSales() != null ? b.getSales() : 0) - (a.getSales() != null ? a.getSales() : 0))
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * 转换浏览记录格式
     * Map -> long[]
     */
    private List<long[]> convertViewLogs(List<Map<String, Object>> viewLogs)
    {
        if (viewLogs == null || viewLogs.isEmpty())
        {
            return new ArrayList<>();
        }
        
        List<long[]> result = new ArrayList<>();
        for (Map<String, Object> log : viewLogs)
        {
            Object userIdObj = log.get("userId");
            Object goodsIdObj = log.get("goodsId");
            if (userIdObj != null && goodsIdObj != null)
            {
                long userId = ((Number) userIdObj).longValue();
                long goodsId = ((Number) goodsIdObj).longValue();
                result.add(new long[]{userId, goodsId});
            }
        }
        return result;
    }

    /**
     * 转换评论记录格式
     * Map -> Object[]
     */
    private List<Object[]> convertComments(List<Map<String, Object>> comments)
    {
        if (comments == null || comments.isEmpty())
        {
            return new ArrayList<>();
        }
        
        List<Object[]> result = new ArrayList<>();
        for (Map<String, Object> comment : comments)
        {
            Object userIdObj = comment.get("userId");
            Object goodsIdObj = comment.get("goodsId");
            Object starObj = comment.get("star");
            if (userIdObj != null && goodsIdObj != null && starObj != null)
            {
                result.add(new Object[]{userIdObj, goodsIdObj, starObj});
            }
        }
        return result;
    }
}
