<template>
  <div class="vitamins-page">
    <div class="page-header">
      <h2>维生素知识</h2>
      <el-input
        v-model="searchQuery"
        placeholder="搜索维生素..."
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
        v-for="vitamin in vitamins"
        :key="vitamin.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <el-card
          class="vitamin-card"
          shadow="hover"
          @click="goToDetail(vitamin.id)"
        >
          <div class="vitamin-header">
            <h3>{{ vitamin.name }}</h3>
            <p v-if="vitamin.name_en" class="name-en">{{ vitamin.name_en }}</p>
          </div>
          <p class="vitamin-desc">{{ getShortDesc(vitamin.function) }}</p>
          <div class="vitamin-footer">
            <el-tag size="small" v-if="vitamin.daily_requirement">
              {{ vitamin.daily_requirement }}
            </el-tag>
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
      @current-change="loadVitamins"
      style="margin-top: 20px; justify-content: center"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { vitaminAPI } from '../api'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const vitamins = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(12)
const searchQuery = ref('')

const getShortDesc = (text) => {
  if (!text) return '暂无描述'
  return text.length > 100 ? text.substring(0, 100) + '...' : text
}

const loadVitamins = async () => {
  try {
    const params = {
      page: currentPage.value,
      page_size: pageSize.value
    }
    const response = await vitaminAPI.getList(params)
    vitamins.value = response.items || []
    total.value = response.total || 0
  } catch (error) {
    ElMessage.error('加载维生素信息失败')
    console.error(error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadVitamins()
}

const goToDetail = (id) => {
  router.push(`/vitamins/${id}`)
}

onMounted(() => {
  loadVitamins()
})
</script>

<style scoped>
.vitamins-page {
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

.vitamin-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
  height: 100%;
}

.vitamin-card:hover {
  transform: translateY(-5px);
}

.vitamin-header h3 {
  margin: 0 0 5px 0;
  font-size: 20px;
  color: #333;
}

.name-en {
  margin: 0 0 10px 0;
  color: #999;
  font-size: 14px;
}

.vitamin-desc {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin: 15px 0;
  min-height: 60px;
}

.vitamin-footer {
  margin-top: 15px;
}
</style>

