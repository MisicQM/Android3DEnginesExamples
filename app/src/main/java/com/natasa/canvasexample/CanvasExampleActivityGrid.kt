package com.natasa.canvasexample

import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.natasa.canvasexample.ImageHelper.loadImageBitmap
import kotlinx.coroutines.launch
import kotlin.random.Random

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
                        var trajectoryPoints by remember { mutableStateOf(generateRandomFloatPoints()) }

                        //MovingGridCanvas()
                        GridCanvasWithButton(
                            gridOffset = gridOffset,
                            lineColor = Color.LightGray,trajectoryPoints,
                            onGenerateNewTrajectory = {
                                trajectoryPoints = generateRandomFloatPoints()
                                Log.d("","trajectory points $trajectoryPoints")
                            }
                        )
                        CarAndTrajectoryCanvas2(gridRotation, viewModel)
                        CoordinateTextCanvas()

                    }
                }
            }
        }
    }
}

@Composable
fun CarAndTrajectoryCanvas2(gridRotation: Animatable<Float, AnimationVector1D>, viewModel: TrajectoryViewModel) {
    val context = LocalContext.current
    val imageBitmap: ImageBitmap by remember {
        mutableStateOf(loadImageBitmap(context.assets.open("defaultCar.png")))
    }
    val canvasGridAnimatable = remember { Animatable(0f) }
    // Convert dp to pixel
    val carWidthPx = with(LocalDensity.current) { 140.dp.toPx() }
    val carHeightPx = with(LocalDensity.current) { 120.dp.toPx() }

    LaunchedEffect(Unit) {
        gridRotation.animateTo(
            targetValue = 160f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 12000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }
    LaunchedEffect(Unit) {
        launch {
            canvasGridAnimatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 12000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

        }
    }
    // Draw the car at the center of the screen
    Canvas(modifier = Modifier.fillMaxSize()) {
        val start = Offset(size.width / 2, size.height/2)
        val control1 = Offset(size.width / 4, size.height / 2)
        val control2 = Offset(2 * size.width / 4, size.height / 2)
        val end = Offset(size.width / 2, 0f)


        // Draw the trajectory path
        val trajectoryPath = Path().apply {
            moveTo(start.x, start.y)
            cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y)
        }
val points = listOf(start, control1, control2, end)
        drawTrajectoryPath(trajectoryPath = trajectoryPath)
        val scale = minOf(size.width / imageBitmap.width, size.height / imageBitmap.height)

        val carCenter = Offset(size.width / 2 - carWidthPx , size.height / 2- carHeightPx )
        val carTopLeft = Offset(carCenter.x - carWidthPx / 2, carCenter.y - carHeightPx )
        withTransform({
            // Rotate the image
            rotate(degrees = 90f)
            scale(scale, scale, pivot = carCenter)

        }) {
            drawImage(image = imageBitmap, topLeft = carTopLeft)
        }
    }
}


fun DrawScope.drawTrajectoryPath(trajectoryPath: Path) {

        val pathWidth = 150.dp.toPx()
        val borderStrokeWidth = 8.dp.toPx() // Set the width of the border stroke
        val borderColor = Color.Gray // Color for the border
        val fillColor = Color.Green.copy(alpha = 0.2f) // Color for the fill
        // drawPath(
           // path = trajectoryPath,
           // color = borderColor,
           // style = Stroke(width = pathWidth + borderStrokeWidth) // Total width = pathWidth + border on each side
       // )
        drawPath(
            path = trajectoryPath,
            color = fillColor,
            style = Stroke(width = pathWidth) // The original path width
        )
    }

@Composable
fun GridCanvasAnimated(
    gridOffset: Animatable<Offset, AnimationVector2D>,
    lineColor: Color,   trajectoryPoints: List<Float> ) {

    val animatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        launch {
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 24000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

        }
    }
    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 1.dp.toPx() // Set the stroke width for the grid lines
        val cellSize = Size(60.dp.toPx(), 60.dp.toPx()) // The size for each cell
        val start = Offset(trajectoryPoints[0]*size.width / 2, trajectoryPoints[0]*size.height/2)
        val control1 = Offset(trajectoryPoints[1]*size.width / 4, trajectoryPoints[1]*size.height / 2)
        val control2 = Offset(trajectoryPoints[2]*  size.width / 4, trajectoryPoints[2]*size.height / 2)
        val end = Offset(trajectoryPoints[3]*size.width / 2, 0f)
        val trajectoryPoints = listOf(start, control1, control2, end)

        // Calculate the number of cells horizontally and vertically
        val cellsInRow = (size.width / cellSize.width).toInt() + 50 //get more cells
        val cellsInColumn = (size.height / cellSize.height).toInt() + 50
        // Make sure we have enough points to draw the curve
        if (trajectoryPoints.size < 4) return@Canvas

        // Handle the animatable value for multiple segments
        val segmentCount =
            (trajectoryPoints.size - 1) / 3 // Calculate the number of segments dynamically
        val currentSegment =
            (animatable.value * segmentCount).coerceIn(0f, segmentCount.toFloat()).toInt()
        val t = ((animatable.value * segmentCount) % 1f).coerceIn(0f, 1f)

        // Calculate the indices for the current segment
        val segmentIndex = currentSegment * 3
        val segmentEndIndex = minOf(segmentIndex + 4, trajectoryPoints.size)

        // Get the points for the current segment ensuring we don't go out of bounds
        val segmentPoints = trajectoryPoints.subList(segmentIndex, segmentEndIndex)

        // If we don't have enough points for a full cubic curve, return
        if (segmentPoints.size < 4) return@Canvas

        // Calculate the tangent angle for the current segment
        val tangentAngle = PathHelper.cubicBezierTangent(
            t,
            segmentPoints[0],
            segmentPoints[1],
            segmentPoints[2],
            segmentPoints[3]
        )

        // Apply rotation and offset to the grid
        withTransform({
            rotate(degrees = tangentAngle/2, pivot = Offset(size.width / 2, size.height ))
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
@Composable
fun GridCanvasWithButton(
    gridOffset: Animatable<Offset, AnimationVector2D>,
    lineColor: Color, trajectoryPoints: List<Float>,
    onGenerateNewTrajectory: () -> Unit // Callback when new trajectory is needed
) {

   // GridCanvasAnimated( gridOffset, Color.LightGray, trajectoryPoints)

    Box(contentAlignment = Alignment.BottomCenter) {
        GridCanvasAnimated(gridOffset, lineColor, trajectoryPoints) // Your existing Canvas composable

        // Place a Button over the Canvas
        Button(
            onClick = { onGenerateNewTrajectory() }, // Invoke the callback when the button is clicked
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp) // Add some padding from the bottom
        ) {
            Text("Generate New Animation")
        }
    }
}

fun generateRandomTrajectoryPoints(): List<Offset> {
    // Generate a random set of trajectory points
    return List(4) { Offset(Random.nextFloat(), Random.nextFloat()) }
}
fun generateRandomFloatPoints(): List<Float> {
    return List(4) {Random.nextFloat() }
}
