package com.ruoyi.mall.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mall.domain.MallGoods;
import com.ruoyi.mall.domain.MallOrder;
import com.ruoyi.mall.domain.MallComment;
import com.ruoyi.mall.domain.MallViewLog;
import com.ruoyi.mall.mapper.MallGoodsMapper;
import com.ruoyi.mall.mapper.MallOrderMapper;
import com.ruoyi.mall.mapper.MallCommentMapper;
import com.ruoyi.mall.mapper.MallViewLogMapper;
import com.ruoyi.mall.service.IMallStatisticsService;

/**
 * 商城统计Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class MallStatisticsServiceImpl implements IMallStatisticsService
{
    @Autowired
    private MallGoodsMapper mallGoodsMapper;

    @Autowired
    private MallOrderMapper mallOrderMapper;

    @Autowired
    private MallCommentMapper mallCommentMapper;

    @Autowired
    private MallViewLogMapper mallViewLogMapper;

    /**
     * 获取商城概览统计数据
     * 
     * @return 统计数据
     */
    @Override
    public Map<String, Object> getOverviewStatistics()
    {
        Map<String, Object> result = new HashMap<>();

        // 1. 商品统计
        MallGoods goodsQuery = new MallGoods();
        int goodsTotal = mallGoodsMapper.selectMallGoodsList(goodsQuery).size();
        goodsQuery.setStatus("0"); // 上架商品
        int goodsOnSale = mallGoodsMapper.selectMallGoodsList(goodsQuery).size();
        result.put("goodsTotal", goodsTotal);
        result.put("goodsOnSale", goodsOnSale);

        // 2. 订单统计
        MallOrder orderQuery = new MallOrder();
        int orderTotal = mallOrderMapper.selectMallOrderList(orderQuery).size();
        orderQuery.setPayStatus("1"); // 已支付
        int orderPaid = mallOrderMapper.selectMallOrderList(orderQuery).size();
        result.put("orderTotal", orderTotal);
        result.put("orderPaid", orderPaid);

        // 3. 评论统计
        MallComment commentQuery = new MallComment();
        int commentTotal = mallCommentMapper.selectMallCommentList(commentQuery).size();
        result.put("commentTotal", commentTotal);

        // 4. 浏览记录统计
        MallViewLog viewLogQuery = new MallViewLog();
        int viewTotal = mallViewLogMapper.selectMallViewLogList(viewLogQuery).size();
        result.put("viewTotal", viewTotal);

        return result;
    }
}
