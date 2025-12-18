from pydantic import BaseModel, Field
from typing import Optional, List, Any
from datetime import datetime


# 食谱相关Schema
class RecipeBase(BaseModel):
    title: str = Field(..., description="食谱标题")
    description: Optional[str] = None
    ingredients: str = Field(..., description="食材列表（JSON字符串）")
    steps: str = Field(..., description="制作步骤（JSON字符串）")
    image_url: Optional[str] = None
    calories: Optional[float] = None
    protein: Optional[float] = None
    carbs: Optional[float] = None
    fat: Optional[float] = None
    fiber: Optional[float] = None
    category: Optional[str] = None
    difficulty: str = "简单"
    prep_time: Optional[int] = None
    cook_time: Optional[int] = None
    servings: int = 1
    is_active: bool = True


class RecipeCreate(RecipeBase):
    pass


class RecipeUpdate(BaseModel):
    title: Optional[str] = None
    description: Optional[str] = None
    ingredients: Optional[str] = None
    steps: Optional[str] = None
    image_url: Optional[str] = None
    calories: Optional[float] = None
    protein: Optional[float] = None
    carbs: Optional[float] = None
    fat: Optional[float] = None
    fiber: Optional[float] = None
    category: Optional[str] = None
    difficulty: Optional[str] = None
    prep_time: Optional[int] = None
    cook_time: Optional[int] = None
    servings: Optional[int] = None
    is_active: Optional[bool] = None


class RecipeResponse(RecipeBase):
    id: int
    created_at: datetime
    updated_at: datetime
    
    model_config = {"from_attributes": True}


# 维生素相关Schema
class VitaminBase(BaseModel):
    name: str = Field(..., description="维生素名称")
    name_en: Optional[str] = None
    alias: Optional[str] = None
    description: Optional[str] = None
    function: str = Field(..., description="功能和作用")
    benefits: Optional[str] = None
    deficiency_symptoms: Optional[str] = None
    food_sources: Optional[str] = None
    daily_requirement: Optional[str] = None
    overdose_symptoms: Optional[str] = None
    image_url: Optional[str] = None
    is_active: bool = True


class VitaminCreate(VitaminBase):
    pass


class VitaminUpdate(BaseModel):
    name: Optional[str] = None
    name_en: Optional[str] = None
    alias: Optional[str] = None
    description: Optional[str] = None
    function: Optional[str] = None
    benefits: Optional[str] = None
    deficiency_symptoms: Optional[str] = None
    food_sources: Optional[str] = None
    daily_requirement: Optional[str] = None
    overdose_symptoms: Optional[str] = None
    image_url: Optional[str] = None
    is_active: Optional[bool] = None


class VitaminResponse(VitaminBase):
    id: int
    created_at: datetime
    updated_at: datetime
    
    model_config = {"from_attributes": True}


# 健康小贴士相关Schema
class HealthTipBase(BaseModel):
    title: str = Field(..., description="标题")
    content: str = Field(..., description="内容")
    category: Optional[str] = None
    image_url: Optional[str] = None
    is_active: bool = True


class HealthTipCreate(HealthTipBase):
    pass


class HealthTipUpdate(BaseModel):
    title: Optional[str] = None
    content: Optional[str] = None
    category: Optional[str] = None
    image_url: Optional[str] = None
    is_active: Optional[bool] = None


class HealthTipResponse(HealthTipBase):
    id: int
    created_at: datetime
    updated_at: datetime
    
    model_config = {"from_attributes": True}


# 通用响应
class MessageResponse(BaseModel):
    message: str


# 认证相关Schema
class Token(BaseModel):
    access_token: str
    token_type: str = "bearer"


class UserBase(BaseModel):
    username: str


class UserCreate(UserBase):
    password: str = Field(..., min_length=6)


class UserResponse(UserBase):
    id: int
    is_admin: bool = False
    created_at: datetime
    model_config = {"from_attributes": True}


# 分页响应
class PaginatedResponse(BaseModel):
    total: int
    page: int
    page_size: int
    items: List[Any]


# 反馈相关 Schema
class FeedbackBase(BaseModel):
    title: str
    content: str
    contact: Optional[str] = None


class FeedbackCreate(FeedbackBase):
    pass


class FeedbackUpdate(BaseModel):
    title: Optional[str] = None
    content: Optional[str] = None
    contact: Optional[str] = None
    status: Optional[str] = None


class FeedbackResponse(FeedbackBase):
    id: int
    user_id: Optional[int] = None
    status: str
    created_at: datetime
    updated_at: datetime
    model_config = {"from_attributes": True}


class FeedbackReplyBase(BaseModel):
    content: str


class FeedbackReplyCreate(FeedbackReplyBase):
    pass


class FeedbackReplyResponse(FeedbackReplyBase):
    id: int
    feedback_id: int
    user_id: Optional[int] = None
    created_at: datetime
    model_config = {"from_attributes": True}

