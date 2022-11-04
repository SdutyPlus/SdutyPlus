package com.d205.sdutyplus.view.feed

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentFeedDecoBinding
import com.d205.sdutyplus.uitls.PROFILE
import com.d205.sdutyplus.view.common.CropImageActivity

class FeedDecoFragment : BaseFragment<FragmentFeedDecoBinding>(R.layout.fragment_feed_deco) {

    private var imageUrl: String = ""

    override fun initOnViewCreated() {
        initView()
    }

    private fun initView() {
        launchImageCrop()
    }

    private fun launchImageCrop() {
        val intent = Intent(requireContext(), CropImageActivity::class.java)
        intent.putExtra("flag", PROFILE)
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
}