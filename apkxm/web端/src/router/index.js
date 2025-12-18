import { createRouter, createWebHistory } from 'vue-router'
import Recipes from '../views/Recipes.vue'
import RecipeDetail from '../views/RecipeDetail.vue'
import Vitamins from '../views/Vitamins.vue'
import VitaminDetail from '../views/VitaminDetail.vue'
import HealthTips from '../views/HealthTips.vue'
import HealthTipDetail from '../views/HealthTipDetail.vue'
import Admin from '../views/Admin.vue'
import Login from '../views/Login.vue'
import Analytics from '../views/Analytics.vue'

const routes = [
  {
    path: '/',
    redirect: '/recipes'
  },
  {
    path: '/recipes',
    name: 'Recipes',
    component: Recipes
  },
  {
    path: '/recipes/:id',
    name: 'RecipeDetail',
    component: RecipeDetail
  },
  {
    path: '/vitamins',
    name: 'Vitamins',
    component: Vitamins
  },
  {
    path: '/vitamins/:id',
    name: 'VitaminDetail',
    component: VitaminDetail
  },
  {
    path: '/health-tips',
    name: 'HealthTips',
    component: HealthTips
  },
  {
    path: '/health-tips/:id',
    name: 'HealthTipDetail',
    component: HealthTipDetail
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
    meta: { requiresAuth: true }
  },
  {
    path: '/analytics',
    name: 'Analytics',
    component: Analytics,
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta?.requiresAuth) {
    const token = localStorage.getItem('token')
    if (!token) return next({ path: '/login', query: { redirect: to.fullPath } })
  }
  next()
})

export default router

