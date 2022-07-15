package com.example.myapplication.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {
    private var binding: FragmentSecondBinding? = null
    private val CAMERA_PIC_REQUEST = 1
    private lateinit var camera: Camera

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSecondBinding.bind(view)

        binding?.thirdFragment?.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_dataShown)
        }

        binding?.galleryFragment?.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_galleryFragment)
        }
        binding?.opencamera?.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
        }
        binding?.customCamera?.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_customCameraFragment)
        }
        binding?.surfaceCamera?.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_sufraceViewCamera)
        }

        activity?.let { getCameraAutoFocus(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_PIC_REQUEST) {
            val image: Bitmap = data?.getExtras()?.get("data") as Bitmap
            binding?.getImage?.setImageBitmap(image)
        }
    }

    fun getCameraAutoFocus(activity: Activity) {
        val pm: PackageManager? = activity?.packageManager
        if (pm?.hasSystemFeature(PackageManager.FEATURE_CAMERA) == true && pm.hasSystemFeature(
                PackageManager.FEATURE_CAMERA_AUTOFOCUS
            )
        ) {
            // True means the camera has autofocus mode on. Do what ever you want to do
            Log.d(
                "TAG",
                "getCameraAutoFocus: ${pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)}"
            )

        }
    }


}