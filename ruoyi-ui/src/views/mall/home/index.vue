<template>
  <div class="mall-home">
    <!-- 顶部横幅 -->
    <div class="mall-banner">
      <div class="banner-content">
        <h1 class="banner-title">🍼 母婴优选商城</h1>
        <p class="banner-subtitle">为宝宝甄选全球好物，让爱从这里开始</p>
      </div>
    </div>

    <!-- 分类导航 -->
    <div class="category-nav">
      <div class="category-container">
        <div 
          class="category-item"
          :class="{ active: currentCategory === null }"
          @click="handleCategoryClick(null)"
        >
          <i class="el-icon-s-goods"></i>
          <span>全部商品</span>
        </div>
        <div 
          v-for="category in categoryList" 
          :key="category.categoryId"
          class="category-item"
          :class="{ active: currentCategory === category.categoryId }"
          @click="handleCategoryClick(category.categoryId)"
        >
          <i class="el-icon-present"></i>
          <span>{{ category.categoryName }}</span>
        </div>
      </div>
    </div>

    <!-- 商品展示区 -->
    <div class="goods-section">
      <div class="section-header">
        <h2 class="section-title">
          <i class="el-icon-star-on"></i>
          {{ currentCategoryName }}
        </h2>
        <span class="goods-count">共 {{ total }} 件商品</span>
      </div>

      <!-- 商品列表 -->
      <div class="goods-grid" v-loading="loading">
        <el-card 
          v-for="goods in goodsList" 
          :key="goods.goodsId"
          class="goods-card"
          :body-style="{ padding: '0' }"
          shadow="hover"
          @click.native="goToDetail(goods.goodsId)"
        >
          <!-- 商品图片 -->
          <div class="goods-image-wrapper">
            <img 
              :src="getImageUrl(goods.goodsCover)" 
              :alt="goods.goodsName"
              class="goods-image"
            />
            <div class="goods-tag" v-if="goods.ageStage">
              {{ goods.ageStage }}
            </div>
          </div>
          
          <!-- 商品信息 -->
          <div class="goods-info">
            <h3 class="goods-name">{{ goods.goodsName }}</h3>
            <p class="goods-intro">{{ goods.goodsIntro || '暂无简介' }}</p>
            <div class="goods-bottom">
              <div class="goods-price">
                <span class="price-symbol">¥</span>
                <span class="price-value">{{ formatPrice(goods.price) }}</span>
                <span class="original-price" v-if="goods.originalPrice > goods.price">
                  ¥{{ formatPrice(goods.originalPrice) }}
                </span>
              </div>
              <div class="goods-sales">
                <i class="el-icon-shopping-bag-1"></i>
                已售 {{ goods.sales || 0 }}
              </div>
            </div>
          </div>
        </el-card>

        <!-- 空状态 -->
        <el-empty 
          v-if="!loading && goodsList.length === 0" 
          description="暂无商品"
          class="empty-state"
        />
      </div>

      <!-- 分页 -->
      <div class="pagination-wrapper" v-if="total > 0">
        <el-pagination
          background
          layout="prev, pager, next, jumper"
          :total="total"
          :page-size="queryParams.pageSize"
          :current-page.sync="queryParams.pageNum"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 页脚 -->
    <div class="mall-footer">
      <p>© 2026 母婴优选商城 - 用心呵护每一位宝宝</p>
    </div>
  </div>
</template>

<script>
import { listGoods } from "@/api/mall/goods";
import { selectAllCategory } from "@/api/mall/category";

