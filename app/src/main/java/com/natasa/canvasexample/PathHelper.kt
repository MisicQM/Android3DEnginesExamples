package com.natasa.canvasexample

import androidx.compose.ui.geometry.Offset
import kotlin.math.atan2
import kotlin.math.pow

internal object PathHelper {
    // Helper functions for a single cubic Bezier curve
    fun cubicBezier(t: Float, p0: Offset, p1: Offset, p2: Offset, p3: Offset): Offset {
        val oneMinusT = 1 - t
        return Offset(
            x = oneMinusT.pow(3) * p0.x + 3 * oneMinusT.pow(2) * t * p1.x + 3 * oneMinusT * t.pow(2) * p2.x + t.pow(3) * p3.x,
            y = oneMinusT.pow(3) * p0.y + 3 * oneMinusT.pow(2) * t * p1.y + 3 * oneMinusT * t.pow(2) * p2.y + t.pow(3) * p3.y
        )
    }

    fun cubicBezierTangent(t: Float, p0: Offset, p1: Offset, p2: Offset, p3: Offset): Float {
        val oneMinusT = 1 - t
        val dx = 3 * oneMinusT.pow(2) * (p1.x - p0.x) +
                6 * oneMinusT * t * (p2.x - p1.x) +
                3 * t.pow(2) * (p3.x - p2.x)
        val dy = 3 * oneMinusT.pow(2) * (p1.y - p0.y) +
                6 * oneMinusT * t * (p2.y - p1.y) +
                3 * t.pow(2) * (p3.y - p2.y)
        return atan2(dy, dx) * (180 / Math.PI).toFloat()
    }
}