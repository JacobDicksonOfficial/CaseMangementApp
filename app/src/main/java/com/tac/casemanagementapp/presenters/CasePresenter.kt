package com.tac.casemanagementapp.presenters

import android.app.Activity.RESULT_OK
import com.tac.casemanagementapp.main.MainApp
import com.tac.casemanagementapp.models.CaseModel
import com.tac.casemanagementapp.views.case.CaseView

class CasePresenter(private val view: CaseView) {

    private val app: MainApp = view.application as MainApp
    var case: CaseModel = CaseModel()
    private var edit = false

    init {
        if (view.intent.hasExtra("case_edit")) {
            edit = true
            case = view.intent.extras?.getParcelable("case_edit")!!
            view.showCase(case)
        }
    }

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

    fun doCancel() {
        view.finish()
    }

    fun cacheCase(title: String, description: String, gender: String) {
        case.title = title
        case.description = description
        case.gender = gender
    }

    fun setImage(image: String) {
        case.image = image
        view.updateImage(image)
    }

    // 📍 Called by MapViewActivity
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
