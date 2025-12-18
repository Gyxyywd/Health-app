import axios from 'axios'

const api = axios.create({
  baseURL: '/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error?.response?.status === 401) {
      localStorage.removeItem('token')
      if (location.pathname !== '/login') {
        location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

// 食谱API
export const recipeAPI = {
  getList: (params) => api.get('/recipes', { params }),
  getDetail: (id) => api.get(`/recipes/${id}`),
  create: (data) => api.post('/recipes', data),
  update: (id, data) => api.put(`/recipes/${id}`, data),
  delete: (id) => api.delete(`/recipes/${id}`)
}

// 维生素API
export const vitaminAPI = {
  getList: (params) => api.get('/vitamins', { params }),
  getDetail: (id) => api.get(`/vitamins/${id}`),
  create: (data) => api.post('/vitamins', data),
  update: (id, data) => api.put(`/vitamins/${id}`, data),
  delete: (id) => api.delete(`/vitamins/${id}`)
}

// 健康小贴士API
export const healthTipAPI = {
  getList: (params) => api.get('/health-tips', { params }),
  getDetail: (id) => api.get(`/health-tips/${id}`),
  create: (data) => api.post('/health-tips', data),
  update: (id, data) => api.put(`/health-tips/${id}`, data),
  delete: (id) => api.delete(`/health-tips/${id}`)
}

// 认证
export const authAPI = {
  login: (username, password) =>
    api.post('/auth/login', new URLSearchParams({ username, password })),
  me: () => api.get('/auth/me')
}

// 分析
export const analyticsAPI = {
  paths: (days = 7) => api.get('/analytics/paths', { params: { days } }),
  top: (limit = 10) => api.get('/analytics/top', { params: { limit } }),
  activeUsers: (days = 7) => api.get('/analytics/active-users', { params: { days } })
}

// 反馈
export const feedbackAPI = {
  getList: (params) => api.get('/feedbacks', { params }),
  getDetail: (id) => api.get(`/feedbacks/${id}`),
  create: (data) => api.post('/feedbacks', data),
  update: (id, data) => api.put(`/feedbacks/${id}`, data),
  getReplies: (id) => api.get(`/feedbacks/${id}/replies`),
  createReply: (id, data) => api.post(`/feedbacks/${id}/replies`, data)
}

export default api

