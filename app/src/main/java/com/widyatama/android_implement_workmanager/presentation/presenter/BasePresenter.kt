package com.widyatama.android_implement_workmanager.presentation.presenter

import com.widyatama.android_implement_workmanager.view.BaseView


/**
 * Created by iman on 25/02/2019.
 */

interface BasePresenter<T: BaseView> {

    fun onDetach()

}