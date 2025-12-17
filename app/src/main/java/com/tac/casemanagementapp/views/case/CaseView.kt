package com.tac.casemanagementapp.views.case

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
import com.tac.casemanagementapp.models.CaseModel
import com.tac.casemanagementapp.presenters.CasePresenter
import com.tac.casemanagementapp.views.map.MapViewActivity

import timber.log.Timber

class CaseView : AppCompatActivity() {

    private lateinit var binding: ActivityCaseBinding
    lateinit var presenter: CasePresenter

    /* ---------- IMAGE PICKER ---------- */
    private val imageIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri: Uri = result.data!!.data!!
                presenter.setImage(imageUri.toString())
                Timber.i("Image selected: $imageUri")
            }
        }

    /* ---------- MAP PICKER ---------- */
    private val mapIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {

                val lat = result.data!!.getDoubleExtra("latitude", 0.0)
                val lng = result.data!!.getDoubleExtra("longitude", 0.0)
                val address = result.data!!.getStringExtra("address") ?: ""

                presenter.setLocation(lat, lng, address)
                binding.locationText.setText(address)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = CasePresenter(this)
        Timber.i("CaseView started")

        setupGenderSpinner()

        binding.btnSave.setOnClickListener {
            presenter.doSave(
                binding.caseTitle.text.toString(),
                binding.caseDescription.text.toString(),
                binding.genderSpinner.selectedItem.toString()
            )
        }

        binding.btnCancel.setOnClickListener {
            presenter.doCancel()
        }

        binding.btnAddImage.setOnClickListener {
            cacheForm()
            showImagePicker(imageIntentLauncher)
        }

        binding.btnPickLocation.setOnClickListener {
            cacheForm()
            mapIntentLauncher.launch(
                Intent(this, MapViewActivity::class.java)
            )
        }
    }

    private fun setupGenderSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.gender_options)
        )
        binding.genderSpinner.adapter = adapter
    }

    private fun cacheForm() {
        presenter.cacheCase(
            binding.caseTitle.text.toString(),
            binding.caseDescription.text.toString(),
            binding.genderSpinner.selectedItem.toString()
        )
    }

    fun showCase(case: CaseModel) {
        binding.caseTitle.setText(case.title)
        binding.caseDescription.setText(case.description)
        binding.locationText.setText(case.address)

        val genders = resources.getStringArray(R.array.gender_options)
        val index = genders.indexOf(case.gender)
        if (index >= 0) binding.genderSpinner.setSelection(index)

        if (case.image.isNotEmpty()) {
            Picasso.get().load(Uri.parse(case.image)).into(binding.caseImage)
        }

        binding.btnSave.text = getString(R.string.save_case)
    }

    fun updateImage(image: String) {
        if (image.isNotEmpty()) {
            Picasso.get().load(Uri.parse(image)).into(binding.caseImage)
        }
    }
}
