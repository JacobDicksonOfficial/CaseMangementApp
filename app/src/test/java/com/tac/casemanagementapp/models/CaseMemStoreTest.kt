package com.tac.casemanagementapp.models

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for CaseMemStore class.
 * Verifies create() and delete() functionality.
 */

class CaseMemStoreTest {

    private lateinit var store: CaseMemStore // in-memory store instance

    @Before
    fun setUp() {
        // Initialize store before each test
        store = CaseMemStore()
    }

    @Test
    fun testCreateCase() {
        // Arrange: create a new case model
        val case = CaseModel(
            title = "Unit Test Case",
            description = "Testing create() function",
            image = ""
        )

        // Act: add the case to the store
        store.create(case)

        // Assert: verify store contains exactly 1 item
        val allCases = store.findAll()
        assertEquals(1, allCases.size)
        assertEquals("Unit Test Case", allCases[0].title)
        assertEquals("Testing create() function", allCases[0].description)
    }

    @Test
    fun testDeleteCase() {
        // Arrange: create and add two cases
        val case1 = CaseModel(title = "Case 1", description = "To be deleted")
        val case2 = CaseModel(title = "Case 2", description = "Should stay")

        store.create(case1)
        store.create(case2)

        // Act: delete the first case
        store.delete(case1)

        // Assert: check that only case2 remains
        val allCases = store.findAll()
        assertEquals(1, allCases.size)
        assertEquals("Case 2", allCases[0].title)
        assertNotEquals("Case 1", allCases[0].title)
    }
}
