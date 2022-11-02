package com.d205.sdutyplus.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import com.d205.sdutyplus.view.MainViewModel

abstract class BaseFragment<T: ViewDataBinding>(
    @LayoutRes val layoutResId: Int
)  : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!
    protected val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        displayBottomNav(false)
        initOnViewCreated()
    }

    protected fun displayBottomNav(flag : Boolean) {
        mainViewModel.displayBottomNav(flag)
    }

    protected abstract fun initOnViewCreated()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}