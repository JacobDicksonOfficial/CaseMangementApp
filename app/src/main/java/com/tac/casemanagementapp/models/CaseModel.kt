package com.tac.casemanagementapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CaseModel(
    var id: Long = 0,
    var name: String = "",
    var details: String = "",
    var image: Uri = Uri.EMPTY
) : Parcelable
