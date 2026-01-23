<template>
  <div class="goods-detail" v-loading="loading">
    <!-- 返回导航 -->
    <div class="back-nav">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回商城首页</el-button>
    </div>

    <div class="detail-container" v-if="goods.goodsId">
      <!-- 商品主要信息 -->
      <div class="goods-main">
        <!-- 左侧图片 -->
        <div class="goods-gallery">
          <div class="main-image">
            <img :src="getImageUrl(goods.goodsCover)" :alt="goods.goodsName" />
          </div>
        </div>

        <!-- 右侧信息 -->
        <div class="goods-info">
          <div class="goods-tag" v-if="goods.ageStage">
            <i class="el-icon-s-custom"></i> 适用年龄：{{ goods.ageStage }}
          </div>
          
          <h1 class="goods-title">{{ goods.goodsName }}</h1>
          <p class="goods-intro">{{ goods.goodsIntro }}</p>

          <!-- 价格区域 -->
          <div class="price-section">
            <div class="current-price">
              <span class="label">商城价</span>
              <span class="symbol">¥</span>
              <span class="value">{{ formatPrice(goods.price) }}</span>
            </div>
            <div class="original-price" v-if="goods.originalPrice > goods.price">
              <span class="label">原价</span>
              <span class="value">¥{{ formatPrice(goods.originalPrice) }}</span>
              <span class="discount">省 ¥{{ formatPrice(goods.originalPrice - goods.price) }}</span>
            </div>
          </div>

          <!-- 商品属性 -->
          <div class="goods-attrs">
            <div class="attr-item">
              <span class="attr-label">分类</span>
              <span class="attr-value category-path">
                <template v-if="goods.parentCategoryName">
                  {{ goods.parentCategoryName }} <i class="el-icon-arrow-right"></i> {{ goods.categoryName }}
                </template>
                <template v-else>
                  {{ goods.categoryName || '未分类' }}
                </template>
              </span>
            </div>
            <div class="attr-item">
              <span class="attr-label">库存</span>
              <span class="attr-value" :class="{ 'low-stock': goods.stock < 10 }">
                {{ goods.stock > 0 ? goods.stock + ' 件' : '暂时缺货' }}
              </span>
            </div>
            <div class="attr-item">
              <span class="attr-label">销量</span>
              <span class="attr-value">{{ goods.sales || 0 }} 件</span>
            </div>
          </div>

          <!-- 数量选择 -->
          <div class="quantity-section">
            <span class="quantity-label">购买数量</span>
            <el-input-number 
              v-model="quantity" 
              :min="1" 
              :max="goods.stock || 99"
              size="medium"
            />
          </div>

          <!-- 操作按钮 -->
          <div class="action-buttons">
            <el-button 
              type="danger" 
              size="large" 
              icon="el-icon-shopping-cart-2"
              :disabled="goods.stock <= 0"
              @click="addToCart"
            >
              加入购物车
            </el-button>
            <el-button 
              type="warning" 
              size="large" 
              icon="el-icon-sell"
              :disabled="goods.stock <= 0"
              @click="buyNow"
            >
              立即购买
            </el-button>
          </div>

          <!-- 服务保障 -->
          <div class="service-tags">
            <span class="service-item">
              <i class="el-icon-circle-check"></i> 正品保证
            </span>
            <span class="service-item">
              <i class="el-icon-circle-check"></i> 7天退换
            </span>
            <span class="service-item">
              <i class="el-icon-circle-check"></i> 急速发货
            </span>
          </div>
        </div>
      </div>

      <!-- 商品详情和评论Tab切换 -->
      <div class="goods-detail-section">
        <div class="section-tabs">
          <div 
            class="tab-item" 
            :class="{ active: activeTab === 'detail' }"
            @click="activeTab = 'detail'"
          >
            <i class="el-icon-document"></i> 商品详情
          </div>
          <div 
            class="tab-item" 
            :class="{ active: activeTab === 'comment' }"
            @click="activeTab = 'comment'"
          >
            <i class="el-icon-chat-dot-round"></i> 商品评价 ({{ commentCount }})
          </div>
        </div>
        
        <!-- 商品详情内容 -->
        <div class="detail-content" v-show="activeTab === 'detail'" v-html="goods.detailContent || '<p style=\'text-align:center;color:#999;padding:60px 0;\'>暂无详情</p>'">
        </div>

        <!-- 评论区域 -->
        <div class="comment-section" v-show="activeTab === 'comment'">
          <!-- 评分统计 -->
          <div class="comment-stats">
            <div class="avg-star">
              <span class="star-value">{{ avgStar.toFixed(1) }}</span>
              <el-rate v-model="avgStar" disabled allow-half />
            </div>
            <span class="comment-total">共 {{ commentCount }} 条评价</span>
          </div>

          <!-- 发表评论 -->
          <div class="comment-form">
            <h4><i class="el-icon-edit"></i> 发表评价</h4>
            <el-form ref="commentForm" :model="commentForm" :rules="commentRules">
              <el-form-item prop="star">
                <span class="form-label">评分：</span>
                <el-rate v-model="commentForm.star" show-text :texts="['很差', '较差', '一般', '满意', '非常满意']" />
              </el-form-item>
              <el-form-item prop="content">
                <el-input
                  v-model="commentForm.content"
                  type="textarea"
                  :rows="3"
                  placeholder="说说你的使用感受吧~"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :loading="commentLoading" @click="submitComment">
                  <i class="el-icon-s-promotion"></i> 发表评价
                </el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 评论列表 -->
          <div class="comment-list">
            <div v-if="commentList.length === 0" class="no-comment">
              <el-empty description="暂无评价，快来发表第一条评价吧~" />
            </div>
            <div v-else class="comment-item" v-for="item in commentList" :key="item.commentId">
              <div class="comment-avatar">
                <el-avatar v-if="item.avatar" :src="getAvatarUrl(item.avatar)" :size="45" />
                <el-avatar v-else :size="45" icon="el-icon-user-solid" />
              </div>
              <div class="comment-content">
                <div class="comment-header">
                  <span class="user-name">{{ item.userName || '匿名用户' }}</span>
                  <el-rate v-model="item.star" disabled :show-score="false" />
                </div>
                <p class="comment-text">{{ item.content }}</p>
                <div class="comment-footer">
                  <span class="comment-time">{{ parseTime(item.createTime) }}</span>
                  <el-button 
                    v-if="isMyComment(item.userId)"
                    type="text" 
                    size="mini" 
                    class="delete-btn"
                    @click="handleDeleteComment(item)"
                  >
                    <i class="el-icon-delete"></i> 删除
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty v-if="!loading && !goods.goodsId" description="商品不存在或已下架">
      <el-button type="primary" @click="goBack">返回商城首页</el-button>
    </el-empty>

    <!-- 购买确认弹窗 -->
    <el-dialog 
      title="确认订单" 
      :visible.sync="orderDialogVisible" 
      width="500px" 
      append-to-body
      class="order-dialog"
    >
      <div class="order-goods-info">
        <img :src="getImageUrl(goods.goodsCover)" class="order-goods-img" />
        <div class="order-goods-detail">
          <h4>{{ goods.goodsName }}</h4>
          <p class="order-price">¥{{ formatPrice(goods.price) }} × {{ quantity }}</p>
        </div>
      </div>
      <el-divider></el-divider>
      <el-form ref="orderForm" :model="orderForm" :rules="orderRules" label-width="80px">
        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="orderForm.receiverName" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="手机号码" prop="receiverPhone">
          <el-input v-model="orderForm.receiverPhone" placeholder="请输入手机号码" maxlength="11" />
        </el-form-item>
        <el-form-item label="收货地址" prop="receiverAddress">
          <el-input 
            v-model="orderForm.receiverAddress" 
            type="textarea" 
            :rows="2" 
            placeholder="请输入详细收货地址"
          />
        </el-form-item>
      </el-form>
      <div class="order-total">
        <span>应付金额：</span>
        <span class="total-price">¥{{ formatPrice(goods.price * quantity) }}</span>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="orderDialogVisible = false">取 消</el-button>
        <el-button type="danger" :loading="submitLoading" @click="submitOrder">确认下单</el-button>
      </div>
    </el-dialog>

    <!-- 下单成功弹窗 -->
    <el-dialog 
      title="下单成功" 
      :visible.sync="successDialogVisible" 
      width="400px" 
      append-to-body
      center
      class="success-dialog"
    >
      <div class="success-content">
        <i class="el-icon-circle-check success-icon"></i>
        <h3>恭喜您，下单成功！</h3>
        <p>订单号：<span class="order-no">{{ orderNo }}</span></p>
        <p class="tips">请妥善保管订单号，以便查询订单状态</p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="successDialogVisible = false">继续购物</el-button>
        <el-button type="primary" @click="goToOrders">查看订单</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getGoods } from "@/api/mall/goods";
