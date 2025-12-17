package com.tac.casemanagementapp.main

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.tac.casemanagementapp.models.CaseJSONStore
import com.tac.casemanagementapp.models.CaseStore
import timber.log.Timber

/**
 * Application class (runs once when the app starts).
 * Used for:
 * - Setting up logging (Timber)
 * - Loading theme preference (dark/light)
 * - Creating the main data store (CaseStore)
 */
class MainApp : Application() {

    // Global store that can be accessed by Activities/Presenters
    lateinit var cases: CaseStore

    override fun onCreate() {
        super.onCreate()

        // Enables Timber logging for debugging
        Timber.plant(Timber.DebugTree())

        // Read saved theme preference (Dark mode is default)
        val prefs = getSharedPreferences("tac_prefs", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", true)

        // Applies theme mode across the entire app
        AppCompatDelegate.setDefaultNightMode(
            if (isDark)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        // Uses JSON storage for cases (saved in internal storage)
        cases = CaseJSONStore(applicationContext)

        Timber.i("Case Management: TAC - Initiated (JSON Store)")
    }
}
