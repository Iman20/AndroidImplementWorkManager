package com.widyatama.android_implement_workmanager.api

import retrofit2.Response
import java.io.IOException

class APIResponse(response: Response<Any>){


    var body : Any? = null
    var code : Int? = null
    var errorMessage: String? = null

    init {
        code = response.code()
        if(response.isSuccessful()){
            body = response.body()
            errorMessage = null
        } else {
            var message: String? = null
            if (response.body() != null){
                try {
                    message = response.errorBody()!!.string()
                } catch (ignored: IOException) {
                    ignored.printStackTrace()
                }

            }
            if (message == null || message.trim { it <= ' ' }.length == 0) {
                message = response.message()
            }
            errorMessage = message
            body = null
        }
    }
//
//    constructor(error: Throwable){
//        code = 500
//        body = null
//        errorMessage = error.message
//    }

    fun isSuccessful(): Boolean {
        return code!! >= 200 && code!! < 300
    }
}