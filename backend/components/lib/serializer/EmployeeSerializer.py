from marshmallow import fields, Schema
from pydantic import BaseModel



class EmployeeSchema(Schema):

    id = fields.Integer()
    title = fields.String()
    fName = fields.String()
    surname = fields.String()
    userName = fields.String()
    userGroup = fields.String()    
    
    
class EmployeePostSchema(BaseModel):

    id: int
    passcode: str