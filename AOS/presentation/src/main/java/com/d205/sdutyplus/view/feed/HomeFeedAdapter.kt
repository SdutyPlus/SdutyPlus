package com.d205.sdutyplus.view.feed

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.feed.Feed
import com.d205.domain.model.feed.HomeFeed
import com.d205.sdutyplus.databinding.ItemHomeFeedBinding


private const val TAG ="HomeFeedAdapter"
class HomeFeedAdapter(val activity: Activity): PagingDataAdapter<Feed, HomeFeedAdapter.ViewHolder>(
    IMAGE_COMPARATOR
) {
    inner class ViewHolder(val binding: ItemHomeFeedBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(homeFeed: Feed){
            binding.apply {
                Log.d(TAG, "bind: $homeFeed")
                feed = homeFeed
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
    }

    lateinit var onClickStoryListener: OnClickStoryListener
    interface OnClickStoryListener{
        fun onClick(homeFeed: Feed)
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