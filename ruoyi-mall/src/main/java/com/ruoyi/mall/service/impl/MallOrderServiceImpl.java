package com.ruoyi.mall.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.mall.domain.MallGoods;
import com.ruoyi.mall.mapper.MallGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.mall.mapper.MallOrderMapper;
import com.ruoyi.mall.domain.MallOrder;
import com.ruoyi.mall.service.IMallOrderService;

/**
 * 订单Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class MallOrderServiceImpl implements IMallOrderService
{
    @Autowired
    private MallOrderMapper mallOrderMapper;

    @Autowired
    private MallGoodsMapper mallGoodsMapper;

    /**
     * 查询订单
     * 
     * @param orderId 订单ID
     * @return 订单
     */
    @Override
    public MallOrder selectMallOrderById(Long orderId)
    {
        return mallOrderMapper.selectMallOrderById(orderId);
    }

    /**
     * 根据订单号查询订单
     * 
     * @param orderNo 订单号
     * @return 订单
     */
    @Override
    public MallOrder selectMallOrderByOrderNo(String orderNo)
    {
        return mallOrderMapper.selectMallOrderByOrderNo(orderNo);
    }

    /**
     * 查询订单列表
     * 
     * @param mallOrder 订单
     * @return 订单
     */
    @Override
    public List<MallOrder> selectMallOrderList(MallOrder mallOrder)
    {
        return mallOrderMapper.selectMallOrderList(mallOrder);
    }

    /**
     * 查询用户订单列表
     * 
     * @param userId 用户ID
     * @return 订单集合
     */
    @Override
    public List<MallOrder> selectMallOrderByUserId(Long userId)
    {
        return mallOrderMapper.selectMallOrderByUserId(userId);
    }

    /**
     * 创建订单（用户下单）
     * 
     * @param mallOrder 订单信息
     * @return 订单号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(MallOrder mallOrder)
    {
        // 1. 校验商品
        Long goodsId = mallOrder.getGoodsId();
        Integer quantity = mallOrder.getQuantity();
        if (goodsId == null || quantity == null || quantity <= 0)
        {
            throw new ServiceException("商品信息不完整");
        }

        MallGoods goods = mallGoodsMapper.selectMallGoodsById(goodsId);
        if (goods == null)
        {
            throw new ServiceException("商品不存在");
        }
        if (!"0".equals(goods.getStatus()))
        {
            throw new ServiceException("商品已下架");
        }
        if (goods.getStock() < quantity)
        {
            throw new ServiceException("商品库存不足");
        }

        // 2. 计算总金额
        BigDecimal totalAmount = goods.getPrice().multiply(new BigDecimal(quantity));

        // 3. 生成订单号
        String orderNo = generateOrderNo();

        // 4. 创建订单
        mallOrder.setOrderNo(orderNo);
        mallOrder.setTotalAmount(totalAmount);
        mallOrder.setPayStatus("0"); // 未支付
        mallOrder.setOrderStatus("0"); // 待发货
        mallOrder.setCreateTime(DateUtils.getNowDate());

        mallOrderMapper.insertMallOrder(mallOrder);

        // 5. 扣减库存、增加销量
        goods.setStock(goods.getStock() - quantity);
        goods.setSales((goods.getSales() == null ? 0 : goods.getSales()) + quantity);
        mallGoodsMapper.updateMallGoods(goods);

        return orderNo;
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo()
    {
        // 格式：年月日时分秒 + 6位随机数
        String timestamp = DateUtils.dateTimeNow("yyyyMMddHHmmss");
        String random = IdUtils.fastSimpleUUID().substring(0, 6).toUpperCase();
        return timestamp + random;
    }

    /**
     * 新增订单
     * 
     * @param mallOrder 订单
     * @return 结果
     */
    @Override
    public int insertMallOrder(MallOrder mallOrder)
    {
        mallOrder.setCreateTime(DateUtils.getNowDate());
        return mallOrderMapper.insertMallOrder(mallOrder);
    }

    /**
     * 修改订单
     * 
     * @param mallOrder 订单
     * @return 结果
     */
    @Override
    public int updateMallOrder(MallOrder mallOrder)
    {
        return mallOrderMapper.updateMallOrder(mallOrder);
    }

    /**
     * 批量删除订单
     * 
     * @param orderIds 需要删除的订单ID
     * @return 结果
     */
    @Override
    public int deleteMallOrderByIds(Long[] orderIds)
    {
        return mallOrderMapper.deleteMallOrderByIds(orderIds);
    }

    /**
     * 删除订单
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public int deleteMallOrderById(Long orderId)
    {
        return mallOrderMapper.deleteMallOrderById(orderId);
    }
}
