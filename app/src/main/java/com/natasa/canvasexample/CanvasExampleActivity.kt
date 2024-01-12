package com.natasa.canvasexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.natasa.canvasexample.ImageHelper.loadImageBitmap
import com.natasa.canvasexample.PathHelper.cubicBezier
import com.natasa.canvasexample.PathHelper.cubicBezierTangent
import kotlinx.coroutines.launch


class CanvasExampleActivity : ComponentActivity() {
    private val viewModel: TrajectoryViewModel by viewModels()
    private val paint = android.graphics.Paint().apply {
        isAntiAlias = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                       // MovingGridCanvas()
                        GridCanvas(lineColor = Color.LightGray)
                      CarAndTrajectoryCanvas(viewModel = viewModel)
                       // TrajectoryViewMoreUsecases(viewModel = viewModel)

                        CoordinateTextCanvas()

                    }
                }
            }
        }
    }
}



@Composable
fun CarAndTrajectoryCanvas(viewModel : TrajectoryViewModel) {
    val animatable = remember { Animatable(0f) }
    val context = LocalContext.current
    val imageBitmap: ImageBitmap by remember {
        mutableStateOf(loadImageBitmap(context.assets.open("car.png")))
    }
    // Convert dp to pixel
    val carWidthPx = with(LocalDensity.current) { 240.dp.toPx() }
    val carHeightPx = with(LocalDensity.current) { 570.dp.toPx() }
    LaunchedEffect(Unit) {
        launch {
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 12000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

        }
    }
    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)) {
        // Define the starting point and the control points for the trajectory:
        //Offset( x: Float, y: Float)

        val start = Offset(size.width / 2, size.height)
        val control1 = Offset(size.width / 4, size.height / 2)
        val control2 = Offset(3 * size.width / 4, size.height / 2)
        val end = Offset(size.width / 2, 0f)

val points = listOf(start, control1, control2, end)
        // Draw the trajectory path
        val trajectoryPath = Path().apply {
            moveTo(start.x, start.y)
            cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y)
        }
        drawPath(path = trajectoryPath, color = Color.Green, style = Stroke(width = 150.dp.toPx()))

        // Place the car at the starting point of the trajectory
        val carTopLeft = start.copy(x = start.x - carWidthPx / 2, y = start.y - carHeightPx / 2)

        // Rotate the car rectangle to match the trajectory's initial direction
        val angle = 90f // Rotate the rectangle to align with the trajectory visually
        drawCar(carTopLeft, carWidthPx, carHeightPx, angle, imageBitmap, animatable, points)
    }
}


