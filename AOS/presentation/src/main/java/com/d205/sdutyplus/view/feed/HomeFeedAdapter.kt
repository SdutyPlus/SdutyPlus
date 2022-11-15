package com.d205.sdutyplus.view.feed

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.feed.HomeFeed
import com.d205.domain.model.mypage.Feed
import com.d205.sdutyplus.databinding.ItemFeedBinding
import com.d205.sdutyplus.databinding.ItemHomeFeedBinding


private const val TAG ="HomeFeedAdapter"
class HomeFeedAdapter(val activity: Activity): PagingDataAdapter<HomeFeed, HomeFeedAdapter.ViewHolder>(
    IMAGE_COMPARATOR
) {
    inner class ViewHolder(val binding: ItemHomeFeedBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(homeFeed: HomeFeed){
            binding.apply {
                Log.d(TAG, "bind: $homeFeed")
                data = homeFeed
                ivFeed.setOnClickListener {
                    onClickStoryListener.onClick(homeFeed)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemHomeFeedBinding = ItemHomeFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemHomeFeedBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(getItem(position) !=null) {
            holder.bind(getItem(position)!!)
        }
        val displaymetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val deviceWidth = displaymetrics.widthPixels / 3
        val deviceHeight = deviceWidth * 4 / 3
        holder.binding.apply {
            ivFeed.layoutParams.width = deviceWidth
            ivFeed.layoutParams.height = deviceHeight
        }
    }

    lateinit var onClickStoryListener: OnClickStoryListener
    interface OnClickStoryListener{
        fun onClick(homeFeed: HomeFeed)
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<HomeFeed>() {
            override fun areItemsTheSame(oldItem: HomeFeed, newItem: HomeFeed) =
                oldItem.seq == newItem.seq

            override fun areContentsTheSame(oldItem: HomeFeed, newItem: HomeFeed) =
                oldItem == newItem
        }
    }
}