package com.d205.sdutyplus.view.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.core.app.ActivityCompat
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.d205.sdutyplus.databinding.ActivityCropImageBinding
import com.d205.sdutyplus.utills.NOT_PROFILE
import com.d205.sdutyplus.utills.PROFILE

internal class CropImageActivity : CropActivity() {

    companion object {
        fun start(activity: Activity) {
            ActivityCompat.startActivity(
                activity,
                Intent(activity, CropImageActivity::class.java),
                null
            )
        }
    }

    private lateinit var binding: ActivityCropImageBinding
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCropImageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        updateRotationCounter(counter.toString())

        binding.ivRegisterStory.setOnClickListener { cropImage() } // CropImageActivity.cropImage()
        binding.ivBack.setOnClickListener { onBackPressed() } // CropImageActivity.onBackPressed()
        binding.ivRotate.setOnClickListener { onRotateClick() }

        setCropImageView(binding.cropImageView)

    }

    override fun showImageSourceDialog() {
        // Override this if you wanna a custom dialog layout
        super.showImageSourceDialog()
    }

    override fun setContentView(view: View) {
        // Override this to use your custom layout
        super.setContentView(binding.root)
    }

    private fun updateRotationCounter(counter: String) {
    }

    override fun onPickImageResult(resultUri: Uri?) {
        super.onPickImageResult(resultUri)
        Log.d("TAG", "onPickImageResult: $resultUri")
        if (resultUri != null) binding.cropImageView.setImageUriAsync(resultUri)
        val flag = intent.getIntExtra("flag", 1).toInt()
        when(flag){
            PROFILE -> {
                binding.cropImageView.apply {
                    resetCropRect()
                    cropShape = CropImageView.CropShape.OVAL
                    setAspectRatio(1,1)
                    guidelines = CropImageView.Guidelines.ON
                }
            }
            NOT_PROFILE -> {
                binding.cropImageView.apply {
                    resetCropRect()
                    cropShape = CropImageView.CropShape.RECTANGLE
                    setAspectRatio(3,4)
                    guidelines = CropImageView.Guidelines.ON
                }
            }
            else ->{
                finish()
            }

        }
    }

    // Override this to add more information into the intent
    override fun getResultIntent(uri: Uri?, error: java.lang.Exception?, sampleSize: Int): Intent {
        val result = super.getResultIntent(uri, error, sampleSize)
        return result.putExtra("EXTRA_KEY", "Extra data")
    }

    override fun setResult(uri: Uri?, error: Exception?, sampleSize: Int) {
        val result = CropImage.ActivityResult(
            originalUri = binding.cropImageView.imageUri,
            uriContent = uri,
            error = error,
            cropPoints = binding.cropImageView.cropPoints,
            cropRect = binding.cropImageView.cropRect,
            rotation = binding.cropImageView.rotatedDegrees,
            wholeImageRect = binding.cropImageView.wholeImageRect,
            sampleSize = sampleSize
        )

        Log.v("File Path", result.getUriFilePath(this).toString())
        binding.cropImageView.setImageUriAsync(result.uriContent)
    }

    override fun setResultCancel() {
        Log.i("extend", "User this override to change behaviour when cancel")
        super.setResultCancel()
    }

    override fun updateMenuItemIconColor(menu: Menu, itemId: Int, color: Int) {
        Log.i(
            "extend",
            "If not using your layout, this can be one option to change colours. Check README and wiki for more"
        )
        super.updateMenuItemIconColor(menu, itemId, color)
    }

    private fun onRotateClick() {
        counter += 90
        binding.cropImageView.rotateImage(90)
        if (counter == 360) counter = 0
        updateRotationCounter(counter.toString())
    }
}
