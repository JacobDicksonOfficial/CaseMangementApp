package com.tac.casemanagementapp.models

import timber.log.Timber

// In-memory implementation of CaseStore
// Data is lost when the app is closed (nothing is saved to disk)
class CaseMemStore : CaseStore {

    // List that holds cases while the app is running
    private val cases = ArrayList<CaseModel>()

    // Returns all cases currently in memory
    override fun findAll(): List<CaseModel> {
        return cases
    }

    // Adds a new case to memory
    override fun create(case: CaseModel) {
        case.id = getId()      // create a unique id
        cases.add(case)        // add to list
        logAll()               // log list for debugging
    }

    // Updates an existing case (matching by id)
    override fun update(case: CaseModel) {
        val foundCase = cases.find { it.id == case.id }
        if (foundCase != null) {
            foundCase.title = case.title
            foundCase.description = case.description
            foundCase.image = case.image
            logAll()
        }
    }

    // Deletes a case by id
    override fun delete(case: CaseModel) {
        cases.removeIf { it.id == case.id }
        logAll()
    }

    // Prints all cases to the log (debugging)
    private fun logAll() {
        Timber.i("Listing all cases:")
        cases.forEach { Timber.i("Case: $it") }
    }

    // Generates a simple id:
    // If list is empty it will then start at 1
    // Otherwise it will use the max id + 1
    private fun getId(): Long = if (cases.isEmpty()) 1 else cases.maxOf { it.id } + 1
}
