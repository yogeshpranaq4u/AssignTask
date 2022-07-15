package com.example.myapplication.ui

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.SurfaceViewCameraBinding
import com.example.myapplication.utils.Constant
import java.io.IOException


class SufraceViewCamera : Fragment(), SurfaceHolder.Callback, Camera.PictureCallback {

    private var binding: SurfaceViewCameraBinding? = null
    private val surfaceView: SurfaceView? = null
    private var camera: Camera? = null
    private var surfaceHolder: SurfaceHolder? = null

    private var mPicture: PictureCallback? = null

    companion object{
        var bitmap: Bitmap? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SurfaceViewCameraBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            if (allPermissionGranted()) {
                setupSurfaceHolder()
            }
        activity?.let { getCameraAutoFocus(it) }

    }

    private fun getCameraAutoFocus(activity: Activity){
        val pm: PackageManager? = activity.packageManager
        if (pm?.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == true && pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
            // True means the camera has autofocus mode on. Do what ever you want to do
            Log.d("TAG", "getCameraAutoFocus123: ${pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)}")
            captureImage()
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constant.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                //startCamera()
                setupSurfaceHolder()
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

    private fun setupSurfaceHolder() {
        surfaceHolder = binding?.surfaceView?.holder
        surfaceHolder?.addCallback(this)
        setBtnClick()
    }

    private fun setBtnClick() {
        binding?.takePicture?.setOnClickListener {
            captureImage()
        }
        binding?.retakePicture?.setOnClickListener {
            binding?.surfaceView?.visibility = View.VISIBLE
            binding?.view?.visibility = View.VISIBLE
            binding?.viewImage?.visibility = View.GONE
            binding?.retakePicture?.visibility = View.GONE
//            setupSurfaceHolder()
        }
    }

    fun captureImage() {
        camera?.takePicture(null, null, mPicture)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        startCamera()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        resetCamera()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
    }

    override fun onPictureTaken(p0: ByteArray?, p1: Camera?) {
    }


    private fun startCamera() {
        camera = Camera.open()
        camera?.setDisplayOrientation(90)
        mPicture = getPictureCallback()
        try {
            camera?.setPreviewDisplay(surfaceHolder)
            camera?.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun resetCamera() {
        if (surfaceHolder?.getSurface() == null) {
            // Return if preview surface does not exist
            return;
        }

        if (camera != null) {
            // Stop if preview surface is already running.
            camera?.stopPreview();
            try {
                // Set preview display
                camera?.setPreviewDisplay(surfaceHolder);
            } catch (e: IOException) {
                e.printStackTrace();
            }
            // Start the camera preview...
            camera?.startPreview();
        }
    }

    private fun getPictureCallback(): PictureCallback? {
        return PictureCallback { data, camera ->
            binding?.surfaceView?.visibility = View.GONE
            binding?.view?.visibility = View.GONE
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            //findNavController().navigate(R.id.action_sufraceViewCamera_to_previewImage,)
            binding?.viewImage?.visibility = View.VISIBLE
            binding?.retakePicture?.visibility = View.VISIBLE
            binding?.viewImage?.setImageBitmap(bitmap)
            val matrix = ColorMatrix()
            matrix.setSaturation(0.0f)

            val filter = ColorMatrixColorFilter(matrix)
            binding?.viewImage?.setColorFilter(filter)
        }
    }
}
