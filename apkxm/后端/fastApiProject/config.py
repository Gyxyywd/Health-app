from pydantic_settings import BaseSettings
from typing import Optional


class Settings(BaseSettings):
    # 数据库配置
    DB_HOST: str = "127.0.0.1"
    DB_PORT: int = 3306
    DB_USER: str = "root"
    DB_PASSWORD: str = "root"
    DB_NAME: str = "health_app"
    
    # API配置
    API_V1_PREFIX: str = "/api/v1"
    
    # CORS配置
    CORS_ORIGINS: list = ["http://localhost:8080", "http://localhost:5173", "*"]

    # Auth 配置
    SECRET_KEY: str = "change_me_to_a_random_secret"
    ACCESS_TOKEN_EXPIRE_MINUTES: int = 60 * 24  # 24小时
    
    class Config:
        env_file = ".env"
        case_sensitive = True


settings = Settings()

