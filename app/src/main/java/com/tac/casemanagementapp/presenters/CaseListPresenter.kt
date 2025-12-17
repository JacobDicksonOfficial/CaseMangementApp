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
 * Owns navigation to CaseView and store operations (delete).
 */
class CaseListPresenter(private val view: CaseListView) {

    private val app: MainApp = view.application as MainApp

    private lateinit var refreshLauncher: ActivityResultLauncher<Intent>

    init {
        registerRefreshCallback()
    }

    fun getCases(): List<CaseModel> = app.cases.findAll()

    /**
     * Enhanced search:
     * - Description
     * - Title
     * - Location address
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

    fun doAddCase() {
        val intent = Intent(view, CaseView::class.java)
        refreshLauncher.launch(intent)
    }

    fun doEditCase(case: CaseModel) {
        val intent = Intent(view, CaseView::class.java)
        intent.putExtra("case_edit", case)
        refreshLauncher.launch(intent)
    }

    fun doDeleteCase(case: CaseModel) {
        app.cases.delete(case)
        view.onRefresh()
    }

    private fun registerRefreshCallback() {
        refreshLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    view.onRefresh()
                }
            }
    }
}
