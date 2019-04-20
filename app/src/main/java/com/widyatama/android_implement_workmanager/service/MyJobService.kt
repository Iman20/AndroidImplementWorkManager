package com.widyatama.android_implement_workmanager.service

import android.app.job.JobParameters
import android.app.job.JobService


/**
 * Created by iman mutaqin on 19/02/2019
 */

class MyJobService : JobService() {

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        return false
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return false
    }

    companion object {
        private const val TAG = "MyJobService"
    }
}