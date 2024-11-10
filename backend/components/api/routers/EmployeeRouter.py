import traceback
from fastapi import APIRouter, Depends, Request
from sqlalchemy.orm import Session
from components.lib.enums.userGroupEnum import UserGroup
from components.lib.serializer.EmployeeSerializer import EmployeePostSchema, EmployeeSchema
from components.lib.Employee import Employee
from components.util.Connection import get_db
from components.util.Util import logger, response_temp


router = APIRouter(prefix="/api/v1")
       

@router.get("/employee")
async def get_drivers(request: Request, userGroup: UserGroup, db: Session = Depends(get_db)):
    try:
        result = dict(response_temp)
        drivers = db.query(Employee).filter(Employee.userGroup == userGroup.value).all()

        return {
            "data": EmployeeSchema(many=True).dump(drivers) ,
            "success" : True
        }

    except Exception as ex:
        logger.error(traceback.format_exc())
        result["error"] = str(ex)
    return result


@router.post("/login")
async def login(request: Request, data: EmployeePostSchema, db: Session = Depends(get_db), user_id: str = "") -> dict:

    try:
        result = dict(response_temp)
        validated_data = data.dict()
        user = db.query(Employee).filter(Employee.id==validated_data["id"],Employee.userPassword==validated_data["passcode"]).scalar()

        if not user:
            raise Exception("User does not exist.")
                
        return {
            "data": EmployeeSchema(many=True).dump([user]),
            "success": True
        }
        
    except Exception as ex:
        logger.error(traceback.format_exc())
        result["error"] = str(ex)
    return result