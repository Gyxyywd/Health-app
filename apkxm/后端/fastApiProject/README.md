# 健康管理API后端

基于FastAPI的健康管理后端服务，提供健康食谱、维生素信息和健康小贴士管理功能。

## 功能特性

- 健康食谱管理（CRUD）
- 维生素信息管理（CRUD）
- 健康小贴士管理（CRUD）
- RESTful API设计
- 支持分页和筛选
- 自动API文档（Swagger）

## 安装和运行

### 1. 安装依赖

```bash
pip install -r requirements.txt
```

### 2. 初始化数据库

首先确保MySQL服务正在运行，然后执行：

```bash
python init_db.py
```

这将创建数据库（如果不存在）。

### 3. 运行服务

```bash
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

服务将在 http://localhost:8000 启动

### 4. 访问API文档

- Swagger UI: http://localhost:8000/docs
- ReDoc: http://localhost:8000/redoc

## API端点

### 食谱相关
- `GET /api/v1/recipes` - 获取食谱列表
- `GET /api/v1/recipes/{id}` - 获取食谱详情
- `POST /api/v1/recipes` - 创建食谱
- `PUT /api/v1/recipes/{id}` - 更新食谱
- `DELETE /api/v1/recipes/{id}` - 删除食谱

### 维生素相关
- `GET /api/v1/vitamins` - 获取维生素列表
- `GET /api/v1/vitamins/{id}` - 获取维生素详情
- `POST /api/v1/vitamins` - 创建维生素信息
- `PUT /api/v1/vitamins/{id}` - 更新维生素信息
- `DELETE /api/v1/vitamins/{id}` - 删除维生素信息

### 健康小贴士相关
- `GET /api/v1/health-tips` - 获取健康小贴士列表
- `GET /api/v1/health-tips/{id}` - 获取健康小贴士详情
- `POST /api/v1/health-tips` - 创建健康小贴士
- `PUT /api/v1/health-tips/{id}` - 更新健康小贴士
- `DELETE /api/v1/health-tips/{id}` - 删除健康小贴士

## 环境变量

复制 `.env.example` 为 `.env` 并修改配置：

```env
DB_HOST=127.0.0.1
DB_PORT=3306
DB_USER=root
DB_PASSWORD=root
DB_NAME=health_app
```

## 扩展性

项目采用模块化设计，易于扩展：

1. 添加新模型：在 `models.py` 中定义
2. 添加新Schema：在 `schemas.py` 中定义
3. 添加新路由：在 `routers/` 目录下创建新文件
4. 在 `main.py` 中注册新路由

## 未来扩展

- 用户认证和授权
- AI健康建议功能
- 健康数据记录和分析
- 图片上传功能
- 缓存支持

