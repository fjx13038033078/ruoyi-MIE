<template>
  <div class="mall-layout">
    <!-- 顶部导航栏 -->
    <div class="mall-header">
      <div class="header-content">
        <div class="logo" @click="goHome">
          <span class="logo-icon">🍼</span>
          <span class="logo-text">母婴优选</span>
        </div>
        <div class="header-right">
          <el-dropdown class="user-dropdown" trigger="click">
            <div class="user-info">
              <img :src="avatar" class="user-avatar" />
              <span class="user-name">{{ name }}</span>
              <i class="el-icon-arrow-down"></i>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="goOrders">
                <i class="el-icon-s-order"></i> 我的订单
              </el-dropdown-item>
              <el-dropdown-item @click.native="goProfile">
                <i class="el-icon-user"></i> 个人中心
              </el-dropdown-item>
              <el-dropdown-item divided @click.native="logout">
                <i class="el-icon-switch-button"></i> 退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!-- 主内容区 -->
    <div class="mall-main">
      <router-view />
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'MallLayout',
  computed: {
    ...mapGetters(['avatar', 'name'])
  },
  methods: {
    goHome() {
      this.$router.push('/mall/home')
    },
    goOrders() {
      this.$router.push('/mall/orders')
    },
    goProfile() {
      this.$router.push('/user/profile')
    },
    async logout() {
      this.$confirm('确定退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/login'
        })
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.mall-layout {
  min-height: 100vh;
  background: #f5f5f5;
}

.mall-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: linear-gradient(135deg, #ff6b9d 0%, #ff8a65 100%);
  box-shadow: 0 2px 20px rgba(255, 107, 157, 0.3);
  z-index: 1000;

  .header-content {
    max-width: 1400px;
    margin: 0 auto;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
  }

  .logo {
    display: flex;
    align-items: center;
    gap: 10px;
    cursor: pointer;
    transition: opacity 0.3s;

    &:hover {
      opacity: 0.9;
    }

    .logo-icon {
      font-size: 28px;
    }

    .logo-text {
      font-size: 22px;
      font-weight: 700;
      color: #fff;
      letter-spacing: 2px;
    }
  }

  .header-right {
    display: flex;
    align-items: center;
  }

  .user-dropdown {
    cursor: pointer;
  }

  .user-info {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 8px 15px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 25px;
    transition: background 0.3s;

    &:hover {
      background: rgba(255, 255, 255, 0.3);
    }

    .user-avatar {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      border: 2px solid rgba(255, 255, 255, 0.5);
    }

    .user-name {
      color: #fff;
      font-size: 14px;
      font-weight: 500;
    }

    i {
      color: rgba(255, 255, 255, 0.8);
      font-size: 12px;
    }
  }
}

.mall-main {
  padding-top: 60px;
  min-height: 100vh;
}
</style>
