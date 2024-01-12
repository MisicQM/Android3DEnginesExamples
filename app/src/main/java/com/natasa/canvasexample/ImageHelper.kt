package com.natasa.canvasexample

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.InputStream

internal object ImageHelper {
    fun loadImageBitmap(inputStream: InputStream): ImageBitmap {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        return bitmap.asImageBitmap()
    }
}