package com.natasa

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.google.android.filament.Camera
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode

class SceneViewFixedCameraActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[SceneFixedCameraViewModel::class.java]

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black
            ) {
            SceneviewTheme {
                val engine = rememberEngine()
                val modelLoader = rememberModelLoader(engine)
                val environmentLoader = rememberEnvironmentLoader(engine)

                // Set up nodes for the car and other moving objects
                val carNode = rememberNode {
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(
                            assetFileLocation = "models/environment.glb",

                        ), scaleToUnits = 320.0f).apply {
                        rotation = Rotation(x = -90f, y = 90f, z = 0f)
                    }
                }

                // Set up the camera with a top-down view
                val cameraNode = rememberCameraNode(engine).apply {
                    position = Position(x = 0f, y = 30f, z = 0f) // Y is the elevation

                    rotation = Rotation(x = -90f, y = 0f, z = 0f) // -90 degrees around the X-axis to look down
                    // Set to orthographic projection, if supported
                    setProjection(
                        Camera.Projection.ORTHO,
                        left = -350.0, // negative half-width of the visible area
                        right = 350.0, // positive half-width of the visible area
                        bottom = -350.0, // negative half-height of the visible area
                        top = 350.0, // positive half-height of the visible area
                        near = 1.0, // near clipping plane (should be positive and as small as possible)
                        far = 100.0 // far clipping plane (should be as large as necessary to encompass the scene)
                    )
                }

                // Center node to position the camera node in the scene
                val centerNode = rememberNode(engine).apply {
                    addChildNode(cameraNode)
                }

                // Scene setup
                Box(modifier = Modifier
                    .fillMaxSize()
                    .pointerInteropFilter { // Intercepts touch events
                        true // Return true to consume the event and prevent further processing
                    }) {
                    Scene(
                        modifier = Modifier.fillMaxSize(),
                        isOpaque = true,
                        engine = engine,
                        modelLoader = modelLoader,
                        cameraNode = cameraNode,
                        childNodes = listOf(
                            carNode, centerNode
                        ),
                        environment = environmentLoader.createHDREnvironment(
                            assetFileLocation = "models/wide_street_01_2k.hdr"
                        )!!,
                        onFrame = {
                            // Update the moving objects in the scene
                            // Note: The camera node is not updated here to maintain the top-down view
                        }
                    )
                }

                // UI for controlling the moving car
                SceneViewMovingObject(movingObjectNode = centerNode, viewModel = viewModel)
            }
        }
    }
    }

    @Composable
    fun SceneViewMovingObject(viewModel: SceneFixedCameraViewModel, movingObjectNode: Node) {
        val position by viewModel.position.collectAsState()
        Log.d("scene fixed camera","position::: $position")
        // Update the position of movingObjectNode
        movingObjectNode.position = position

        // Buttons for moving the object
        Column(modifier = Modifier.background(Color.Transparent)) {
            Text(  modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),text = "The camera with a top-down view\nThe camera node is not updated here to maintain the top-down view. \nAssets used here are from the project: \nRemoteParkAssist - 3DAssets. ", fontSize = 13.sp, style = TextStyle(color = Color.Yellow)
            )
            Button(onClick = { viewModel.moveForward() }) {
                Text(text ="Move Forward" )
                Log.d("scene","Move Forward")
            }
            Button(onClick = { viewModel.moveBackward() }) {
                Text(text ="Move Backward" )
                Log.d("scene","Move Backward")
            }
        }
    }
}
