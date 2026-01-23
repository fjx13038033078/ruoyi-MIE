package com.ruoyi.mall.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.mall.domain.MallComment;
import com.ruoyi.mall.mapper.MallCommentMapper;
import com.ruoyi.mall.service.IMallCommentService;

/**
 * 商品评价Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class MallCommentServiceImpl implements IMallCommentService
{
    @Autowired
    private MallCommentMapper mallCommentMapper;

    /**
     * 查询商品评价
     * 
     * @param commentId 评价ID
     * @return 商品评价
     */
    @Override
    public MallComment selectMallCommentById(Long commentId)
    {
        return mallCommentMapper.selectMallCommentById(commentId);
    }

    /**
     * 查询商品评价列表
     * 
     * @param mallComment 商品评价
     * @return 商品评价
     */
    @Override
    public List<MallComment> selectMallCommentList(MallComment mallComment)
    {
        return mallCommentMapper.selectMallCommentList(mallComment);
    }

    /**
     * 查询指定商品的评价列表
     * 
     * @param goodsId 商品ID
     * @return 商品评价集合
     */
    @Override
    public List<MallComment> selectMallCommentByGoodsId(Long goodsId)
    {
        return mallCommentMapper.selectMallCommentByGoodsId(goodsId);
    }

    /**
     * 查询指定用户的评价列表
     * 
     * @param userId 用户ID
     * @return 商品评价集合
     */
    @Override
    public List<MallComment> selectMallCommentByUserId(Long userId)
    {
        return mallCommentMapper.selectMallCommentByUserId(userId);
    }

    /**
     * 新增商品评价
     * 
     * @param mallComment 商品评价
     * @return 结果
     */
    @Override
    public int insertMallComment(MallComment mallComment)
    {
        mallComment.setCreateTime(new Date());
        return mallCommentMapper.insertMallComment(mallComment);
    }

    /**
     * 修改商品评价
     * 
     * @param mallComment 商品评价
     * @return 结果
     */
    @Override
    public int updateMallComment(MallComment mallComment)
    {
        return mallCommentMapper.updateMallComment(mallComment);
    }

    /**
     * 批量删除商品评价
     * 
     * @param commentIds 需要删除的评价ID
     * @return 结果
     */
    @Override
    public int deleteMallCommentByIds(Long[] commentIds)
    {
        return mallCommentMapper.deleteMallCommentByIds(commentIds);
    }

    /**
     * 删除商品评价
     * 
     * @param commentId 评价ID
     * @return 结果
     */
    @Override
    public int deleteMallCommentById(Long commentId)
    {
        return mallCommentMapper.deleteMallCommentById(commentId);
    }

    /**
     * 用户删除自己的评论
     * 
     * @param commentId 评价ID
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteMyComment(Long commentId, Long userId)
    {
        MallComment comment = mallCommentMapper.selectMallCommentById(commentId);
        if (comment == null)
        {
            throw new ServiceException("评论不存在");
        }
        if (!comment.getUserId().equals(userId))
        {
            throw new ServiceException("只能删除自己的评论");
        }
        return mallCommentMapper.deleteMallCommentById(commentId);
    }

    /**
     * 获取商品的平均评分
     * 
     * @param goodsId 商品ID
     * @return 平均评分
     */
    @Override
    public Double getAvgStarByGoodsId(Long goodsId)
    {
        Double avgStar = mallCommentMapper.selectAvgStarByGoodsId(goodsId);
        return avgStar != null ? avgStar : 5.0;
    }

    /**
     * 获取商品的评价数量
     * 
     * @param goodsId 商品ID
     * @return 评价数量
     */
    @Override
    public int getCountByGoodsId(Long goodsId)
    {
        return mallCommentMapper.selectCountByGoodsId(goodsId);
    }
}
