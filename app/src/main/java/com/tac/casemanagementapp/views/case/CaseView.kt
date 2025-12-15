package com.tac.casemanagementapp.views.case

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.tac.casemanagementapp.R
import com.tac.casemanagementapp.databinding.ActivityCaseBinding
import com.tac.casemanagementapp.helpers.showImagePicker
import com.tac.casemanagementapp.models.CaseModel
import com.tac.casemanagementapp.presenters.CasePresenter
import timber.log.Timber

/**
 * View (UI layer) for creating/editing a Case.
 * - Handles widgets, displaying data, and launching the image picker.
 * - Delegates all create/update/delete logic to the Presenter.
 */
class CaseView : AppCompatActivity() {

    private lateinit var binding: ActivityCaseBinding
    lateinit var presenter: CasePresenter

    // Image picker launcher (UI responsibility)
    private val imageIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data!!.data!!
                presenter.setImage(imageUri.toString())
                Timber.i("Image selected: $imageUri")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = CasePresenter(this)
        Timber.i("CaseView started")

        setupGenderSpinner()

        // Save
        binding.btnSave.setOnClickListener {
            presenter.doSave(
                title = binding.caseTitle.text.toString(),
                description = binding.caseDescription.text.toString(),
                gender = binding.genderSpinner.selectedItem.toString()
            )
        }

        // Add Image
        binding.btnAddImage.setOnClickListener {
            // Cache typed text before leaving the screen (so it isn’t lost)
            presenter.cacheCase(
                title = binding.caseTitle.text.toString(),
                description = binding.caseDescription.text.toString(),
                gender = binding.genderSpinner.selectedItem.toString()
            )
            showImagePicker(imageIntentLauncher)
        }

        // Cancel
        binding.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            presenter.doCancel()
        }
    }

    private fun setupGenderSpinner() {
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            genderOptions
        )
        binding.genderSpinner.adapter = spinnerAdapter
    }

    /**
     * Called by Presenter when editing an existing Case.
     * Populates UI from the model.
     */
    fun showCase(case: CaseModel) {
        binding.caseTitle.setText(case.title)
        binding.caseDescription.setText(case.description)

        val genderOptions = resources.getStringArray(R.array.gender_options)
        val genderIndex = genderOptions.indexOf(case.gender)
        if (genderIndex >= 0) binding.genderSpinner.setSelection(genderIndex)

        if (case.image.isNotEmpty()) {
            Picasso.get().load(Uri.parse(case.image)).into(binding.caseImage)
        }

        binding.btnSave.text = getString(R.string.save_case)
    }

    /**
     * Called by Presenter after an image is selected.
     * Updates the ImageView.
     */
    fun updateImage(image: String) {
        if (image.isNotEmpty()) {
            Picasso.get().load(Uri.parse(image)).into(binding.caseImage)
        }
    }
}
