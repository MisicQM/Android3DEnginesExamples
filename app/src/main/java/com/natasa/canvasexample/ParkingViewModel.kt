package com.natasa.canvasexample


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

class ParkingViewModel : ViewModel() {
    private val _vehiclePosition = MutableStateFlow(Offset(500f, 1000f))
    val vehiclePosition = _vehiclePosition.asStateFlow()

    private val _vehicleTangent = MutableStateFlow(0f)
    val vehicleTangent = _vehicleTangent.asStateFlow()

    private val _drivingDirection = MutableStateFlow(true) // True for forward, false for backward
    val drivingDirection = _drivingDirection.asStateFlow()

    private val _trajectoryPoints = MutableStateFlow(
        listOf(
            Offset(500f, 1000f),
            Offset(600f, 800f),
            Offset(700f, 600f),
            Offset(800f, 400f),
            Offset(100f, 1200f)
        )
    )
    val trajectoryPoints = _trajectoryPoints.asStateFlow()
}



