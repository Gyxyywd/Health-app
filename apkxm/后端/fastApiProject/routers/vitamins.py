from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from database import get_db
from models import Vitamin
from schemas import VitaminCreate, VitaminUpdate, VitaminResponse, MessageResponse, PaginatedResponse

router = APIRouter(prefix="/vitamins", tags=["维生素"])


@router.get("", response_model=PaginatedResponse, summary="获取维生素列表")
async def get_vitamins(
    page: int = Query(1, ge=1, description="页码"),
    page_size: int = Query(10, ge=1, le=100, description="每页数量"),
    is_active: bool = Query(None, description="是否启用"),
    db: Session = Depends(get_db)
):
    """获取维生素列表，支持分页和筛选"""
    query = db.query(Vitamin)
    
    if is_active is not None:
        query = query.filter(Vitamin.is_active == is_active)
    
    total = query.count()
    vitamins = query.offset((page - 1) * page_size).limit(page_size).all()
    
    return {
        "total": total,
        "page": page,
        "page_size": page_size,
        "items": [VitaminResponse.model_validate(vitamin).model_dump() for vitamin in vitamins]
    }


@router.get("/{vitamin_id}", response_model=VitaminResponse, summary="获取维生素详情")
async def get_vitamin(vitamin_id: int, db: Session = Depends(get_db)):
    """根据ID获取维生素详情"""
    vitamin = db.query(Vitamin).filter(Vitamin.id == vitamin_id).first()
    if not vitamin:
        raise HTTPException(status_code=404, detail="维生素信息不存在")
    return vitamin


@router.post("", response_model=VitaminResponse, summary="创建维生素信息")
async def create_vitamin(vitamin: VitaminCreate, db: Session = Depends(get_db)):
    """创建新维生素信息"""
    # 检查名称是否已存在
    existing = db.query(Vitamin).filter(Vitamin.name == vitamin.name).first()
    if existing:
        raise HTTPException(status_code=400, detail="维生素名称已存在")
    
    db_vitamin = Vitamin(**vitamin.model_dump())
    db.add(db_vitamin)
    db.commit()
    db.refresh(db_vitamin)
    return db_vitamin


@router.put("/{vitamin_id}", response_model=VitaminResponse, summary="更新维生素信息")
async def update_vitamin(
    vitamin_id: int,
    vitamin: VitaminUpdate,
    db: Session = Depends(get_db)
):
    """更新维生素信息"""
    db_vitamin = db.query(Vitamin).filter(Vitamin.id == vitamin_id).first()
    if not db_vitamin:
        raise HTTPException(status_code=404, detail="维生素信息不存在")
    
    # 如果更新名称，检查是否重复
    if vitamin.name and vitamin.name != db_vitamin.name:
        existing = db.query(Vitamin).filter(Vitamin.name == vitamin.name).first()
        if existing:
            raise HTTPException(status_code=400, detail="维生素名称已存在")
    
    update_data = vitamin.model_dump(exclude_unset=True)
    for field, value in update_data.items():
        setattr(db_vitamin, field, value)
    
    db.commit()
    db.refresh(db_vitamin)
    return db_vitamin


@router.delete("/{vitamin_id}", response_model=MessageResponse, summary="删除维生素信息")
async def delete_vitamin(vitamin_id: int, db: Session = Depends(get_db)):
    """删除维生素信息"""
    db_vitamin = db.query(Vitamin).filter(Vitamin.id == vitamin_id).first()
    if not db_vitamin:
        raise HTTPException(status_code=404, detail="维生素信息不存在")
    
    db.delete(db_vitamin)
    db.commit()
    return {"message": "维生素信息删除成功"}

