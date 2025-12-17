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
 * Login screen for Email/Password sign-in using Firebase Authentication.
 * - Users must already exist in Firebase Console (this screen does sign-in only).
 * - If user is already logged in, we skip this screen.
 */
class LoginActivity : AppCompatActivity() {

    // FirebaseAuth object used to sign in and check current user
    private lateinit var auth: FirebaseAuth

    // UI references from activity_login.xml
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var signInButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the action bar for a clean fullscreen login look
        supportActionBar?.hide()

        // Connect this Activity to its XML layout
        setContentView(R.layout.activity_login)

        // Fetches FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Link Kotlin variables to the views in the XML
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        signInButton = findViewById(R.id.signInButton)

        // When the button is pressed, attempt login
        signInButton.setOnClickListener { signInWithEmail() }
    }

    // Handles Firebase sign-in using entered email + password
    private fun signInWithEmail() {
        // Read and clean up text from input fields
        val email = emailInput.text?.toString()?.trim().orEmpty()
        val password = passwordInput.text?.toString()?.trim().orEmpty()

        // Basic validation: email must not be empty
        if (email.isBlank()) {
            emailInput.error = "Email is required"
            emailInput.requestFocus()
            return
        }

        // Basic validation: password must not be empty
        if (password.isBlank()) {
            passwordInput.error = "Password is required"
            passwordInput.requestFocus()
            return
        }

        // Disable button to prevent multiple login requests
        signInButton.isEnabled = false
        val oldText = signInButton.text
        signInButton.text = "Signing in..."

        // Firebase sign-in call
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                // Enable button and restore text when finished
                signInButton.isEnabled = true
                signInButton.text = oldText

                if (task.isSuccessful) {
                    // Login worked flows into the main screen
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                } else {
                    // if Login failed it shows error message from Firebase
                    Toast.makeText(
                        this,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    // Opens the main Case list screen and clears the back stack
    private fun navigateToMain() {
        val intent = Intent(this, CaseListView::class.java)

        // Clear all previous activities so user can't go "back" to login
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        // If user is already logged in, skip the login screen
        if (auth.currentUser != null) {
            navigateToMain()
        }
    }
}
