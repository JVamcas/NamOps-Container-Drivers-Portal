
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.declarative import declarative_base
import os

from dotenv import load_dotenv, find_dotenv

load_dotenv(find_dotenv(), verbose=True)


host=os.getenv("DATABASE_HOST")
user=os.getenv("DATABASE_USER")
pwd=os.getenv("DATABASE_PASSWORD")
db_name=os.getenv("DATABASE_NAME")
pwd=os.getenv("DATABASE_PASSWORD")


conn_str=f"postgresql://{user}:{pwd}@{host}:5432/{db_name}"
engine = create_engine(conn_str)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

def get_db():
    
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
        

        
