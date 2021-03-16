package com.pet001kambala.namopscontainers.repo

import com.pet001kambala.namopscontainers.model.JobCard

class JobCardRepo {


    suspend fun loadPreAssignedJobCards(): ArrayList<JobCard>{


        return arrayListOf()
    }

    suspend fun loadUnAssignedJobCards(): ArrayList<JobCard>{


        return arrayListOf()
    }

    suspend fun loadAllJobCards(): ArrayList<JobCard>{


        return arrayListOf()
    }
}