export default {
  name: "MallHome",
  data() {
    return {
      // 加载状态
      loading: false,
      // 商品列表
      goodsList: [],
      // 分类列表
      categoryList: [],
      // 当前选中分类
      currentCategory: null,
      // 总条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 12,
        categoryId: undefined,
        status: '0' // 只查询上架商品
      }
    };
  },
  computed: {
    currentCategoryName() {
      if (this.currentCategory === null) {
        return '全部商品';
      }
      const category = this.categoryList.find(c => c.categoryId === this.currentCategory);
      return category ? category.categoryName : '全部商品';
    }
  },
  created() {
    this.getCategoryList();
    this.getGoodsList();
  },
  methods: {
    /** 获取分类列表（只获取一级分类） */
    getCategoryList() {
      selectAllCategory().then(response => {
        // 过滤出一级分类（parentId 为 0 或 null）
        this.categoryList = response.data.filter(item => 
          item.parentId === 0 || item.parentId === null
        );
      });
    },
    /** 获取商品列表 */
    getGoodsList() {
      this.loading = true;
      listGoods(this.queryParams).then(response => {
        this.goodsList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    /** 点击分类 */
    handleCategoryClick(categoryId) {
      this.currentCategory = categoryId;
      this.queryParams.categoryId = categoryId;
      this.queryParams.pageNum = 1;
      this.getGoodsList();
    },
    /** 分页切换 */
    handlePageChange(page) {
      this.queryParams.pageNum = page;
      this.getGoodsList();
      // 滚动到商品区域顶部
      window.scrollTo({ top: 300, behavior: 'smooth' });
    },
    /** 跳转到商品详情 */
    goToDetail(goodsId) {
      this.$router.push({ path: `/mall/goods/detail/${goodsId}` });
    },
    /** 获取图片完整URL */
    getImageUrl(url) {
      if (!url) {
        return 'https://via.placeholder.com/300x300?text=暂无图片';
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
    }
  }
};
</script>

<style scoped lang="scss">
.mall-home {
  min-height: 100vh;
  background: linear-gradient(135deg, #fef9f3 0%, #fdf2f8 50%, #f0f9ff 100%);
}

/* 顶部横幅 */
.mall-banner {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 50%, #fecfef 100%);
  padding: 60px 20px;
  text-align: center;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(255,255,255,0.3) 0%, transparent 70%);
    animation: shimmer 3s ease-in-out infinite;
  }

  .banner-content {
    position: relative;
    z-index: 1;
  }

  .banner-title {
    font-size: 42px;
    font-weight: 700;
    color: #fff;
    margin: 0 0 15px;
    text-shadow: 2px 2px 4px rgba(0,0,0,0.1);
    letter-spacing: 2px;
  }

  .banner-subtitle {
    font-size: 18px;
    color: rgba(255,255,255,0.9);
    margin: 0;
    font-weight: 300;
  }
}

@keyframes shimmer {
  0%, 100% { transform: rotate(0deg); }
  50% { transform: rotate(180deg); }
}

/* 分类导航 */
.category-nav {
  background: #fff;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  position: sticky;
  top: 60px; /* 顶部导航栏高度 */
  z-index: 100;

  .category-container {
    max-width: 1400px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    padding: 0 20px;
    overflow-x: auto;
    
    &::-webkit-scrollbar {
      height: 0;
    }
  }

  .category-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 20px 28px;
    cursor: pointer;
    white-space: nowrap;
    font-size: 15px;
    color: #666;
    transition: all 0.3s ease;
    border-bottom: 3px solid transparent;
    
    i {
      font-size: 18px;
    }

    &:hover {
      color: #ff6b9d;
      background: #fff5f8;
    }

    &.active {
      color: #ff6b9d;
      font-weight: 600;
      border-bottom-color: #ff6b9d;
      background: linear-gradient(to bottom, #fff5f8, #fff);
    }
  }
}

/* 商品展示区 */
.goods-section {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 20px;

  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 30px;
    padding-bottom: 15px;
    border-bottom: 2px solid #ffeef5;
  }

  .section-title {
    font-size: 24px;
    font-weight: 600;
    color: #333;
    margin: 0;
    display: flex;
    align-items: center;
    gap: 10px;

    i {
      color: #ffb347;
    }
  }

  .goods-count {
    font-size: 14px;
    color: #999;
  }
}

/* 商品网格 */
.goods-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  min-height: 400px;

  @media (max-width: 1200px) {
    grid-template-columns: repeat(3, 1fr);
  }

  @media (max-width: 900px) {
    grid-template-columns: repeat(2, 1fr);
  }

  @media (max-width: 500px) {
    grid-template-columns: 1fr;
  }
}

/* 商品卡片 */
.goods-card {
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  background: #fff;

  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 20px 40px rgba(255, 107, 157, 0.15);

    .goods-image {
      transform: scale(1.05);
    }
  }

  .goods-image-wrapper {
    position: relative;
    padding-top: 100%;
    overflow: hidden;
    background: #f9f9f9;
  }

  .goods-image {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.5s ease;
  }

  .goods-tag {
    position: absolute;
    top: 12px;
    left: 12px;
    background: linear-gradient(135deg, #ff6b9d, #ff8a65);
    color: #fff;
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
  }

  .goods-info {
    padding: 16px;
  }

  .goods-name {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin: 0 0 8px;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    height: 44px;
  }

  .goods-intro {
    font-size: 13px;
    color: #999;
    margin: 0 0 12px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .goods-bottom {
    display: flex;
    align-items: flex-end;
    justify-content: space-between;
  }

  .goods-price {
    display: flex;
    align-items: baseline;
    gap: 4px;

    .price-symbol {
      font-size: 14px;
      color: #ff4757;
      font-weight: 600;
    }

    .price-value {
      font-size: 24px;
      color: #ff4757;
      font-weight: 700;
      line-height: 1;
    }

    .original-price {
      font-size: 12px;
      color: #bbb;
      text-decoration: line-through;
      margin-left: 6px;
    }
  }

  .goods-sales {
    font-size: 12px;
    color: #999;
    display: flex;
    align-items: center;
    gap: 4px;

    i {
      font-size: 14px;
    }
  }
}

/* 空状态 */
.empty-state {
  grid-column: 1 / -1;
  padding: 60px 0;
}

/* 分页 */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 50px;
  padding-bottom: 30px;

  ::v-deep .el-pagination {
    .btn-prev, .btn-next, .el-pager li {
      background: #fff;
      border-radius: 8px;
      margin: 0 4px;
      
      &:hover {
        color: #ff6b9d;
      }
      
      &.active {
        background: linear-gradient(135deg, #ff6b9d, #ff8a65);
        color: #fff;
      }
    }
  }
}

/* 页脚 */
.mall-footer {
  background: #fff;
  padding: 30px 20px;
  text-align: center;
  border-top: 1px solid #f0f0f0;

  p {
    margin: 0;
    color: #999;
    font-size: 14px;
  }
}
</style>
