package com.tac.casemanagementapp.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.tac.casemanagementapp.databinding.ActivityCaseBinding
import com.tac.casemanagementapp.helpers.showImagePicker
import com.tac.casemanagementapp.main.MainApp
import com.tac.casemanagementapp.models.CaseModel
import timber.log.Timber

// Activity for creating or editing a single case
class CaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaseBinding
    lateinit var app: MainApp
    var case = CaseModel()
    var edit = false

    // Image picker launcher
    private val imageIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data!!.data!!
                case.image = imageUri.toString() // save as image as string
                Picasso.get().load(imageUri).into(binding.caseImage)
                Timber.i("Image selected: ${case.image}")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        Timber.i("CaseActivity started")

        // Check if we're editing
        if (intent.hasExtra("case_edit")) {
            edit = true
            case = intent.extras?.getParcelable("case_edit")!!
            binding.caseTitle.setText(case.title)
            binding.caseDescription.setText(case.description)
            if (case.image.isNotEmpty()) {
                Picasso.get().load(Uri.parse(case.image)).into(binding.caseImage)
            }
            binding.btnSave.setText("Save Case")
        }

        // Save button handler
        binding.btnSave.setOnClickListener {
            case.title = binding.caseTitle.text.toString()
            case.description = binding.caseDescription.text.toString()
            if (edit) {
                app.cases.update(case)
            } else {
                app.cases.create(case)
            }
            setResult(RESULT_OK)
            finish()
        }

        // Add Image button handler
        binding.btnAddImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        // Cancel button handler
        binding.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
