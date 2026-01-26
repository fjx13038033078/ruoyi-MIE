<template>
  <div class="dashboard-container">
    <!-- 顶部标题区域 -->
    <div class="dashboard-header">
      <div class="header-left">
        <div class="title-line"></div>
        <div class="title-content">
          <h1 class="main-title">母婴商城管理平台</h1>
          <p class="sub-title">欢迎回来，祝您工作顺利</p>
        </div>
      </div>
      <div class="header-right">
        <i class="el-icon-date"></i>
        <span class="header-time">{{ currentTime }}</span>
      </div>
    </div>

    <!-- 统计卡片区域 -->
    <el-row :gutter="16" class="stats-row">
      <!-- 商品统计 -->
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-header">
            <span class="stat-title">商品总数</span>
            <i class="el-icon-goods stat-icon goods-icon"></i>
          </div>
          <div class="stat-value">{{ statistics.goodsTotal || 0 }}</div>
          <div class="stat-footer">
            <span>在售商品</span>
            <span class="stat-sub-value">{{ statistics.goodsOnSale || 0 }} 件</span>
          </div>
        </el-card>
      </el-col>

      <!-- 订单统计 -->
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-header">
            <span class="stat-title">订单总数</span>
            <i class="el-icon-s-order stat-icon order-icon"></i>
          </div>
          <div class="stat-value">{{ statistics.orderTotal || 0 }}</div>
          <div class="stat-footer">
            <span>已支付</span>
            <span class="stat-sub-value">{{ statistics.orderPaid || 0 }} 笔</span>
          </div>
        </el-card>
      </el-col>

      <!-- 评论统计 -->
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-header">
            <span class="stat-title">评论总数</span>
            <i class="el-icon-chat-dot-round stat-icon comment-icon"></i>
          </div>
          <div class="stat-value">{{ statistics.commentTotal || 0 }}</div>
          <div class="stat-footer">
            <span>用户反馈</span>
            <span class="stat-sub-value">持续增长</span>
          </div>
        </el-card>
      </el-col>

      <!-- 浏览统计 -->
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-header">
            <span class="stat-title">浏览记录</span>
            <i class="el-icon-view stat-icon view-icon"></i>
          </div>
          <div class="stat-value">{{ statistics.viewTotal || 0 }}</div>
          <div class="stat-footer">
            <span>行为数据</span>
            <span class="stat-sub-value">推荐依据</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷入口 -->
    <el-card class="quick-card" shadow="never">
      <div slot="header" class="quick-header">
        <i class="el-icon-menu"></i>
        <span>快捷入口</span>
      </div>
      <el-row :gutter="16">
        <el-col :xs="12" :sm="6" :lg="3" v-for="item in quickLinks" :key="item.name">
          <div class="quick-item" @click="navigateTo(item.path)">
            <div class="quick-icon" :class="item.iconClass">
              <i :class="item.icon"></i>
            </div>
            <span class="quick-name">{{ item.name }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { getOverviewStatistics } from "@/api/mall/statistics";

export default {
  name: "Dashboard",
  data() {
    return {
      statistics: {
        goodsTotal: 0,
        goodsOnSale: 0,
        orderTotal: 0,
        orderPaid: 0,
        commentTotal: 0,
        viewTotal: 0
      },
      currentTime: '',
      timer: null,
      quickLinks: [
        { name: '商品管理', path: '/mall/goods', icon: 'el-icon-goods', iconClass: 'icon-purple' },
        { name: '分类管理', path: '/mall/category', icon: 'el-icon-folder', iconClass: 'icon-teal' },
        { name: '订单管理', path: '/mall/order', icon: 'el-icon-s-order', iconClass: 'icon-blue' },
        { name: '评论管理', path: '/mall/comment', icon: 'el-icon-chat-dot-round', iconClass: 'icon-green' },
        { name: '用户管理', path: '/system/user', icon: 'el-icon-user', iconClass: 'icon-orange' }
      ]
    };
  },
  created() {
    this.getStatistics();
    this.updateTime();
    this.timer = setInterval(this.updateTime, 1000);
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  methods: {
    getStatistics() {
      getOverviewStatistics().then(response => {
        this.statistics = response.data || {};
      }).catch(() => {});
    },
    updateTime() {
      const now = new Date();
      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, '0');
      const day = String(now.getDate()).padStart(2, '0');
      const hours = String(now.getHours()).padStart(2, '0');
      const minutes = String(now.getMinutes()).padStart(2, '0');
      const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
      const weekday = weekdays[now.getDay()];
      this.currentTime = `${year}-${month}-${day} ${weekday} ${hours}:${minutes}`;
    },
    navigateTo(path) {
      this.$router.push(path);
    }
  }
};
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);
}

/* 顶部标题 */
.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 24px;
  background: #fff;
  border-left: 4px solid #409eff;

  .header-left {
    display: flex;
    align-items: stretch;

    .title-line {
      width: 4px;
      background: #409eff;
      margin-right: 16px;
      display: none;
    }

    .title-content {
      .main-title {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 6px;
        letter-spacing: 1px;
      }

      .sub-title {
        font-size: 13px;
        color: #909399;
        margin: 0;
      }
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    color: #606266;
    font-size: 14px;

    i {
      margin-right: 6px;
      color: #909399;
    }
  }
}

/* 统计卡片 */
.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 4px;
  margin-bottom: 16px;

  .stat-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    .stat-title {
      font-size: 14px;
      color: #909399;
    }

    .stat-icon {
      font-size: 24px;
    }

    .goods-icon { color: #7c4dff; }
    .order-icon { color: #ff6b81; }
    .comment-icon { color: #3498db; }
    .view-icon { color: #2ecc71; }
  }

  .stat-value {
    font-size: 32px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 12px;
  }

  .stat-footer {
    display: flex;
    justify-content: space-between;
    font-size: 13px;
    color: #909399;
    padding-top: 12px;
    border-top: 1px solid #f0f0f0;

    .stat-sub-value {
      color: #606266;
    }
  }
}

/* 快捷入口 */
.quick-card {
  border-radius: 4px;

  .quick-header {
    font-size: 15px;
    font-weight: 500;
    color: #303133;

    i {
      margin-right: 8px;
      color: #409eff;
    }
  }
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 8px;
  cursor: pointer;
  border-radius: 4px;
  transition: background 0.2s;

  &:hover {
    background: #f5f7fa;
  }

  .quick-icon {
    width: 44px;
    height: 44px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 8px;

    i {
      font-size: 20px;
      color: #fff;
    }
  }

  .quick-name {
    font-size: 13px;
    color: #606266;
  }
}

/* 图标颜色 */
.icon-purple { background: #7c4dff; }
.icon-teal { background: #009688; }
.icon-blue { background: #3498db; }
.icon-green { background: #2ecc71; }
.icon-orange { background: #f39c12; }

/* 响应式 */
@media (max-width: 768px) {
  .dashboard-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .stat-card {
    margin-bottom: 12px;
  }
}
</style>
