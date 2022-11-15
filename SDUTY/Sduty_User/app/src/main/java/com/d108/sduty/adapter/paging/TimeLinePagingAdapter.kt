package com.d108.sduty.adapter.paging

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.databinding.ItemTimelineBinding
import com.d108.sduty.model.dto.Timeline
import com.d108.sduty.utils.convertDpToPx

private const val TAG ="TimeLinePagingAdapter"
class TimeLinePagingAdapter(val activity: Activity, val mContext: Context): PagingDataAdapter<Timeline, TimeLinePagingAdapter.ViewHolder>(
    IMAGE_COMPARATOR
) {
    inner class ViewHolder(val binding: ItemTimelineBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(timeline: Timeline){
            binding.apply {
                data = timeline
                ivFavorite.setOnClickListener {
                    onClickTimelineItem.onFavoriteClicked(it, timeline, bindingAdapterPosition)
                }
                ivMenu.setOnClickListener {
                    onClickTimelineItem.onMenuClicked(it, timeline)
                }
                ivScrap.setOnClickListener {
                    onClickTimelineItem.onScrapClicked(timeline, bindingAdapterPosition)
                }
                ivProfile.setOnClickListener {
                    onClickTimelineItem.onProfileClicked(timeline)
                }
                constComments.setOnClickListener {
                    onClickTimelineItem.onReplyClicked(timeline)
                }
                tvContent.setOnClickListener{
                    onClickTimelineItem.onReplyClicked(timeline)
                }
                tvNickname.setOnClickListener {
                    onClickTimelineItem.onProfileClicked(timeline)
                }
            }
        }
    }

    fun changeLikes(position: Int){
        if(getItem(position)!!.likes){
            getItem(position)!!.numLikes = getItem(position)!!.numLikes - 1
        }else{
            getItem(position)!!.numLikes = getItem(position)!!.numLikes + 1
        }
        getItem(position)!!.likes = !getItem(position)!!.likes
        notifyItemChanged(position)
    }

    fun changeScrap(position: Int){
        getItem(position)!!.scrap = !getItem(position)!!.scrap
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemTimelineBinding = ItemTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemTimelineBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(getItem(position) !=null) {
            holder.bind(getItem(position)!!)
        }
        val displaymetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val deviceWidth = displaymetrics.widthPixels - convertDpToPx(mContext, 30f)
        val deviceHeight = deviceWidth / 3 * 4
        holder.binding.apply {
            ivTimelineContent.layoutParams.width = deviceWidth
            ivTimelineContent.layoutParams.height = deviceHeight
        }
    }

    interface TimeLineClickListener{
        fun onFavoriteClicked(view: View, timeline: Timeline, position: Int)
        fun onScrapClicked(timeline: Timeline, position: Int)
        fun onReplyClicked(timeline: Timeline)
        fun onMenuClicked(view: View, timeline: Timeline)
        fun onProfileClicked(timeline: Timeline)
    }
    lateinit var onClickTimelineItem: TimeLineClickListener

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<Timeline>() {
            override fun areItemsTheSame(oldItem: Timeline, newItem: Timeline) =
                oldItem.story.seq == newItem.story.seq

            override fun areContentsTheSame(oldItem: Timeline, newItem: Timeline) =
                oldItem == newItem
        }
    }
}