package com.d205.sdutyplus.view.feed

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentFeedDecoBinding
import com.d205.sdutyplus.di.ApplicationClass
import com.d205.sdutyplus.uitls.NOT_PROFILE
import com.d205.sdutyplus.uitls.convertTimeDateToString
import com.d205.sdutyplus.uitls.getTodayDateString
import com.d205.sdutyplus.view.common.CropImageActivity
import com.d205.sdutyplus.view.feed.viewmodel.FeedCreateViewModel
import com.d205.sdutyplus.view.timer.viewmodel.TimerViewModel
import java.time.LocalDate

class FeedDecoFragment : BaseFragment<FragmentFeedDecoBinding>(R.layout.fragment_feed_deco) {
    private val feedCreateViewModel: FeedCreateViewModel by activityViewModels()
    private val timerViewModel: TimerViewModel by activityViewModels()
    private var imageUrl: String = ""

    override fun initOnViewCreated() {
        initView()

        initViewModel()
    }

    private fun initView() {
        launchImageCrop()

        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnDecoNone.setOnClickListener {
                setPolaroidVisiblity(View.GONE)
                setModernFrameVisibility(View.GONE)
            }
            btnDecoPolaroidWhite.setOnClickListener {
                setPolaroidVisiblity(View.VISIBLE)
                setModernFrameVisibility(View.GONE)
            }
            btnDecoFrameModern.setOnClickListener {
                setPolaroidVisiblity(View.GONE)
                setModernFrameVisibility(View.VISIBLE)
            }
            ivCheck.setOnClickListener {
                imageToBitmap()
            }
            setTextTotalStudyTime()
        }
    }

    private fun setTextTotalStudyTime() {
        timerViewModel.getTodayTotalStudyTime()
    }

    private fun initViewModel() {
        timerViewModel.todayTotalStudyTime.observe(viewLifecycleOwner) {
            binding.tvPolaroidTotalStudyTime.text = getTodayDateString() + " " + it
            binding.tvModernTotalStudyTime.text = getTodayDateString() + " " + it
        }
    }

    private fun launchImageCrop() {
        val intent = Intent(requireContext(), CropImageActivity::class.java)
        intent.putExtra("flag", NOT_PROFILE)
        getImageResultLauncher.launch(intent)
    }

    private val getImageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            imageUrl = it.data!!.getStringExtra("uri")!!
            setUriIntoImgPreview()
        }
        else{
            findNavController().popBackStack() // 전에 보니까 이것만 Utils로 따로 빼서 쓰시던데..
        }
    }

    private fun setUriIntoImgPreview() {
        Glide.with(requireContext())
            .load(Uri.parse(imageUrl).path)
            .into(binding.imgPreview)
    }
    
    private fun setPolaroidVisiblity(visiblity: Int) {
        binding.layoutPolaroid.visibility = visiblity
    }

    private fun setModernFrameVisibility(visibility: Int) {
        binding.layoutFrameModern.visibility = visibility
    }

    private fun imageToBitmap() {
        binding.apply {
            val bitmap = Bitmap.createBitmap(layoutPreview.width, layoutPreview.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            layoutPreview.draw(canvas)
            saveImageBitmap(bitmap)
        }
    }

    private fun saveImageBitmap(bitmap: Bitmap) {
        feedCreateViewModel.setBitmapAndImage(bitmap)
        findNavController().popBackStack()
    }
}