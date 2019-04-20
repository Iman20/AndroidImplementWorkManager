package com.widyatama.android_implement_workmanager.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.widyatama.android_implement_workmanager.R
import com.widyatama.android_implement_workmanager.api.APIService
import com.widyatama.android_implement_workmanager.di.CurrencyApplication
import com.widyatama.android_implement_workmanager.utils.Constants
import com.widyatama.android_implement_workmanager.utils.Constants.CHANNEL_ID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import okhttp3.ResponseBody
import java.io.BufferedInputStream
import java.io.IOException


/**
 * Created by iman on 09/04/2019.
 */

class DownloadImageWorker(context: Context, parameters: WorkerParameters): Worker(context, parameters){
    private val TAG = "Download Image Worker"
    init {
        initializeDagger()
    }

    @Inject
    lateinit var apiService: APIService

    var bmp : Bitmap? = null

    override fun doWork(): Result {
        WorkerUtils.makeStatusNotification("Preparing download", applicationContext)
        val request = apiService.getPhoto()
        request.subscribeOn(Schedulers.io())
        request.observeOn(AndroidSchedulers.mainThread())
        request.subscribe {
            WorkerUtils.makeStatusNotification("Success download", applicationContext)
            downloadedImage(it)
        }

        val output = bmp?.let { WorkerUtils.writeBitmapToFile(applicationContext, it) }

        val outputData = Data.Builder()
            .putString(Constants.KEY_IMAGE_URI, output?.toString())
            .build()

        return Result.success(outputData)
    }

    @Throws(IOException::class)
    private fun downloadedImage(body: ResponseBody) {

        val inputStream = BufferedInputStream(body.byteStream(), 1024 * 8)
        bmp = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

    }

    private fun initializeDagger() = CurrencyApplication.appComponent.inject(this)
}