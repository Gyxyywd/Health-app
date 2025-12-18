<template>
  <div class="admin-page">
    <h2>后台管理</h2>
    
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="健康食谱" name="recipes">
        <AdminRecipes />
      </el-tab-pane>
      <el-tab-pane label="维生素" name="vitamins">
        <AdminVitamins />
      </el-tab-pane>
      <el-tab-pane label="健康小贴士" name="health-tips">
        <AdminHealthTips />
      </el-tab-pane>
      <el-tab-pane label="反馈管理" name="feedbacks">
        <AdminFeedbacks />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import AdminRecipes from '../components/admin/AdminRecipes.vue'
import AdminVitamins from '../components/admin/AdminVitamins.vue'
import AdminHealthTips from '../components/admin/AdminHealthTips.vue'
import AdminFeedbacks from '../components/admin/AdminFeedbacks.vue'

const activeTab = ref('recipes')

const handleTabChange = () => {
  // Tab切换时的处理
}
</script>

<style scoped>
.admin-page {
  padding: 20px 0;
}

.admin-page h2 {
  margin-bottom: 20px;
  color: #333;
}
</style>

