package com.d205.sdutyplus.view.feed

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.mypage.Feed
import com.d205.sdutyplus.databinding.ItemFeedBinding


private const val TAG ="FeedPagingAdapter"
class FeedAdapter(val activity: Activity): PagingDataAdapter<Feed, FeedAdapter.ViewHolder>(
    IMAGE_COMPARATOR
) {
    inner class ViewHolder(val binding: ItemFeedBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(feed: Feed){
            binding.apply {
                Log.d(TAG, "bind: $feed")
                data = feed
                ivStory.setOnClickListener {
                    onClickStoryListener.onClick(feed)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemFeedBinding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemFeedBinding)
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
            ivStory.layoutParams.width = deviceWidth
            ivStory.layoutParams.height = deviceHeight
        }
    }

    lateinit var onClickStoryListener: OnClickStoryListener
    interface OnClickStoryListener{
        fun onClick(feed: Feed)
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed) =
                oldItem.seq == newItem.seq

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed) =
                oldItem == newItem
        }
    }
}