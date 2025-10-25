package com.tac.casemanagementapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tac.casemanagementapp.adapters.CaseAdapter
import com.tac.casemanagementapp.adapters.CaseListener
import com.tac.casemanagementapp.databinding.ActivityCaseListBinding
import com.tac.casemanagementapp.main.MainApp
import com.tac.casemanagementapp.models.CaseModel
import timber.log.Timber

// Displays all cases in a RecyclerView
class CaseListActivity : AppCompatActivity(), CaseListener {

    private lateinit var binding: ActivityCaseListBinding
    lateinit var app: MainApp

    // Launcher for creating new case
    private val caseActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            refreshList()
        }

    // Launcher for editing existing case
    private val caseEditLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            refreshList()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        Timber.i("CaseListActivity started")

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = CaseAdapter(app.cases.findAll(), this)

        // Floating Action Button to add a new case
        binding.fab.setOnClickListener {
            val launcherIntent = Intent(this, CaseActivity::class.java)
            caseActivityLauncher.launch(launcherIntent)
        }
    }

    // Handles click on an existing case
    override fun onCaseClick(case: CaseModel) {
        val launcherIntent = Intent(this, CaseActivity::class.java)
        launcherIntent.putExtra("case_edit", case)
        caseEditLauncher.launch(launcherIntent)
    }

    // Refreshes list when returning from add/edit screen
    private fun refreshList() {
        Timber.i("Refreshing Case List")
        binding.recyclerView.adapter = CaseAdapter(app.cases.findAll(), this)
    }
}
