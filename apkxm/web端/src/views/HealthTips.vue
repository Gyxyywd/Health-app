<template>
  <div class="health-tips-page">
    <div class="page-header">
      <h2>健康小贴士</h2>
      <el-input
        v-model="searchQuery"
        placeholder="搜索小贴士..."
        style="width: 300px"
        clearable
        @input="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>

    <el-row :gutter="20">
      <el-col
        v-for="tip in tips"
        :key="tip.id"
        :xs="24"
        :sm="12"
        :md="8"
      >
        <el-card
          class="tip-card"
          shadow="hover"
          @click="goToDetail(tip.id)"
        >
          <div class="tip-image" v-if="tip.image_url">
            <el-image :src="tip.image_url" fit="cover" />
          </div>
          <div class="tip-content">
            <h3>{{ tip.title }}</h3>
            <p>{{ getShortContent(tip.content) }}</p>
            <div class="tip-meta">
              <el-tag size="small" v-if="tip.category">{{ tip.category }}</el-tag>
              <span class="date">{{ formatDate(tip.created_at) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-pagination
      v-if="total > 0"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="loadTips"
      style="margin-top: 20px; justify-content: center"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { healthTipAPI } from '../api'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const tips = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(12)
const searchQuery = ref('')

const getShortContent = (text) => {
  if (!text) return '暂无内容'
  return text.length > 150 ? text.substring(0, 150) + '...' : text
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const loadTips = async () => {
  try {
    const params = {
      page: currentPage.value,
      page_size: pageSize.value
    }
    const response = await healthTipAPI.getList(params)
    tips.value = response.items || []
    total.value = response.total || 0
  } catch (error) {
    ElMessage.error('加载健康小贴士失败')
    console.error(error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadTips()
}

const goToDetail = (id) => {
  router.push(`/health-tips/${id}`)
}

onMounted(() => {
  loadTips()
})
</script>

<style scoped>
.health-tips-page {
  padding: 20px 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #333;
}

.tip-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
  height: 100%;
}

.tip-card:hover {
  transform: translateY(-5px);
}

.tip-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  margin-bottom: 15px;
}

.tip-image .el-image {
  width: 100%;
  height: 100%;
}

.tip-content h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  color: #333;
}

.tip-content p {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin: 10px 0;
}

.tip-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}

.date {
  color: #999;
  font-size: 12px;
}
</style>

