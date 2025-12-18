from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import List
from database import get_db
from models import Recipe
from schemas import RecipeCreate, RecipeUpdate, RecipeResponse, MessageResponse, PaginatedResponse

router = APIRouter(prefix="/recipes", tags=["食谱"])


@router.get("", response_model=PaginatedResponse, summary="获取食谱列表")
async def get_recipes(
    page: int = Query(1, ge=1, description="页码"),
    page_size: int = Query(10, ge=1, le=100, description="每页数量"),
    category: str = Query(None, description="分类筛选"),
    is_active: bool = Query(None, description="是否启用"),
    db: Session = Depends(get_db)
):
    """获取食谱列表，支持分页和筛选"""
    query = db.query(Recipe)
    
    if category:
        query = query.filter(Recipe.category == category)
    if is_active is not None:
        query = query.filter(Recipe.is_active == is_active)
    
    total = query.count()
    recipes = query.offset((page - 1) * page_size).limit(page_size).all()
    
    return {
        "total": total,
        "page": page,
        "page_size": page_size,
        "items": [RecipeResponse.model_validate(recipe).model_dump() for recipe in recipes]
    }


@router.get("/{recipe_id}", response_model=RecipeResponse, summary="获取食谱详情")
async def get_recipe(recipe_id: int, db: Session = Depends(get_db)):
    """根据ID获取食谱详情"""
    recipe = db.query(Recipe).filter(Recipe.id == recipe_id).first()
    if not recipe:
        raise HTTPException(status_code=404, detail="食谱不存在")
    return recipe


@router.post("", response_model=RecipeResponse, summary="创建食谱")
async def create_recipe(recipe: RecipeCreate, db: Session = Depends(get_db)):
    """创建新食谱"""
    db_recipe = Recipe(**recipe.model_dump())
    db.add(db_recipe)
    db.commit()
    db.refresh(db_recipe)
    return db_recipe


@router.put("/{recipe_id}", response_model=RecipeResponse, summary="更新食谱")
async def update_recipe(
    recipe_id: int,
    recipe: RecipeUpdate,
    db: Session = Depends(get_db)
):
    """更新食谱信息"""
    db_recipe = db.query(Recipe).filter(Recipe.id == recipe_id).first()
    if not db_recipe:
        raise HTTPException(status_code=404, detail="食谱不存在")
    
    update_data = recipe.model_dump(exclude_unset=True)
    for field, value in update_data.items():
        setattr(db_recipe, field, value)
    
    db.commit()
    db.refresh(db_recipe)
    return db_recipe


@router.delete("/{recipe_id}", response_model=MessageResponse, summary="删除食谱")
async def delete_recipe(recipe_id: int, db: Session = Depends(get_db)):
    """删除食谱"""
    db_recipe = db.query(Recipe).filter(Recipe.id == recipe_id).first()
    if not db_recipe:
        raise HTTPException(status_code=404, detail="食谱不存在")
    
    db.delete(db_recipe)
    db.commit()
    return {"message": "食谱删除成功"}

