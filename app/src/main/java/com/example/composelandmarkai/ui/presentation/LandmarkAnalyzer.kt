package com.example.composelandmarkai.ui.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.composelandmarkai.ui.domain.Classification
import com.example.composelandmarkai.ui.domain.TfLiteLandmarkClassifier

class LandmarkAnalyzer(
    private val landmarkClassifier: TfLiteLandmarkClassifier,
    private val onResults: (List<Classification>) -> Unit
): ImageAnalysis.Analyzer{

    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        //println("Analyze going to start")
        if(frameSkipCounter % 60 == 0) {
            println("Analyze started")
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image.toBitmap().centerCrop(321, 321)

            val results = landmarkClassifier.classify(bitmap, rotationDegrees)
            onResults(results)
        }
        //println("Analyze ended")
        frameSkipCounter ++
        image.close()
    }
}