package com.natasa.canvasexample

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import java.io.InputStream

 public object ImageHelper {
    fun loadImageBitmap(inputStream: InputStream): ImageBitmap {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        return bitmap.asImageBitmap()
    }

     fun loadImageBitmapFromStream(inputStream: InputStream): ImageBitmap {
         val bitmap = BitmapFactory.decodeStream(inputStream)
         return bitmap.asImageBitmap()
     }
     @Composable
     fun getAssetInputStream(assetName: String): InputStream {
         val context = LocalContext.current
         return context.assets.open(assetName)
     }
     @SuppressLint("DiscouragedApi")
     @Composable
     fun getDrawableInputStream(resourceName: String): InputStream? {
         val context = LocalContext.current
         val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
         return if (resourceId != 0) {
             context.resources.openRawResource(resourceId)
         } else {
             null
         }
     }


}