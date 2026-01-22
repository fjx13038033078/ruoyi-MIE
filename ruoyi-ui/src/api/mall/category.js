import request from '@/utils/request'

// 查询分类列表
export function listCategory(query) {
  return request({
    url: '/mall/category/list',
    method: 'get',
    params: query
  })
}

// 查询分类列表（树形结构）
export function treeListCategory(query) {
  return request({
    url: '/mall/category/treeList',
    method: 'get',
    params: query
  })
}

// 查询所有分类（下拉选择用）
export function selectAllCategory() {
  return request({
    url: '/mall/category/selectAll',
    method: 'get'
  })
}

// 查询分类详细
export function getCategory(categoryId) {
  return request({
    url: '/mall/category/' + categoryId,
    method: 'get'
  })
}

// 新增分类
export function addCategory(data) {
  return request({
    url: '/mall/category',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateCategory(data) {
  return request({
    url: '/mall/category',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delCategory(categoryId) {
  return request({
    url: '/mall/category/' + categoryId,
    method: 'delete'
  })
}
