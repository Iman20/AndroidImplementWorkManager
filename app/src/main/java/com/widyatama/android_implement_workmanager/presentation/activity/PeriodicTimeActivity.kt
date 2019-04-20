package com.widyatama.android_implement_workmanager.presentation.activity

import android.arch.lifecycle.Observer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import com.widyatama.android_implement_workmanager.R
import com.widyatama.android_implement_workmanager.utils.Constants
import com.widyatama.android_implement_workmanager.utils.gone
import com.widyatama.android_implement_workmanager.utils.toast
import com.widyatama.android_implement_workmanager.utils.visible
import com.widyatama.android_implement_workmanager.workers.DownloadImageWorker
import kotlinx.android.synthetic.main.activity_periodic_time.*
import java.util.concurrent.TimeUnit

class PeriodicTimeActivity : AppCompatActivity() {

    private var mWorkManager: WorkManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_periodic_time)
        mWorkManager = WorkManager.getInstance()
        btnPeriodicTime.setOnClickListener {

            progressBar.visible()
            val periodicWorkRequest = PeriodicWorkRequest.Builder(DownloadImageWorker::class.java!!, 1, TimeUnit.MINUTES)
                .setConstraints(networkConstraints())
                .build()
            mWorkManager?.enqueue(periodicWorkRequest)
            mWorkManager?.getWorkInfoByIdLiveData(periodicWorkRequest.id)?.observe(this, Observer { workInfo ->
                if (workInfo != null){
                    if (workInfo.state == WorkInfo.State.ENQUEUED) {
                        toast("download enqueued")
                    } else if (workInfo.state == WorkInfo.State.BLOCKED) {
                        toast("download blocked")
                    } else if (workInfo.state == WorkInfo.State.SUCCEEDED){
                        toast("download succesed")

                        val data = workInfo.outputData
                        val uri = data.getString(Constants.KEY_IMAGE_URI)
                        uri.apply {
                            progressBar.gone()
                            imageResult.setImageURI(Uri.parse(uri))
                        }
                    } else if (workInfo.state == WorkInfo.State.FAILED) {
                        toast("download failed")
                    } else if (workInfo.state == WorkInfo.State.CANCELLED) {
                        toast("download canceled")
                    }
                }

            })
        }
    }

    private fun networkConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }
}
