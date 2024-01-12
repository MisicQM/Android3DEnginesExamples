package com.natasa.canvasexample

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
/**Canvas coordinates:
 * (0,0): Top-left corner of the canvas.
 * (width, 0): Top-right corner of the canvas.
 * (0, height): Bottom-left corner of the canvas.
 * (width, height): Bottom-right corner of the canvas.
 *
 * Center:
 * (width / 2, height / 2): Center of the canvas.
 *
 * (0,0)                               (width,0)
 *   +--------------------------------------+
 *   |                                      |
 *   |                                      |
 *   |                  C1                  |
 *   |               , - - - .              |
 *   |           ,-'           '-.          |
 *   |         /                   \        |
 *   |        /                     \       |
 *   |       ;                       :      |
 *   |       |           + (center)  |      |
 *   |       :                       ;      |
 *   |        \                     /       |
 *   |         \,                 ,/        |
 *   |           '-.           ,-'          |
 *   |               ` - - - '              |
 *   |                  C2                  |
 *   |                                      |
 *   +--------------------------------------+
 * (0,height)                           (width,height)
 */
@Composable
fun CoordinateTextCanvas() {
    val textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
    val context = LocalContext.current
    val textPainter = remember { TextPainter(context) }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val topLeftText = "0,0"
        val topRightText = "${canvasWidth.toInt()},0"
        val bottomLeftText = "0,${canvasHeight.toInt()}"
        val bottomRightText = "${canvasWidth.toInt()},${canvasHeight.toInt()}"
        val centerText = "${canvasWidth.toInt() / 2},${canvasHeight.toInt() / 2}"
        // Draw text in the top-left corner
        drawText(textPainter, topLeftText, Offset(0f, 0f), textStyle)
        //center
        val centerTextWidth = textPainter.measureTextWidth(centerText, textStyle)
        drawText(
            textPainter,
            centerText,
            Offset(canvasWidth / 2, canvasHeight / 2 - centerTextWidth),
            textStyle
        )
        // Draw text in the top-right corner
        // Adjust the x position by the width of the text to align it to the right, paint
        val topRightTextWidth = textPainter.measureTextWidth(topRightText, textStyle)
        drawText(textPainter, topRightText, Offset(canvasWidth - topRightTextWidth, 0f), textStyle)

        // Draw text in the bottom-left corner
        // Adjust the y position by the height of the text to align it to the bottom
        val bottomLeftTextHeight = textPainter.measureTextHeight(bottomLeftText, textStyle)
        drawText(
            textPainter,
            bottomLeftText,
            Offset(0f, canvasHeight - bottomLeftTextHeight),
            textStyle
        )

        // Draw text in the bottom-right corner
        // Adjust both x and y positions
        val bottomRightTextWidth = textPainter.measureTextWidth(bottomRightText, textStyle)
        val bottomRightTextHeight = textPainter.measureTextHeight(bottomRightText, textStyle)
        drawText(
            textPainter,
            bottomRightText,
            Offset(canvasWidth - bottomRightTextWidth, canvasHeight - bottomRightTextHeight),
            textStyle
        )
    }
}
fun DrawScope.drawText(painter: TextPainter, text: String, position: Offset, textStyle: TextStyle) {
    val textHeight = painter.measureTextHeight(text, textStyle)
    drawContext.canvas.nativeCanvas.drawText(
        text,
        position.x,
        position.y + textHeight, // Offset to account for the baseline
        painter.paint.apply {
            textSize = textStyle.fontSize.toPx()
            color = textStyle.color.toArgb()
        }
    )
}
@Composable
fun GridCanvas(lineColor: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 1.dp.toPx() // Set the stroke width for the grid lines
        val cellSize = Size(100.dp.toPx(), 100.dp.toPx()) // The size for each cell

        // Calculate the number of cells horizontally and vertically
        val cellsInRow = (size.width / cellSize.width).toInt()
        val cellsInColumn = (size.height / cellSize.height).toInt()

        // Draw the grid
        for (i in 0..cellsInRow) {
            for (j in 0..cellsInColumn) {
                val topLeft = Offset(i * cellSize.width, j * cellSize.height)
                drawRect(
                    color = lineColor,
                    topLeft = topLeft,
                    size = cellSize,
                    style = Stroke(width = strokeWidth)
                )
            }
        }
    }
}
