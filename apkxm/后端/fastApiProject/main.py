from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from config import settings
from database import engine, Base
from routers import recipes, vitamins, health_tips
from routers import auth as auth_router
from routers import analytics as analytics_router
from routers import feedbacks as feedbacks_router
from routers import health_tools as health_tools_router

# 创建数据库表
Base.metadata.create_all(bind=engine)

# 创建FastAPI应用（使用本地 Swagger UI 资源，避免外网CDN超时）
app = FastAPI(
    title="健康管理API",
    description="健康食谱、维生素信息和健康小贴士管理API",
    version="1.0.0",
    redoc_url=None,
    swagger_ui_parameters={"useLocalAssets": True}
)

# 配置CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.CORS_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 注册路由
app.include_router(recipes.router, prefix=settings.API_V1_PREFIX)
app.include_router(vitamins.router, prefix=settings.API_V1_PREFIX)
app.include_router(health_tips.router, prefix=settings.API_V1_PREFIX)
app.include_router(auth_router.router, prefix=settings.API_V1_PREFIX)
app.include_router(analytics_router.router, prefix=settings.API_V1_PREFIX)
app.include_router(feedbacks_router.router, prefix=settings.API_V1_PREFIX)
app.include_router(health_tools_router.router, prefix=settings.API_V1_PREFIX)


@app.get("/")
async def root():
    return {
        "message": "健康管理API",
        "version": "1.0.0",
        "docs": "/docs"
    }


@app.get("/health")
async def health_check():
    return {"status": "healthy"}


# 访问日志中间件
from starlette.requests import Request
from sqlalchemy.orm import Session
from database import get_db
from models import AccessLog
from routers.auth import get_current_user, oauth2_scheme


@app.middleware("http")
async def access_log_middleware(request: Request, call_next):
    response = await call_next(request)
    try:
        # 仅记录 API 访问
        if request.url.path.startswith(settings.API_V1_PREFIX):
            token = None
            auth_header = request.headers.get("authorization")
            if auth_header and auth_header.lower().startswith("bearer "):
                token = auth_header.split(" ", 1)[1]

            user_id = None
            if token:
                from jose import jwt
                try:
                    payload = jwt.decode(token, settings.SECRET_KEY, algorithms=["HS256"])
                    # 通过用户名查用户ID
                    from models import User
                    db_gen = get_db()
                    db: Session = next(db_gen)
                    user = db.query(User).filter(User.username == payload.get("sub")).first()
                    if user:
                        user_id = user.id
                    db.close()
                except Exception:
                    pass

            ua = request.headers.get("user-agent", "")[:255]
            ip = request.client.host if request.client else ""
            db_gen2 = get_db()
            db2: Session = next(db_gen2)
            db2.add(AccessLog(user_id=user_id, path=request.url.path, method=request.method, user_agent=ua, ip=ip))
            db2.commit()
            db2.close()
    except Exception:
        # 日志失败不影响主流程
        pass
    return response
