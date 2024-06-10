package com.example.composelandmarkai.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.composelandmarkai.ui.domain.Classification
import com.example.composelandmarkai.ui.domain.LandmarkClassifier
import com.example.composelandmarkai.ui.domain.TfLiteLandmarkClassifier
import com.example.composelandmarkai.ui.presentation.CameraPreview
import com.example.composelandmarkai.ui.presentation.LandmarkAnalyzer
import com.example.composelandmarkai.ui.theme.ComposeLandmarkAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(isPermissionGranted(this).not()){
            ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSIONS,
                0
            )
        }
        enableEdgeToEdge()
        setContent {
            ComposeLandmarkAITheme {

                val context = LocalContext.current

                val results = remember {
                    mutableStateOf(emptyList<Classification>())
                }

                val analyzer = remember {
                    LandmarkAnalyzer(
                        TfLiteLandmarkClassifier(context),
                    ){
                        results.value = it
                    }
                }

                val cameraLifecycleCameraController = remember{
                    LifecycleCameraController(
                        context
                    ).apply {
                        setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
                        setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            analyzer
                        )
                    }
                }

                Surface(modifier = Modifier.safeDrawingPadding()){
                    Box(modifier = Modifier.fillMaxSize()){
                        CameraPreview(
                            cameraController = cameraLifecycleCameraController,
                            modifier = Modifier.fillMaxSize()
                        )
                        Column (modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)) {
                            results.value.forEach{
                                Text(
                                    text = it.name,
                                    modifier = Modifier.fillMaxWidth()
                                        .background(Color.Cyan),
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    color = Color.Blue
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    companion object{
        val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }
}

private fun isPermissionGranted(context: Context): Boolean = MainActivity.CAMERA_PERMISSIONS.all{
    ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
}