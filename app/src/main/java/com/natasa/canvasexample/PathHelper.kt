package com.natasa.canvasexample

import androidx.compose.ui.geometry.Offset
import kotlin.math.atan2
import kotlin.math.pow

internal object PathHelper {
    // Helper functions for a single cubic Bezier curve
    /***
     *  The [cubicBezierTangent] function calculates the angle of the tangent line relative to a reference
     *  (usually the horizontal axis).
     *  This angle is crucial for orienting the car so that it appears to follow the curve naturally.
     *  Without rotating the car to match the tangent, the car would not look like it's turning as it follows the path of the curve,
     *  it would instead slide sideways or move forwards/backwards without changing direction.
     * */
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
    /**
     * A [cubicBezier] curve is defined by four points: the start point, two control points, and the end point.
     * The curve starts at the start point and ends at the end point, with its shape being influenced by the two control points.
     *
     * Interpolation: The cubicBezier function calculates the interpolated position of the car on the curve for a given t.
     * As t goes from 0 to 1, the car moves from the start point to the end point of the current segment of the curve.
     * **/
     fun cubicBezier(t: Float, start: Offset, cp1: Offset, cp2: Offset, end: Offset): Offset {
        val oneMinusT = 1 - t
        return Offset(
            x = oneMinusT.pow(3) * start.x + 3 * oneMinusT.pow(2) * t * cp1.x + 3 * oneMinusT * t.pow(2) * cp2.x + t.pow(3) * end.x,
            y = oneMinusT.pow(3) * start.y + 3 * oneMinusT.pow(2) * t * cp1.y + 3 * oneMinusT * t.pow(2) * cp2.y + t.pow(3) * end.y
        )
    }
}