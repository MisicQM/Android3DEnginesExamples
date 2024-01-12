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
       //trajetory points


        val start = Offset(100f, 100f)
        val cp1 = Offset(300f, 300f)
        val cp2 = Offset(500f, 800f)
        val cp3 = Offset(500f, 900f)
        val cp4 = Offset(500f, 1000f)
        val cp5 = Offset(300f, 1200f)
        val end = Offset(100f, 1200f)
        return listOf( start, cp1, cp2, cp3, cp4, cp5, end )
    }
}