package com.natasa

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.*

import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

class CanvasExampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CarAndTrajectoryCanvas()
                    }
                }
            }
        }
    }
}

@Composable
fun CarAndTrajectoryCanvas() {
    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray)) {
        // Define the car rectangle size
        val carWidth = 160.dp.toPx()
        val carHeight = 90.dp.toPx()

        // Define the starting point and the control points for the trajectory
        val start = center.copy(y = size.height - carHeight * 2)
        val control1 = Offset(size.width / 2f, size.height / 1.5f)
        val control2 = Offset(size.width / 2f, size.height / 3f)
        val end = Offset(size.width / 2f, carHeight)

        // Draw the trajectory path
        val trajectoryPath = Path().apply {
            moveTo(start.x, start.y)
            cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y)
        }
        drawPath(path = trajectoryPath, color = Color.Green, style = Stroke(width = 100.dp.toPx()))

        // Place the car at the starting point of the trajectory
        val carTopLeft = start.copy(x = start.x - carWidth / 2, y = start.y - carHeight / 2)

        // Rotate the car rectangle to match the trajectory's initial direction
        val angle = -90f // Rotate the rectangle to align with the trajectory visually
        drawCar(carTopLeft, carWidth, carHeight, angle)
    }
}

fun DrawScope.drawCar(topLeft: Offset, width: Float, height: Float, angle: Float) {
    rotate(degrees = angle, pivot = topLeft) {
        drawRect(
            color = Color.Gray,
            topLeft = topLeft,
            size = androidx.compose.ui.geometry.Size(width, height)
        )
    }
}
