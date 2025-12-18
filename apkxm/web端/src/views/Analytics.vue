<template>
  <div class="analytics-page">
    <div class="toolbar">
      <el-select v-model="days" style="width: 140px" @change="loadAll">
        <el-option :value="7" label="近7天" />
        <el-option :value="14" label="近14天" />
        <el-option :value="30" label="近30天" />
      </el-select>
      <el-button @click="loadAll">刷新</el-button>
    </div>

    <el-row :gutter="20">
      <el-col :xs="24" :md="16">
        <el-card>
          <template #header>每日访问趋势</template>
          <div ref="trendRef" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="8">
        <el-card>
          <template #header>Top 路径</template>
          <el-table :data="topPaths" size="small" style="width: 100%">
            <el-table-column prop="path" label="路径" />
            <el-table-column prop="count" label="次数" width="90" />
          </el-table>
        </el-card>
        <el-card style="margin-top: 20px">
          <template #header>活跃用户（去重）</template>
          <div ref="activeRef" class="chart small"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { analyticsAPI } from '../api'
import { ElMessage } from 'element-plus'

const days = ref(7)
const trendRef = ref(null)
const activeRef = ref(null)
let trendChart, activeChart

const topPaths = ref([])

const loadAll = async () => {
  try {
    // 分析趋势
    const pathsRes = await analyticsAPI.paths(days.value)
    const dates = Object.keys(pathsRes.data || {})
    const dateTotals = dates.map(d => (pathsRes.data[d] || []).reduce((sum, i) => sum + i.count, 0))
    renderTrend(dates, dateTotals)

    // Top路径
    topPaths.value = await analyticsAPI.top(10)

    // 活跃用户
    const active = await analyticsAPI.activeUsers(days.value)
    renderActive(active.map(i => i.date), active.map(i => i.active_users))
  } catch (e) {
    ElMessage.error('加载分析数据失败')
  }
}

const renderTrend = (labels, values) => {
  if (!trendChart) trendChart = echarts.init(trendRef.value)
  trendChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: labels },
    yAxis: { type: 'value' },
    series: [{ type: 'line', data: values, smooth: true, areaStyle: {} }]
  })
}

const renderActive = (labels, values) => {
  if (!activeChart) activeChart = echarts.init(activeRef.value)
  activeChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: labels },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: values }]
  })
}

onMounted(() => {
  loadAll()
  window.addEventListener('resize', () => {
    trendChart && trendChart.resize()
    activeChart && activeChart.resize()
  })
})
</script>

<style scoped>
.analytics-page .toolbar {
  margin-bottom: 16px;
  display: flex;
  gap: 12px;
}
.chart {
  width: 100%;
  height: 360px;
}
.chart.small {
  height: 280px;
}
</style>


