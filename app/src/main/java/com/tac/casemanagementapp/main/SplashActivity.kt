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
 * - Shows logo + animated loading bar
 * - Bar always completes to 100%
 * - Routes based on Firebase auth state
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private val handler = Handler(Looper.getMainLooper())

    private val splashDurationMs = 2500L
    private val tickMs = 25L
    private val maxProgress = 100

    private var progress = 0

    private val progressRunnable = object : Runnable {
        override fun run() {
            if (progress < maxProgress) {
                progress++
                progressBar.progress = progress
                handler.postDelayed(this, tickMs)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_splash)
        progressBar = findViewById(R.id.progressBar)

        progressBar.max = maxProgress
        progressBar.progress = 0

        // Start smooth progress animation
        handler.post(progressRunnable)

        // Finish splash AFTER progress visually completes
        handler.postDelayed({
            forceCompleteAndNavigate()
        }, splashDurationMs)
    }

    /**
     * Ensures progress bar finishes before navigation
     */
    private fun forceCompleteAndNavigate() {
        progressBar.progress = maxProgress
        navigateNext()
    }

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
