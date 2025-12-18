<template>
  <el-container class="app-container">
    <el-header class="app-header">
      <div class="header-content">
        <h1>健康管理应用</h1>
        <el-menu
          :default-active="activeIndex"
          mode="horizontal"
          router
          class="header-menu"
        >
          <el-menu-item index="/recipes">健康食谱</el-menu-item>
          <el-menu-item index="/vitamins">维生素</el-menu-item>
          <el-menu-item index="/health-tips">健康小贴士</el-menu-item>
          <el-menu-item index="/admin">后台管理</el-menu-item>
          <el-menu-item index="/analytics">访问分析</el-menu-item>
          <el-menu-item v-if="!isAuthed" index="/login">登录</el-menu-item>
          <el-sub-menu v-else index="user">
            <template #title>已登录</template>
            <el-menu-item @click="logout">退出</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
    </el-header>
    <el-main class="app-main">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const activeIndex = computed(() => route.path)
const isAuthed = computed(() => !!localStorage.getItem('token'))
const logout = () => {
  localStorage.removeItem('token')
  router.replace('/login')
}
</script>

<style scoped>
.app-container {
  min-height: 100vh;
}

.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 20px;
}

.header-content h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.header-menu {
  background: transparent;
  border: none;
}

.header-menu .el-menu-item {
  color: white;
  border-bottom: 2px solid transparent;
}

.header-menu .el-menu-item:hover,
.header-menu .el-menu-item.is-active {
  color: white;
  background: rgba(255, 255, 255, 0.1);
  border-bottom-color: white;
}

.app-main {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}
</style>

