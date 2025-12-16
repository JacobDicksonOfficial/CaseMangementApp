package com.tac.casemanagementapp.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tac.casemanagementapp.R
import com.tac.casemanagementapp.views.caseslist.CaseListView

/**
 * Splash screen shown at app launch.
 * - Displays logo + loading bar briefly
 * - Then routes based on Firebase authentication state:
 *   - Logged in  -> CaseListView
 *   - Not logged -> LoginActivity
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
                    Thread.sleep(35) // adjust if you want longer/shorter splash
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            handler.post {
                navigateNext()
            }
        }.start()
    }

    /**
     * Decides where to go after splash:
     * - If a Firebase user exists -> go straight to the app list screen
     * - Otherwise -> require login
     */
    private fun navigateNext() {
        val auth = FirebaseAuth.getInstance()

        val intent = if (auth.currentUser != null) {
            Intent(this, CaseListView::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
