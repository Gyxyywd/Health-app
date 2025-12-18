<template>
  <div class="admin-vitamins">
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">添加维生素</el-button>
    </div>

    <el-table :data="vitamins" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="name_en" label="英文名" />
      <el-table-column prop="daily_requirement" label="每日需求量" width="150" />
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
      @current-change="loadVitamins"
      style="margin-top: 20px"
    />

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑维生素' : '添加维生素'"
      width="800px"
    >
      <el-form :model="form" label-width="120px">
        <el-form-item label="名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="英文名称">
          <el-input v-model="form.name_en" />
        </el-form-item>
        <el-form-item label="别名">
          <el-input v-model="form.alias" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="功能和作用" required>
          <el-input v-model="form.function" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="益处">
          <el-input v-model="form.benefits" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="食物来源">
          <el-input
            v-model="form.food_sources"
            type="textarea"
            :rows="4"
            placeholder="每行一个来源，或JSON格式数组"
          />
        </el-form-item>
        <el-form-item label="每日需求量">
          <el-input v-model="form.daily_requirement" />
        </el-form-item>
        <el-form-item label="缺乏症状">
          <el-input v-model="form.deficiency_symptoms" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="过量症状">
          <el-input v-model="form.overdose_symptoms" type="textarea" :rows="3" />
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
import { vitaminAPI } from '../../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const vitamins = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  name: '',
  name_en: '',
  alias: '',
  description: '',
  function: '',
  benefits: '',
  food_sources: '',
  daily_requirement: '',
  deficiency_symptoms: '',
  overdose_symptoms: '',
  image_url: '',
  is_active: true
})

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
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    name: '',
    name_en: '',
    alias: '',
    description: '',
    function: '',
    benefits: '',
    food_sources: '',
    daily_requirement: '',
    deficiency_symptoms: '',
    overdose_symptoms: '',
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
      await vitaminAPI.update(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await vitaminAPI.create(form.value)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadVitamins()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个维生素信息吗？', '提示', {
      type: 'warning'
    })
    await vitaminAPI.delete(id)
    ElMessage.success('删除成功')
    loadVitamins()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadVitamins()
})
</script>

<style scoped>
.admin-vitamins {
  padding: 20px;
}

.toolbar {
  margin-bottom: 20px;
}
</style>

