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

private val gsonBuilder: Gson = GsonBuilder()
    .setPrettyPrinting()
    .create()

private val listType: Type =
    object : TypeToken<ArrayList<CaseModel>>() {}.type

private fun generateRandomId(): Long = Random().nextLong()

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

            // 📍 location persistence
            foundCase.latitude = case.latitude
            foundCase.longitude = case.longitude
            foundCase.address = case.address

            serialize()
        }
    }

    override fun delete(case: CaseModel) {
        cases.removeIf { it.id == case.id }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(cases, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        cases = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        Timber.i("---- CaseJSONStore: listing cases ----")
        cases.forEach { Timber.i("Case: $it") }
    }
}
