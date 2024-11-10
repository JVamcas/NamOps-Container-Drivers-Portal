from fastapi import FastAPI

# from components.api.xray_middleware import XRayMiddleware
from fastapi.middleware.cors import CORSMiddleware
from components.api.routers import EmployeeRouter


app = FastAPI()

# Configure CORS settings
origins = [ "*"
    # "http://localhost:3000",
    # "https://namopsipk.vercel.app"
]

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
    expose_headers=["*"]
)

# app.add_middleware(XRayMiddleware)


app.include_router(EmployeeRouter.router)



# if __name__ == "__main__":
#     import uvicorn
    
#     uvicorn.run(app, host="0.0.0.0", port=5000)