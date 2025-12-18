<template>
  <div class="recipe-detail" v-if="recipe">
    <el-card>
      <template #header>
        <div class="detail-header">
          <h2>{{ recipe.title }}</h2>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <div class="detail-content">
        <el-row :gutter="20">
          <el-col :xs="24" :md="12">
            <div class="recipe-image">
              <el-image
                v-if="recipe.image_url"
                :src="recipe.image_url"
                fit="cover"
              />
              <div v-else class="image-placeholder">
                <el-icon><Picture /></el-icon>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :md="12">
            <div class="recipe-info">
              <p class="description">{{ recipe.description || '暂无描述' }}</p>
              
              <div class="meta-info">
                <el-descriptions :column="1" border>
                  <el-descriptions-item label="分类">{{ recipe.category || '未分类' }}</el-descriptions-item>
                  <el-descriptions-item label="难度">{{ recipe.difficulty }}</el-descriptions-item>
                  <el-descriptions-item label="准备时间">{{ recipe.prep_time || 0 }} 分钟</el-descriptions-item>
                  <el-descriptions-item label="烹饪时间">{{ recipe.cook_time || 0 }} 分钟</el-descriptions-item>
                  <el-descriptions-item label="份数">{{ recipe.servings }} 份</el-descriptions-item>
                </el-descriptions>
              </div>

              <div class="nutrition" v-if="recipe.calories">
                <h3>营养信息</h3>
                <el-row :gutter="10">
                  <el-col :span="6">
                    <div class="nutrition-item">
                      <div class="value">{{ recipe.calories }}</div>
                      <div class="label">卡路里</div>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="nutrition-item">
                      <div class="value">{{ recipe.protein || 0 }}g</div>
                      <div class="label">蛋白质</div>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="nutrition-item">
                      <div class="value">{{ recipe.carbs || 0 }}g</div>
                      <div class="label">碳水</div>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="nutrition-item">
                      <div class="value">{{ recipe.fat || 0 }}g</div>
                      <div class="label">脂肪</div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-divider />

        <el-row :gutter="20">
          <el-col :xs="24" :md="12">
            <h3>食材清单</h3>
            <ul class="ingredients-list">
              <li v-for="(ingredient, index) in ingredientsList" :key="index">
                {{ ingredient }}
              </li>
            </ul>
          </el-col>
          <el-col :xs="24" :md="12">
            <h3>制作步骤</h3>
            <ol class="steps-list">
              <li v-for="(step, index) in stepsList" :key="index">
                {{ step }}
              </li>
            </ol>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { recipeAPI } from '../api'
import { Picture } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const recipe = ref(null)

const ingredientsList = computed(() => {
  if (!recipe.value?.ingredients) return []
  try {
    return JSON.parse(recipe.value.ingredients)
  } catch {
    return recipe.value.ingredients.split('\n').filter(Boolean)
  }
})

const stepsList = computed(() => {
  if (!recipe.value?.steps) return []
  try {
    return JSON.parse(recipe.value.steps)
  } catch {
    return recipe.value.steps.split('\n').filter(Boolean)
  }
})

const loadRecipe = async () => {
  try {
    const id = route.params.id
    recipe.value = await recipeAPI.getDetail(id)
  } catch (error) {
    ElMessage.error('加载食谱详情失败')
    console.error(error)
  }
}

onMounted(() => {
  loadRecipe()
})
</script>

<style scoped>
.recipe-detail {
  max-width: 1200px;
  margin: 0 auto;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-header h2 {
  margin: 0;
}

.detail-content {
  padding: 20px 0;
}

.recipe-image {
  width: 100%;
  height: 400px;
  background: #f5f5f5;
  border-radius: 8px;
  overflow: hidden;
}

.recipe-image .el-image {
  width: 100%;
  height: 100%;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 64px;
  color: #ccc;
}

.description {
  font-size: 16px;
  color: #666;
  line-height: 1.8;
  margin-bottom: 20px;
}

.meta-info {
  margin: 20px 0;
}

.nutrition {
  margin-top: 20px;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
}

.nutrition h3 {
  margin: 0 0 15px 0;
  font-size: 18px;
}

.nutrition-item {
  text-align: center;
  padding: 10px;
  background: white;
  border-radius: 4px;
}

.nutrition-item .value {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.nutrition-item .label {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.ingredients-list,
.steps-list {
  padding-left: 20px;
  line-height: 2;
}

.ingredients-list li,
.steps-list li {
  margin-bottom: 10px;
  color: #666;
}
</style>

