package com.widyatama.android_implement_workmanager.api

import com.widyatama.android_implement_workmanager.BuildConfig
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*
import retrofit2.http.POST



/**
 * Created by iman mutaqin on 02/03/2019.
 */

interface APIService {

    @GET("movie/now_playing")
    fun getFilm(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = BuildConfig.LANGUAGE,
        @Query("page") page: Int
    ): Observable<String>

    @GET("1366630/pexels-photo-1366630.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940")
    fun getPhoto(): Observable<ResponseBody>

}