package com.tac.casemanagementapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tac.casemanagementapp.R
import com.tac.casemanagementapp.adapters.CaseAdapter
import com.tac.casemanagementapp.adapters.CaseListener
import com.tac.casemanagementapp.databinding.ActivityCaseListBinding
import com.tac.casemanagementapp.main.MainApp
import com.tac.casemanagementapp.models.CaseModel
import timber.log.Timber

// Activity that displays all cases in a RecyclerView with search and delete features
class CaseListActivity : AppCompatActivity(), CaseListener {

    private lateinit var binding: ActivityCaseListBinding // view binding for layout
    lateinit var app: MainApp                              // global app reference
    private lateinit var adapter: CaseAdapter              // adapter reference for filtering

    // Launcher for creating a new case
    private val caseActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            refreshList() // refresh when coming back
        }

    // Launcher for editing an existing case
    private val caseEditLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            refreshList() // refresh when edit completes
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize global app object
        app = application as MainApp
        Timber.i("CaseListActivity started")

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CaseAdapter(app.cases.findAll(), this)
        binding.recyclerView.adapter = adapter

        // Floating Action Button: add new case
        binding.fab.setOnClickListener {
            val launcherIntent = Intent(this, CaseActivity::class.java)
            caseActivityLauncher.launch(launcherIntent)
        }
    }

    // Inflate the search menu in the toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu) // Inflate search icon
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.queryHint = "Search by case details..."

        // Listen for text changes to filter RecyclerView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterCases(newText)
                return true
            }
        })

        return true
    }

    // Filter the list by description text (case details)
    private fun filterCases(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            app.cases.findAll()
        } else {
            app.cases.findAll().filter {
                it.description.contains(query, ignoreCase = true)
            }
        }
        adapter = CaseAdapter(filteredList, this)
        binding.recyclerView.adapter = adapter
    }

    // When user clicks a case card → open for editing
    override fun onCaseClick(case: CaseModel) {
        val launcherIntent = Intent(this, CaseActivity::class.java)
        launcherIntent.putExtra("case_edit", case)
        caseEditLauncher.launch(launcherIntent)
    }

    // When user clicks the delete icon → confirm and delete
    override fun onCaseDelete(case: CaseModel) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Case")
            .setMessage("Are you sure you want to delete '${case.title}'?")
            .setPositiveButton("Yes") { _, _ ->
                app.cases.delete(case) // remove from store
                refreshList() // refresh RecyclerView
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Helper: refresh list after add/edit/delete
    private fun refreshList() {
        Timber.i("Refreshing Case List")
        adapter = CaseAdapter(app.cases.findAll(), this)
        binding.recyclerView.adapter = adapter
    }
}
