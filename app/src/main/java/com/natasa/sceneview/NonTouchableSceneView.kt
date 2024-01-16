package com.natasa.sceneview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import io.github.sceneview.SceneView

class NonTouchableSceneView : SceneView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    // Override the onTouchEvent method to ignore touch events
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Return true to indicate that we've handled the touch event
        // and do not want any further processing.
        return true
    }

}
