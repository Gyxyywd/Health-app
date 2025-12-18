from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from database import get_db
from models import HealthTip
from schemas import HealthTipCreate, HealthTipUpdate, HealthTipResponse, MessageResponse, PaginatedResponse

router = APIRouter(prefix="/health-tips", tags=["健康小贴士"])


@router.get("", response_model=PaginatedResponse, summary="获取健康小贴士列表")
async def get_health_tips(
    page: int = Query(1, ge=1, description="页码"),
    page_size: int = Query(10, ge=1, le=100, description="每页数量"),
    category: str = Query(None, description="分类筛选"),
    is_active: bool = Query(None, description="是否启用"),
    db: Session = Depends(get_db)
):
    """获取健康小贴士列表，支持分页和筛选"""
    query = db.query(HealthTip)
    
    if category:
        query = query.filter(HealthTip.category == category)
    if is_active is not None:
        query = query.filter(HealthTip.is_active == is_active)
    
    total = query.count()
    tips = query.offset((page - 1) * page_size).limit(page_size).all()
    
    return {
        "total": total,
        "page": page,
        "page_size": page_size,
        "items": [HealthTipResponse.model_validate(tip).model_dump() for tip in tips]
    }


@router.get("/{tip_id}", response_model=HealthTipResponse, summary="获取健康小贴士详情")
async def get_health_tip(tip_id: int, db: Session = Depends(get_db)):
    """根据ID获取健康小贴士详情"""
    tip = db.query(HealthTip).filter(HealthTip.id == tip_id).first()
    if not tip:
        raise HTTPException(status_code=404, detail="健康小贴士不存在")
    return tip


@router.post("", response_model=HealthTipResponse, summary="创建健康小贴士")
async def create_health_tip(tip: HealthTipCreate, db: Session = Depends(get_db)):
    """创建新健康小贴士"""
    db_tip = HealthTip(**tip.model_dump())
    db.add(db_tip)
    db.commit()
    db.refresh(db_tip)
    return db_tip


@router.put("/{tip_id}", response_model=HealthTipResponse, summary="更新健康小贴士")
async def update_health_tip(
    tip_id: int,
    tip: HealthTipUpdate,
    db: Session = Depends(get_db)
):
    """更新健康小贴士信息"""
    db_tip = db.query(HealthTip).filter(HealthTip.id == tip_id).first()
    if not db_tip:
        raise HTTPException(status_code=404, detail="健康小贴士不存在")
    
    update_data = tip.model_dump(exclude_unset=True)
    for field, value in update_data.items():
        setattr(db_tip, field, value)
    
    db.commit()
    db.refresh(db_tip)
    return db_tip


@router.delete("/{tip_id}", response_model=MessageResponse, summary="删除健康小贴士")
async def delete_health_tip(tip_id: int, db: Session = Depends(get_db)):
    """删除健康小贴士"""
    db_tip = db.query(HealthTip).filter(HealthTip.id == tip_id).first()
    if not db_tip:
        raise HTTPException(status_code=404, detail="健康小贴士不存在")
    
    db.delete(db_tip)
    db.commit()
    return {"message": "健康小贴士删除成功"}

