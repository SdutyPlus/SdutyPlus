package com.d205.sdutyplus.view.mypage

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d205.domain.model.user.User
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentMyPageBinding
import com.d205.sdutyplus.view.MainViewModel

private const val TAG = "MyPageFragment"
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val args by navArgs<MyPageFragmentArgs>()
    private lateinit var user: User

    override fun initOnViewCreated() {
//        val tmp = args.qwe
//        Log.d(TAG, "init: $tmp")
        user = mainViewModel.user.value!!
        binding.apply {
            ivProfile.setImageURI(Uri.parse(user.imgUrl))
        }
    }

}