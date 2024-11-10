from datetime import datetime
import re
import pytz
from sqlalchemy import Column, DateTime, ForeignKey, Integer, String, Text
from sqlalchemy.orm import relationship
from sqlalchemy.orm import Session
from components.util.Connection import Base


class Employee(Base):

    __tablename__ = "tblEmployees"
    id = Column(Integer, primary_key=True,name="empLog")
    title = Column(Text,name="strTitle")
    fName = Column(String, name="strName")
    surname = Column(String, name="strSurname")
    userPassword = Column(String, name="strUserPassword")
    userName = Column(String, name="strUserName")
    userGroup = Column(String, name="strUserGroup")

    
    



