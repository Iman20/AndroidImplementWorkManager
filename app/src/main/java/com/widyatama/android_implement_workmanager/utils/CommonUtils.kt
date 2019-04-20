package com.widyatama.android_implement_workmanager.utils

import android.annotation.*
import android.app.*
import android.content.*
import android.graphics.*
import android.graphics.drawable.*
import android.icu.text.SimpleDateFormat
import android.net.*
import android.os.*
import android.provider.*
import android.support.design.widget.*
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.text.*
import android.view.*
import com.widyatama.android_implement_workmanager.R
import com.widyatama.android_implement_workmanager.utils.Constants.CHANNEL_ID


import org.json.*

import java.text.NumberFormat
import java.util.*
import java.util.regex.*

object CommonUtils {

    private val TAG = "CommonUtils"
    val localeID: Locale
        get() = Locale("in", "ID")

    fun showLoadingDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings
                .Secure.ANDROID_ID)
    }

    fun isEmailValid(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher

        val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun loadJSONFromAsset(context: Context, jsonFileName: String): String {

        val manager = context.assets
        val `is` = manager.open(jsonFileName)

        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()

        return String(buffer, Charsets.UTF_8)
    }

    fun isJSONValid(test: String?): Boolean {

        if (test == null || test.isEmpty()) {
            return false
        }

        try {
            JSONObject(test)
        } catch (ex: JSONException) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                JSONArray(test)
            } catch (ex1: JSONException) {
                return false
            }

        }

        return true
    }


    fun getPriceFormat(locale: Locale, price: Double): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(locale)

        return currencyFormat.format(price)
    }

    fun getSplittedString(text: String, regex: String): Array<String> {
        return text.split(regex.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    fun getColor(context: Context, id: Int): Int {
        return if (Build.VERSION.SDK_INT >= 23) {
            context.resources.getColor(id, context.theme)
        } else context.resources.getColor(id)

    }

    fun getDrawable(context: Context, id: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= 21) {
            context.resources.getDrawable(id, context.theme)
        } else context.resources.getDrawable(id)
    }

    fun getTextFromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(html)
        }
    }


    fun setTypeface(context: Context, font: String): Typeface {
        return Typeface.createFromAsset(context.assets, font)
    }

    fun isServiceRunning(activity: Activity, serviceClass: Class<*>): Boolean {
        val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun makeStatusNotification(message: String, context: Context) {

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = Constants.VERBOSE_NOTIFICATION_CHANNEL_NAME
            val description = Constants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager?.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Local Notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        // Show the notification
        NotificationManagerCompat.from(context).notify(Constants.NOTIFICATION_ID, builder.build())
    }

}// this utility class is not publicy instantiable
