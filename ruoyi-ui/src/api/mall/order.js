import request from '@/utils/request'

// 创建订单（用户下单）
export function createOrder(data) {
  return request({
    url: '/mall/order/create',
    method: 'post',
    data: data
  })
}

// 查询当前用户的订单列表
export function myOrders(query) {
  return request({
    url: '/mall/order/my',
    method: 'get',
    params: query
  })
}

// 根据订单号查询订单
export function queryOrderByNo(orderNo) {
  return request({
    url: '/mall/order/query/' + orderNo,
    method: 'get'
  })
}

// ==================== 管理端接口 ====================

// 查询订单列表
export function listOrder(query) {
  return request({
    url: '/mall/order/list',
    method: 'get',
    params: query
  })
}

// 查询订单详细
export function getOrder(orderId) {
  return request({
    url: '/mall/order/' + orderId,
    method: 'get'
  })
}

// 修改订单
export function updateOrder(data) {
  return request({
    url: '/mall/order',
    method: 'put',
    data: data
  })
}

// 删除订单
export function delOrder(orderId) {
  return request({
    url: '/mall/order/' + orderId,
    method: 'delete'
  })
}
