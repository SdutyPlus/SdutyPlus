package com.d205.sdutyplus.view.feed

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d205.domain.model.mypage.Feed
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentFeedDetailBinding
import com.d205.sdutyplus.uitls.showToast
import com.d205.sdutyplus.view.feed.dialog.FeedDeleteDialog
import com.d205.sdutyplus.view.feed.dialog.FeedDeleteDialogListener
import kotlinx.coroutines.*

private const val TAG = "FeedDetailFragment"
class FeedDetailFragment : BaseFragment<FragmentFeedDetailBinding>(R.layout.fragment_feed_detail), FeedDeleteDialogListener {
    private val args by navArgs<FeedDetailFragmentArgs>()
    private lateinit var feed: Feed
    private val feedViewModel: FeedViewModel by activityViewModels()

    override fun initOnViewCreated() {
        feed = args.feed
        initView()
    }

    private fun initView() {
        binding.apply {
            feed = this@FeedDetailFragment.feed

            ivTopBack.setOnClickListener {
                findNavController().popBackStack()
            }

            tvDelete.setOnClickListener {
                FeedDeleteDialog(requireContext(), this@FeedDetailFragment).show()
            }

            ivScrap.setOnClickListener {
                if(feed.feedScrapFlag) {
                    removeScrap()
                    ivScrap.setImageResource(R.drawable.ic_baseline_bookmark_border_black_24)
                }
                else {
                    scrapFeed()
                    ivScrap.setImageResource(R.drawable.ic_gradient_book_mark)
                }
            }
        }
    }

    private fun removeScrap() {
        CoroutineScope(Dispatchers.IO).launch {
            feedViewModel.deleteScrapFeed(feed.seq)
        }
    }

    private fun scrapFeed() {
        CoroutineScope(Dispatchers.IO).launch {
            feedViewModel.scrapFeed(feed.seq)
        }
    }

    override fun onOkButtonClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            deleteFeed()
        }
    }

    private suspend fun check() = this@FeedDetailFragment.feedViewModel.isFeedDeletedSucceeded.value

    private suspend fun deleteFeed() {
        this@FeedDetailFragment.feedViewModel.deleteFeed(this@FeedDetailFragment.feed.seq)

        if (this@FeedDetailFragment.feedViewModel.isFeedDeleted()) {
            withContext(Dispatchers.Main) {
                requireContext().showToast("피드를 성공적으로 삭제했습니다.")
                findNavController().popBackStack()
            }

        } else {
            withContext(Dispatchers.Main) {
                requireContext().showToast("피드 삭제에 실패했습니다.")
            }

        }
    }
}