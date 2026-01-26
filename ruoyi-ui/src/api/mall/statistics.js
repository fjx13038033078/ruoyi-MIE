import request from '@/utils/request'

/**
 * 获取商城概览统计数据
 */
export function getOverviewStatistics() {
  return request({
    url: '/mall/statistics/overview',
    method: 'get'
  })
}
