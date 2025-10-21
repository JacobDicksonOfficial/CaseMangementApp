package com.tac.casemanagementapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.tac.casemanagementapp.R
import com.tac.casemanagementapp.databinding.ActivityCaseBinding
import timber.log.Timber

class CaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        Timber.plant(Timber.DebugTree())
        Timber.i("CaseActivity started")

        binding.btnAdd.setOnClickListener { view ->
            val name = binding.caseName.text.toString()
            val details = binding.caseDetails.text.toString()

            if (name.isEmpty()) {
                Snackbar.make(view, R.string.enter_case_name, Snackbar.LENGTH_LONG).show()
            } else {
                Timber.i("Added case: $name ($details)")
                Snackbar.make(view, getString(R.string.case_added), Snackbar.LENGTH_SHORT).show()
                finish()
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
