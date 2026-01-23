package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallViewLog;

/**
 * 用户商品浏览记录Service接口
 * 
 * @author ruoyi
 */
public interface IMallViewLogService
{
    /**
     * 查询浏览记录
     * 
     * @param viewId 浏览记录ID
     * @return 浏览记录
     */
    public MallViewLog selectMallViewLogById(Long viewId);

    /**
     * 查询浏览记录列表
     * 
     * @param mallViewLog 浏览记录
     * @return 浏览记录集合
     */
    public List<MallViewLog> selectMallViewLogList(MallViewLog mallViewLog);

    /**
     * 查询用户的浏览记录
     * 
     * @param userId 用户ID
     * @return 浏览记录集合
     */
    public List<MallViewLog> selectMallViewLogByUserId(Long userId);

    /**
     * 记录用户浏览行为（核心方法）
     * 如果用户已浏览过该商品，则更新浏览时间；否则新增记录
     * 
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 结果
     */
    public int recordViewLog(Long userId, Long goodsId);

    /**
     * 新增浏览记录
     * 
     * @param mallViewLog 浏览记录
     * @return 结果
     */
    public int insertMallViewLog(MallViewLog mallViewLog);

    /**
     * 修改浏览记录
     * 
     * @param mallViewLog 浏览记录
     * @return 结果
     */
    public int updateMallViewLog(MallViewLog mallViewLog);

    /**
     * 批量删除浏览记录
     * 
     * @param viewIds 需要删除的浏览记录ID
     * @return 结果
     */
    public int deleteMallViewLogByIds(Long[] viewIds);

    /**
     * 删除浏览记录
     * 
     * @param viewId 浏览记录ID
     * @return 结果
     */
    public int deleteMallViewLogById(Long viewId);

    /**
     * 查询浏览过某商品的所有用户ID（用于协同过滤）
     * 
     * @param goodsId 商品ID
     * @return 用户ID列表
     */
    public List<Long> selectUserIdsByGoodsId(Long goodsId);

    /**
     * 查询用户浏览过的所有商品ID（用于协同过滤）
     * 
     * @param userId 用户ID
     * @return 商品ID列表
     */
    public List<Long> selectGoodsIdsByUserId(Long userId);

    /**
     * 统计商品的浏览次数
     * 
     * @param goodsId 商品ID
     * @return 浏览次数
     */
    public int selectViewCountByGoodsId(Long goodsId);
}
