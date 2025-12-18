<template>
  <div class="recipes-page">
    <div class="page-header">
      <h2>健康食谱</h2>
      <el-input
        v-model="searchQuery"
        placeholder="搜索食谱..."
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
        v-for="recipe in recipes"
        :key="recipe.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <el-card
          class="recipe-card"
          shadow="hover"
          @click="goToDetail(recipe.id)"
        >
          <div class="recipe-image">
            <el-image
              v-if="recipe.image_url"
              :src="recipe.image_url"
              fit="cover"
            />
            <el-icon v-else class="image-placeholder"><Picture /></el-icon>
          </div>
          <div class="recipe-info">
            <h3>{{ recipe.title }}</h3>
            <p class="recipe-desc">{{ recipe.description || '暂无描述' }}</p>
            <div class="recipe-meta">
              <el-tag size="small" v-if="recipe.category">{{ recipe.category }}</el-tag>
              <el-tag size="small" type="info">{{ recipe.difficulty }}</el-tag>
              <span v-if="recipe.calories" class="calories">{{ recipe.calories }} 卡路里</span>
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
      @current-change="loadRecipes"
      style="margin-top: 20px; justify-content: center"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { recipeAPI } from '../api'
import { Search, Picture } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const recipes = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(12)
const searchQuery = ref('')

const loadRecipes = async () => {
  try {
    const params = {
      page: currentPage.value,
      page_size: pageSize.value
    }
    const response = await recipeAPI.getList(params)
    recipes.value = response.items || []
    total.value = response.total || 0
  } catch (error) {
    ElMessage.error('加载食谱失败')
    console.error(error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadRecipes()
}

const goToDetail = (id) => {
  router.push(`/recipes/${id}`)
}

onMounted(() => {
  loadRecipes()
})
</script>

<style scoped>
.recipes-page {
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

.recipe-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.recipe-card:hover {
  transform: translateY(-5px);
}

.recipe-image {
  width: 100%;
  height: 200px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.recipe-image .el-image {
  width: 100%;
  height: 100%;
}

.image-placeholder {
  font-size: 48px;
  color: #ccc;
}

.recipe-info {
  padding: 15px;
}

.recipe-info h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  color: #333;
}

.recipe-desc {
  color: #666;
  font-size: 14px;
  margin: 10px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.recipe-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.calories {
  margin-left: auto;
  color: #999;
  font-size: 12px;
}
</style>

