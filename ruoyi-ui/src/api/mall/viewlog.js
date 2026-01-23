import request from '@/utils/request'

/**
 * 记录用户浏览商品
 * 当用户进入商品详情页时自动调用
 * @param {Long} goodsId 商品ID
 */
export function recordViewLog(goodsId) {
  return request({
    url: '/mall/viewlog/record/' + goodsId,
    method: 'post'
  })
}
