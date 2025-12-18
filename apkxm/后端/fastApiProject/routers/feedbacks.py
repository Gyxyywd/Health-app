from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import List
from database import get_db
from models import Feedback, FeedbackReply
from routers.auth import get_current_user
from schemas import (
    FeedbackCreate, FeedbackUpdate, FeedbackResponse,
    FeedbackReplyCreate, FeedbackReplyResponse, PaginatedResponse
)

router = APIRouter(prefix="/feedbacks", tags=["反馈"])


@router.get("", response_model=PaginatedResponse)
def list_feedbacks(
    page: int = Query(1, ge=1),
    page_size: int = Query(10, ge=1, le=100),
    status: str = Query(None),
    db: Session = Depends(get_db),
    user=Depends(get_current_user)
):
    q = db.query(Feedback)
    if status:
        q = q.filter(Feedback.status == status)
    total = q.count()
    items = q.order_by(Feedback.created_at.desc()).offset((page-1)*page_size).limit(page_size).all()
    return {
        "total": total,
        "page": page,
        "page_size": page_size,
        "items": [FeedbackResponse.model_validate(i).model_dump() for i in items]
    }


@router.post("", response_model=FeedbackResponse)
def create_feedback(
    payload: FeedbackCreate,
    db: Session = Depends(get_db),
    user=Depends(get_current_user)
):
    fb = Feedback(user_id=user.id if user else None, **payload.model_dump())
    db.add(fb)
    db.commit()
    db.refresh(fb)
    return fb


@router.get("/{fid}", response_model=FeedbackResponse)
def get_feedback(fid: int, db: Session = Depends(get_db), user=Depends(get_current_user)):
    fb = db.query(Feedback).filter(Feedback.id == fid).first()
    if not fb:
        raise HTTPException(status_code=404, detail="反馈不存在")
    return fb


@router.put("/{fid}", response_model=FeedbackResponse)
def update_feedback(fid: int, payload: FeedbackUpdate, db: Session = Depends(get_db), user=Depends(get_current_user)):
    fb = db.query(Feedback).filter(Feedback.id == fid).first()
    if not fb:
        raise HTTPException(status_code=404, detail="反馈不存在")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(fb, k, v)
    db.commit()
    db.refresh(fb)
    return fb


@router.get("/{fid}/replies", response_model=List[FeedbackReplyResponse])
def list_replies(fid: int, db: Session = Depends(get_db), user=Depends(get_current_user)):
    items = db.query(FeedbackReply).filter(FeedbackReply.feedback_id == fid).order_by(FeedbackReply.created_at.asc()).all()
    return items


@router.post("/{fid}/replies", response_model=FeedbackReplyResponse)
def create_reply(fid: int, payload: FeedbackReplyCreate, db: Session = Depends(get_db), user=Depends(get_current_user)):
    fb = db.query(Feedback).filter(Feedback.id == fid).first()
    if not fb:
        raise HTTPException(status_code=404, detail="反馈不存在")
    r = FeedbackReply(feedback_id=fid, user_id=user.id if user else None, content=payload.content)
    db.add(r)
    db.commit()
    db.refresh(r)
    return r


