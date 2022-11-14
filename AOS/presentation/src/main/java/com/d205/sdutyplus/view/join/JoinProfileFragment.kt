package com.d205.sdutyplus.view.join


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d205.domain.model.common.JobHashtag
import com.d205.domain.model.user.UserDto
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentJoinProfileBinding
import com.d205.sdutyplus.uitls.PROFILE
import com.d205.sdutyplus.uitls.showToast
import com.d205.sdutyplus.view.MainActivity
import com.d205.sdutyplus.view.common.CropImageActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

private const val TAG = "JoinProfileFragment"

@AndroidEntryPoint
class JoinProfileFragment : BaseFragment<FragmentJoinProfileBinding>(R.layout.fragment_join_profile) {
    private val args by navArgs<JoinProfileFragmentArgs>()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val joinViewModel: JoinViewModel by viewModels()

    private var profileImageUrl: String? = null
    private var jobHashtag: JobHashtag? = null

    private val getImageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val uri = it.data!!.getStringExtra("uri")
            Log.d(TAG, "uri : $uri")

            binding.ivProfile.setImageURI(Uri.parse(uri))
            profileImageUrl = uri!!
        }
        else{
            Log.d(TAG, "resultLauncher: NO DATA")
        }
    }

    override fun initOnViewCreated() {
        initView()
    }

    private fun initView() {
        binding.apply {
            // 회원 가입 버튼 클릭
            btnJoin.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    // 회원가입에 필요한 정보를 모두 입력했는지 여부 체크
                    if(checkJoinAvailable()) {
                        if(checkNicknameCanUse()) {
                            joinUser()
                            Log.d(TAG, "joinUser finished")

                            // 회원가입 성공 여부 체크    true : 성공, false : 실패
                            if (isUserJoinedSucceeded()) {
                                Log.d(TAG, "joinUser succeed!")
                                moveToMainActivity()
                            } else {
                                Log.d(TAG, "joinUser failed!")
                                showToast("회원가입에 실패했습니다")
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

            ivProfile.setOnClickListener {
                launchImageCrop()
            }

            btnJobSelect.setOnClickListener {
                openTagSelectDialog()
            }

            btnJob.setOnClickListener {
                openTagSelectDialog()
            }
        }
    }

    private suspend fun checkNicknameCanUse(): Boolean {
        Log.d(TAG, "checkNicknameIsUsed: start!")
        return profileViewModel.checkNickname(binding.etNickname.text.toString())
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
        

    private fun getSocialType(): Int = args.socialType

    suspend fun joinUser() {
        joinViewModel.joinUser(
            UserDto(
                imgUrl = profileImageUrl,
                nickname = binding.etNickname.text.toString(),
                userJob = jobHashtag!!.seq)
        )
    }

    private fun showToast(msg: String) {
        requireContext().showToast(msg)
    }

    private fun isUserJoinedSucceeded() = joinViewModel.isJoinSucceeded.value!!

    private fun launchImageCrop() {
        val intent = Intent(requireContext(), CropImageActivity::class.java)
        intent.putExtra("flag", PROFILE)
        getImageResultLauncher.launch(intent)
    }

    private fun moveToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun openTagSelectDialog() {
        TagSelectDialog(requireContext()).let {
            it.arguments = bundleOf("flag" to PROFILE)
            it.onClickConfirm = object : TagSelectDialog.OnClickConfirm {
                override fun onClick(selectedJob: JobHashtag?) {
                    jobHashtag = selectedJob
                    binding.apply {
                        btnJob.text = jobHashtag!!.name
                        btnJob.visibility = View.VISIBLE
                        btnJobSelect.visibility = View.GONE
                    }
                }
            }
            it.show(parentFragmentManager, null)
        }
    }
}