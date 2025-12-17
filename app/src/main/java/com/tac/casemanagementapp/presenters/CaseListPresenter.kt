package com.tac.casemanagementapp.presenters

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.tac.casemanagementapp.main.MainApp
import com.tac.casemanagementapp.models.CaseModel
import com.tac.casemanagementapp.views.case.CaseView
import com.tac.casemanagementapp.views.caseslist.CaseListView

/**
 * Presenter for CaseListView.
 * - Handles business logic for the case list screen
 * - Talks to the data store
 * - Handles navigation to CaseView
 */
class CaseListPresenter(private val view: CaseListView) {

    // Accesses the Application object to reach the CaseStore
    private val app: MainApp = view.application as MainApp

    // Launcher used to refresh the list when returning from CaseView
    private lateinit var refreshLauncher: ActivityResultLauncher<Intent>

    init {
        // This Registers callback as soon as presenter is created
        registerRefreshCallback()
    }

    // Returns all cases from the store
    fun getCases(): List<CaseModel> = app.cases.findAll()

    /**
     * Returns a filtered list of cases based on search text
     * Searches in:
     * - description
     * - title
     * - address
     */
    fun getFilteredCases(query: String?): List<CaseModel> {
        val all = app.cases.findAll()
        if (query.isNullOrBlank()) return all

        val lowerQuery = query.lowercase()

        return all.filter { case ->
            case.description.lowercase().contains(lowerQuery) ||
                    case.title.lowercase().contains(lowerQuery) ||
                    case.address.lowercase().contains(lowerQuery)
        }
    }

    // Opens CaseView to add a new case
    fun doAddCase() {
        val intent = Intent(view, CaseView::class.java)
        refreshLauncher.launch(intent)
    }

    // Opens CaseView to edit an existing case
    fun doEditCase(case: CaseModel) {
        val intent = Intent(view, CaseView::class.java)
        intent.putExtra("case_edit", case)
        refreshLauncher.launch(intent)
    }

    // Deletes a case and refreshes the list
    fun doDeleteCase(case: CaseModel) {
        app.cases.delete(case)
        view.onRefresh()
    }

    // Registers a callback to refresh list after CaseView closes
    private fun registerRefreshCallback() {
        refreshLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    view.onRefresh()
                }
            }
    }
}
