package com.tac.casemanagementapp.presenters

import android.app.Activity.RESULT_OK
import com.tac.casemanagementapp.main.MainApp
import com.tac.casemanagementapp.models.CaseModel
import com.tac.casemanagementapp.views.case.CaseView

/**
 * Presenter for CaseView.
 * Handles creating, editing, and updating a single CaseModel.
 */
class CasePresenter(private val view: CaseView) {

    // Accesses global CaseStore via Application object
    private val app: MainApp = view.application as MainApp

    // Cases being created or edited
    var case: CaseModel = CaseModel()

    // Flags to check if we are editing an existing case
    private var edit = false

    init {
        // Check if CaseView was opened in edit mode
        if (view.intent.hasExtra("case_edit")) {
            edit = true
            case = view.intent.extras?.getParcelable("case_edit")!!
            view.showCase(case)
        }
    }

    // Save button pressed
    fun doSave(title: String, description: String, gender: String) {
        case.title = title
        case.description = description
        case.gender = gender

        if (edit) {
            app.cases.update(case)
        } else {
            app.cases.create(case)
        }

        view.setResult(RESULT_OK)
        view.finish()
    }

    // Cancel button pressed
    fun doCancel() {
        view.finish()
    }

    // Cache form values when navigating away (image/map picker)
    fun cacheCase(title: String, description: String, gender: String) {
        case.title = title
        case.description = description
        case.gender = gender
    }

    // Save selected image
    fun setImage(image: String) {
        case.image = image
        view.updateImage(image)
    }

    // Save selected location from map screen
    fun setLocation(
        latitude: Double,
        longitude: Double,
        address: String
    ) {
        case.latitude = latitude
        case.longitude = longitude
        case.address = address
    }
}
