package com.d205.sdutyplus.view.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentTimerBinding
import com.d205.sdutyplus.view.MainViewModel


private const val TAG = "TimerFragment"
class TimerFragment : BaseFragment<FragmentTimerBinding>(R.layout.fragment_timer) {
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun init() {

    }
}