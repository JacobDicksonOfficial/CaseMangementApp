package com.tac.casemanagementapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tac.casemanagementapp.R
import timber.log.Timber

class CaseListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_list)

        Timber.plant(Timber.DebugTree())
        Timber.i("CaseListActivity started")
    }
}
