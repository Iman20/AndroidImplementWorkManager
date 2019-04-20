package com.widyatama.android_implement_workmanager.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.widyatama.android_implement_workmanager.utils.Constants
import com.widyatama.android_implement_workmanager.workers.WorkerUtils.*
import java.io.FileNotFoundException


/**
 * Created by iman on 11/03/2019.
 */

class BlurWorker(context: Context, params : WorkerParameters) : Worker(context, params) {

    private val TAG by lazy { BlurWorker::class.java.simpleName }

    override fun doWork(): Result {
        val appContext = applicationContext
        WorkerUtils.makeStatusNotification("Blurring image", appContext)
        sleep()

        val resourceUri = inputData.getString(Constants.KEY_IMAGE_URI)

        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }
            val resolver = appContext.contentResolver
            val bitmap = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
            )
            val output = blurBitmap(bitmap, appContext)
            val outputUri = writeBitmapToFile(appContext, output)
            val outputData = Data.Builder().putString(
                Constants.KEY_IMAGE_URI, outputUri.toString()
            ).build()
            Result.success(outputData)
        } catch (fileNotFoundException : FileNotFoundException) {
            Log.e(TAG, "Failed to decode input stream", fileNotFoundException)
            throw RuntimeException("Failed to decode input stream", fileNotFoundException)

        } catch (throwable : Throwable){
            Log.e(TAG, "Error applying Blur", throwable)
            Result.failure()
        }

    }
}