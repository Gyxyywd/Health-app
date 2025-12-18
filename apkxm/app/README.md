# 健康管理应用 - Android客户端

基于Kotlin和Android Jetpack的健康管理Android应用。

## 功能特性

- 健康食谱浏览和详情查看
- 维生素信息浏览和详情查看
- 健康小贴士浏览和详情查看
- 下拉刷新功能
- 图片加载（使用Glide）

## 技术栈

- Kotlin
- Android Jetpack:
  - ViewModel
  - LiveData
  - Navigation Component
  - ViewBinding
- Retrofit (网络请求)
- Gson (JSON解析)
- Glide (图片加载)
- OkHttp (HTTP客户端)

## 项目结构

```
app/src/main/java/com/example/myapplication/
├── api/              # API服务
│   ├── ApiClient.kt
│   └── ApiService.kt
├── model/            # 数据模型
│   ├── Recipe.kt
│   ├── Vitamin.kt
│   ├── HealthTip.kt
│   └── PaginatedResponse.kt
└── ui/               # UI组件
    ├── recipes/      # 食谱相关
    ├── vitamins/     # 维生素相关
    └── healthtips/   # 健康小贴士相关
```

## 配置说明

### API地址配置

在 `ApiClient.kt` 中配置API基础URL：

- Android模拟器：使用 `http://10.0.2.2:8000/api/v1/`（访问localhost）
- 真机调试：使用你的电脑IP地址，例如 `http://192.168.1.100:8000/api/v1/`

### 网络权限

应用已配置必要的网络权限：
- INTERNET
- ACCESS_NETWORK_STATE

### 允许HTTP明文流量

在 `AndroidManifest.xml` 中已设置 `android:usesCleartextTraffic="true"`，允许HTTP连接（开发环境）。

## 注意事项

1. **布局文件**：需要创建对应的布局XML文件：
   - `fragment_recipes.xml`
   - `fragment_recipe_detail.xml`
   - `fragment_vitamins.xml`
   - `fragment_vitamin_detail.xml`
   - `fragment_health_tips.xml`
   - `fragment_health_tip_detail.xml`
   - `item_recipe.xml`
   - `item_vitamin.xml`
   - `item_health_tip.xml`

2. **网络配置**：确保后端API可访问，并配置正确的API地址。

3. **依赖同步**：首次运行前需要同步Gradle依赖。

## 未来扩展

- 用户认证
- 数据缓存（Room数据库）
- AI健康建议集成
- 推送通知
- 离线支持

