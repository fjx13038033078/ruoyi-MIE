import request from '@/utils/request'

// ==================== 管理端接口 ====================

// 查询商品评价列表
export function listComment(query) {
  return request({
    url: '/mall/comment/list',
    method: 'get',
    params: query
  })
}

// 查询商品评价详细
export function getComment(commentId) {
  return request({
    url: '/mall/comment/' + commentId,
    method: 'get'
  })
}

// 删除商品评价
export function delComment(commentId) {
  return request({
    url: '/mall/comment/' + commentId,
    method: 'delete'
  })
}

// 导出商品评价
export function exportComment(query) {
  return request({
    url: '/mall/comment/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// ==================== 用户端接口 ====================

// 获取商品的评价列表
export function listCommentByGoodsId(goodsId) {
  return request({
    url: '/mall/comment/goods/' + goodsId,
    method: 'get'
  })
}

// 获取商品的评价统计
export function getCommentStats(goodsId) {
  return request({
    url: '/mall/comment/stats/' + goodsId,
    method: 'get'
  })
}

// 用户添加评价
export function addComment(data) {
  return request({
    url: '/mall/comment/add',
    method: 'post',
    data: data
  })
}

// 用户删除自己的评价
export function deleteMyComment(commentId) {
  return request({
    url: '/mall/comment/my/' + commentId,
    method: 'delete'
  })
}

// 获取当前用户的评价列表
export function myComments(query) {
  return request({
    url: '/mall/comment/my',
    method: 'get',
    params: query
  })
}
