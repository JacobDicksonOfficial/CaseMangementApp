package com.tac.casemanagementapp.main

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.tac.casemanagementapp.models.CaseJSONStore
import com.tac.casemanagementapp.models.CaseStore
import timber.log.Timber

class MainApp : Application() {

    lateinit var cases: CaseStore

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        // Apply saved theme (Dark by default)
        val prefs = getSharedPreferences("tac_prefs", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", true)

        AppCompatDelegate.setDefaultNightMode(
            if (isDark)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        cases = CaseJSONStore(applicationContext)

        Timber.i("Case Management: TAC - Initiated (JSON Store)")
    }
}
