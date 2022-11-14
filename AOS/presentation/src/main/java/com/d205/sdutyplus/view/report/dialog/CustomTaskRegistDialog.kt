//package com.d205.sdutyplus.view.report.dialog
//
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.Window
//import android.widget.EditText
//import android.widget.ImageView
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.fragment.app.DialogFragment
//import androidx.fragment.app.activityViewModels
//import com.d205.sdutyplus.databinding.DialogTaskRegistBinding
//import com.d205.sdutyplus.view.report.ReportViewModel
//
//class CustomTaskRegistDialog: DialogFragment() {
//    private lateinit var binding: DialogTaskRegistBinding
//    private val reportViewModel: ReportViewModel by activityViewModels()
//
//    private val contentViews: List<ConstraintLayout> by lazy {
//        listOf(binding.clContent1, binding.clContent2, binding.clContent3)
//    }
//
//    private val contentEditTexts: List<EditText> by lazy {
//        listOf(binding.etContent1, binding.etContent2, binding.etContent3)
//    }
//
//    private val removeContentBtns: List<ImageView> by lazy {
//        listOf(binding.ivRemoveContent1, binding.ivRemoveContent2, binding.ivRemoveContent3)
//    }
//
//    private val contentStrings: MutableList<String> by lazy {
//        mutableListOf()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DialogTaskRegistBinding.inflate(inflater, container, false)
//
//        initDialogWindow()
//
//        return binding.root
//    }
//
//    private fun initDialogWindow() {
//        dialog?.window?.apply{
//            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            requestFeature(Window.FEATURE_NO_TITLE)
//        }
//        // 여백 클릭 종료 불가 설정
//        isCancelable = false
//    }