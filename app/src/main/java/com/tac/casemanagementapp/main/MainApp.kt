package com.tac.casemanagementapp.main

import android.app.Application
import com.tac.casemanagementapp.models.CaseMemStore
import com.tac.casemanagementapp.models.CaseStore
import timber.log.Timber

// Main Application class (created once when the app launches)
// Holds shared data stores and plants Timber for logging
class MainApp : Application() {

    lateinit var cases: CaseStore  // global access to the in-memory store

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())  // one global Timber setup
        cases = CaseMemStore()            // initialize in-memory data store
        Timber.i("Case Management: TAC - Initiated")
    }
}
