package com.widyatama.android_implement_workmanager.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.work.*
import com.widyatama.android_implement_workmanager.R
import com.widyatama.android_implement_workmanager.presentation.activity.OneTimeActivity
import com.widyatama.android_implement_workmanager.presentation.activity.PeriodicTimeActivity
import kotlinx.android.synthetic.main.activity_home.*


/**
 * Created by iman on 12/04/2019.
 */

class WorkManagerFragment : Fragment() {

    private var mWorkManager: WorkManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mWorkManager = WorkManager.getInstance()

        btnOneTime.setOnClickListener {
            startActivity(Intent(context, OneTimeActivity::class.java))
        }

        btnPeriodic.setOnClickListener {
            startActivity(Intent(context, PeriodicTimeActivity::class.java))

        }

    }
}