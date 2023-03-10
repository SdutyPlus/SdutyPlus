package com.d205.sdutyplus.view.common

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.d205.sdutyplus.R
import com.d205.sdutyplus.databinding.DialogCropSelectBinding
import com.d205.sdutyplus.utils.FLAG_CAMERA
import com.d205.sdutyplus.utils.FLAG_GALLERY
import com.d205.sdutyplus.utils.FLAG_NO_SELECT

private const val TAG ="CropSelectDialog"
class CropSelectDialog(val activity: Activity) : DialogFragment() {
    private lateinit var binding: DialogCropSelectBinding
    private var flag = FLAG_NO_SELECT
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCropSelectBinding.inflate(inflater, container, false)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDialog()

        binding.apply {
            cardCamera.setOnClickListener {
                flag = FLAG_CAMERA
                onClickListener.onClick(FLAG_CAMERA)
                dismiss()
            }
            cardGallery.setOnClickListener {
                flag = FLAG_GALLERY
                onClickListener.onClick(FLAG_GALLERY)
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(flag == FLAG_NO_SELECT){
            activity.finish()
        }
    }

    lateinit var onClickListener: OnClickListener
    interface OnClickListener{
        fun onClick(flag: Int)
    }

    private fun showDialog() {
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // TRANSPARENT?
        dialog?.window?.attributes?.windowAnimations = R.style.RegisterDialogAnimation
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
        dialog?.show()
    }
}
