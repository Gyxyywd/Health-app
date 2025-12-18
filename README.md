# 健康管理应用 - 全栈项目

一个完整的健康管理应用，包含后端API、Web前端和Android客户端。

## 项目结构

```
apkxm/
├── 后端/              # FastAPI后端
│   └── fastApiProject/
├── web端/             # Vue 3前端
└── app/               # Android应用
```

## 功能特性

### 核心功能

- ✅ 健康食谱管理（浏览、详情、后台管理）
- ✅ 维生素信息管理（浏览、详情、后台管理）
- ✅ 健康小贴士管理（浏览、详情、后台管理）
- ✅ 后台管理（CRUD操作）

### 技术栈

**后端**

- FastAPI
- SQLAlchemy
- MySQL
- Pydantic

**Web前端**

- Vue 3
- Element Plus
- Vue Router
- Axios
- Vite

**Android客户端**

- Kotlin
- Android Jetpack (ViewModel, LiveData, Navigation)
- Retrofit
- Glide

## 快速开始

### 1. 数据库准备

确保MySQL服务正在运行，然后执行：

```bash
cd 后端/fastApiProject
python init_db.py
```

### 2. 启动后端服务

```bash
cd 后端/fastApiProject
pip install -r requirements.txt
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

后端API将在 http://localhost:8000 启动
API文档：http://localhost:8000/docs

### 3. 启动Web前端

```bash
cd web端
npm install
npm run dev
```

Web应用将在 http://localhost:5173 启动

### 4. 运行Android应用

1. 打开Android Studio
2. 打开 `app/` 目录
3. 同步Gradle依赖
4. 配置API地址（在 `ApiClient.kt` 中）
5. 运行应用

**注意**：

- Android模拟器访问localhost：使用 `http://10.0.2.2:8000`
- 真机调试：使用电脑的IP地址，例如 `http://192.168.1.100:8000`

## API端点

### 食谱

- `GET /api/v1/recipes` - 获取食谱列表
- `GET /api/v1/recipes/{id}` - 获取食谱详情
- `POST /api/v1/recipes` - 创建食谱
- `PUT /api/v1/recipes/{id}` - 更新食谱
- `DELETE /api/v1/recipes/{id}` - 删除食谱

### 维生素

- `GET /api/v1/vitamins` - 获取维生素列表
- `GET /api/v1/vitamins/{id}` - 获取维生素详情
- `POST /api/v1/vitamins` - 创建维生素信息
- `PUT /api/v1/vitamins/{id}` - 更新维生素信息
- `DELETE /api/v1/vitamins/{id}` - 删除维生素信息

### 健康小贴士

- `GET /api/v1/health-tips` - 获取健康小贴士列表
- `GET /api/v1/health-tips/{id}` - 获取健康小贴士详情
- `POST /api/v1/health-tips` - 创建健康小贴士
- `PUT /api/v1/health-tips/{id}` - 更新健康小贴士
- `DELETE /api/v1/health-tips/{id}` - 删除健康小贴士

## 数据库配置

默认配置（可在后端 `config.py` 或 `.env` 文件中修改）：

- 主机：127.0.0.1
- 端口：3306
- 用户：root
- 密码：root
- 数据库：health_app

## 扩展性

项目采用模块化设计，易于扩展：

1. **添加新功能模块**：
   - 在后端 `models.py` 中添加模型
   - 在 `schemas.py` 中添加Schema
   - 在 `routers/` 中创建路由
   - 在 `main.py` 中注册路由

2. **前端集成**：
   - 在 `api/index.js` 中添加API调用
   - 创建对应的Vue组件
   - 更新路由配置

3. **Android集成**：
   - 在 `ApiService.kt` 中添加接口
   - 创建对应的Fragment和ViewModel
   - 更新导航配置

## 未来扩展计划

- [ ] 用户认证和授权
- [ ] AI健康建议功能
- [ ] 健康数据记录和分析
- [ ] 图片上传功能
- [ ] 数据缓存（Android Room数据库）
- [ ] 推送通知
- [ ] 离线支持

## 许可证

MIT

