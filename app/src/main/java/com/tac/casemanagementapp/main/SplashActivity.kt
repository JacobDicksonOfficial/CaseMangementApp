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
 * Splash screen shown when the app launches.
 * - Shows a logo + progress bar animation
 * - Progress bar always fills to 100%
 * - After the splash time finishes, it navigates:
 *   - Logged in user to CaseListView
 *   - Not logged in to LoginActivity
 */
class SplashActivity : AppCompatActivity() {

    // Progress bar shown on splash screen
    private lateinit var progressBar: ProgressBar

    // Handler used to run timed updates on the main thread
    private val handler = Handler(Looper.getMainLooper())

    // Splash timing settings
    private val splashDurationMs = 2500L  // Total time splash screen stays visible
    private val tickMs = 25L              // How often progress increments
    private val maxProgress = 100         // End value of progress bar

    // Current progress value
    private var progress = 0

    // Runnable that increases progress smoothly until it reaches 100
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

        // Hide action bar for fullscreen splash look
        supportActionBar?.hide()

        // Connect Activity to its layout
        setContentView(R.layout.activity_splash)

        // Get progress bar from XML
        progressBar = findViewById(R.id.progressBar)

        // Setup progress bar
        progressBar.max = maxProgress
        progressBar.progress = 0

        // Start smooth progress animation
        handler.post(progressRunnable)

        // After splash duration, complete progress and navigate
        handler.postDelayed({
            forceCompleteAndNavigate()
        }, splashDurationMs)
    }

    // Forces progress to 100% before navigating away
    private fun forceCompleteAndNavigate() {
        progressBar.progress = maxProgress
        navigateNext()
    }

    // Decides where to go next based on Firebase login state
    private fun navigateNext() {
        val auth = FirebaseAuth.getInstance()

        // If logged in then it enters CaseListView, or else it enters LoginActivity page
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

        // Stop any pending callbacks to avoid leaks/crashes
        handler.removeCallbacksAndMessages(null)
    }
}
