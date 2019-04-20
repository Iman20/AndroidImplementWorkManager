package com.widyatama.android_implement_workmanager.utils

import android.content.Context
import android.widget.Toast
import com.widyatama.android_implement_workmanager.vo.Resource

class HandleResponse(context: Context, response: Resource<Any>){

    var responses : Resource<Any>? = null

    init {
        responses = response
        if (responses?.data == null){
            Toast.makeText(context, responses?.message, Toast.LENGTH_LONG).show()
        }
    }

    fun isSuccess(): Boolean{
        return responses?.data != null
    }
}