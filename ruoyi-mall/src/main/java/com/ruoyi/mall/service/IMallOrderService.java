package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallOrder;

/**
 * 订单Service接口
 * 
 * @author ruoyi
 */
public interface IMallOrderService
{
    /**
     * 查询订单
     * 
     * @param orderId 订单ID
     * @return 订单
     */
    public MallOrder selectMallOrderById(Long orderId);

    /**
     * 根据订单号查询订单
     * 
     * @param orderNo 订单号
     * @return 订单
     */
    public MallOrder selectMallOrderByOrderNo(String orderNo);

    /**
     * 查询订单列表
     * 
     * @param mallOrder 订单
     * @return 订单集合
     */
    public List<MallOrder> selectMallOrderList(MallOrder mallOrder);

    /**
     * 查询用户订单列表
     * 
     * @param userId 用户ID
     * @return 订单集合
     */
    public List<MallOrder> selectMallOrderByUserId(Long userId);

    /**
     * 创建订单（用户下单）
     * 
     * @param mallOrder 订单信息（包含商品ID、数量、收货信息）
     * @return 订单号
     */
    public String createOrder(MallOrder mallOrder);

    /**
     * 新增订单
     * 
     * @param mallOrder 订单
     * @return 结果
     */
    public int insertMallOrder(MallOrder mallOrder);

    /**
     * 修改订单
     * 
     * @param mallOrder 订单
     * @return 结果
     */
    public int updateMallOrder(MallOrder mallOrder);

    /**
     * 批量删除订单
     * 
     * @param orderIds 需要删除的订单ID
     * @return 结果
     */
    public int deleteMallOrderByIds(Long[] orderIds);

    /**
     * 删除订单
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    public int deleteMallOrderById(Long orderId);
}
