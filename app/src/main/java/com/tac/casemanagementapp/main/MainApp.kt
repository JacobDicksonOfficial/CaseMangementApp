package com.tac.casemanagementapp.main

import android.app.Application
import com.tac.casemanagementapp.models.CaseJSONStore
import com.tac.casemanagementapp.models.CaseStore
import timber.log.Timber

class MainApp : Application() {

    lateinit var cases: CaseStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        cases = CaseJSONStore(applicationContext)

        Timber.i("Case Management: TAC - Initiated (JSON Store)")
    }
}
