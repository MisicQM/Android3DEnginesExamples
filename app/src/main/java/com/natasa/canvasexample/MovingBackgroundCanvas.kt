package com.natasa.canvasexample

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp

@Composable
fun MovingGridCanvas() {
    val infiniteTransition = rememberInfiniteTransition()
    val scrollState = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cellSize = Size(50.dp.toPx(), 50.dp.toPx())
        val strokeWidth = 1.dp.toPx()
        val cellsInRow = (size.width / cellSize.width).toInt()
        val cellsInColumn = (size.height / cellSize.height).toInt()

        // Use scrollState value to calculate the offset for the grid
        val offsetX = scrollState.value * cellSize.width
        val offsetY = scrollState.value * cellSize.height

        // Translate the entire canvas based on the animation state
        translate(left = -offsetX, top = -offsetY) {
            // Draw the translated grid
            for (i in -1..cellsInRow) {
                for (j in -1..cellsInColumn) {
                    val topLeft = Offset(i * cellSize.width, j * cellSize.height)
                    drawRect(
                        color = Color.Black,
                        topLeft = topLeft,
                        size = cellSize,
                        style = Stroke(width = strokeWidth)
                    )
                }
            }
        }
    }
}
