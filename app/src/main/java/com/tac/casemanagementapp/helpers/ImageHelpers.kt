package com.tac.casemanagementapp.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import android.provider.MediaStore

// Opens the phone gallery / image picker and returns the chosen image
fun showImagePicker(intentLauncher: ActivityResultLauncher<Intent>) {
    // ACTION_PICK lets the user select an item (image) from a content provider (gallery)
    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    // Launch the picker using the launcher registered in your Activity
    intentLauncher.launch(pickIntent)
}
