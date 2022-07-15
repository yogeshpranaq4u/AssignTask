package com.example.myapplication.ui

import android.content.Context
import android.hardware.Camera
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class CamerPreview(context: Context?, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    private var mSurfaceHolder: SurfaceHolder? = null
    private var mCamera: Camera? = null


    override fun surfaceCreated(p0: SurfaceHolder) {
        TODO("Not yet implemented")
        mCamera?.startPreview()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        TODO("Not yet implemented")
    }
}