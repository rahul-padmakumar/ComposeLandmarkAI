package com.example.composelandmarkai.ui.presentation

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreview(
    cameraController: LifecycleCameraController,
    modifier: Modifier = Modifier
){
    val lifecycle = LocalLifecycleOwner.current
    AndroidView(factory = {
        PreviewView(it).apply {
            controller = cameraController
            cameraController.bindToLifecycle(lifecycle)
        }
    }, modifier = modifier)
}