package com.d205.sdutyplus.view.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.d205.domain.model.mypage.Feed
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentFeedDetailBinding
import com.d205.sdutyplus.view.mypage.MyPageFragmentArgs


class FeedDetailFragment : BaseFragment<FragmentFeedDetailBinding>(R.layout.fragment_feed_detail) {
    private val args by navArgs<FeedDetailFragmentArgs>()
    private lateinit var feed: Feed

    override fun initOnViewCreated() {
        feed = args.feed
    }
}