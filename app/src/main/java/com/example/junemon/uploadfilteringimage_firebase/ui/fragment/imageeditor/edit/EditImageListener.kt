package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.imageeditor.edit

/**
 * Created by ian on 07/02/19.
 */

interface EditImageListener {
    fun onBrightnessChanged(brightness: Int)

    fun onSaturationChanged(saturation: Float)

    fun onContrastChanged(contrast: Float)

    fun onEditStarted()

    fun onEditCompleted()
}