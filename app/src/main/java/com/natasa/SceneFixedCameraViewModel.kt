package com.natasa

import androidx.lifecycle.ViewModel
import io.github.sceneview.math.Position
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SceneFixedCameraViewModel : ViewModel() {
    private val _position = MutableStateFlow(Position(0f, 0f, 0f))
    val position = _position.asStateFlow()

    fun moveForward() {
        _position.value = _position.value.copy(x=_position.value.x, y = _position.value.y, z = _position.value.z+1f ) // Adjust movement step as needed
    }

    fun moveBackward() {
        _position.value = _position.value.copy(x=_position.value.x, y = _position.value.y, z = _position.value.z-1f ) // Adjust movement step as needed
    }
}

