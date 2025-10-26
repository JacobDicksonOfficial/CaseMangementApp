package com.tac.casemanagementapp.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.tac.casemanagementapp.R
import com.tac.casemanagementapp.databinding.ActivityCaseBinding
import com.tac.casemanagementapp.helpers.showImagePicker
import com.tac.casemanagementapp.main.MainApp
import com.tac.casemanagementapp.models.CaseModel
import timber.log.Timber

// Activity for creating or editing a single case
class CaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaseBinding  // ViewBinding for layout
    lateinit var app: MainApp                           // Reference to main app
    var case = CaseModel()                              // Current case being edited
    var edit = false                                    // Edit mode toggle

    // Image picker launcher
    private val imageIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data!!.data!!
                case.image = imageUri.toString() // ✅ Save image as string
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

        // 🔹 Setup Spinner for Gender Selection
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            genderOptions
        )
        binding.genderSpinner.adapter = spinnerAdapter

        // 🔹 Check if Editing an Existing Case
        if (intent.hasExtra("case_edit")) {
            edit = true
            case = intent.extras?.getParcelable("case_edit")!!
            binding.caseTitle.setText(case.title)
            binding.caseDescription.setText(case.description)

            // Set gender spinner to saved value
            val genderIndex = genderOptions.indexOf(case.gender)
            if (genderIndex >= 0) binding.genderSpinner.setSelection(genderIndex)

            // Show saved image
            if (case.image.isNotEmpty()) {
                Picasso.get().load(Uri.parse(case.image)).into(binding.caseImage)
            }

            binding.btnSave.setText("Save Case")
        }

        // 🔹 Save Button
        binding.btnSave.setOnClickListener {
            case.title = binding.caseTitle.text.toString()
            case.description = binding.caseDescription.text.toString()
            case.gender = binding.genderSpinner.selectedItem.toString() // ✅ Save selected gender

            if (edit) {
                app.cases.update(case)
            } else {
                app.cases.create(case)
            }

            setResult(RESULT_OK)
            finish()
        }

        // 🔹 Add Image Button
        binding.btnAddImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        // 🔹 Cancel Button
        binding.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
