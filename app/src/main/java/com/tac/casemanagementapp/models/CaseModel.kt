package com.tac.casemanagementapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// A data class representing one case in the app
// Using @Parcelize allows the object to be sent between Activities
@Parcelize
data class CaseModel(
    var id: Long = 0,                      // unique identifier
    var title: String = "",                // title of the case
    var description: String = "",          // description or notes
    var image: String = ""            // Store image URI as string for persistence
) : Parcelable
