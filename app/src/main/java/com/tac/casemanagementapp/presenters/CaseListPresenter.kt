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
 * Presenter for the list screen.
 * This is for navigation to (add/edit) and tells the View when to refresh/remove.
 */
class CaseListPresenter(private val view: CaseListView) {

    private val app: MainApp = view.application as MainApp
    private lateinit var refreshLauncher: ActivityResultLauncher<Intent>
    private var position: Int = 0

    init {
        registerRefreshCallback()
    }

    fun getCases() = app.cases.findAll()

    fun doAddCase() {
        val intent = Intent(view, CaseView::class.java)
        refreshLauncher.launch(intent)
    }

    fun doEditCase(case: CaseModel, pos: Int) {
        position = pos
        val intent = Intent(view, CaseView::class.java)
        intent.putExtra("case_edit", case)
        refreshLauncher.launch(intent)
    }

    private fun registerRefreshCallback() {
        refreshLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) view.onRefresh()
                else if (it.resultCode == 99) view.onDelete(position)
            }
    }
}
