package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallComment;

/**
 * 商品评价Service接口
 * 
 * @author ruoyi
 */
public interface IMallCommentService
{
    /**
     * 查询商品评价
     * 
     * @param commentId 评价ID
     * @return 商品评价
     */
    public MallComment selectMallCommentById(Long commentId);

    /**
     * 查询商品评价列表
     * 
     * @param mallComment 商品评价
     * @return 商品评价集合
     */
    public List<MallComment> selectMallCommentList(MallComment mallComment);

    /**
     * 查询指定商品的评价列表
     * 
     * @param goodsId 商品ID
     * @return 商品评价集合
     */
    public List<MallComment> selectMallCommentByGoodsId(Long goodsId);

    /**
     * 查询指定用户的评价列表
     * 
     * @param userId 用户ID
     * @return 商品评价集合
     */
    public List<MallComment> selectMallCommentByUserId(Long userId);

    /**
     * 新增商品评价
     * 
     * @param mallComment 商品评价
     * @return 结果
     */
    public int insertMallComment(MallComment mallComment);

    /**
     * 修改商品评价
     * 
     * @param mallComment 商品评价
     * @return 结果
     */
    public int updateMallComment(MallComment mallComment);

    /**
     * 批量删除商品评价
     * 
     * @param commentIds 需要删除的评价ID
     * @return 结果
     */
    public int deleteMallCommentByIds(Long[] commentIds);

    /**
     * 删除商品评价
     * 
     * @param commentId 评价ID
     * @return 结果
     */
    public int deleteMallCommentById(Long commentId);

    /**
     * 用户删除自己的评论
     * 
     * @param commentId 评价ID
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteMyComment(Long commentId, Long userId);

    /**
     * 获取商品的平均评分
     * 
     * @param goodsId 商品ID
     * @return 平均评分
     */
    public Double getAvgStarByGoodsId(Long goodsId);

    /**
     * 获取商品的评价数量
     * 
     * @param goodsId 商品ID
     * @return 评价数量
     */
    public int getCountByGoodsId(Long goodsId);
}
