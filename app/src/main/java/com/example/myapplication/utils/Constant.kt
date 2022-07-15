package com.example.myapplication.utils

import android.Manifest

object Constant {
    const val TAG ="camerax"
    const val FILE_NAME_FORMAT ="yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSIONS =104
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
}