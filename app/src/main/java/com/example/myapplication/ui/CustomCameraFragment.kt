package com.example.myapplication.ui

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.camera2.internal.compat.workaround.TargetAspectRatio.RATIO_4_3
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.CustomCameraFragmentBinding
import com.example.myapplication.utils.Constant
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CustomCameraFragment : Fragment() {
    private var binding: CustomCameraFragmentBinding? = null
    var imageCapture: ImageCapture? = null
    private lateinit var objectDirectory: File
    private var autoFocus: Boolean? = false
    private var displaymetrics: DisplayMetrics? = null
    private lateinit var savedUri:Uri

    val handler = Handler(Looper.getMainLooper())
    val r = Runnable {takePhoto()}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomCameraFragmentBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (!allPermissionGranted()) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    Constant.REQUIRED_PERMISSIONS,
                    Constant.REQUEST_CODE_PERMISSIONS
                )
            }
        } else {
            startCamera()
        }

        objectDirectory = getObjectDirectory()!!

        val display = binding?.cameraPreview?.display
        displaymetrics =  DisplayMetrics().also { display?.getRealMetrics(it)}

        binding?.clickImage?.setOnClickListener {
            takePhoto()
        }

        activity?.let { getCameraAutoFocus(it) }
        autoCLick()
        binding?.autoclick?.setOnClickListener {
            stopAutoClick()
        }
    }

    private fun getCameraAutoFocus(activity: Activity) {
        val pm: PackageManager? = activity.packageManager
        if (pm?.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == true && pm.hasSystemFeature(
                PackageManager.FEATURE_CAMERA_AUTOFOCUS
            )
        ) {
            // True means the camera has autofocus mode on. Do what ever you want to do
            Log.d(
                "TAG",
                "getCameraAutoFocus123: ${pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)}"
            )
            autoFocus = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constant.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                startCamera()
            } else {
                Log.d(Constant.TAG, "onRequestPermissionsResult: ")
                findNavController().navigateUp()
            }
        }
    }

    private fun allPermissionGranted(): Boolean {
        return Constant.REQUIRED_PERMISSIONS.all {
            context?.let { it1 ->
                ContextCompat.checkSelfPermission(
                    it1,
                    it
                )
            } == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun startCamera() {
        val cameraProvider = activity?.let { ProcessCameraProvider.getInstance(it) }
        cameraProvider?.addListener({
            val cameraProviderFuture: ProcessCameraProvider = cameraProvider.get()
            val preview = Preview.Builder().setTargetAspectRatio(RATIO_4_3).build().also { mpreview ->
                mpreview.setSurfaceProvider(
                    binding?.cameraPreview?.surfaceProvider
                )
            }

            imageCapture = ImageCapture.Builder().setTargetAspectRatio(RATIO_4_3).build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProviderFuture.unbindAll()
                activity?.let {
                    cameraProviderFuture.bindToLifecycle(
                        it,
                        cameraSelector,preview ,imageCapture
                    )
                }
            } catch (e: Exception) {
                Log.d(Constant.TAG, "startCamera: $e")
            }
        }, context?.let { ContextCompat.getMainExecutor(it) })
    }

    private fun getObjectDirectory(): File? {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let { mfile ->
            File(mfile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else activity?.filesDir
    }

    private fun takePhoto() {
        val imageCaptures = imageCapture ?: return

        val photoFile = File(
            objectDirectory,
            SimpleDateFormat(
                Constant.FILE_NAME_FORMAT,
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputoption = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()

        imageCaptures.takePicture(
            outputoption,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo saved"
                    binding?.viewImage?.let {
                        Glide
                            .with(context!!)
                            .load(savedUri)
                            .centerCrop()
                            .into(it)
                    }
                    val matrix = ColorMatrix()
                    matrix.setSaturation(0.2f)

                    val filter = ColorMatrixColorFilter(matrix)
                    binding?.viewImage?.setColorFilter(filter)
                    binding?.clickImage?.visibility = View.GONE
                    binding?.cameraPreview?.visibility = View.GONE
                    Toast.makeText(context, "$msg -- $savedUri", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

    private fun autoCLick(){
        handler.postDelayed(r, 3000)
    }

    private fun stopAutoClick(){
        handler.removeCallbacks(r)

    }
}