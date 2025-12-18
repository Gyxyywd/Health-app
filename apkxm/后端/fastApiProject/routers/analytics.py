from fastapi import APIRouter, Depends, Query
from sqlalchemy.orm import Session
from sqlalchemy import func
from datetime import datetime, timedelta

from database import get_db
from models import AccessLog
from routers.auth import get_current_user

router = APIRouter(prefix="/analytics", tags=["访问分析"])


@router.get("/paths")
def paths_trend(days: int = Query(7, ge=1, le=90), db: Session = Depends(get_db), user=Depends(get_current_user)):
    # 近 N 天每天访问量（按 path 聚合）
    since = datetime.utcnow() - timedelta(days=days)
    q = (
        db.query(
            func.date(AccessLog.created_at).label("date"),
            AccessLog.path,
            func.count(AccessLog.id).label("count")
        )
        .filter(AccessLog.created_at >= since)
        .group_by(func.date(AccessLog.created_at), AccessLog.path)
        .order_by(func.date(AccessLog.created_at))
        .all()
    )
    result = {}
    for d, path, cnt in q:
        key = d.isoformat() if hasattr(d, "isoformat") else str(d)
        result.setdefault(key, []).append({"path": path, "count": int(cnt)})
    return {"days": days, "data": result}


@router.get("/top")
def top_paths(limit: int = Query(10, ge=1, le=100), db: Session = Depends(get_db), user=Depends(get_current_user)):
    q = (
        db.query(AccessLog.path, func.count(AccessLog.id).label("count"))
        .group_by(AccessLog.path)
        .order_by(func.count(AccessLog.id).desc())
        .limit(limit)
        .all()
    )
    return [{"path": path, "count": int(cnt)} for path, cnt in q]


@router.get("/active-users")
def active_users(days: int = Query(7, ge=1, le=90), db: Session = Depends(get_db), user=Depends(get_current_user)):
    since = datetime.utcnow() - timedelta(days=days)
    q = (
        db.query(func.date(AccessLog.created_at).label("date"), func.count(func.distinct(AccessLog.user_id)))
        .filter(AccessLog.created_at >= since, AccessLog.user_id.isnot(None))
        .group_by(func.date(AccessLog.created_at))
        .order_by(func.date(AccessLog.created_at))
        .all()
    )
    return [{"date": (d.isoformat() if hasattr(d, "isoformat") else str(d)), "active_users": int(cnt)} for d, cnt in q]


