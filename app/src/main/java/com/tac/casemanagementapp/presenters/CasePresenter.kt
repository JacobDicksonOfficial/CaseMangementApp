package com.tac.casemanagementapp.presenters

import android.app.Activity.RESULT_OK
import android.content.Intent
import com.tac.casemanagementapp.main.MainApp
import com.tac.casemanagementapp.models.CaseModel
import com.tac.casemanagementapp.views.case.CaseView

/**
 * Presenter class owns the screen logic for creating/editing/deleting a Case.
 * The View handles UI widgets and the Presenter handles decisions and stores calls from functions
 */
class CasePresenter(private val view: CaseView) {

    private val app: MainApp = view.application as MainApp
    var case = CaseModel()
    var edit = false

    init {
        // If we came from list click with an existing case, we are editing
        if (view.intent.hasExtra("case_edit")) {
            edit = true
            case = view.intent.getParcelableExtra("case_edit")!!
            view.showCase(case)
        }
    }

    fun doSave(title: String, description: String, gender: String) {
        case.title = title
        case.description = description
        case.gender = gender

        if (edit) app.cases.update(case) else app.cases.create(case)

        view.setResult(RESULT_OK)
        view.finish()
    }

    fun doDelete() {
        view.setResult(99)
        app.cases.delete(case)
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    fun cacheCase(title: String, description: String, gender: String) {
        case.title = title
        case.description = description
        case.gender = gender
    }

    /**
     * The View will call this after it has obtained an image URI/String.
     */
    fun setImage(image: String) {
        case.image = image
        view.updateImage(image)
    }
}
