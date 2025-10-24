package com.tac.casemanagementapp.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.tac.casemanagementapp.R

fun showImagePicker(intentLauncher: ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, "Select Case Image")
    intentLauncher.launch(chooseFile)
}
