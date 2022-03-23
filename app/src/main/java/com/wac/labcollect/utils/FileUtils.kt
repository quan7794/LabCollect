package com.wac.labcollect.utils

import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore.Images
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object FileUtils {

    fun ContextWrapper.saveBitmapToInternalStorage(bitmapImage: Bitmap, imageName: String): String {
//        val cw = ContextWrapper(ApplicationProvider.getApplicationContext<Context>())
        // path to /data/data/yourapp/app_imageDir
        val directory = this.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val file = File(directory, "$imageName.png")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        Timber.e("File path: ${directory.absolutePath}/${file.name}")
        return "${directory.absolutePath}/${file.name}"
    }

    fun Context.shareImage(bitmapImage: Bitmap, title: String) {
        val share = Intent(Intent.ACTION_SEND).also { it.type = "image/png" }

        val values = ContentValues().apply {
            put(Images.Media.DISPLAY_NAME, title)
            put(Images.Media.TITLE, title)
            put(Images.Media.MIME_TYPE, "image/png")
        }

        val uri = contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values)
        try {
            val outStream = uri?.let { contentResolver.openOutputStream(it) }
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream?.close()
        } catch (e: Exception) {
            Timber.e("Error happen when save image: $e")
        }

        share.putExtra(Intent.EXTRA_STREAM, uri)
        this.startActivity(Intent.createChooser(share, "Chia sẻ hình ảnh"))
    }

    fun Context.addImageToGallery(bitmap: Bitmap, title: String, description: String) {
        val values = ContentValues().apply {
            put(Images.Media.DISPLAY_NAME, title)
            put(Images.Media.DESCRIPTION, description)
            put(Images.Media.TITLE, title)
            put(Images.Media.MIME_TYPE, "image/png")
        }

        val uri = contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values)
        try {
            val outStream = uri?.let { contentResolver.openOutputStream(it) }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream?.close()
        } catch (e: Exception) {
            Timber.e("Error happen when save image: $e")
        }

    }

    fun Context.shareUrl(url: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/html"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}