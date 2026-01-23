package com.ruoyi.mall.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mall.domain.MallViewLog;
import com.ruoyi.mall.mapper.MallViewLogMapper;
import com.ruoyi.mall.service.IMallViewLogService;

/**
 * 用户商品浏览记录Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class MallViewLogServiceImpl implements IMallViewLogService
{
    @Autowired
    private MallViewLogMapper mallViewLogMapper;

    /**
     * 查询浏览记录
     * 
     * @param viewId 浏览记录ID
     * @return 浏览记录
     */
    @Override
    public MallViewLog selectMallViewLogById(Long viewId)
    {
        return mallViewLogMapper.selectMallViewLogById(viewId);
    }

    /**
     * 查询浏览记录列表
     * 
     * @param mallViewLog 浏览记录
     * @return 浏览记录
     */
    @Override
    public List<MallViewLog> selectMallViewLogList(MallViewLog mallViewLog)
    {
        return mallViewLogMapper.selectMallViewLogList(mallViewLog);
    }

    /**
     * 查询用户的浏览记录
     * 
     * @param userId 用户ID
     * @return 浏览记录集合
     */
    @Override
    public List<MallViewLog> selectMallViewLogByUserId(Long userId)
    {
        return mallViewLogMapper.selectMallViewLogByUserId(userId);
    }

    /**
     * 记录用户浏览行为（核心方法）
     * 如果用户已浏览过该商品，则更新浏览时间；否则新增记录
     * 
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 结果
     */
    @Override
    public int recordViewLog(Long userId, Long goodsId)
    {
        if (userId == null || goodsId == null)
        {
            return 0;
        }

        // 查询是否已有浏览记录
        MallViewLog existLog = mallViewLogMapper.selectMallViewLogByUserAndGoods(userId, goodsId);
        
        if (existLog != null)
        {
            // 已有记录，更新浏览时间
            existLog.setViewTime(new Date());
            return mallViewLogMapper.updateMallViewLog(existLog);
        }
        else
        {
            // 新增浏览记录
            MallViewLog viewLog = new MallViewLog();
            viewLog.setUserId(userId);
            viewLog.setGoodsId(goodsId);
            viewLog.setViewTime(new Date());
            return mallViewLogMapper.insertMallViewLog(viewLog);
        }
    }

    /**
     * 新增浏览记录
     * 
     * @param mallViewLog 浏览记录
     * @return 结果
     */
    @Override
    public int insertMallViewLog(MallViewLog mallViewLog)
    {
        mallViewLog.setViewTime(new Date());
        return mallViewLogMapper.insertMallViewLog(mallViewLog);
    }

    /**
     * 修改浏览记录
     * 
     * @param mallViewLog 浏览记录
     * @return 结果
     */
    @Override
    public int updateMallViewLog(MallViewLog mallViewLog)
    {
        return mallViewLogMapper.updateMallViewLog(mallViewLog);
    }

    /**
     * 批量删除浏览记录
     * 
     * @param viewIds 需要删除的浏览记录ID
     * @return 结果
     */
    @Override
    public int deleteMallViewLogByIds(Long[] viewIds)
    {
        return mallViewLogMapper.deleteMallViewLogByIds(viewIds);
    }

    /**
     * 删除浏览记录
     * 
     * @param viewId 浏览记录ID
     * @return 结果
     */
    @Override
    public int deleteMallViewLogById(Long viewId)
    {
        return mallViewLogMapper.deleteMallViewLogById(viewId);
    }

    /**
     * 查询浏览过某商品的所有用户ID（用于协同过滤）
     * 
     * @param goodsId 商品ID
     * @return 用户ID列表
     */
    @Override
    public List<Long> selectUserIdsByGoodsId(Long goodsId)
    {
        return mallViewLogMapper.selectUserIdsByGoodsId(goodsId);
    }

    /**
     * 查询用户浏览过的所有商品ID（用于协同过滤）
     * 
     * @param userId 用户ID
     * @return 商品ID列表
     */
    @Override
    public List<Long> selectGoodsIdsByUserId(Long userId)
    {
        return mallViewLogMapper.selectGoodsIdsByUserId(userId);
    }

    /**
     * 统计商品的浏览次数
     * 
     * @param goodsId 商品ID
     * @return 浏览次数
     */
    @Override
    public int selectViewCountByGoodsId(Long goodsId)
    {
        return mallViewLogMapper.selectViewCountByGoodsId(goodsId);
    }
}
