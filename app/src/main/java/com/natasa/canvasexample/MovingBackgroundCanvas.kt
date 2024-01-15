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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
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

    val rotationState = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 46000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cellSize = Size(100.dp.toPx(), 100.dp.toPx())
        val strokeWidth = 1.dp.toPx()
        val cellsInRow = (size.width / cellSize.width).toInt()
        val cellsInColumn = (size.height / cellSize.height).toInt()

        // Use scrollState value to calculate the offset for the grid
        val offsetX = scrollState.value * size.width // Updated to scroll the entire width of the canvas
        val offsetY = scrollState.value * size.height // Updated to scroll the entire height of the canvas

        // Apply rotation and translation to the grid
        withTransform(
            {
                // Rotate around the center of the canvas
                rotate(rotationState.value, pivot = Offset(0f, size.height))
                // Then translate
                translate(left = -offsetX, top = -offsetY)
            }
        ) {
            // Draw the translated and rotated grid
            for (i in -1..cellsInRow) {
                for (j in -1..cellsInColumn) {
                    val topLeft = Offset(i * cellSize.width, j * cellSize.height)
                    drawRect(
                        color = Color.Gray,
                        topLeft = topLeft,
                        size = cellSize,
                        style = Stroke(width = strokeWidth)
                    )
                }
            }
        }
    }
}
