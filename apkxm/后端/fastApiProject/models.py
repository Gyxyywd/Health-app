from sqlalchemy import Column, Integer, String, Text, DateTime, Float, Boolean
from sqlalchemy.sql import func
from database import Base


class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(100), unique=True, nullable=False, index=True, comment="用户名")
    password_hash = Column(String(255), nullable=False, comment="密码哈希")
    is_active = Column(Boolean, default=True)
    is_admin = Column(Boolean, default=False)
    created_at = Column(DateTime, server_default=func.now())


class Recipe(Base):
    """健康食谱模型"""
    __tablename__ = "recipes"
    
    id = Column(Integer, primary_key=True, index=True)
    title = Column(String(200), nullable=False, comment="食谱标题")
    description = Column(Text, comment="食谱描述")
    ingredients = Column(Text, nullable=False, comment="食材列表（JSON格式）")
    steps = Column(Text, nullable=False, comment="制作步骤（JSON格式）")
    image_url = Column(String(500), comment="图片URL")
    calories = Column(Float, comment="卡路里")
    protein = Column(Float, comment="蛋白质(g)")
    carbs = Column(Float, comment="碳水化合物(g)")
    fat = Column(Float, comment="脂肪(g)")
    fiber = Column(Float, comment="纤维(g)")
    category = Column(String(50), comment="分类（如：早餐、午餐、晚餐、零食）")
    difficulty = Column(String(20), default="简单", comment="难度（简单、中等、困难）")
    prep_time = Column(Integer, comment="准备时间（分钟）")
    cook_time = Column(Integer, comment="烹饪时间（分钟）")
    servings = Column(Integer, default=1, comment="份数")
    is_active = Column(Boolean, default=True, comment="是否启用")
    created_at = Column(DateTime, server_default=func.now(), comment="创建时间")
    updated_at = Column(DateTime, server_default=func.now(), onupdate=func.now(), comment="更新时间")


class Vitamin(Base):
    """维生素信息模型"""
    __tablename__ = "vitamins"
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100), nullable=False, unique=True, comment="维生素名称")
    name_en = Column(String(100), comment="英文名称")
    alias = Column(String(200), comment="别名")
    description = Column(Text, comment="描述")
    function = Column(Text, nullable=False, comment="功能和作用")
    benefits = Column(Text, comment="益处")
    deficiency_symptoms = Column(Text, comment="缺乏症状")
    food_sources = Column(Text, comment="食物来源（JSON格式）")
    daily_requirement = Column(String(100), comment="每日需求量")
    overdose_symptoms = Column(Text, comment="过量症状")
    image_url = Column(String(500), comment="图片URL")
    is_active = Column(Boolean, default=True, comment="是否启用")
    created_at = Column(DateTime, server_default=func.now(), comment="创建时间")
    updated_at = Column(DateTime, server_default=func.now(), onupdate=func.now(), comment="更新时间")


class HealthTip(Base):
    """健康小贴士模型"""
    __tablename__ = "health_tips"
    
    id = Column(Integer, primary_key=True, index=True)
    title = Column(String(200), nullable=False, comment="标题")
    content = Column(Text, nullable=False, comment="内容")
    category = Column(String(50), comment="分类")
    image_url = Column(String(500), comment="图片URL")
    is_active = Column(Boolean, default=True, comment="是否启用")
    created_at = Column(DateTime, server_default=func.now(), comment="创建时间")
    updated_at = Column(DateTime, server_default=func.now(), onupdate=func.now(), comment="更新时间")


class AccessLog(Base):
    __tablename__ = "access_logs"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, index=True, comment="用户ID，匿名则为NULL")
    path = Column(String(255), index=True, comment="访问路径")
    method = Column(String(10), comment="请求方法")
    user_agent = Column(String(255), comment="UA")
    ip = Column(String(50), comment="IP地址")
    created_at = Column(DateTime, server_default=func.now(), comment="访问时间")


class Feedback(Base):
    __tablename__ = "feedbacks"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, index=True, comment="用户ID，匿名可为空")
    title = Column(String(200), nullable=False, comment="反馈主题")
    content = Column(Text, nullable=False, comment="反馈内容")
    contact = Column(String(200), comment="联系方式(可选)")
    status = Column(String(20), default="open", comment="状态: open/resolved")
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(), onupdate=func.now())


class FeedbackReply(Base):
    __tablename__ = "feedback_replies"

    id = Column(Integer, primary_key=True, index=True)
    feedback_id = Column(Integer, index=True, nullable=False)
    user_id = Column(Integer, index=True, comment="回复人(管理员或用户)")
    content = Column(Text, nullable=False)
    created_at = Column(DateTime, server_default=func.now())

