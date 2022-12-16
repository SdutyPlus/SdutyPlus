package com.d205.sdutyplus.view.mypage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.d205.domain.model.common.JobHashtag
import com.d205.domain.model.user.UserDto
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentEditProfileBinding
import com.d205.sdutyplus.utills.PROFILE
import com.d205.sdutyplus.utills.showToast
import com.d205.sdutyplus.view.common.CropImageActivity
import com.d205.sdutyplus.view.common.LoadingDialogFragment
import com.d205.sdutyplus.view.join.JoinViewModel
import com.d205.sdutyplus.view.join.ProfileViewModel
import com.d205.sdutyplus.view.join.TagSelectDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "EditProfileFragment"

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {
    private val profileViewModel: ProfileViewModel by viewModels()
    private val joinViewModel: JoinViewModel by viewModels()
    private val loadingDialogFragment by lazy { LoadingDialogFragment() }

    private var profileImageUrl: String? = null
    private var prevProfileImageUrl: String? = null

    private val getImageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val uri = it.data!!.getStringExtra("uri")
            Log.d(TAG, "uri : $uri")

            binding.ivProfile.setImageURI(Uri.parse(uri))
            profileImageUrl = uri
            Log.d(TAG, "image crop profileImageUrl: $profileImageUrl")
        }
        else{
            Log.d(TAG, "resultLauncher: NO DATA")
        }
    }

    override fun initOnViewCreated() {
        profileImageUrl = this@EditProfileFragment.mainViewModel.user.value!!.imgUrl
        prevProfileImageUrl = profileImageUrl
        Log.d(TAG, "initOnViewCreated profileImageUrl : $profileImageUrl")
        initView()
        joinViewModel.loadingFlag.observe(viewLifecycleOwner) {
            when(it) {
                true -> showLoader()
                false -> hideLoader()
            }
        }
    }
    private fun hideLoader() {
        if(loadingDialogFragment.isAdded) {
            loadingDialogFragment.dismissAllowingStateLoss()
        }
    }

    private fun showLoader() {
        if(!loadingDialogFragment.isAdded) {
            loadingDialogFragment.show(parentFragmentManager, "loader")
        }
    }

    private fun initView() {
        binding.apply {
            mainViewModel = this@EditProfileFragment.mainViewModel
            profileViewModel = this@EditProfileFragment.profileViewModel


            btnJob.apply {
                visibility = View.VISIBLE
                text = this@EditProfileFragment.mainViewModel.user.value!!.userJob
            }

            // 프로필 수정 버튼 클릭
            btnUpdate.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    // 프로필 수정에 필요한 정보를 모두 입력했는지 여부 체크
                    if(checkJoinAvailable()) {
                        if(checkNicknameCanUse()) {

                            updateUser()
                            Log.d(TAG, "updateUser finished")

                            // 프로필 수정 성공 여부 체크    true : 성공, false : 실패
                            if (isUserUpdatedSucceeded()) {
                                Log.d(TAG, "updateUser succeed!")
                                this@EditProfileFragment.mainViewModel.getUser()
                                findNavController().popBackStack()
                            } else {
                                Log.d(TAG, "updateUser failed!")
                                showToast("프로필 수정에 실패했습니다")
                            }
                        }
                        else {
                            showToast("이미 사용중인 닉네임입니다!")
                        }
                    }
                    else {
                        showToast("닉네임과 직업을 모두 기입해주세요!")
                    }
                }
            }

//            Glide.with(requireContext())
//                .load(this@EditProfileFragment.mainViewModel.user.value!!.imgUrl)
//                .error(R.drawable.empty_profile_image)
//                .into(ivProfile)
            if(this@EditProfileFragment.mainViewModel.user.value!!.imgUrl != null) {
                Log.d(TAG, "initView imgUrl: ${this@EditProfileFragment.mainViewModel.user.value!!.imgUrl}")
                //ivProfile.setImageURI(Uri.parse(this@MyPageFragment.mainViewModel.user.value!!.imgUrl))
                Glide.with(requireContext())
                    .load(this@EditProfileFragment.mainViewModel.user.value!!.imgUrl)
                    .error(R.drawable.empty_profile_image)
                    .into(ivProfile)
            }

            ivProfile.setOnClickListener {
                launchImageCrop()
            }

            btnJob.setOnClickListener {
                openTagSelectDialog()
            }

            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private suspend fun checkNicknameCanUse(): Boolean {
        Log.d(TAG, "checkNicknameIsUsed: start!")
        return this@EditProfileFragment.profileViewModel.checkNickname(
            this@EditProfileFragment.mainViewModel.user.value!!.nickname!!,
            binding.etNickname.text.toString())
    }

    private fun checkJoinAvailable() = !isNicknameEmpty() && isJobSelected()

    private fun isNicknameEmpty(): Boolean {
        if(binding.etNickname.text.toString().isEmpty()) {
            showToast("닉네임을 입력해주세요!")
            return true
        }
        return false
    }

    private fun isJobSelected(): Boolean {
        if(binding.btnJob.visibility == View.GONE) {
            showToast("직업을 선택해주세요!")
            return false
        }
        return true
    }

    private fun showToast(msg: String) {
        requireContext().showToast(msg)
    }

    private fun isUserUpdatedSucceeded() = joinViewModel.isUpdateSucceeded.value!!

    private suspend fun updateUser() {
        Log.d(TAG, "updateUser profileImageUrl : $profileImageUrl")
        joinViewModel.updateUser(
            UserDto(
                imgUrl = profileImageUrl,
                nickname = binding.etNickname.text.toString(),
                userJob = binding.btnJob.text.toString()), prevProfileImageUrl
        )
    }

    private fun launchImageCrop() {
        val intent = Intent(requireContext(), CropImageActivity::class.java)
        intent.putExtra("flag", PROFILE)
        getImageResultLauncher.launch(intent)
    }

    private fun openTagSelectDialog() {
        TagSelectDialog(requireContext(), binding.btnJob.text.toString()).let {
            it.arguments = bundleOf("flag" to PROFILE)
            it.onClickConfirm = object : TagSelectDialog.OnClickConfirm {
                override fun onClick(selectedJob: JobHashtag?) {
                    binding.apply {
                        btnJob.text = selectedJob!!.name
                        btnJob.visibility = View.VISIBLE
                    }
                }
            }
            it.show(parentFragmentManager, null)
        }
    }
}