# 健康管理应用 - Web前端

基于Vue 3 + Element Plus的健康管理应用前端。

## 功能特性

- 健康食谱浏览和详情
- 维生素信息浏览和详情
- 健康小贴士浏览和详情
- 后台管理（食谱、维生素、健康小贴士的CRUD）

## 安装和运行

### 1. 安装依赖

```bash
npm install
```

### 2. 运行开发服务器

```bash
npm run dev
```

应用将在 http://localhost:5173 启动

### 3. 构建生产版本

```bash
npm run build
```

## 技术栈

- Vue 3
- Vue Router
- Element Plus
- Axios
- Vite

## 项目结构

```
src/
├── api/          # API接口
├── components/   # 组件
│   └── admin/    # 后台管理组件
├── router/       # 路由配置
├── views/        # 页面视图
├── App.vue       # 根组件
└── main.js       # 入口文件
```

## 页面说明

- `/recipes` - 健康食谱列表
- `/recipes/:id` - 食谱详情
- `/vitamins` - 维生素列表
- `/vitamins/:id` - 维生素详情
- `/health-tips` - 健康小贴士列表
- `/health-tips/:id` - 健康小贴士详情
- `/admin` - 后台管理

