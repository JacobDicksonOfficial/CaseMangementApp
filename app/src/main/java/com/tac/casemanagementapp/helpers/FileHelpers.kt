package com.tac.casemanagementapp.helpers

import android.content.Context
import timber.log.Timber
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

// This writes a String (usually JSON) into internal storage (private to this app)
fun write(context: Context, fileName: String, data: String) {
    try {
        // openFileOutput creates (or overwrites) the file inside internal storage
        val outputStreamWriter = OutputStreamWriter(
            context.openFileOutput(fileName, Context.MODE_PRIVATE)
        )

        // Write the full string into the file
        outputStreamWriter.write(data)

        // Closes the streams when finished
        outputStreamWriter.close()
    } catch (ex: Exception) {
        // Log the error (Timber makes logging cleaner)
        Timber.e("Cannot write file: %s", ex.toString())
    }
}

// Reads a file from internal storage and returns all contents as one String
fun read(context: Context, fileName: String): String {
    var str = ""
    try {
        // Open the file from internal storage
        val inputStream = context.openFileInput(fileName)

        // Convert the stream into something we can read line-by-line
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)

        // Build the full file content
        val builder = StringBuilder()
        while (true) {
            val line = bufferedReader.readLine() ?: break
            builder.append(line)
        }

        // Close stream
        inputStream.close()

        // Save result into str
        str = builder.toString()

    } catch (ex: FileNotFoundException) {
        // Happens if the file doesn't exist yet
        Timber.e("File not found: %s", ex.toString())
    } catch (ex: IOException) {
        // Happens if something goes wrong while reading
        Timber.e("Cannot read file: %s", ex.toString())
    }
    return str
}

// Checks if a file exists in internal storage
fun exists(context: Context, fileName: String): Boolean {
    val file = context.getFileStreamPath(fileName)
    return file.exists()
}
