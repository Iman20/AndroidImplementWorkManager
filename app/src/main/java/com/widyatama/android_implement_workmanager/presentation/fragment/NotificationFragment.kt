package com.widyatama.android_implement_workmanager.presentation.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.widyatama.android_implement_workmanager.R
import com.widyatama.android_implement_workmanager.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_notification.*
import io.fabric.sdk.android.services.settings.IconRequest.build
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import org.json.JSONException
import android.widget.Toast
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import org.json.JSONArray




/**
 * Created by iman on 12/04/2019.
 */

class NotificationFragment : Fragment() {

    var result : String = ""
    var token : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult> {
                override fun onComplete(@NonNull task: Task<InstanceIdResult>) {
                    if (!task.isSuccessful) {
                        //To do//
                        return
                    }

                    // Get the Instance ID token//
                    token = task.result!!.token
                    Log.d("FCM", token)

                }
            })

        btnLocalNotification.setOnClickListener {
            context?.let { it1 -> CommonUtils.makeStatusNotification("This is from local", it1) }
        }

        btnFcmNotification.setOnClickListener {
            sendMessage(token)
        }

    }




    fun sendMessage(token : String) {
        doAsync {
            try {
                val root = JSONObject()
                val data = JSONObject()
                data.put("body", "Test Integrate")
                root.put("notification", data)
                root.put("to", token)

                result = postingToFirebase(root.toString())
                Log.d("chat Activity", "Result: $result")
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            uiThread {
                try {
                    val resultJson = JSONObject(result)
                    val success: Int
                    val failure: Int
                    success = resultJson.getInt("success")
                    failure = resultJson.getInt("failure")
                    Toast.makeText(
                        context,
                        "Message Success: " + success + "Message Failed: " + failure,
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(
                        context,
                        "Message Failed, Unknown error occurred.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    @Throws(IOException::class)
    fun postingToFirebase(bodyString: String): String {

        val mClient = OkHttpClient()
        val JSON = MediaType.parse("application/json; charset=utf-8")

        val body = RequestBody.create(JSON, bodyString)
        val request = Request.Builder()
            .url("https://fcm.googleapis.com/fcm/send")
            .post(body)
            .addHeader("Authorization", "key=" + "AAAAp2amFvg:APA91bHJRODhO-uK2hA5bns4EXJ6b7zL9J3KU-6TDi1rF_FMio7ALcwa9F8EXnRdIvnsZ-vpMKH7kItP-KWv6WvboVgfZxMjyfvUGdQFc3C33xrVsuSGg-8M3RULNS4ES8vGmHWdD9xk")
            .build()
        val response = mClient.newCall(request).execute()
        return response.body()?.string()!!
    }
}