package com.tac.casemanagementapp.models

// Interface defines how we interact with the case data store
// Matches the "Memory Store" lab structure
interface CaseStore {
    fun findAll(): List<CaseModel>     // Return all cases
    fun create(case: CaseModel)        // Add new case
    fun update(case: CaseModel)        // Update existing case
    fun delete(case: CaseModel)        // Delete case
}
