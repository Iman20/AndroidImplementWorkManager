package com.widyatama.android_implement_workmanager.view

import android.arch.lifecycle.LiveData
import androidx.work.WorkInfo

/**
 * Created by iman mutaqin on 02/03/2019.
 */

interface MainView : BaseView{

    fun onLiveData(list: LiveData<List<WorkInfo>>)

    fun onError(errorMsg: Any? = null)

}