import { createOrder } from "@/api/mall/order";
import { listCommentByGoodsId, getCommentStats, addComment, deleteMyComment } from "@/api/mall/comment";
import { recordViewLog } from "@/api/mall/viewlog";
import store from '@/store';

export default {
  name: "GoodsDetail",
  data() {
    // 手机号验证规则
    const validatePhone = (rule, value, callback) => {
      const reg = /^1[3-9]\d{9}$/;
      if (!value) {
        callback(new Error('请输入手机号码'));
      } else if (!reg.test(value)) {
        callback(new Error('请输入正确的手机号码'));
      } else {
        callback();
      }
    };
    return {
      loading: false,
      goods: {},
      quantity: 1,
      // Tab切换
      activeTab: 'detail',
      // 评论相关
      commentList: [],
      commentCount: 0,
      avgStar: 5,
      commentLoading: false,
      commentForm: {
        star: 5,
        content: ''
      },
      commentRules: {
        star: [
          { required: true, message: '请选择评分', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入评价内容', trigger: 'blur' },
          { min: 5, max: 500, message: '评价内容需要5-500个字符', trigger: 'blur' }
        ]
      },
      // 订单弹窗
      orderDialogVisible: false,
      submitLoading: false,
      // 订单表单
      orderForm: {
        receiverName: '',
        receiverPhone: '',
        receiverAddress: ''
      },
      // 表单验证规则
      orderRules: {
        receiverName: [
          { required: true, message: '请输入收货人姓名', trigger: 'blur' }
        ],
        receiverPhone: [
          { required: true, validator: validatePhone, trigger: 'blur' }
        ],
        receiverAddress: [
          { required: true, message: '请输入收货地址', trigger: 'blur' }
        ]
      },
      // 下单成功弹窗
      successDialogVisible: false,
      orderNo: ''
    };
  },
  computed: {
    // 获取当前登录用户ID
    currentUserId() {
      return store.getters && store.getters.userId;
    }
  },
  created() {
    const goodsId = this.$route.params.id;
    if (goodsId) {
      this.getGoodsDetail(goodsId);
      this.getCommentList(goodsId);
      this.getCommentStatistics(goodsId);
    }
  },
  methods: {
    /** 获取商品详情 */
    getGoodsDetail(goodsId) {
      this.loading = true;
      getGoods(goodsId).then(response => {
        this.goods = response.data || {};
        this.loading = false;
        // 记录浏览行为（静默调用，不影响页面展示）
        this.recordView(goodsId);
      }).catch(() => {
        this.loading = false;
      });
    },
    /** 记录用户浏览行为 */
    recordView(goodsId) {
      // 静默记录，不处理响应结果
      recordViewLog(goodsId).catch(() => {
        // 记录失败不影响用户体验，静默处理
      });
    },
    /** 获取评论列表 */
    getCommentList(goodsId) {
      listCommentByGoodsId(goodsId).then(response => {
        this.commentList = response.data || [];
      });
    },
    /** 获取评论统计 */
    getCommentStatistics(goodsId) {
      getCommentStats(goodsId).then(response => {
        this.avgStar = response.avgStar || 5;
        this.commentCount = response.count || 0;
      });
    },
    /** 判断是否是自己的评论 */
    isMyComment(userId) {
      return this.currentUserId && this.currentUserId === userId;
    },
    /** 获取头像URL */
    getAvatarUrl(avatar) {
      if (!avatar) return '';
      if (avatar.startsWith('http')) return avatar;
      return process.env.VUE_APP_BASE_API + avatar;
    },
    /** 提交评论 */
    submitComment() {
      this.$refs['commentForm'].validate(valid => {
        if (valid) {
          this.commentLoading = true;
          const data = {
            goodsId: this.goods.goodsId,
            star: this.commentForm.star,
            content: this.commentForm.content
          };
          addComment(data).then(() => {
            this.$modal.msgSuccess('评价发表成功');
            this.commentForm = { star: 5, content: '' };
            this.$refs['commentForm'].resetFields();
            // 刷新评论列表和统计
            this.getCommentList(this.goods.goodsId);
            this.getCommentStatistics(this.goods.goodsId);
            this.commentLoading = false;
          }).catch(() => {
            this.commentLoading = false;
          });
        }
      });
    },
    /** 删除评论 */
    handleDeleteComment(item) {
      this.$modal.confirm('确定要删除这条评价吗？').then(() => {
        return deleteMyComment(item.commentId);
      }).then(() => {
        this.$modal.msgSuccess('删除成功');
        // 刷新评论列表和统计
        this.getCommentList(this.goods.goodsId);
        this.getCommentStatistics(this.goods.goodsId);
      }).catch(() => {});
    },
    /** 返回首页 */
    goBack() {
      this.$router.push({ path: '/mall/home' });
    },
    /** 获取图片完整URL */
    getImageUrl(url) {
      if (!url) {
        return 'https://via.placeholder.com/500x500?text=暂无图片';
      }
      if (url.startsWith('http')) {
        return url;
      }
      return process.env.VUE_APP_BASE_API + url;
    },
    /** 格式化价格 */
    formatPrice(price) {
      if (!price) return '0.00';
      return Number(price).toFixed(2);
    },
    /** 加入购物车 */
    addToCart() {
      this.$modal.msgSuccess(`已添加 ${this.quantity} 件商品到购物车`);
    },
    /** 立即购买 - 打开确认弹窗 */
    buyNow() {
      // 重置表单
      this.orderForm = {
        receiverName: '',
        receiverPhone: '',
        receiverAddress: ''
      };
      this.orderDialogVisible = true;
    },
    /** 提交订单 */
    submitOrder() {
      this.$refs['orderForm'].validate(valid => {
        if (valid) {
          this.submitLoading = true;
          const orderData = {
            goodsId: this.goods.goodsId,
            quantity: this.quantity,
            receiverName: this.orderForm.receiverName,
            receiverPhone: this.orderForm.receiverPhone,
            receiverAddress: this.orderForm.receiverAddress
          };
          createOrder(orderData).then(response => {
            this.submitLoading = false;
            this.orderDialogVisible = false;
            // 保存订单号并显示成功弹窗（订单号在 data 字段中）
            this.orderNo = response.data;
            this.successDialogVisible = true;
          }).catch(() => {
            this.submitLoading = false;
          });
        }
      });
    },
    /** 跳转到订单列表 */
    goToOrders() {
      this.successDialogVisible = false;
      this.$router.push({ path: '/mall/orders' });
    }
  }
};
</script>

<style scoped lang="scss">
.goods-detail {
  min-height: 100vh;
  background: #f5f5f5;
}

/* 返回导航 */
.back-nav {
  background: #fff;
  padding: 15px 30px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  position: sticky;
  top: 60px; /* 顶部导航栏高度 */
  z-index: 99;

  .el-button {
    font-size: 15px;
    color: #666;
    
    &:hover {
      color: #ff6b9d;
    }
  }
}

/* 详情容器 */
.detail-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
}

