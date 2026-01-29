<template>
  <div class="my-orders">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">我的订单</h2>
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回商城</el-button>
    </div>

    <!-- 订单列表 -->
    <div class="orders-container">
      <el-card shadow="never">
        <!-- 筛选条件 -->
        <div class="filter-bar">
          <el-radio-group v-model="filterStatus" size="small" @change="handleFilterChange">
            <el-radio-button label="">全部订单</el-radio-button>
            <el-radio-button label="0">待支付</el-radio-button>
            <el-radio-button label="1">已支付</el-radio-button>
          </el-radio-group>
        </div>

        <!-- 订单列表 -->
        <div class="order-list" v-loading="loading">
          <div v-if="orderList.length === 0 && !loading" class="empty-orders">
            <el-empty description="暂无订单记录">
              <el-button type="primary" size="small" @click="goShopping">去逛逛</el-button>
            </el-empty>
          </div>

          <div v-for="order in orderList" :key="order.orderId" class="order-item">
            <!-- 订单头部 -->
            <div class="order-header">
              <div class="order-info">
                <span class="order-no">订单号：{{ order.orderNo }}</span>
                <span class="order-time">{{ order.createTime }}</span>
              </div>
              <div class="order-status" :class="getStatusClass(order.payStatus)">
                {{ getPayStatusText(order.payStatus) }}
              </div>
            </div>

            <!-- 订单内容 -->
            <div class="order-goods" @click="viewOrderDetail(order)">
              <div class="goods-info">
                <div class="goods-name">订单金额：¥{{ formatPrice(order.totalAmount) }}</div>
                <div class="goods-quantity">数量：{{ order.quantity || 1 }} 件</div>
              </div>
              <div class="goods-price">
                <el-button type="text" size="small">查看详情 <i class="el-icon-arrow-right"></i></el-button>
              </div>
            </div>

            <!-- 收货信息 -->
            <div class="order-address" v-if="order.receiverName">
              <i class="el-icon-location-outline"></i>
              <span>{{ order.receiverName }} {{ order.receiverPhone }}</span>
              <span class="address-detail">{{ order.receiverAddress }}</span>
            </div>

            <!-- 订单底部 -->
            <div class="order-footer">
              <div class="order-total">
                实付款：<span class="total-price">¥{{ formatPrice(order.totalAmount) }}</span>
              </div>
              <div class="order-actions">
                <el-button size="mini" @click="viewOrderDetail(order)">订单详情</el-button>
                <el-button 
                  v-if="order.payStatus === '0'" 
                  type="primary" 
                  size="mini"
                  @click="handlePay(order)"
                >去支付</el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination-wrapper" v-if="total > 0">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="queryParams.pageSize"
            :current-page.sync="queryParams.pageNum"
            @current-change="getOrderList"
          />
        </div>
      </el-card>
    </div>

    <!-- 订单详情弹窗 -->
    <el-dialog title="订单详情" :visible.sync="detailVisible" width="500px" append-to-body>
      <div class="detail-content" v-if="currentOrder">
        <el-descriptions :column="1" border size="small">
          <el-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="商品名称">{{ currentOrder.goodsName }}</el-descriptions-item>
          <el-descriptions-item label="订单金额">
            <span class="price-text">¥{{ formatPrice(currentOrder.totalAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="支付状态">
            <el-tag :type="currentOrder.payStatus === '1' ? 'success' : 'warning'" size="small">
              {{ getPayStatusText(currentOrder.payStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getOrderStatusType(currentOrder.orderStatus)" size="small">
              {{ getOrderStatusText(currentOrder.orderStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="收货人">{{ currentOrder.receiverName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentOrder.receiverPhone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="收货地址">{{ currentOrder.receiverAddress || '-' }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ currentOrder.createTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="detailVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { myOrders } from "@/api/mall/order";

export default {
  name: "MyOrders",
  data() {
    return {
      loading: false,
      orderList: [],
      total: 0,
      filterStatus: '',
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        payStatus: undefined
      },
      detailVisible: false,
      currentOrder: null
    };
  },
  created() {
    this.getOrderList();
  },
  methods: {
    /** 获取订单列表 */
    getOrderList() {
      this.loading = true;
      const params = { ...this.queryParams };
      if (this.filterStatus) {
        params.payStatus = this.filterStatus;
      }
      myOrders(params).then(response => {
        this.orderList = response.rows || [];
        this.total = response.total || 0;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    /** 筛选状态变化 */
    handleFilterChange() {
      this.queryParams.pageNum = 1;
      this.getOrderList();
    },
    /** 格式化价格 */
    formatPrice(price) {
      if (!price) return '0.00';
      return Number(price).toFixed(2);
    },
    /** 获取支付状态文本 */
    getPayStatusText(status) {
      const map = { '0': '待支付', '1': '已支付' };
      return map[status] || '未知';
    },
    /** 获取支付状态样式 */
    getStatusClass(status) {
      return status === '1' ? 'status-paid' : 'status-unpaid';
    },
    /** 获取订单状态文本 */
    getOrderStatusText(status) {
      const map = { '0': '待发货', '1': '已发货', '2': '已完成' };
      return map[status] || '未知';
    },
    /** 获取订单状态类型 */
    getOrderStatusType(status) {
      const map = { '0': 'warning', '1': '', '2': 'success' };
      return map[status] || 'info';
    },
    /** 查看订单详情 */
    viewOrderDetail(order) {
      this.currentOrder = order;
      this.detailVisible = true;
    },
    /** 去支付 */
    handlePay(order) {
      this.$message.info('支付功能开发中...');
    },
    /** 返回商城 */
    goBack() {
      this.$router.push('/mall/home');
    },
    /** 去逛逛 */
    goShopping() {
      this.$router.push('/mall/home');
    }
  }
};
</script>

<style scoped lang="scss">
.my-orders {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  .page-title {
    font-size: 20px;
    font-weight: 600;
    color: #303133;
    margin: 0;
  }
}

.orders-container {
  .el-card {
    border-radius: 4px;
  }
}

.filter-bar {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.order-list {
  min-height: 300px;
}

.empty-orders {
  padding: 60px 0;
}

.order-item {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 16px;
  overflow: hidden;

  &:last-child {
    margin-bottom: 0;
  }
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #fafafa;
  border-bottom: 1px solid #ebeef5;

  .order-info {
    display: flex;
    gap: 20px;
    font-size: 13px;
    color: #909399;

    .order-no {
      color: #606266;
    }
  }

  .order-status {
    font-size: 13px;
    font-weight: 500;

    &.status-paid {
      color: #67c23a;
    }

    &.status-unpaid {
      color: #e6a23c;
    }
  }
}

.order-goods {
  display: flex;
  align-items: center;
  padding: 16px;
  cursor: pointer;
  transition: background 0.2s;

  &:hover {
    background: #fafafa;
  }

  .goods-info {
    flex: 1;

    .goods-name {
      font-size: 15px;
      color: #303133;
      margin-bottom: 6px;
    }

    .goods-quantity {
      font-size: 13px;
      color: #909399;
    }
  }

  .goods-price {
    display: flex;
    align-items: center;
  }
}

.order-address {
  padding: 10px 16px;
  font-size: 13px;
  color: #909399;
  background: #fafafa;
  border-top: 1px solid #ebeef5;

  i {
    margin-right: 6px;
    color: #409eff;
  }

  .address-detail {
    margin-left: 10px;
    color: #606266;
  }
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-top: 1px solid #ebeef5;

  .order-total {
    font-size: 13px;
    color: #606266;

    .total-price {
      font-size: 18px;
      font-weight: 600;
      color: #ff6b6b;
    }
  }

  .order-actions {
    display: flex;
    gap: 10px;
  }
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: center;
}

/* 详情弹窗 */
.detail-content {
  .price-text {
    color: #ff6b6b;
    font-weight: 600;
  }
}
</style>
