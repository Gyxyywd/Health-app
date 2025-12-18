from fastapi import APIRouter, Query, Depends
from typing import Literal
from routers.auth import get_current_user

router = APIRouter(prefix="/tools", tags=["健康工具"])


@router.get("/weight-assess")
def weight_assess(
    height_cm: float = Query(..., gt=0, description="身高cm"),
    weight_kg: float = Query(..., gt=0, description="体重kg"),
    sex: Literal["male", "female"] = Query("male"),
    age: int = Query(25, ge=1, le=120),
    user=Depends(get_current_user)
):
    height_m = height_cm / 100.0
    bmi = weight_kg / (height_m * height_m)
    # 简单判定
    if bmi < 18.5:
        level = "偏瘦"
        tips = ["增加优质蛋白和复合碳水", "合理加餐", "适度力量训练"]
    elif bmi < 24:
        level = "正常"
        tips = ["保持均衡饮食和作息", "每周适量运动 150 分钟"]
    elif bmi < 28:
        level = "超重"
        tips = ["控制精制糖与油脂", "增加有氧运动", "监测体重变化"]
    else:
        level = "肥胖"
        tips = ["咨询专业营养建议", "低糖低油膳食", "坚持有氧+抗阻训练"]

    suggest_weight_min = 18.5 * height_m * height_m
    suggest_weight_max = 23.9 * height_m * height_m
    return {
        "bmi": round(bmi, 1),
        "level": level,
        "suggest_weight_range": [round(suggest_weight_min, 1), round(suggest_weight_max, 1)],
        "tips": tips
    }