/* 商品主要信息 */
.goods-main {
  display: flex;
  gap: 40px;
  background: #fff;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.06);
  margin-bottom: 30px;

  @media (max-width: 900px) {
    flex-direction: column;
  }
}

/* 商品图片 */
.goods-gallery {
  flex: 0 0 450px;

  @media (max-width: 900px) {
    flex: none;
  }

  .main-image {
    width: 100%;
    border-radius: 12px;
    overflow: hidden;
    background: #f9f9f9;

    img {
      width: 100%;
      height: auto;
      display: block;
    }
  }
}

/* 商品信息 */
.goods-info {
  flex: 1;
  display: flex;
  flex-direction: column;

  .goods-tag {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    background: linear-gradient(135deg, #fff5f8, #fef0f5);
    color: #ff6b9d;
    padding: 8px 16px;
    border-radius: 20px;
    font-size: 13px;
    font-weight: 500;
    width: fit-content;
    margin-bottom: 15px;
  }

  .goods-title {
    font-size: 24px;
    font-weight: 600;
    color: #333;
    margin: 0 0 12px;
    line-height: 1.4;
  }

  .goods-intro {
    font-size: 14px;
    color: #888;
    margin: 0 0 20px;
    line-height: 1.6;
  }
}

/* 价格区域 */
.price-section {
  background: linear-gradient(135deg, #fff9f0, #fff5f5);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;

  .current-price {
    display: flex;
    align-items: baseline;
    gap: 4px;
    margin-bottom: 8px;

    .label {
      font-size: 14px;
      color: #999;
      margin-right: 10px;
    }

    .symbol {
      font-size: 18px;
      color: #ff4757;
      font-weight: 600;
    }

    .value {
      font-size: 36px;
      color: #ff4757;
      font-weight: 700;
      line-height: 1;
    }
  }

  .original-price {
    display: flex;
    align-items: center;
    gap: 10px;

    .label {
      font-size: 13px;
      color: #999;
    }

    .value {
      font-size: 14px;
      color: #bbb;
      text-decoration: line-through;
    }

    .discount {
      background: #ff4757;
      color: #fff;
      padding: 2px 8px;
      border-radius: 4px;
      font-size: 12px;
    }
  }
}

/* 商品属性 */
.goods-attrs {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px dashed #eee;

  .attr-item {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .attr-label {
    font-size: 14px;
    color: #999;
  }

  .attr-value {
    font-size: 14px;
    color: #333;
    font-weight: 500;

    &.low-stock {
      color: #ff4757;
    }

    &.category-path {
      color: #ff6b9d;
      display: flex;
      align-items: center;
      gap: 6px;
      
      i {
        font-size: 12px;
        color: #999;
      }
    }
  }
}

/* 数量选择 */
.quantity-section {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 25px;

  .quantity-label {
    font-size: 14px;
    color: #666;
  }
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 15px;
  margin-bottom: 25px;

  .el-button {
    flex: 1;
    height: 50px;
    font-size: 16px;
    border-radius: 25px;

    &.el-button--danger {
      background: linear-gradient(135deg, #ff6b9d, #ff8a65);
      border: none;

      &:hover {
        opacity: 0.9;
      }
    }

    &.el-button--warning {
      background: linear-gradient(135deg, #ffb347, #ffcc33);
      border: none;
      color: #fff;

      &:hover {
        opacity: 0.9;
      }
    }
  }
}

/* 服务保障 */
.service-tags {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;

  .service-item {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 13px;
    color: #52c41a;

    i {
      font-size: 14px;
    }
  }
}

/* 商品详情区域 */
.goods-detail-section {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0,0,0,0.06);

  .section-tabs {
    display: flex;
    border-bottom: 1px solid #f0f0f0;
    padding: 0 30px;

    .tab-item {
      padding: 18px 30px;
      font-size: 16px;
      color: #666;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 8px;
      border-bottom: 3px solid transparent;
      margin-bottom: -1px;
      transition: all 0.3s;

      &:hover {
        color: #ff6b9d;
      }

      &.active {
        color: #ff6b9d;
        font-weight: 600;
        border-bottom-color: #ff6b9d;
      }
    }
  }

  .detail-content {
    padding: 30px;
    min-height: 300px;
    line-height: 1.8;
    color: #555;

    ::v-deep img {
      max-width: 100%;
      height: auto;
    }
  }
}

/* 评论区域 */
.comment-section {
  padding: 30px;

  .comment-stats {
    display: flex;
    align-items: center;
    gap: 20px;
    padding: 20px;
    background: linear-gradient(135deg, #fff9f0, #fff5f5);
    border-radius: 12px;
    margin-bottom: 25px;

    .avg-star {
      display: flex;
      align-items: center;
      gap: 10px;

      .star-value {
        font-size: 36px;
        font-weight: 700;
        color: #ff6b9d;
      }
    }

    .comment-total {
      font-size: 14px;
      color: #999;
    }
  }

  .comment-form {
    background: #fafafa;
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 25px;

    h4 {
      margin: 0 0 15px;
      font-size: 16px;
      color: #333;
      display: flex;
      align-items: center;
      gap: 6px;

      i {
        color: #ff6b9d;
      }
    }

    .form-label {
      font-size: 14px;
      color: #666;
      margin-right: 10px;
    }

    .el-button--primary {
      background: linear-gradient(135deg, #ff6b9d, #ff8a65);
      border: none;
      border-radius: 20px;
      padding: 10px 30px;

      &:hover {
        opacity: 0.9;
      }
    }
  }

  .comment-list {
    .no-comment {
      padding: 40px 0;
    }

    .comment-item {
      display: flex;
      gap: 15px;
      padding: 20px 0;
      border-bottom: 1px solid #f5f5f5;

      &:last-child {
        border-bottom: none;
      }

      .comment-avatar {
        flex-shrink: 0;
      }

      .comment-content {
        flex: 1;

        .comment-header {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-bottom: 8px;

          .user-name {
            font-size: 15px;
            font-weight: 500;
            color: #333;
          }
        }

        .comment-text {
          font-size: 14px;
          color: #555;
          line-height: 1.6;
          margin: 0 0 10px;
        }

        .comment-footer {
          display: flex;
          align-items: center;
          justify-content: space-between;

          .comment-time {
            font-size: 12px;
            color: #999;
          }

          .delete-btn {
            color: #ff4757;
            padding: 0;

            &:hover {
              opacity: 0.8;
            }
          }
        }
      }
    }
  }
}

/* 订单确认弹窗 */
.order-dialog {
  .order-goods-info {
    display: flex;
    gap: 15px;
    padding: 10px;
    background: #f9f9f9;
    border-radius: 8px;

    .order-goods-img {
      width: 80px;
      height: 80px;
      object-fit: cover;
      border-radius: 8px;
    }

    .order-goods-detail {
      flex: 1;

      h4 {
        margin: 0 0 10px;
        font-size: 15px;
        color: #333;
        line-height: 1.4;
      }

      .order-price {
        margin: 0;
        font-size: 16px;
        color: #ff4757;
        font-weight: 600;
      }
    }
  }

  .order-total {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 10px;
    padding: 15px 0;
    font-size: 14px;
    color: #666;

    .total-price {
      font-size: 24px;
      color: #ff4757;
      font-weight: 700;
    }
  }
}

/* 下单成功弹窗 */
.success-dialog {
  .success-content {
    text-align: center;
    padding: 20px 0;

    .success-icon {
      font-size: 60px;
      color: #52c41a;
      margin-bottom: 15px;
    }

    h3 {
      margin: 0 0 15px;
      font-size: 20px;
      color: #333;
    }

    p {
      margin: 0 0 8px;
      font-size: 14px;
      color: #666;
    }

    .order-no {
      color: #ff6b9d;
      font-weight: 600;
      font-family: monospace;
      font-size: 16px;
    }

    .tips {
      font-size: 12px;
      color: #999;
      margin-top: 15px;
    }
  }
}
</style>
