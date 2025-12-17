package com.tac.casemanagementapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Data class that represents one "Case" in the app
// @Parcelize makes it easy to send this object between Activities using Intents
@Parcelize
data class CaseModel(
    var id: Long = 0,                 // unique id for update/delete
    var title: String = "",           // case title/name
    var description: String = "",     // case description/details
    var image: String = "",           // image Uri/String (saved from picker)
    var gender: String = "N/A",       // gender field

    // Location info (used for map + map preview)
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var address: String = ""
) : Parcelable
