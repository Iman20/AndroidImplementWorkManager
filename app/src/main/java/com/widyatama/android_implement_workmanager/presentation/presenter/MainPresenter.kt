package com.widyatama.android_implement_workmanager.presentation.presenter

import android.arch.lifecycle.LiveData
import android.net.Uri
import android.text.TextUtils
import androidx.work.*
import com.widyatama.android_implement_workmanager.presentation.activity.BlurImageActivity
import com.widyatama.android_implement_workmanager.api.APIService
import com.widyatama.android_implement_workmanager.di.CurrencyApplication
import com.widyatama.android_implement_workmanager.utils.Constants.IMAGE_MANIPULATION_WORK_NAME
import com.widyatama.android_implement_workmanager.utils.Constants.KEY_IMAGE_URI
import com.widyatama.android_implement_workmanager.utils.Constants.TAG_OUTPUT
import com.widyatama.android_implement_workmanager.view.MainView
import com.widyatama.android_implement_workmanager.workers.BlurWorker
import com.widyatama.android_implement_workmanager.workers.CleanupWorker
import com.widyatama.android_implement_workmanager.workers.SaveImageToFileWorker
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by iman mutaqin on 02/03/2019.
 */

class MainPresenter(
    private var context : BlurImageActivity,
    private var view: MainView? ): BasePresenter<MainView>{

    private var mWorkManager: WorkManager? = null
    private var mImageUri: Uri? = null
    private var mOutputUri: Uri? = null
    private val mSavedWorkInfo: LiveData<List<WorkInfo>>

    init {
        initializeDagger()
        mWorkManager = WorkManager.getInstance()
        mSavedWorkInfo = mWorkManager!!.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    @Inject
    lateinit var apiService: APIService

    private var myCompositeDisposable: CompositeDisposable? = null


    fun applyBlur(blurLevel : Int){
        var continuation = mWorkManager
            ?.beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )
        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequest.Builder(BlurWorker::class.java!!)

            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }

            continuation = continuation?.then(blurBuilder.build())
        }

        // Create charging constraint
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        val save = OneTimeWorkRequest.Builder(SaveImageToFileWorker::class.java!!)
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()
        continuation = continuation?.then(save)

        // Actually start the work
        continuation?.enqueue()
    }

    fun cancelWork(){
        mWorkManager?.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        if (mImageUri != null) {
            builder.putString(KEY_IMAGE_URI, mImageUri.toString())
        }
        return builder.build()
    }

    private fun uriOrNull(uriString: String): Uri? {
        return if (!TextUtils.isEmpty(uriString)) {
            Uri.parse(uriString)
        } else null
    }

    fun setImageUri(uri: String) {
        mImageUri = uriOrNull(uri)
    }

    fun setOutputUri(outputImageUri: String) {
        mOutputUri = uriOrNull(outputImageUri)
    }

    fun getImageUri(): Uri {
        return mImageUri!!
    }

    fun getOutputUri(): Uri {
        return mOutputUri!!
    }

    fun getOutputWorkInfo() : LiveData<List<WorkInfo>>{
        return mSavedWorkInfo
    }

    override fun onDetach() {
        view = null
        mWorkManager?.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    private fun initializeDagger() = CurrencyApplication.appComponent.inject(this)
}