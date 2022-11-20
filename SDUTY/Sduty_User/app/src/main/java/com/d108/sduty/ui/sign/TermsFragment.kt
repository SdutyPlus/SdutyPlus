package com.d108.sduty.ui.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d108.sduty.common.COMMON_JOIN
import com.d108.sduty.common.KAKAO_JOIN
import com.d108.sduty.common.NAVER_JOIN
import com.d108.sduty.databinding.FragmentTermsBinding
import com.d108.sduty.ui.sign.dialog.TemrsDialog
import com.d108.sduty.utils.showToast

// 회원가입 약관 - 동의 시 가입화면 이동
private const val TAG = "TermsFragment"
class TermsFragment : Fragment() {
    private lateinit var binding: FragmentTermsBinding
    private val args: TermsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTermsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.apply {
            commonTopBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnAccept.setOnClickListener {
                if(radioSelectPrivacy.isChecked && radioSelectTerms.isChecked){
                    when(args.route){
                        COMMON_JOIN -> findNavController().navigate(TermsFragmentDirections.actionTermsFragmentToJoinRegistFragment(args.route))
                        KAKAO_JOIN -> findNavController().navigate(TermsFragmentDirections.actionTermsFragmentToJoinRegistFragment(args.route, args.token))
                        NAVER_JOIN -> findNavController().navigate(TermsFragmentDirections.actionTermsFragmentToJoinRegistFragment(args.route, args.token))
                    }
                }else{
                    requireContext().showToast("모든 항목에 동의해 주세요")
                }
            }
            radioSelectAll.setOnClickListener {
                if(!radioSelectAll.isChecked){
                    radioSelectPrivacy.isChecked = false
                    radioSelectTerms.isChecked = false
                }else {
                    radioSelectPrivacy.isChecked = true
                    radioSelectTerms.isChecked = true
                }
            }
            radioSelectPrivacy.setOnClickListener {
                if(radioSelectPrivacy.isChecked) {
                    radioSelectPrivacy.isChecked = false
                    openDialog("privacy")
                }
            }
            radioSelectTerms.setOnClickListener {
                if(radioSelectTerms.isChecked) {
                    radioSelectTerms.isChecked = false
                    openDialog("terms")
                }
            }
            ivSelectPrivacy.setOnClickListener {
                openDialog("privacy")
            }
            ivSelectTerms.setOnClickListener {
                openDialog("terms")
            }
        }
    }
    private fun openDialog(flag: String){
        TemrsDialog(requireContext()).let {
            it.arguments = bundleOf("flag" to flag)
            it.show(parentFragmentManager.beginTransaction(),"")
            it.onClickConfirm = object :TemrsDialog.OnClickConfirm{
                override fun onClicked(confirm: Boolean, flag: String) {
                    when(flag){
                        "terms" -> binding.radioSelectTerms.isChecked = confirm
                        "privacy" -> binding.radioSelectPrivacy.isChecked = confirm
                    }
                }
            }
        }
    }


}