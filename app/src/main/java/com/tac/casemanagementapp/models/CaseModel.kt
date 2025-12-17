package com.tac.casemanagementapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CaseModel(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var image: String = "",
    var gender: String = "N/A",

    // 📍 Location data
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var address: String = ""
) : Parcelable
