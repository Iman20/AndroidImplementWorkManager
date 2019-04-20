package com.widyatama.android_implement_workmanager.presentation.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.work.WorkManager
import com.widyatama.android_implement_workmanager.R
import com.widyatama.android_implement_workmanager.presentation.fragment.NotificationFragment
import com.widyatama.android_implement_workmanager.presentation.fragment.WorkManagerFragment
import kotlinx.android.synthetic.main.activity_container.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        val fragmentManager = supportFragmentManager

        val worManagerFragment = WorkManagerFragment()
        val notificationFragment = NotificationFragment()

        val fragmentTrasaction = fragmentManager.beginTransaction()
        fragmentTrasaction.replace(R.id.fragment, worManagerFragment).commit()

        btnWorkManager.setOnClickListener {
            val fragmentTrasaction = fragmentManager.beginTransaction()
            fragmentTrasaction.replace(R.id.fragment, worManagerFragment).commit()
        }

        btnNotification.setOnClickListener {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment, notificationFragment).commit()

        }
    }
}
