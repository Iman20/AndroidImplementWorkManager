package com.widyatama.android_implement_workmanager.presentation.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.work.WorkInfo
import com.bumptech.glide.Glide
import com.widyatama.android_implement_workmanager.R
import com.widyatama.android_implement_workmanager.presentation.presenter.MainPresenter
import com.widyatama.android_implement_workmanager.utils.Constants
import com.widyatama.android_implement_workmanager.view.MainView
import kotlinx.android.synthetic.main.activity_blur.*

/**
 * Created by iman mutaqin on 02/03/2019.
 */

class BlurImageActivity : AppCompatActivity(), MainView {

    private var presenter : MainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blur)
        presenter = MainPresenter(this, this)

        val intent = intent
        val imageUriExtra = intent.getStringExtra(Constants.KEY_IMAGE_URI)
        presenter?.setImageUri(imageUriExtra)
        if (presenter?.getImageUri() != null)
            Glide.with(this).load(presenter?.getImageUri()).into(image_view)
        go_button.setOnClickListener {
            presenter?.applyBlur(getBlurLevel())
        }
        see_file_button.setOnClickListener {
            val currentUri = presenter?.getOutputUri()
            if (currentUri != null) {
                val actionView = Intent(Intent.ACTION_VIEW, currentUri)
                if (actionView.resolveActivity(packageManager) != null) {
                    startActivity(actionView)
                }
            }
        }
        cancel_button.setOnClickListener {
            presenter?.cancelWork()
        }

        presenter?.getOutputWorkInfo()?.observe(this@BlurImageActivity, Observer { response ->
            if (response == null || response.isEmpty())
                return@Observer
            response?.let { proses(it) }
        })
    }

    private fun proses(it: List<WorkInfo>) {
        val workInfo = it.get(0)

        val finished = workInfo.getState().isFinished()
        if (!finished) {
            hideLoading()
        } else {
            showLoading()

            // Normally this processing, which is not directly related to drawing views on
            // screen would be in the ViewModel. For simplicity we are keeping it here.
            val outputData = workInfo.getOutputData()

            val outputImageUri = outputData.getString(Constants.KEY_IMAGE_URI)

            // If there is an output file show "See File" button
            if (!TextUtils.isEmpty(outputImageUri)) {
                if (outputImageUri != null) {
                    presenter?.setOutputUri(outputImageUri)
                }
                see_file_button.setVisibility(View.VISIBLE)
            }
        }


    }

    override fun onError(errorMsg: Any?) {
        if (errorMsg is String)
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG)
        else if (errorMsg is Int)
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG)
    }

    override fun showLoading() {
        progress_bar.setVisibility(View.GONE)
        cancel_button.setVisibility(View.GONE)
        go_button.setVisibility(View.VISIBLE)
    }

    override fun hideLoading() {
        progress_bar.setVisibility(View.VISIBLE)
        cancel_button.setVisibility(View.VISIBLE)
        go_button.setVisibility(View.GONE)
        see_file_button.setVisibility(View.GONE)
    }

    override fun onLiveData(list: LiveData<List<WorkInfo>>) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getBlurLevel(): Int {
        when (radio_blur_group.getCheckedRadioButtonId()) {
            R.id.radio_blur_lv_1 -> return 1
            R.id.radio_blur_lv_2 -> return 2
            R.id.radio_blur_lv_3 -> return 3
        }

        return 1
    }
}
