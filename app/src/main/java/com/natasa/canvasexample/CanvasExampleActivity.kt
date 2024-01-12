package com.natasa.canvasexample

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import java.io.InputStream

class CanvasExampleActivity : ComponentActivity() {
    private val viewModel: TrajectoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                       // TrajectoryView(viewModel = viewModel)
                      CarAndTrajectoryCanvas()
                    }
                }
            }
        }
    }
}
fun loadImageBitmap(inputStream: InputStream): ImageBitmap {
    val bitmap = BitmapFactory.decodeStream(inputStream)
    return bitmap.asImageBitmap()
}
@Composable
fun CarAndTrajectoryCanvas() {

    val context = LocalContext.current
    val imageBitmap: ImageBitmap by remember {
        mutableStateOf(loadImageBitmap(context.assets.open("car.png")))
    }
    // Convert dp to pixel
    val carWidthPx = with(LocalDensity.current) { 150.dp.toPx() }
    val carHeightPx = with(LocalDensity.current) { 170.dp.toPx() }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray)) {
        // Define the car rectangle size


        // Define the starting point and the control points for the trajectory:

        val start = center.copy(y = size.height - carHeightPx * 2)
        val control1 = Offset(size.width / 2f, size.height / 1.5f)
        val control2 = Offset(size.width / 2f, size.height / 3f)
        val end = Offset(size.width / 2f, carHeightPx)

        // Draw the trajectory path
        val trajectoryPath = Path().apply {
            moveTo(start.x, start.y)
            cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y)
        }
        drawPath(path = trajectoryPath, color = Color.Green, style = Stroke(width = 100.dp.toPx()))

        // Place the car at the starting point of the trajectory
        val carTopLeft = start.copy(x = start.x - carWidthPx / 2, y = start.y - carHeightPx / 2)

        // Rotate the car rectangle to match the trajectory's initial direction
        val angle = -90f // Rotate the rectangle to align with the trajectory visually
        drawCar(carTopLeft, carWidthPx, carHeightPx, angle, imageBitmap)
    }
}
@Composable
fun TrajectoryView(viewModel: TrajectoryViewModel) {
    // Observe the trajectory points from the ViewModel.
    val trajectoryPoints by viewModel.trajectoryPoints.collectAsState()

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Your existing drawing logic here.
        // For example, draw a line for each segment in the trajectory.
        trajectoryPoints.zipWithNext { start, end ->
            drawLine(
                color = Color.Green,
                start = start,
                end = end,
                strokeWidth = 4.dp.toPx()
            )
        }
    }}
fun DrawScope.drawCar(
    topLeft: Offset,
    width: Float,
    height: Float,
    angle: Float,
    imageBitmap: ImageBitmap
) {

    val scale = minOf(width / imageBitmap.width, height / imageBitmap.height)

    withTransform({
        // Rotate the image
        rotate(degrees =angle+90, pivot = topLeft)
        // Proportionally scale the image
        scale(scale, scale, pivot = topLeft)
    }) {

        // Draw the image
        drawImage(
            image = imageBitmap,
            topLeft = topLeft
        )
    }

}
/**Canvas coord:
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
 *
 *
 * */