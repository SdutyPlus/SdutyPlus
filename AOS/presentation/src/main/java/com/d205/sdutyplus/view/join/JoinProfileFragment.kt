package com.d205.sdutyplus.view.join


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
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
import kotlinx.coroutines.*

private const val TAG = "JoinProfileFragment"
class JoinProfileFragment : BaseFragment<FragmentJoinProfileBinding>(R.layout.fragment_join_profile) {
    private val args by navArgs<JoinProfileFragmentArgs>()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val joinViewModel: JoinViewModel by viewModels()

    private lateinit var imageUrl: String
    private var jobHashtag: JobHashtag? = null

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val uri = it.data!!.getStringExtra("uri")
            Log.d(TAG, "uri : $uri")

            binding.ivProfile.setImageURI(Uri.parse(uri))
            imageUrl = uri!!
        }
        else{
            Log.d(TAG, "resultLauncher: NO DATA")
        }
    }

    override fun init() {
        initView()
    }

    private fun initView() {
        binding.apply {
            btnJoin.setOnClickListener {
                updateNicknameUsedFlag()

                if(isNicknameUsed()) {
                    requireContext().showToast("이미 사용중인 닉네임입니다.")
                }
                else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val isAddUserSucceeded = joinViewModel.addKakaoUser(
                            UserDto(
                                token = args.token,
                                nickName = binding.etNickname.text.toString(),
                                job = jobHashtag!!.seq))
                        if(isAddUserSucceeded) {
                            moveToMainActivity()
                        }
                        else {
                            requireContext().showToast("회원가입에 실패했습니다.")
                        }
                    }
                }
            }

            ivProfile.setOnClickListener {
                launchImageCrop()
            }

            btnJobSelect.setOnClickListener {
                openTagSelectDialog()
            }
        }
    }

    private fun isNicknameUsed() = profileViewModel.isUsedNickname.value!!

    private fun updateNicknameUsedFlag() {
        profileViewModel.checkNickname(binding.etNickname.text.toString())
    }

    private fun launchImageCrop() {
        val intent = Intent(requireContext(), CropImageActivity::class.java)
        intent.putExtra("flag", PROFILE)
        resultLauncher.launch(intent)
    }

    private fun moveToMainActivity() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
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