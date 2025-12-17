package com.tac.casemanagementapp.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tac.casemanagementapp.helpers.exists
import com.tac.casemanagementapp.helpers.read
import com.tac.casemanagementapp.helpers.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.Random

// Name of the JSON file saved in internal storage
private const val JSON_FILE = "cases.json"

// Gson instance used to convert Kotlin objects to JSON
// Pretty printing makes the JSON easier to read when debugging
private val gsonBuilder: Gson = GsonBuilder()
    .setPrettyPrinting()
    .create()

// Type information needed so Gson can correctly read/write a list of CaseModel objects
private val listType: Type =
    object : TypeToken<ArrayList<CaseModel>>() {}.type

// Generates a random id for a new case
private fun generateRandomId(): Long = Random().nextLong()

// JSON-based implementation of CaseStore
// Saves and loads cases from a local JSON file inside internal storage
class CaseJSONStore(private val context: Context) : CaseStore {

    // Holds all cases currently loaded into memory
    private var cases = mutableListOf<CaseModel>()

    init {
        // When store is created, load existing data if the file already exists
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    // Returns all stored cases
    override fun findAll(): List<CaseModel> {
        logAll()
        return cases
    }

    // Adds a new case and saves to JSON
    override fun create(case: CaseModel) {
        case.id = generateRandomId()   // give the case a unique id
        cases.add(case)                // add to list
        serialize()                    // save list to file
    }

    // Updates an existing case (matching by id) and saves to JSON
    override fun update(case: CaseModel) {
        // Find the case in the list that matches the id
        val foundCase = cases.find { it.id == case.id }
        if (foundCase != null) {
            // Update the stored case fields with the new values
            foundCase.title = case.title
            foundCase.description = case.description
            foundCase.image = case.image
            foundCase.gender = case.gender

            // Save location-related fields too
            foundCase.latitude = case.latitude
            foundCase.longitude = case.longitude
            foundCase.address = case.address

            // Save changes to JSON file
            serialize()
        }
    }

    // Deletes a case by id and saves to JSON
    override fun delete(case: CaseModel) {
        cases.removeIf { it.id == case.id }
        serialize()
    }

    // Converts the cases list into JSON and writes it to file
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(cases, listType)
        write(context, JSON_FILE, jsonString)
    }

    // Reads JSON from file and converts it back into a list of CaseModel objects
    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        cases = gsonBuilder.fromJson(jsonString, listType)
    }

    // Logs all cases (useful for debugging)
    private fun logAll() {
        Timber.i("---- CaseJSONStore: listing cases ----")
        cases.forEach { Timber.i("Case: $it") }
    }
}
