<template>
  <div class="admin-feedbacks">
    <div class="toolbar">
      <el-input v-model="query" placeholder="搜索标题..." style="width:260px" clearable @input="loadList" />
      <el-select v-model="status" placeholder="状态" style="width:140px" @change="loadList">
        <el-option label="全部" :value="''" />
        <el-option label="未解决" value="open" />
        <el-option label="已解决" value="resolved" />
      </el-select>
      <el-button @click="loadList">刷新</el-button>
    </div>

    <el-collapse v-model="activeNames">
      <el-collapse-item v-for="item in items" :key="item.id" :name="item.id">
        <template #title>
          <div class="item-title">
            <span class="title">{{ item.title }}</span>
            <el-tag :type="item.status === 'resolved' ? 'success' : 'warning'" size="small">{{ item.status }}</el-tag>
            <span class="date">{{ formatDate(item.created_at) }}</span>
          </div>
        </template>

        <div class="content">
          <p class="text">{{ item.content }}</p>
          <p v-if="item.contact" class="contact">联系方式：{{ item.contact }}</p>

          <div class="replies">
            <h4>回复</h4>
            <el-empty v-if="replies[item.id]?.length === 0" description="暂无回复" />
            <el-timeline v-else>
              <el-timeline-item v-for="r in replies[item.id]" :key="r.id" :timestamp="formatDateTime(r.created_at)">
                {{ r.content }}
              </el-timeline-item>
            </el-timeline>
          </div>

          <div class="reply-box">
            <el-input v-model="replyContent[item.id]" type="textarea" :rows="2" placeholder="输入回复..." />
            <div class="actions">
              <el-button type="primary" size="small" @click="submitReply(item.id)">回复</el-button>
              <el-button size="small" @click="markResolved(item)">标记已解决</el-button>
            </div>
          </div>
        </div>
      </el-collapse-item>
    </el-collapse>

    <el-pagination
      v-if="total > 0"
      v-model:current-page="page"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="loadList"
      style="margin-top: 16px"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { feedbackAPI } from '../../api'
import { ElMessage } from 'element-plus'

const items = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const query = ref('')
const status = ref('')
const activeNames = ref([])
const replies = reactive({})
const replyContent = reactive({})

const loadList = async () => {
  try {
    const params = { page: page.value, page_size: pageSize.value }
    if (status.value) params.status = status.value
    const res = await feedbackAPI.getList(params)
    items.value = res.items
    total.value = res.total
    // 预加载每条的回复
    for (const it of items.value) {
      replies[it.id] = await feedbackAPI.getReplies(it.id)
      replyContent[it.id] = ''
    }
  } catch (e) {
    ElMessage.error('加载失败')
  }
}

const submitReply = async (fid) => {
  try {
    const content = (replyContent[fid] || '').trim()
    if (!content) return ElMessage.warning('请输入回复内容')
    await feedbackAPI.createReply(fid, { content })
    replyContent[fid] = ''
    replies[fid] = await feedbackAPI.getReplies(fid)
    ElMessage.success('回复成功')
  } catch (e) {
    ElMessage.error('回复失败')
  }
}

const markResolved = async (item) => {
  try {
    await feedbackAPI.update(item.id, { status: 'resolved' })
    item.status = 'resolved'
    ElMessage.success('已标记为已解决')
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const formatDate = (s) => (s ? new Date(s).toLocaleDateString('zh-CN') : '')
const formatDateTime = (s) => (s ? new Date(s).toLocaleString('zh-CN') : '')

onMounted(loadList)
</script>

<style scoped>
.admin-feedbacks { padding: 12px 0; }
.toolbar { display: flex; gap: 10px; margin-bottom: 12px; }
.item-title { display: flex; align-items: center; gap: 10px; }
.item-title .title { font-weight: 600; }
.item-title .date { color: #999; margin-left: auto; font-size: 12px; }
.content { padding: 8px 4px; }
.text { white-space: pre-wrap; line-height: 1.7; }
.contact { color: #666; margin-top: 6px; }
.replies { margin-top: 14px; }
.reply-box { margin-top: 10px; }
.actions { margin-top: 8px; display: flex; gap: 8px; }
</style>


