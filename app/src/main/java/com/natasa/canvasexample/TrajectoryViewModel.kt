package com.natasa.canvasexample

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrajectoryViewModel : ViewModel() {
    private val _trajectoryPoints = MutableStateFlow<List<Offset>>(emptyList())
    val trajectoryPoints: StateFlow<List<Offset>> = _trajectoryPoints

    init {
        viewModelScope.launch {
            _trajectoryPoints.value = calculateTrajectoryPoints()
        }
    }

    private fun calculateTrajectoryPoints(): List<Offset> {
        //  calculation logic here.
        return listOf( /* ... */ )
    }
}