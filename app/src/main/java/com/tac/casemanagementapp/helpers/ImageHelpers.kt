package com.tac.casemanagementapp.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import android.provider.MediaStore

// Helper function to open the image picker
fun showImagePicker(intentLauncher: ActivityResultLauncher<Intent>) {
    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    intentLauncher.launch(pickIntent)
}
