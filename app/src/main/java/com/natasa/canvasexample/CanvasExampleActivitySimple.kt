package com.natasa.canvasexample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.natasa.R
import com.natasa.canvasexample.ImageHelper.loadImageBitmapFromStream
import com.natasa.canvasexample.PathHelper.cubicBezier
import com.natasa.canvasexample.PathHelper.cubicBezierTangent
import kotlinx.coroutines.launch
import java.io.InputStream

class CanvasExampleActivitySimple : ComponentActivity() {
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

            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(width = 40.dp.toPx())
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
        val option = BitmapFactory.Options()
        option.inPreferredConfig = Bitmap.Config.ARGB_8888
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
            val carPosition = cubicBezier(t, segmentPoints[0], segmentPoints[1], segmentPoints[2], segmentPoints[3])

            // Calculate the tangent angle for the current segment
            val tangentAngle = cubicBezierTangent(t, segmentPoints[0], segmentPoints[1], segmentPoints[2], segmentPoints[3])

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
}