//drawing more trajectory points and handking of the drawing path smoothly by taking care not only to draw the line,
//to draw the cubic Bezier curve or quadratic  Bezier curve
@Composable
fun TrajectoryViewMoreUsecases(viewModel: TrajectoryViewModel) {
    val trajectoryPoints by viewModel.trajectoryPoints.collectAsState()

    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = Path().apply {
            // Check the number of points we have
            when {
                trajectoryPoints.size >= 4 -> {
                    // If we have at least 4 points, we can draw at least one cubic Bezier curve
                    moveTo(trajectoryPoints[0].x, trajectoryPoints[0].y)
                    cubicTo(
                        trajectoryPoints[1].x, trajectoryPoints[1].y,
                        trajectoryPoints[2].x, trajectoryPoints[2].y,
                        trajectoryPoints[3].x, trajectoryPoints[3].y
                    )
                    // If more points are present, draw additional curves
                    for (i in 4 until trajectoryPoints.size - 2 step 3) {
                        cubicTo(
                            trajectoryPoints[i].x, trajectoryPoints[i].y,
                            trajectoryPoints[i + 1].x, trajectoryPoints[i + 1].y,
                            trajectoryPoints[i + 2].x, trajectoryPoints[i + 2].y
                        )
                    }
                }
                trajectoryPoints.size == 3 -> {
                    // With 3 points, we can draw a quadratic Bezier curve
                    moveTo(trajectoryPoints[0].x, trajectoryPoints[0].y)
                    quadraticBezierTo(
                        trajectoryPoints[1].x, trajectoryPoints[1].y,
                        trajectoryPoints[2].x, trajectoryPoints[2].y
                    )
                }
                trajectoryPoints.size == 2 -> {
                    // With 2 points, we can only draw a straight line
                    moveTo(trajectoryPoints[0].x, trajectoryPoints[0].y)
                    lineTo(trajectoryPoints[1].x, trajectoryPoints[1].y)
                }
                trajectoryPoints.size == 1 -> {
                    // With a single point, we can just draw a dot
                    addOval(Rect(trajectoryPoints[0] - Offset(5.dp.toPx(), 5.dp.toPx()), Size(10.dp.toPx(), 10.dp.toPx())))
                }
            }
        }

        // Draw the path on the canvas
        drawPath(
            path = path,
            color = Color.Cyan,
            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

fun DrawScope.drawCar(
    topLeft: Offset,
    width: Float,
    height: Float,
    angle: Float,
    imageBitmap: ImageBitmap,
    animatable: Animatable<Float, AnimationVector1D>,
    trajectoryPoints: List<Offset>
) {
    // Make sure we have enough points to draw the curve
    if (trajectoryPoints.size < 4) return

    // Handle the animatable value for multiple segments
    val segmentCount = (trajectoryPoints.size - 1) / 3 // Calculate the number of segments dynamically
    val segmentLength = 1f / segmentCount
    val currentSegment = (animatable.value * segmentCount).coerceIn(0f, segmentCount.toFloat()).toInt()
    val t = ((animatable.value * segmentCount) % 1f).coerceIn(0f, 1f)

    // Calculate the indices for the current segment
    val segmentIndex = currentSegment * 3
    val segmentEndIndex = minOf(segmentIndex + 4, trajectoryPoints.size)

    // Get the points for the current segment ensuring we don't go out of bounds
    val segmentPoints = trajectoryPoints.subList(segmentIndex, segmentEndIndex)

    // If we don't have enough points for a full cubic curve, return
    if (segmentPoints.size < 4) return

    // Interpolate the position for the current segment
    val carPosition = cubicBezier(t, segmentPoints[0], segmentPoints[1], segmentPoints[2], segmentPoints[3])

    // Calculate the tangent angle for the current segment
    val tangentAngle = cubicBezierTangent(t, segmentPoints[0], segmentPoints[1], segmentPoints[2], segmentPoints[3])

    val scale = minOf(width / imageBitmap.width, height / imageBitmap.height)

    withTransform({
        // Rotate the image
        rotate(degrees = tangentAngle + 90, pivot = carPosition)
        // Proportionally scale the image
        scale(scale, scale, pivot = carPosition)
    }) {
        // Calculate the scaled dimensions of the car
        val scaledCarWidth = imageBitmap.width * scale
        val scaledCarHeight = imageBitmap.height * scale

        // Adjust topLeft position so the center of the car aligns with carPosition
        val carTopLeft = Offset(
            x = carPosition.x - scaledCarWidth, //for the position seems somehow better to have it more on the center of the path
            y = carPosition.y - scaledCarHeight / 2
        )

        // Draw the image
        drawImage(
            image = imageBitmap,
            topLeft = carTopLeft
        )
    }
}

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
 *
 *In this diagram:
 *
 * C1 and C2 are control points for the Bezier curve.
 * The curve starts from the bottom, goes up towards C1, turns towards C2, and then ends at the top.
 * When set these points programmatically, you will use the canvas size to determine their exact position.
 * For example:
 *
 * val start = Offset(size.width / 2, size.height)
 * val control1 = Offset(size.width / 4, size.height / 2)
 * val control2 = Offset(3 * size.width / 4, size.height / 2)
 * val end = Offset(size.width / 2, 0f)
 * This will create a vertical 'S' shaped curve from the bottom to the top of the canvas. The car would then be placed at the start or end of this path, depending on the animation or position you want to achieve.
 * */