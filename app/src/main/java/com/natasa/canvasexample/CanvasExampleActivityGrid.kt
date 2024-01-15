package com.natasa.canvasexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.natasa.canvasexample.ImageHelper.loadImageBitmap
class CanvasExampleActivityGrid : ComponentActivity() {
    private val viewModel: TrajectoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        val gridRotation = remember { Animatable(0f) }
                        val gridOffset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

                        //MovingGridCanvas()
                        GridCanvas2(gridRotation, gridOffset, Color.LightGray)
                        CarAndTrajectoryCanvas2(gridRotation, gridOffset, viewModel)
                        CoordinateTextCanvas()

                    }
                }
            }
        }
    }
}

@Composable
fun CarAndTrajectoryCanvas2(gridRotation: Animatable<Float, AnimationVector1D>, gridOffset: Animatable<Offset, AnimationVector2D>, viewModel: TrajectoryViewModel) {
    val context = LocalContext.current
    val imageBitmap: ImageBitmap by remember {
        mutableStateOf(loadImageBitmap(context.assets.open("defaultCar.png")))
    }
    // Convert dp to pixel
    val carWidthPx = with(LocalDensity.current) { 340.dp.toPx() }
    val carHeightPx = with(LocalDensity.current) { 220.dp.toPx() }

    LaunchedEffect(Unit) {
        gridRotation.animateTo(
            targetValue = 160f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 12000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }



    // Draw the car at the center of the screen
    Canvas(modifier = Modifier.fillMaxSize()) {
        val start = Offset(size.width / 2, size.height)
        val control1 = Offset(size.width / 4, size.height / 2)
        val control2 = Offset(3 * size.width / 4, size.height / 2)
        val end = Offset(size.width / 2, 0f)

        //val points = listOf(start, control1, control2, end)
        // Draw the trajectory path
        val trajectoryPath = Path().apply {
            moveTo(start.x, start.y)
            cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y)
        }

        drawTrajectoryPath(trajectoryPath = trajectoryPath)
        val carCenter = Offset(size.width / 2 - carWidthPx / 2, size.height / 2- carHeightPx )
        val carTopLeft = Offset(carCenter.x - carWidthPx / 2, carCenter.y - carHeightPx / 2)
        withTransform({
            // Rotate the image
            rotate(degrees =90f)
            // Proportionally scale the image
        }) {

            drawImage(image = imageBitmap, topLeft = carTopLeft)
        }
    }
}


fun DrawScope.drawTrajectoryPath(trajectoryPath: Path) {

        val pathWidth = 180.dp.toPx()
        val borderStrokeWidth = 8.dp.toPx() // Set the width of the border stroke
        val borderColor = Color.Gray // Color for the border
        val fillColor = Color.Green.copy(alpha = 0.2f) // Color for the fill
         drawPath(
            path = trajectoryPath,
            color = borderColor,
            style = Stroke(width = pathWidth + borderStrokeWidth) // Total width = pathWidth + border on each side
        )
        drawPath(
            path = trajectoryPath,
            color = fillColor,
            style = Stroke(width = pathWidth) // The original path width
        )
    }


@Composable
fun GridCanvas2(gridRotation: Animatable<Float, AnimationVector1D>, gridOffset: Animatable<Offset, AnimationVector2D>, lineColor: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 1.dp.toPx() // Set the stroke width for the grid lines
        val cellSize = Size(60.dp.toPx(), 60.dp.toPx()) // The size for each cell

        // Calculate the number of cells horizontally and vertically
        val cellsInRow = (size.width / cellSize.width).toInt() + 2
        val cellsInColumn = (size.height / cellSize.height).toInt() + 2

        // Apply rotation and offset to the grid
        withTransform({
            rotate(degrees = gridRotation.value/4, pivot = Offset(size.width / 2, size.height / 2))
            translate(left = gridOffset.value.x, top = gridOffset.value.y)
        }) {
            // Draw the grid
            for (i in -1 until cellsInRow) {
                for (j in -1 until cellsInColumn) {
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
}



fun DrawScope.drawVehicle(position: Offset, angle: Float) {
    // Draw the vehicle at 'position' rotated by 'angle'
}
