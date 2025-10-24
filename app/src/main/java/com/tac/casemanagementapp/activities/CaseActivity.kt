package com.tac.casemanagementapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.tac.casemanagementapp.R
import com.tac.casemanagementapp.databinding.ActivityCaseBinding
import com.tac.casemanagementapp.helpers.showImagePicker
import com.tac.casemanagementapp.main.MainApp
import com.tac.casemanagementapp.models.CaseModel
import timber.log.Timber
import timber.log.Timber.i

class CaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaseBinding
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var app: MainApp
    private var case = CaseModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        Timber.plant(Timber.DebugTree())
        i("CaseActivity started")

        // --- Register Image Picker Callback ---
        registerImagePickerCallback()

        // --- Handle Add Case Button ---
        binding.btnAdd.setOnClickListener { view ->
            val name = binding.caseName.text.toString()
            val details = binding.caseDetails.text.toString()

            if (name.isEmpty()) {
                Snackbar.make(view, R.string.enter_case_name, Snackbar.LENGTH_LONG).show()
            } else {
                case.name = name
                case.details = details
                app.cases.create(case.copy())
                i("Added case: $case")
                Snackbar.make(view, getString(R.string.case_added), Snackbar.LENGTH_SHORT).show()
                finish()
            }
        }


        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got image result: ${result.data!!.data}")
                            case.image = result.data!!.data!!
                            Picasso.get()
                                .load(case.image)
                                .into(binding.caseImage)
                        }
                    }
                    RESULT_CANCELED -> Timber.i("Image selection canceled")
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_case, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_cancel) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
