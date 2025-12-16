package com.tac.casemanagementapp.views.caseslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.tac.casemanagementapp.R
import com.tac.casemanagementapp.adapters.CaseAdapter
import com.tac.casemanagementapp.adapters.CaseListener
import com.tac.casemanagementapp.databinding.ActivityCaseListBinding
import com.tac.casemanagementapp.main.LoginActivity
import com.tac.casemanagementapp.models.CaseModel
import com.tac.casemanagementapp.presenters.CaseListPresenter
import timber.log.Timber

class CaseListView : AppCompatActivity(), CaseListener {

    private lateinit var binding: ActivityCaseListBinding
    lateinit var presenter: CaseListPresenter
    private lateinit var adapter: CaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCaseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = CaseListPresenter(this)
        Timber.i("CaseListView started")

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CaseAdapter(presenter.getCases(), this)
        binding.recyclerView.adapter = adapter

        // Add new case FAB
        binding.fab.setOnClickListener {
            presenter.doAddCase()
        }

        // Theme toggle FAB (NEW)
        binding.themeFab.setOnClickListener {
            toggleTheme()
        }

        updateThemeFabIcon()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.queryHint = "Search by case details..."

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterCases(newText)
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun filterCases(query: String?) {
        val filteredList = presenter.getFilteredCases(query)
        adapter = CaseAdapter(filteredList, this)
        binding.recyclerView.adapter = adapter
    }

    override fun onCaseClick(case: CaseModel) {
        presenter.doEditCase(case)
    }

    override fun onCaseDelete(case: CaseModel) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Case")
            .setMessage("Are you sure you want to delete '${case.title}'?")
            .setPositiveButton("Yes") { _, _ ->
                presenter.doDeleteCase(case)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun onRefresh() {
        Timber.i("Refreshing Case List")
        adapter = CaseAdapter(presenter.getCases(), this)
        binding.recyclerView.adapter = adapter
    }

    // =====================================================
    // Theme toggle logic
    // =====================================================

    private fun toggleTheme() {
        val prefs = getSharedPreferences("tac_prefs", Context.MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", true)

        val newIsDark = !isDark
        prefs.edit().putBoolean("dark_mode", newIsDark).apply()

        AppCompatDelegate.setDefaultNightMode(
            if (newIsDark)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        updateThemeFabIcon()
    }

    private fun updateThemeFabIcon() {
        val prefs = getSharedPreferences("tac_prefs", Context.MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", true)

        binding.themeFab.setImageResource(
            if (isDark) R.drawable.ic_light_mode_24
            else R.drawable.ic_dark_mode_24
        )
    }
}
