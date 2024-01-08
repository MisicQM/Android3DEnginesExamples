package com.natasa

import androidx.lifecycle.ViewModel
import io.github.sceneview.math.Position
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SceneView3DViewModel : ViewModel() {
    private val _position = MutableStateFlow(Position(0f, 0f, 0f))
    val position = _position.asStateFlow()

    private val _isObjectVisible = MutableStateFlow(true) // Default to visible
    val isObjectVisible = _isObjectVisible.asStateFlow()
    private val _isObjectAnimated = MutableStateFlow(true)
    val isObjectAnimated = _isObjectAnimated.asStateFlow()

    fun toggleObjectVisibility() {
        _isObjectVisible.value = !_isObjectVisible.value
    }

    fun toggleObjectAnimation() {
        _isObjectAnimated.value = !_isObjectAnimated.value
    }

    fun moveForward() {
        _position.value = _position.value.copy(
            x = _position.value.x + 1f,
            y = _position.value.y,
            z = _position.value.z
        ) // Adjust movement step as needed
    }

    fun moveBackward() {
        _position.value = _position.value.copy(
            x = _position.value.x - 1f,
            y = _position.value.y,
            z = _position.value.z
        ) // Adjust movement step as needed
    }
}

