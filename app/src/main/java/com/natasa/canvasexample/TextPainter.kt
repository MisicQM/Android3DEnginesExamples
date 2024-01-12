package com.natasa.canvasexample

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp

// Helper functions for text measurement and drawing
public class TextPainter(val context: Context) {
    internal val paint = android.graphics.Paint().apply {
        isAntiAlias = true
    }

    fun measureTextWidth(text: String, textStyle: TextStyle): Float {
        paint.textSize = textStyle.setFontSizeToPx(context)
        return paint.measureText(text)
    }

    fun measureTextHeight(text: String, textStyle: TextStyle): Float {
        paint.textSize = textStyle.setFontSizeToPx(context)
        val bounds = android.graphics.Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return bounds.height().toFloat()
    }



    private fun TextStyle.setFontSizeToPx(context: Context): Float =
        this.fontSize.value * context.resources.displayMetrics.scaledDensity
}

