<template>
  <div class="vitamin-detail" v-if="vitamin">
    <el-card>
      <template #header>
        <div class="detail-header">
          <div>
            <h2>{{ vitamin.name }}</h2>
            <p v-if="vitamin.name_en" class="name-en">{{ vitamin.name_en }}</p>
            <p v-if="vitamin.alias" class="alias">别名：{{ vitamin.alias }}</p>
          </div>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <div class="detail-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="每日需求量" v-if="vitamin.daily_requirement">
            {{ vitamin.daily_requirement }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="section" v-if="vitamin.description">
          <h3>描述</h3>
          <p>{{ vitamin.description }}</p>
        </div>

        <div class="section">
          <h3>功能和作用</h3>
          <p>{{ vitamin.function }}</p>
        </div>

        <div class="section" v-if="vitamin.benefits">
          <h3>益处</h3>
          <p>{{ vitamin.benefits }}</p>
        </div>

        <div class="section" v-if="vitamin.food_sources">
          <h3>食物来源</h3>
          <ul class="food-sources">
            <li v-for="(source, index) in foodSourcesList" :key="index">
              {{ source }}
            </li>
          </ul>
        </div>

        <el-row :gutter="20">
          <el-col :xs="24" :md="12" v-if="vitamin.deficiency_symptoms">
            <div class="section warning">
              <h3>缺乏症状</h3>
              <p>{{ vitamin.deficiency_symptoms }}</p>
            </div>
          </el-col>
          <el-col :xs="24" :md="12" v-if="vitamin.overdose_symptoms">
            <div class="section danger">
              <h3>过量症状</h3>
              <p>{{ vitamin.overdose_symptoms }}</p>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { vitaminAPI } from '../api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const vitamin = ref(null)

const foodSourcesList = computed(() => {
  if (!vitamin.value?.food_sources) return []
  try {
    return JSON.parse(vitamin.value.food_sources)
  } catch {
    return vitamin.value.food_sources.split('\n').filter(Boolean)
  }
})

const loadVitamin = async () => {
  try {
    const id = route.params.id
    vitamin.value = await vitaminAPI.getDetail(id)
  } catch (error) {
    ElMessage.error('加载维生素详情失败')
    console.error(error)
  }
}

onMounted(() => {
  loadVitamin()
})
</script>

<style scoped>
.vitamin-detail {
  max-width: 1200px;
  margin: 0 auto;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.detail-header h2 {
  margin: 0 0 10px 0;
}

.name-en {
  margin: 5px 0;
  color: #666;
  font-size: 16px;
}

.alias {
  margin: 5px 0 0 0;
  color: #999;
  font-size: 14px;
}

.detail-content {
  padding: 20px 0;
}

.section {
  margin: 30px 0;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
}

.section.warning {
  background: #fff7e6;
  border-left: 4px solid #faad14;
}

.section.danger {
  background: #fff1f0;
  border-left: 4px solid #ff4d4f;
}

.section h3 {
  margin: 0 0 15px 0;
  font-size: 18px;
  color: #333;
}

.section p {
  margin: 0;
  line-height: 1.8;
  color: #666;
}

.food-sources {
  padding-left: 20px;
  line-height: 2;
}

.food-sources li {
  margin-bottom: 8px;
  color: #666;
}
</style>

