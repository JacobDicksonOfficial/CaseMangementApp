package com.tac.casemanagementapp.models

import timber.log.Timber

// In-memory implementation of the CaseStore interface
// Stores all cases in an ArrayList while the app runs
class CaseMemStore : CaseStore {

    private val cases = ArrayList<CaseModel>()  // holds our in-memory list

    override fun findAll(): List<CaseModel> {
        return cases                            // simply return list
    }

    override fun create(case: CaseModel) {
        case.id = getId()                       // assign a unique id
        cases.add(case)                         // add to list
        logAll()                                // log for debug
    }

    override fun update(case: CaseModel) {
        val foundCase = cases.find { it.id == case.id } // find by id
        if (foundCase != null) {
            foundCase.title = case.title
            foundCase.description = case.description
            foundCase.image = case.image
            logAll()
        }
    }

    override fun delete(case: CaseModel) {
        cases.removeIf { it.id == case.id }     // remove by id
        logAll()
    }

    private fun logAll() {
        Timber.i("Listing all cases:")
        cases.forEach { Timber.i("Case: $it") }
    }

    // Generates a simple unique id (auto-increment)
    private fun getId(): Long = if (cases.isEmpty()) 1 else cases.maxOf { it.id } + 1
}
