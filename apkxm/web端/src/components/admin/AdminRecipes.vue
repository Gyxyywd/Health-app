<template>
  <div class="admin-recipes">
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">添加食谱</el-button>
    </div>

    <el-table :data="recipes" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="category" label="分类" width="100" />
      <el-table-column prop="difficulty" label="难度" width="100" />
      <el-table-column prop="calories" label="卡路里" width="100" />
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
      @current-change="loadRecipes"
      style="margin-top: 20px"
    />

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑食谱' : '添加食谱'"
      width="800px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="食材" required>
          <el-input
            v-model="form.ingredients"
            type="textarea"
            :rows="5"
            placeholder="每行一个食材，或JSON格式数组"
          />
        </el-form-item>
        <el-form-item label="步骤" required>
          <el-input
            v-model="form.steps"
            type="textarea"
            :rows="5"
            placeholder="每行一个步骤，或JSON格式数组"
          />
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="form.image_url" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类">
              <el-input v-model="form.category" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="难度">
              <el-select v-model="form.difficulty">
                <el-option label="简单" value="简单" />
                <el-option label="中等" value="中等" />
                <el-option label="困难" value="困难" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="卡路里">
              <el-input-number v-model="form.calories" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="蛋白质(g)">
              <el-input-number v-model="form.protein" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="碳水(g)">
              <el-input-number v-model="form.carbs" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="脂肪(g)">
              <el-input-number v-model="form.fat" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="准备时间(分钟)">
              <el-input-number v-model="form.prep_time" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="烹饪时间(分钟)">
              <el-input-number v-model="form.cook_time" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
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
import { recipeAPI } from '../../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const recipes = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  title: '',
  description: '',
  ingredients: '',
  steps: '',
  image_url: '',
  category: '',
  difficulty: '简单',
  calories: null,
  protein: null,
  carbs: null,
  fat: null,
  prep_time: null,
  cook_time: null,
  servings: 1,
  is_active: true
})

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
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    title: '',
    description: '',
    ingredients: '',
    steps: '',
    image_url: '',
    category: '',
    difficulty: '简单',
    calories: null,
    protein: null,
    carbs: null,
    fat: null,
    prep_time: null,
    cook_time: null,
    servings: 1,
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
      await recipeAPI.update(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await recipeAPI.create(form.value)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadRecipes()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个食谱吗？', '提示', {
      type: 'warning'
    })
    await recipeAPI.delete(id)
    ElMessage.success('删除成功')
    loadRecipes()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadRecipes()
})
</script>

<style scoped>
.admin-recipes {
  padding: 20px;
}

.toolbar {
  margin-bottom: 20px;
}
</style>

