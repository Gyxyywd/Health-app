<template>
  <div class="health-tip-detail" v-if="tip">
    <el-card>
      <template #header>
        <div class="detail-header">
          <h2>{{ tip.title }}</h2>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <div class="detail-content">
        <div class="tip-image" v-if="tip.image_url">
          <el-image :src="tip.image_url" fit="cover" />
        </div>

        <div class="meta-info">
          <el-tag v-if="tip.category">{{ tip.category }}</el-tag>
          <span class="date">发布时间：{{ formatDate(tip.created_at) }}</span>
        </div>

        <div class="content">
          <p v-html="formatContent(tip.content)"></p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { healthTipAPI } from '../api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const tip = ref(null)

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const formatContent = (content) => {
  if (!content) return ''
  return content.replace(/\n/g, '<br>')
}

const loadTip = async () => {
  try {
    const id = route.params.id
    tip.value = await healthTipAPI.getDetail(id)
  } catch (error) {
    ElMessage.error('加载健康小贴士详情失败')
    console.error(error)
  }
}

onMounted(() => {
  loadTip()
})
</script>

<style scoped>
.health-tip-detail {
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

.tip-image {
  width: 100%;
  max-height: 400px;
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
}

.tip-image .el-image {
  width: 100%;
  height: 100%;
}

.meta-info {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.date {
  color: #999;
  font-size: 14px;
}

.content {
  line-height: 2;
  font-size: 16px;
  color: #333;
}

.content p {
  margin: 0;
}
</style>

