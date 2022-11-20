package com.d108.sduty.ui.sign.dialog

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.d108.sduty.databinding.DialogTermsBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

private const val TAG ="TemrsDialog"
class TemrsDialog(var mContext: Context) : DialogFragment() {
    private lateinit var binding: DialogTermsBinding
    private lateinit var inputStream: InputStream
    lateinit var onClickConfirm: OnClickConfirm
    private lateinit var termsFlag: String

    override fun onResume() {
        super.onResume()
        resizeDialog()
    }

    private fun resizeDialog() {
        val windowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceHeight * 0.95).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTermsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        termsFlag = arguments?.getString("flag").toString()
        var string: String? = ""
        val stringBuilder = StringBuilder()
        when(requireArguments().getString("flag")){
            "terms" -> inputStream = requireContext().assets.open("Terms.txt")
            "privacy" -> inputStream = requireContext().assets.open("PrivacyPolish.txt")
        }
        val reader = BufferedReader(InputStreamReader(inputStream))
        while (true) {
            try {
                if (reader.readLine().also { string = it } == null) break
            } catch (e: IOException) {
                e.printStackTrace()
            }
            stringBuilder.append(string).append("\n")
        }
        inputStream.close()

        binding.apply {
            tvText.text = stringBuilder
            btnConfirm.setOnClickListener {
                onClickConfirm.onClicked(true, termsFlag)
                dismiss()
            }
        }
    }
    interface OnClickConfirm{
        fun onClicked(confirm: Boolean, flag: String)
    }
}