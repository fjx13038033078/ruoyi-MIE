package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallGoods;

/**
 * 商品信息Service接口
 * 
 * @author ruoyi
 */
public interface IMallGoodsService
{
    /**
     * 查询商品信息
     * 
     * @param goodsId 商品ID
     * @return 商品信息
     */
    public MallGoods selectMallGoodsById(Long goodsId);

    /**
     * 查询商品信息列表
     * 
     * @param mallGoods 商品信息
     * @return 商品信息集合
     */
    public List<MallGoods> selectMallGoodsList(MallGoods mallGoods);

    /**
     * 新增商品信息
     * 
     * @param mallGoods 商品信息
     * @return 结果
     */
    public int insertMallGoods(MallGoods mallGoods);

    /**
     * 修改商品信息
     * 
     * @param mallGoods 商品信息
     * @return 结果
     */
    public int updateMallGoods(MallGoods mallGoods);

    /**
     * 批量删除商品信息
     * 
     * @param goodsIds 需要删除的商品ID
     * @return 结果
     */
    public int deleteMallGoodsByIds(Long[] goodsIds);

    /**
     * 删除商品信息
     * 
     * @param goodsId 商品ID
     * @return 结果
     */
    public int deleteMallGoodsById(Long goodsId);
}
