package com.natasa.libgdxexample

import android.content.Context
import android.os.Bundle
import android.opengl.GLSurfaceView
import android.os.Handler
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.ApplicationLogger
import com.badlogic.gdx.Audio
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Graphics
import com.badlogic.gdx.LifecycleListener
import com.badlogic.gdx.Net
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.backends.android.AndroidApplicationBase
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidAudio
import com.badlogic.gdx.backends.android.AndroidGraphics
import com.badlogic.gdx.backends.android.AndroidInput
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Clipboard
import com.badlogic.gdx.utils.SnapshotArray

import javax.microedition.khronos.opengles.GL10

class LibGdx2DActivity : ComponentActivity(), AndroidApplicationBase {
    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit
    var applicationListener: ApplicationListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create and configure LibGDX application listener
        applicationListener = LibGDX2DTest() // Replace with your actual LibGDX app

        // Configure LibGDX with AndroidApplicationConfiguration
        val config = AndroidApplicationConfiguration()

        Gdx.graphics = AndroidGraphics(this, config, config.resolutionStrategy)

       // val resolutionStrategy = FillResolutionStrategy()
        glSurfaceView = GLSurfaceView(this).apply {
            preserveEGLContextOnPause = true
            setEGLContextClientVersion(2) // Use OpenGL ES 2.0

            // Set up the renderer for the GLSurfaceView
            setRenderer(object : GLSurfaceView.Renderer {
                override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {

                    applicationListener.create()
                }

                override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                    applicationListener.resize(width, height)

                }

                override fun onDrawFrame(gl: GL10?) {
                    applicationListener.render()
                }
            })
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }

        setContent {
            Scaffold(
                content = { padding ->
                    Box(
                        Modifier
                            .padding(padding)
                            .background(Color.LightGray)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AndroidView(
                            factory = { context ->
                                glSurfaceView
                            },
                            Modifier.fillMaxSize()
                        )
                    }
                }
            )
        }
    }


    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun getApplicationListener(): ApplicationListener {
        return applicationListener
    }

    override fun getGraphics(): Graphics {
       return Gdx.graphics
    }

    override fun getAudio(): Audio {
        TODO("Not yet implemented")
    }

    override fun getInput(): AndroidInput {
        TODO("Not yet implemented")
    }

    override fun getFiles(): Files {
        TODO("Not yet implemented")
    }

    override fun getNet(): Net {
        TODO("Not yet implemented")
    }

    override fun log(tag: String?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun log(tag: String?, message: String?, exception: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun error(tag: String?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun error(tag: String?, message: String?, exception: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun debug(tag: String?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun debug(tag: String?, message: String?, exception: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun setLogLevel(logLevel: Int) {
        TODO("Not yet implemented")
    }

    override fun getLogLevel(): Int {
        TODO("Not yet implemented")
    }

    override fun setApplicationLogger(applicationLogger: ApplicationLogger?) {
        TODO("Not yet implemented")
    }

    override fun getApplicationLogger(): ApplicationLogger {
        TODO("Not yet implemented")
    }

    override fun getType(): Application.ApplicationType {
        TODO("Not yet implemented")
    }

    override fun getVersion(): Int {
        TODO("Not yet implemented")
    }

    override fun getJavaHeap(): Long {
        TODO("Not yet implemented")
    }

    override fun getNativeHeap(): Long {
        TODO("Not yet implemented")
    }

    override fun getPreferences(name: String?): Preferences {
        TODO("Not yet implemented")
    }

    override fun getClipboard(): Clipboard {
        TODO("Not yet implemented")
    }

    override fun postRunnable(runnable: Runnable?) {
        TODO("Not yet implemented")
    }

    override fun exit() {
        TODO("Not yet implemented")
    }

    override fun addLifecycleListener(listener: LifecycleListener?) {
        TODO("Not yet implemented")
    }

    override fun removeLifecycleListener(listener: LifecycleListener?) {
        TODO("Not yet implemented")
    }

    override fun getContext(): Context {
       return this
    }

    override fun getRunnables(): Array<Runnable> {
        TODO("Not yet implemented")
    }

    override fun getExecutedRunnables(): Array<Runnable> {
        TODO("Not yet implemented")
    }

    override fun getLifecycleListeners(): SnapshotArray<LifecycleListener> {
        TODO("Not yet implemented")
    }

    override fun getApplicationWindow(): Window {
        TODO("Not yet implemented")
    }

    override fun useImmersiveMode(b: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getHandler(): Handler {
        TODO("Not yet implemented")
    }

    override fun createAudio(
        context: Context?,
        config: AndroidApplicationConfiguration?
    ): AndroidAudio {
        TODO("Not yet implemented")
    }

    override fun createInput(
        activity: Application?,
        context: Context?,
        view: Any?,
        config: AndroidApplicationConfiguration?
    ): AndroidInput {
        TODO("Not yet implemented")
    }
}