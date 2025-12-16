package com.tac.casemanagementapp.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.tac.casemanagementapp.R
import com.tac.casemanagementapp.views.caseslist.CaseListView

/**
 * Login screen (Email/Password only).
 *
 * Users are created manually in Firebase Console:
 * Firebase Console -> Authentication -> Users -> Add user
 *
 * This screen only performs sign-in.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var signInButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fullscreen look (no action bar)
        supportActionBar?.hide()

        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        signInButton = findViewById(R.id.signInButton)

        signInButton.setOnClickListener { signInWithEmail() }
    }

    private fun signInWithEmail() {
        val email = emailInput.text?.toString()?.trim().orEmpty()
        val password = passwordInput.text?.toString()?.trim().orEmpty()

        if (email.isBlank()) {
            emailInput.error = "Email is required"
            emailInput.requestFocus()
            return
        }

        if (password.isBlank()) {
            passwordInput.error = "Password is required"
            passwordInput.requestFocus()
            return
        }

        // UX: prevent double taps during sign-in
        signInButton.isEnabled = false
        val oldText = signInButton.text
        signInButton.text = "Signing in..."

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                signInButton.isEnabled = true
                signInButton.text = oldText

                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                } else {
                    Toast.makeText(
                        this,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun navigateToMain() {
        val intent = Intent(this, CaseListView::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        // If already signed in, skip login screen
        if (auth.currentUser != null) {
            navigateToMain()
        }
    }
}
