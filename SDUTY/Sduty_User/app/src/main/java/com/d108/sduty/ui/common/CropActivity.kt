package com.d108.sduty.ui.common

import android.Manifest
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.net.toUri
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageIntentChooser
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.databinding.CropImageActivityBinding
import com.d108.sduty.common.FLAG_CAMERA
import com.d108.sduty.common.FLAG_GALLERY
import com.d108.sduty.ui.MainActivity
import com.d108.sduty.utils.getUriForFile
import com.d108.sduty.utils.showToast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import java.io.File

private const val TAG ="CropActivity"
open class CropActivity : AppCompatActivity(),
    CropImageView.OnSetImageUriCompleteListener,
    CropImageView.OnCropImageCompleteListener {

    /**
     * Persist URI image to crop URI if specific permissions are required
     */
    private var cropImageUri: Uri? = null

    /**
     * the options that were set for the crop image
     */
    private lateinit var cropImageOptions: CropImageOptions

    private var galleryIntent: Intent? = null

    /** The crop image view library widget used in the activity */
    private var cropImageView: CropImageView? = null
    private lateinit var binding: CropImageActivityBinding
    private var latestTmpUri: Uri? = null
    private val pickImageGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { uri ->
            onPickImageResult(uri.data?.data)
        }
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) onPickImageResult(latestTmpUri) else onPickImageResult(null)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CropImageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCropImageView(binding.cropImageView)
        val bundle = intent.getBundleExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE)
        cropImageUri = bundle?.getParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE)
        cropImageOptions =
            bundle?.getParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS) ?: CropImageOptions()

        galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent!!.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")

        if (savedInstanceState == null) {
            if (cropImageUri == null || cropImageUri == Uri.EMPTY) {
                when {
                    cropImageOptions.showIntentChooser -> showIntentChooser()
                    cropImageOptions.imageSourceIncludeGallery &&
                            cropImageOptions.imageSourceIncludeCamera ->
                        showImageSourceDialog()
                    cropImageOptions.imageSourceIncludeGallery ->
                        pickImageGallery.launch(galleryIntent)
                    cropImageOptions.imageSourceIncludeCamera ->
                        openCamera()
                    else -> finish()
                }
            } else cropImageView?.setImageUriAsync(cropImageUri)
        } else {
            latestTmpUri = savedInstanceState.getString(BUNDLE_KEY_TMP_URI)?.toUri()
        }

        supportActionBar?.let {
            title =
                if (cropImageOptions.activityTitle.isNotEmpty())
                    cropImageOptions.activityTitle
                else
                    resources.getString(com.canhub.cropper.R.string.crop_image_activity_title)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun showIntentChooser() {
        val ciIntentChooser = CropImageIntentChooser(
            activity = this,
            callback = object : CropImageIntentChooser.ResultCallback {
                override fun onSuccess(uri: Uri?) {
                    onPickImageResult(uri)
                }

                override fun onCancelled() {
                    setResultCancel()
                }
            }
        )
        cropImageOptions.let { options ->
            options.intentChooserTitle
                ?.takeIf { title -> title.isNotBlank() }
                ?.let { icTitle ->
                    ciIntentChooser.setIntentChooserTitle(icTitle)
                }
            options.intentChooserPriorityList
                ?.takeIf { appPriorityList -> appPriorityList.isNotEmpty() }
                ?.let { appsList ->
                    ciIntentChooser.setupPriorityAppsList(appsList)
                }
            val cameraUri: Uri? = if (options.imageSourceIncludeCamera) getTmpFileUri() else null
            ciIntentChooser.showChooserIntent(
                includeCamera = options.imageSourceIncludeCamera,
                includeGallery = options.imageSourceIncludeGallery,
                cameraImgUri = cameraUri
            )
        }
    }

    private fun openSource(source: Source) {
        when (source) {
            Source.CAMERA -> openCamera()
            Source.GALLERY -> {
                pickImageGallery.launch(galleryIntent)
            }
        }
    }

    private fun openCamera() {
        getTmpFileUri().let { uri ->
            latestTmpUri = uri
            takePicture.launch(uri)
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return getUriForFile(this, tmpFile)
    }

    /**
     * This method show the dialog for user source choice, it is an open function so can be override
     * and customised with the app layout if need.
     */
    open fun showImageSourceDialog() {
        CropSelectDialog(this).let{
            it.onClickListener = object : CropSelectDialog.OnClickListener{
                override fun onClick(flag: Int) {
                    when(flag){
                        FLAG_CAMERA -> checkPermission()
                        FLAG_GALLERY -> openSource(Source.GALLERY)
                    }
                }
            }
            it.show(supportFragmentManager, null)
        }
    }

    public override fun onStart() {
        super.onStart()
        cropImageView?.setOnSetImageUriCompleteListener(this)
        cropImageView?.setOnCropImageCompleteListener(this)
    }

    public override fun onStop() {
        super.onStop()
        cropImageView?.setOnSetImageUriCompleteListener(null)
        cropImageView?.setOnCropImageCompleteListener(null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BUNDLE_KEY_TMP_URI, latestTmpUri.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (cropImageOptions.skipEditing) return true
        menuInflater.inflate(com.canhub.cropper.R.menu.crop_image_menu, menu)

        if (!cropImageOptions.allowRotation) {
            menu.removeItem(com.canhub.cropper.R.id.ic_rotate_left_24)
            menu.removeItem(com.canhub.cropper.R.id.ic_rotate_right_24)
        } else if (cropImageOptions.allowCounterRotation) {
            menu.findItem(com.canhub.cropper.R.id.ic_rotate_left_24).isVisible = true
        }

        if (!cropImageOptions.allowFlipping) menu.removeItem(com.canhub.cropper.R.id.ic_flip_24)

        if (cropImageOptions.cropMenuCropButtonTitle != null) {
            menu.findItem(com.canhub.cropper.R.id.crop_image_menu_crop).title =
                cropImageOptions.cropMenuCropButtonTitle
        }
        var cropIcon: Drawable? = null
        try {
            if (cropImageOptions.cropMenuCropButtonIcon != 0) {
                cropIcon = ContextCompat.getDrawable(this, cropImageOptions.cropMenuCropButtonIcon)
                menu.findItem(com.canhub.cropper.R.id.crop_image_menu_crop).icon = cropIcon
            }
        } catch (e: Exception) {
            Log.w("AIC", "Failed to read menu crop drawable", e)
        }
        if (cropImageOptions.activityMenuIconColor != 0) {
            updateMenuItemIconColor(
                menu,
                com.canhub.cropper.R.id.ic_rotate_left_24,
                cropImageOptions.activityMenuIconColor
            )
            updateMenuItemIconColor(
                menu,
                com.canhub.cropper.R.id.ic_rotate_right_24,
                cropImageOptions.activityMenuIconColor
            )
            updateMenuItemIconColor(menu, com.canhub.cropper.R.id.ic_flip_24, cropImageOptions.activityMenuIconColor)

            if (cropIcon != null) {
                updateMenuItemIconColor(
                    menu,
                    com.canhub.cropper.R.id.crop_image_menu_crop,
                    cropImageOptions.activityMenuIconColor
                )
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.canhub.cropper.R.id.crop_image_menu_crop -> cropImage()
            com.canhub.cropper.R.id.ic_rotate_left_24 -> rotateImage(-cropImageOptions.rotationDegrees)
            com.canhub.cropper.R.id.ic_rotate_right_24 -> rotateImage(cropImageOptions.rotationDegrees)
            com.canhub.cropper.R.id.ic_flip_24_horizontally -> cropImageView?.flipImageHorizontally()
            com.canhub.cropper.R.id.ic_flip_24_vertically -> cropImageView?.flipImageVertically()
            android.R.id.home -> setResultCancel()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResultCancel()
    }

    protected open fun onPickImageResult(resultUri: Uri?) {
        when (resultUri) {
            null -> setResultCancel()
            else -> {
                cropImageUri = resultUri
                cropImageView?.setImageUriAsync(cropImageUri)
            }
        }
    }

    override fun onSetImageUriComplete(view: CropImageView, uri: Uri, error: Exception?) {
        if (error == null) {
            if (cropImageOptions.initialCropWindowRectangle != null)
                cropImageView?.cropRect = cropImageOptions.initialCropWindowRectangle

            if (cropImageOptions.initialRotation > 0)
                cropImageView?.rotatedDegrees = cropImageOptions.initialRotation

            if (cropImageOptions.skipEditing) {
                cropImage()
            }
        } else setResult(null, error, 1)
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        setResult(result.uriContent, result.error, result.sampleSize)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("uri", result.getUriFilePath(context, true))
        setResult(RESULT_OK, intent)
        finish()
    }

    /**
     * Execute crop image and save the result tou output uri.
     */
    open fun cropImage() {
        if (cropImageOptions.noOutputImage) setResult(null, null, 1)
        else {
            cropImageView?.croppedImageAsync(
                saveCompressFormat = cropImageOptions.outputCompressFormat,
                saveCompressQuality = cropImageOptions.outputCompressQuality,
                reqWidth = cropImageOptions.outputRequestWidth,
                reqHeight = cropImageOptions.outputRequestHeight,
                options = cropImageOptions.outputRequestSizeOptions,
                customOutputUri = cropImageOptions.customOutputUri,
            )

//            onCropImageComplete(binding.cropImageView, )
        }
    }

    /**
     * When extending this activity, please set your own ImageCropView
     */
    open fun setCropImageView(cropImageView: CropImageView) {
        this.cropImageView = cropImageView
    }

    /**
     * Rotate the image in the crop image view.
     */
    open fun rotateImage(degrees: Int) {
        cropImageView?.rotateImage(degrees)
    }

    /**
     * Result with cropped image data or error if failed.
     */
    open fun setResult(uri: Uri?, error: Exception?, sampleSize: Int) {
        setResult(
            error?.let { CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE } ?: RESULT_OK,
            getResultIntent(uri, error, sampleSize)
        )
        finish()
    }

    /**
     * Cancel of cropping activity.
     */
    open fun setResultCancel() {
        setResult(RESULT_CANCELED)
        finish()
    }

    /**
     * Get intent instance to be used for the result of this activity.
     */
    open fun getResultIntent(uri: Uri?, error: Exception?, sampleSize: Int): Intent {
        val result = CropImage.ActivityResult(
            cropImageView?.imageUri,
            uri,
            error,
            cropImageView?.cropPoints,
            cropImageView?.cropRect,
            cropImageView?.rotatedDegrees ?: 0,
            cropImageView?.wholeImageRect,
            sampleSize
        )
        val intent = Intent()
        intent.putExtras(getIntent())
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_RESULT, result)
        return intent
    }

    /**
     * Update the color of a specific menu item to the given color.
     */
    open fun updateMenuItemIconColor(menu: Menu, itemId: Int, color: Int) {
        val menuItem = menu.findItem(itemId)
        if (menuItem != null) {
            val menuItemIcon = menuItem.icon
            if (menuItemIcon != null) {
                try {
                    menuItemIcon.apply {
                        mutate()
                        colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                            color,
                            BlendModeCompat.SRC_ATOP
                        )
                    }
                    menuItem.icon = menuItemIcon
                } catch (e: Exception) {
                    Log.w("AIC", "Failed to update menu item color", e)
                }
            }
        }
    }

    enum class Source { CAMERA, GALLERY }

    private companion object {

        const val BUNDLE_KEY_TMP_URI = "bundle_key_tmp_uri"
    }

    private fun checkPermission(){
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openSource(Source.CAMERA)
            }
            override fun onPermissionDenied(deniedPermissions: List<String>) {
                showToast("카메라 권한을 허용해야 이용이 가능합니다.")
                finish()
            }

        }
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(Manifest.permission.CAMERA)
            .check()
    }
}
