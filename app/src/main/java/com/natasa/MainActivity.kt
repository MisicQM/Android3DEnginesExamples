package com.natasa

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.natasa.canvasexample.CanvasExampleActivity
import com.natasa.canvasexample.CanvasExampleActivityCarImage
import com.natasa.canvasexample.CanvasExampleActivityGrid
import com.natasa.canvasexample.CanvasExampleActivitySimple
import com.natasa.filamentexample.FilamentActivity
import com.natasa.sceneview.SceneView3DActivity
import com.natasa.sceneview.SceneViewFixedCameraActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(26.dp),
                                text = "Different 2D/3D Android examples:",
                                fontSize = 16.sp
                            )
                            ElevatedButton( modifier = Modifier.padding(all = 16.dp),onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        FilamentActivity::class.java
                                    )
                                )
                            }) {
                                Text("Go to Filament Example")
                            }
                            ElevatedButton(
                                modifier = Modifier.padding(all = 16.dp),
                                onClick = {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            SceneView3DActivity::class.java
                                        )
                                    )
                                }) {
                                Text("Go to SceneView 3D Example ")
                            }
                            ElevatedButton( modifier = Modifier.padding(all = 16.dp),onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        SceneViewFixedCameraActivity::class.java
                                    )
                                )
                            }) {
                                Text("Go to SceneView Fixed Camera Example")
                            }


                            ElevatedButton( modifier = Modifier.padding(all = 16.dp),onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        CanvasExampleActivitySimple::class.java
                                    )
                                )
                            }) {
                                Text("Go to Canvas Simple Path")
                            }
                            ElevatedButton( modifier = Modifier.padding(all = 16.dp),onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        CanvasExampleActivityCarImage::class.java
                                    )
                                )
                            }) {
                                Text("Go to Canvas Example With Car Image")
                            }
                            ElevatedButton( modifier = Modifier.padding(all = 16.dp),onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        CanvasExampleActivity::class.java
                                    )
                                )
                            }) {
                                Text("Go to Canvas Example With Car Animation")
                            }
                            ElevatedButton( modifier = Modifier.padding(all = 16.dp),onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        CanvasExampleActivityGrid::class.java
                                    )
                                )
                            }) {
                                Text("Go to Canvas Example Grid Animation")
                            }
                        }
                    }

                }
            )
        }
    }
}
