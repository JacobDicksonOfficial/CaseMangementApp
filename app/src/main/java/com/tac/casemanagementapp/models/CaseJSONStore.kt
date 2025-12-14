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

private const val JSON_FILE = "cases.json"

/**
 * Gson instance for converting between and  List<CaseModel>  and the  JSON String
 */
private val gsonBuilder: Gson = GsonBuilder()
    .setPrettyPrinting()
    .create()

/**
 * Indicating to Gson that it needs to know "this JSON is a List<CaseModel>"
 */
private val listType: Type = object : TypeToken<ArrayList<CaseModel>>() {}.type

/**
 * Generates an id when creating new cases (Same approach as the labs: random long id)
 */
private fun generateRandomId(): Long = Random().nextLong()

/**
 * JSON-backed implementation of CaseStore.
 *
 * - Keeps data in memory as `cases`
 * - After every create/update/delete, it serializes to cases.json
 * - On startup, it deserializes from cases.json if it exists
 */
class CaseJSONStore(private val context: Context) : CaseStore {

    private var cases = mutableListOf<CaseModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): List<CaseModel> {
        logAll()
        return cases
    }

    override fun create(case: CaseModel) {
        case.id = generateRandomId()
        cases.add(case)
        serialize()
    }

    override fun update(case: CaseModel) {
        val foundCase = cases.find { it.id == case.id }
        if (foundCase != null) {
            foundCase.title = case.title
            foundCase.description = case.description
            foundCase.image = case.image
            foundCase.gender = case.gender
            serialize()
        }
    }

    override fun delete(case: CaseModel) {
        cases.removeIf { it.id == case.id }
        serialize()
    }

    /** Converts the current in-memory list to the JSON string which writes to a file.
     */
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(cases, listType)
        write(context, JSON_FILE, jsonString)
    }

    /**
     * Reads JSON string from file and converts to list,  then it stores it in memory.
     */
    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        cases = gsonBuilder.fromJson(jsonString, listType)
    }

    /**
     * Logging helper (helps you prove it’s loading/saving correctly).
     */
    private fun logAll() {
        Timber.i("---- CaseJSONStore: listing cases ----")
        cases.forEach { Timber.i("Case: $it") }
    }
}
