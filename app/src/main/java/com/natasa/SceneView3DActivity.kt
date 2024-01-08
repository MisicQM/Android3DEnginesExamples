package com.natasa


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import io.github.sceneview.Scene
import io.github.sceneview.animation.Transition.animateRotation
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit.MILLISECONDS

class SceneView3DActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[SceneView3DViewModel::class.java]

        setContent {
            val isObjectVisible by viewModel.isObjectVisible.collectAsState()
            val isObjectAnimated by viewModel.isObjectAnimated.collectAsState()

            SceneviewTheme {
                val engine = rememberEngine()
                val modelLoader = rememberModelLoader(engine)
                val environmentLoader = rememberEnvironmentLoader(engine)
                val newMovingNodePosition = Position(x = 2f, y = 2f, z = 0f)
                val carFlatNode = rememberNode {
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(
                            assetFileLocation = "models/defaultCar.gltf"
                        ), scaleToUnits = 1.0f
                    )
                }
                val carNode = rememberNode {
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(
                            assetFileLocation = "models/car.glb"
                        ), scaleToUnits = 100.0f
                    )
                }
                val movingObjectsNode = rememberNode {
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(
                            assetFileLocation = "models/untitled.glb"
                        ), scaleToUnits = 1.0f
                    )
                }
                val cameraNode = rememberCameraNode(engine).apply {
                    position = Position(x = 1f, z = 6.0f)
                }
                val centerNode = rememberNode(engine).addChildNode(cameraNode)
                val cameraTransition = rememberInfiniteTransition(label = "CameraTransition")
                //apply animation
                val cameraMovingNodeRotation by cameraTransition.animateRotation(
                    initialValue = Rotation(y = 0.0f),
                    targetValue = Rotation(y = 360.0f),
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 7.seconds.toInt(MILLISECONDS))
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Scene(modifier = Modifier.fillMaxSize(),
                        engine = engine,
                        modelLoader = modelLoader,
                        cameraNode = cameraNode,
                        childNodes = listOf(
                            centerNode, carFlatNode, carNode, movingObjectsNode
                        ),
                        environment = environmentLoader.createHDREnvironment(
                            assetFileLocation = "models/wide_street_01_2k.hdr"
                        )!!,
                        onFrame = {
                            carNode.position = Position(2f, 3f, 0f)
                            if (isObjectAnimated) {
                                carNode.rotation = cameraMovingNodeRotation
                            }
                            movingObjectsNode.position = newMovingNodePosition
                            movingObjectsNode.rotation = cameraMovingNodeRotation
                            movingObjectsNode.isVisible = isObjectVisible
                            carNode.isVisible = isObjectVisible
                          //  Log.d("scene", "position::: ${cameraNode.position}")
                            cameraNode.lookAt(centerNode)
                        })

                }
                SceneViewMovingObject(
                    viewModel = viewModel,
                    movingObjectNode = centerNode,
                    isObjectVisible = isObjectVisible,
                    isObjectAnimated = isObjectAnimated
                )
            }
        }


    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(
            this@SceneView3DActivity,
            "Zoom out and rotate the view with finger gestures.",
            Toast.LENGTH_SHORT
        ).show()
    }
    @Composable
    fun SceneViewMovingObject(
        viewModel: SceneView3DViewModel,
        movingObjectNode: Node,
        isObjectVisible: Boolean, isObjectAnimated: Boolean
    ) {
        val position by viewModel.position.collectAsState()


        movingObjectNode.position = position

        // Buttons for moving the object
        Column {
            Text(  modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),text = "Assets used here are from the project: \nRemoteParkAssist - 3DAssets and created in the Blender. \nRendered objects shows animation, movement and visibility changes.  ", fontSize = 13.sp, style = TextStyle(color = Color.Yellow)
            )


            ElevatedButton(onClick = { viewModel.moveForward() }) {
                Text(text = "Move Forward")
            }
            ElevatedButton(onClick = { viewModel.moveBackward() }) {
                Text(text = "Move Backward")
            }
            ElevatedButton(onClick = { viewModel.toggleObjectVisibility() }) {
                Text(text = if (isObjectVisible) "Hide Objects" else "Show Objects")
            }
            ElevatedButton(onClick = { viewModel.toggleObjectAnimation() }) {
                Text(text = if (isObjectAnimated) "Stop Blue car animation" else "Start Blue car animation")
            }
        }
    }

}