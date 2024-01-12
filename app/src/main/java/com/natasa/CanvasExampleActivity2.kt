package com.natasa

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.natasa.filamentexample.FilamentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.material.animation.AnimationUtils.lerp
import kotlinx.coroutines.launch
import java.io.InputStream
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.random.Random

class CanvasExampleActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                content = { padding ->

                    Box(
                        Modifier
                            .padding(padding)
                            .background(Color.LightGray)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        TrajectoryRoadWithCarDots3()

                    }

                }
            )
        }
    }

    // Helper function to calculate the tangent angle at a point on a cubic Bezier curve
    fun cubicBezierTangent(t: Float, start: Offset, cp1: Offset, cp2: Offset, end: Offset): Float {
        val oneMinusT = 1 - t
        val dx = 3 * oneMinusT.pow(2) * (cp1.x - start.x) +
                6 * oneMinusT * t * (cp2.x - cp1.x) +
                3 * t.pow(2) * (end.x - cp2.x)
        val dy = 3 * oneMinusT.pow(2) * (cp1.y - start.y) +
                6 * oneMinusT * t * (cp2.y - cp1.y) +
                3 * t.pow(2) * (end.y - cp2.y)
        return atan2(dy, dx) * (180 / Math.PI).toFloat()
    }
    // Helper function to interpolate a point on a cubic Bezier curve
    private fun cubicBezier(t: Float, start: Offset, cp1: Offset, cp2: Offset, end: Offset): Offset {
        val oneMinusT = 1 - t
        return Offset(
            x = oneMinusT.pow(3) * start.x + 3 * oneMinusT.pow(2) * t * cp1.x + 3 * oneMinusT * t.pow(2) * cp2.x + t.pow(3) * end.x,
            y = oneMinusT.pow(3) * start.y + 3 * oneMinusT.pow(2) * t * cp1.y + 3 * oneMinusT * t.pow(2) * cp2.y + t.pow(3) * end.y
        )
    }

    @Composable
    fun TrajectoryRoadWithCarDots2() {
        val animatable = remember { Animatable(0f) }
        // Define the start, control, and end points of the cubic Bezier curve
        val start = Offset(100f, 100f)
        val cp1 = Offset(200f, 200f)
        val cp2 = Offset(400f, 800f)
        val end = Offset(700f, 700f)

        LaunchedEffect(Unit) {
            launch {
                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 8000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
        }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)) {

            val path = Path().apply {
                moveTo(start.x, start.y)
                cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, end.x, end.y)
            }

            // Draw the road 5 times bigger
            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(width = 40.dp.toPx()) // 5 times the original width
            )

            // Get the car's position and tangent angle along the curve
            val carPosition = cubicBezier(animatable.value, start, cp1, cp2, end)
            val tangentAngle = cubicBezierTangent(animatable.value, start, cp1, cp2, end)

            // Rotate and draw the car based on the tangent angle
            withTransform({
                rotate(degrees = tangentAngle, pivot = carPosition)
            }) {
                val carWidth = 60.dp.toPx()
                val carHeight = 30.dp.toPx()
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(carPosition.x - carWidth / 2, carPosition.y - carHeight / 2),
                    size = androidx.compose.ui.geometry.Size(carWidth, carHeight)
                )
            }
        }
    }
    // Helper functions for a single cubic Bezier curve
    fun cubicBezier2(t: Float, p0: Offset, p1: Offset, p2: Offset, p3: Offset): Offset {
        val oneMinusT = 1 - t
        return Offset(
            x = oneMinusT.pow(3) * p0.x + 3 * oneMinusT.pow(2) * t * p1.x + 3 * oneMinusT * t.pow(2) * p2.x + t.pow(3) * p3.x,
            y = oneMinusT.pow(3) * p0.y + 3 * oneMinusT.pow(2) * t * p1.y + 3 * oneMinusT * t.pow(2) * p2.y + t.pow(3) * p3.y
        )
    }

    fun cubicBezierTangent2(t: Float, p0: Offset, p1: Offset, p2: Offset, p3: Offset): Float {
        val oneMinusT = 1 - t
        val dx = 3 * oneMinusT.pow(2) * (p1.x - p0.x) +
                6 * oneMinusT * t * (p2.x - p1.x) +
                3 * t.pow(2) * (p3.x - p2.x)
        val dy = 3 * oneMinusT.pow(2) * (p1.y - p0.y) +
                6 * oneMinusT * t * (p2.y - p1.y) +
                3 * t.pow(2) * (p3.y - p2.y)
        return atan2(dy, dx) * (180 / Math.PI).toFloat()
    }

    // Your Composable function
    @Composable
    fun TrajectoryRoadWithCarDots3() {
        val animatable = remember { Animatable(0f) }
        // Define the start, control, and end points of the cubic Bezier curve
        val start = Offset(100f, 100f)
        val cp1 = Offset(300f, 300f)
        val cp2 = Offset(500f, 800f)
        val cp3 = Offset(500f, 900f)
        val cp4 = Offset(500f, 1000f)
        val cp5 = Offset(300f, 1200f)
        val end = Offset(100f, 1200f)

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

        val carPainter = painterResource(id = R.drawable.car)
        val carWidth = 60.dp // Adjust size as needed
        val carHeight = 30.dp // Adjust size as needed
       // val image: ImageBitmap = loadImageBitmap(resource = "drawable/car.png") // Adjust the path as necessary
        val option = BitmapFactory.Options()
        option.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeResource(
            LocalContext.current.resources,
            R.drawable.car,
            option
        ).asImageBitmap()
        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)) {
            // Define the path using the given points
            val path = Path().apply {
                moveTo(start.x, start.y)
                cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, cp3.x, cp3.y)
                // Continue the curve with additional segments
                cubicTo(cp3.x, cp3.y, cp4.x, cp4.y, cp5.x, cp5.y)
                lineTo(end.x, end.y)
            }
            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(width = 40.dp.toPx()) // 5 times the original width
            )
            // Handle the animatable value for multiple segments
            val segmentCount = 2 // You have 2 cubic Bezier curves
            val segmentLength = 1f / segmentCount
            val currentSegment = (animatable.value * segmentCount).toInt()
            val t = (animatable.value - currentSegment * segmentLength) / segmentLength

            // Define the points for each segment
            val points = listOf(start, cp1, cp2, cp3, cp4, cp5, end)

            // Get the points for the current segment
            val segmentIndex = currentSegment * 3
            val segmentPoints = points.subList(segmentIndex, segmentIndex + 4)

            // Interpolate the position for the current segment
            val carPosition = cubicBezier2(t, segmentPoints[0], segmentPoints[1], segmentPoints[2], segmentPoints[3])

            // Calculate the tangent angle for the current segment
            val tangentAngle = cubicBezierTangent2(t, segmentPoints[0], segmentPoints[1], segmentPoints[2], segmentPoints[3])

            // Rotate and draw the car based on the tangent angle
            withTransform({
                rotate(degrees = tangentAngle, pivot = carPosition)
            }) {
                val carWidth = 60.dp.toPx()
                val carHeight = 30.dp.toPx()
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(carPosition.x - carWidth / 2, carPosition.y - carHeight / 2),
                    size = androidx.compose.ui.geometry.Size(carWidth, carHeight)
                )

            }
        }
    }
    @Composable
    fun loadImageBitmapFromStream(inputStream: InputStream): ImageBitmap {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        return bitmap.asImageBitmap()
    }
    @Composable
    fun getAssetInputStream(assetName: String): InputStream {
        val context = LocalContext.current
        return context.assets.open(assetName)
    }
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
    @Composable
    fun MyCanvasWithImage() {
        // Load the image
        val imageStream: InputStream = this::class.java.classLoader.getResourceAsStream("drawable/car.png")
        val imageBitmap: ImageBitmap = loadImageBitmapFromStream(imageStream)

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Define the desired width and height
            val desiredWidth = 100.dp.toPx()
            val desiredHeight = 50.dp.toPx()

            // Calculate the scale factors
            val scaleX = desiredWidth / imageBitmap.width
            val scaleY = desiredHeight / imageBitmap.height

            withTransform({
                // Scale the canvas
                scale(scaleX, scaleY, pivot = Offset.Zero)
            }) {
                // Draw the image on the canvas
                drawImage(
                    image = imageBitmap,
                    topLeft = center.copy(x = center.x / scaleX - imageBitmap.width / 2, y = center.y / scaleY - imageBitmap.height / 2)
                )
            }
        }
    }
}
