<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2>登录</h2>
      <el-form :model="form" label-width="80px" @keyup.enter.native="handleLogin">
        <el-form-item label="用户名">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
  </template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { authAPI } from '../api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const form = ref({ username: '', password: '' })
const loading = ref(false)

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    return ElMessage.warning('请输入用户名和密码')
  }
  loading.value = true
  try {
    const res = await authAPI.login(form.value.username, form.value.password)
    localStorage.setItem('token', res.access_token)
    ElMessage.success('登录成功')
    const redirect = route.query.redirect || '/admin'
    router.replace(redirect)
  } catch (e) {
    ElMessage.error('登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
}
.login-card {
  width: 420px;
}
.login-card h2 {
  margin: 0 0 20px 0;
  text-align: center;
}
</style>


