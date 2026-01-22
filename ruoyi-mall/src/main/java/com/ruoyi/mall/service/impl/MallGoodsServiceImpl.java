package com.ruoyi.mall.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mall.mapper.MallGoodsMapper;
import com.ruoyi.mall.domain.MallGoods;
import com.ruoyi.mall.service.IMallGoodsService;

/**
 * 商品信息Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class MallGoodsServiceImpl implements IMallGoodsService
{
    @Autowired
    private MallGoodsMapper mallGoodsMapper;

    /**
     * 查询商品信息
     * 
     * @param goodsId 商品ID
     * @return 商品信息
     */
    @Override
    public MallGoods selectMallGoodsById(Long goodsId)
    {
        return mallGoodsMapper.selectMallGoodsById(goodsId);
    }

    /**
     * 查询商品信息列表
     * 
     * @param mallGoods 商品信息
     * @return 商品信息
     */
    @Override
    public List<MallGoods> selectMallGoodsList(MallGoods mallGoods)
    {
        return mallGoodsMapper.selectMallGoodsList(mallGoods);
    }

    /**
     * 新增商品信息
     * 
     * @param mallGoods 商品信息
     * @return 结果
     */
    @Override
    public int insertMallGoods(MallGoods mallGoods)
    {
        mallGoods.setCreateTime(DateUtils.getNowDate());
        return mallGoodsMapper.insertMallGoods(mallGoods);
    }

    /**
     * 修改商品信息
     * 
     * @param mallGoods 商品信息
     * @return 结果
     */
    @Override
    public int updateMallGoods(MallGoods mallGoods)
    {
        mallGoods.setUpdateTime(DateUtils.getNowDate());
        return mallGoodsMapper.updateMallGoods(mallGoods);
    }

    /**
     * 批量删除商品信息
     * 
     * @param goodsIds 需要删除的商品ID
     * @return 结果
     */
    @Override
    public int deleteMallGoodsByIds(Long[] goodsIds)
    {
        return mallGoodsMapper.deleteMallGoodsByIds(goodsIds);
    }

    /**
     * 删除商品信息
     * 
     * @param goodsId 商品ID
     * @return 结果
     */
    @Override
    public int deleteMallGoodsById(Long goodsId)
    {
        return mallGoodsMapper.deleteMallGoodsById(goodsId);
    }
}
