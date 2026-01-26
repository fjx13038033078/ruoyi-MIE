import request from '@/utils/request'

/**
 * 获取推荐商品列表（猜你喜欢）
 * 基于UserCF协同过滤算法
 * @param {Number} count 推荐数量
 */
export function getRecommendList(count) {
  return request({
    url: '/mall/recommend/list',
    method: 'get',
    params: { count }
  })
}

/**
 * 获取热门商品列表
 * 用于未登录用户或冷启动场景
 * @param {Number} count 数量
 */
export function getHotList(count) {
  return request({
    url: '/mall/recommend/hot',
    method: 'get',
    params: { count }
  })
}
