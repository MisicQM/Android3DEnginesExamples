package com.natasa.canvasexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.natasa.canvasexample.ImageHelper.loadImageBitmap
/**
 * Not used for now
 * to be continuned and refactored...
 * */
class CanvasExampleActivityGridTraj : ComponentActivity() {

    private val viewModel: ParkingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        val gridRotation = remember { Animatable(0f) }
                        val gridOffset =
                            remember { Animatable(Offset.Zero, Offset.VectorConverter) }

                                //  GridCanvasAnimated( gridOffset, Color.LightGray,)
                        CarAndTrajectoryCanvas3(gridRotation, gridOffset, viewModel)
                        CoordinateTextCanvas()
                    }
                }
            }
        }
    }


    @Composable
    fun CarAndTrajectoryCanvas3( gridRotation: Animatable<Float, AnimationVector1D>,
                                 gridOffset: Animatable<Offset, AnimationVector2D>,
                                 viewModel: ParkingViewModel) {
        val context = LocalContext.current
        val imageBitmap: ImageBitmap by remember {
            mutableStateOf(loadImageBitmap(context.assets.open("car.png")))
        }
        val carWidthPx = with(LocalDensity.current) { 140.dp.toPx() }
        val carHeightPx = with(LocalDensity.current) { 270.dp.toPx() }

        // Mocked data for vehicle position and trajectory points
        val vehiclePosition by viewModel.vehiclePosition.collectAsState()
        val vehicleTangent by viewModel.vehicleTangent.collectAsState()
        val drivingDirection by viewModel.drivingDirection.collectAsState()
        val trajectoryPoints by viewModel.trajectoryPoints.collectAsState()

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw the vehicle
            drawImage(
                image = imageBitmap,
                topLeft = Offset(
                    x = vehiclePosition.x - carWidthPx / 2,
                    y = vehiclePosition.y - carHeightPx / 2
                ),
               // size = Size(carWidthPx, carHeightPx)
            )

            // Draw the trajectory
            val path = Path().apply {
                moveTo(vehiclePosition.x, vehiclePosition.y)
                trajectoryPoints.forEach { point ->
                    lineTo(point.x, point.y)
                }
            }
            drawPath(path = path, color = Color.Green, style = Stroke(width = 5.dp.toPx()))
        }
    }
}
