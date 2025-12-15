package com.tac.casemanagementapp.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.tac.casemanagementapp.R
import com.tac.casemanagementapp.views.caseslist.CaseListView

/**
 * Splash screen shown at app launch.
 * - Displays logo + loading bar for a short time
 * - Then navigates to the real main screen: CaseListView
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private val handler = Handler(Looper.getMainLooper())
    private var progressStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fullscreen splash (no action bar)
        supportActionBar?.hide()

        setContentView(R.layout.activity_splash)
        progressBar = findViewById(R.id.progressBar)

        startLoadingProgress()
    }

    private fun startLoadingProgress() {
        Thread {
            while (progressStatus < 100) {
                progressStatus += 1

                handler.post {
                    progressBar.progress = progressStatus
                }

                try {
                    Thread.sleep(35) // ~3 seconds total
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            handler.post {
                navigateToMain()
            }
        }.start()
    }

    private fun navigateToMain() {
        val intent = Intent(this, CaseListView::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
