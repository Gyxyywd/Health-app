<template>
  <div class="admin-health-tips">
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">添加健康小贴士</el-button>
    </div>

    <el-table :data="tips" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="category" label="分类" width="120" />
      <el-table-column prop="is_active" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.is_active ? 'success' : 'info'">
            {{ row.is_active ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-if="total > 0"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="loadTips"
      style="margin-top: 20px"
    />

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑健康小贴士' : '添加健康小贴士'"
      width="800px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input v-model="form.content" type="textarea" :rows="10" />
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="form.category" />
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="form.image_url" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.is_active" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { healthTipAPI } from '../../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const tips = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  title: '',
  content: '',
  category: '',
  image_url: '',
  is_active: true
})

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
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    title: '',
    content: '',
    category: '',
    image_url: '',
    is_active: true
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await healthTipAPI.update(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await healthTipAPI.create(form.value)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadTips()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个健康小贴士吗？', '提示', {
      type: 'warning'
    })
    await healthTipAPI.delete(id)
    ElMessage.success('删除成功')
    loadTips()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadTips()
})
</script>

<style scoped>
.admin-health-tips {
  padding: 20px;
}

.toolbar {
  margin-bottom: 20px;
}
</style>

