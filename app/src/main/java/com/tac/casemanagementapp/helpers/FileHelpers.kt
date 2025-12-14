package com.tac.casemanagementapp.helpers

import android.content.Context
import timber.log.Timber
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/**
 * Writes a String (your JSON) into internal storage.
 *
 * - context.openFileOutput(fileName, MODE_PRIVATE) creates/overwrites the file
 * - MODE_PRIVATE means only your app can read it
 */
fun write(context: Context, fileName: String, data: String) {
    try {
        val outputStreamWriter = OutputStreamWriter(
            context.openFileOutput(fileName, Context.MODE_PRIVATE)
        )
        outputStreamWriter.write(data)
        outputStreamWriter.close()
    } catch (ex: Exception) {
        Timber.e("Cannot write file: %s", ex.toString())
    }
}

/**
 * Reads the entire file contents as a single String.
 *
 * - Reads line by line and appends to a StringBuilder
 * - Returns "" if file doesn't exist or can't be read
 */
fun read(context: Context, fileName: String): String {
    var str = ""
    try {
        val inputStream = context.openFileInput(fileName)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)

        val builder = StringBuilder()
        while (true) {
            val line = bufferedReader.readLine() ?: break
            builder.append(line)
        }
        inputStream.close()
        str = builder.toString()

    } catch (ex: FileNotFoundException) {
        Timber.e("File not found: %s", ex.toString())
    } catch (ex: IOException) {
        Timber.e("Cannot read file: %s", ex.toString())
    }
    return str
}

/**
 * Checks whether a file exists in your app's internal storage.
 */
fun exists(context: Context, fileName: String): Boolean {
    val file = context.getFileStreamPath(fileName)
    return file.exists()
}
