package com.natasa.filamentexample

import android.content.res.AssetManager
import android.view.Choreographer
import android.view.SurfaceView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.filament.View
import com.google.android.filament.android.UiHelper
import com.google.android.filament.utils.Mat4
import com.google.android.filament.utils.ModelViewer
import com.google.ar.sceneform.lullmodel.Vec3
import java.nio.ByteBuffer
import java.nio.ByteOrder

class FilamentModelRenderer {
    private lateinit var surfaceView: SurfaceView
    private lateinit var lifecycle: Lifecycle

    private lateinit var choreographer: Choreographer
    private lateinit var uiHelper: UiHelper

    private lateinit var modelViewer: ModelViewer

    private val assets: AssetManager
        get() = surfaceView.context.assets

    private val frameScheduler = FrameCallback()

    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            choreographer.postFrameCallback(frameScheduler)
        }

        override fun onPause(owner: LifecycleOwner) {
            choreographer.removeFrameCallback(frameScheduler)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            choreographer.removeFrameCallback(frameScheduler)
            lifecycle.removeObserver(this)
        }
    }


    fun onSurfaceAvailable(surfaceView: SurfaceView, lifecycle: Lifecycle) {
        this.surfaceView = surfaceView
        this.lifecycle = lifecycle

        lifecycle.addObserver(lifecycleObserver)

        choreographer = Choreographer.getInstance()
        uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK).apply {
            // This is needed to make the background transparent
            isOpaque = false
        }

        modelViewer = ModelViewer(surfaceView = surfaceView, uiHelper = uiHelper)

        // This is needed so we can move the camera in the rendering
        surfaceView.setOnTouchListener { _, event ->
            modelViewer.onTouchEvent(event)
            true
        }

        // This is the other code needed to make the background transparent
        modelViewer.scene.skybox = null
        modelViewer.view.blendMode = View.BlendMode.TRANSLUCENT
        modelViewer.renderer.clearOptions = modelViewer.renderer.clearOptions.apply {
            clear = true
        }

        // This part defines the quality of your model
        modelViewer.view.apply {
            renderQuality = renderQuality.apply {
                hdrColorBuffer = View.QualityLevel.HIGH
            }
        }

        createRenderables()
    }

    private fun createRenderables() {

        val buffer = assets.open("models/car.glb").use { input ->
            val bytes = ByteArray(input.available())
            input.read(bytes)
            ByteBuffer.allocateDirect(bytes.size).apply {
                order(ByteOrder.nativeOrder())
                put(bytes)
                rewind()
            }
        }

        //not working?
        val texture = assets.open("models/defaultCar.png").use { stream ->
            val bytes = ByteArray(stream.available())
            stream.read(bytes)
            ByteBuffer.allocateDirect(bytes.size).apply {
                order(ByteOrder.nativeOrder())
                put(bytes)
                rewind()
            }
        }
        //not working?  modelViewer.loadModelGltf(buffer1) { texture }

        modelViewer.loadModelGlb(buffer)
        modelViewer.transformToUnitCube()
    }

    inner class FrameCallback : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            choreographer.postFrameCallback(this)
            // modelViewer.renderer.beginFrame(this, frameTimeNanos=frameTimeNanos)
            modelViewer.render(frameTimeNanos)
            // modelViewer.renderer.endFrame()
        }
    }


}