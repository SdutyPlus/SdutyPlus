package com.d108.sduty.adapter

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d108.sduty.databinding.ItemAchievementBinding
import com.d108.sduty.model.dto.Achievement

class AchievementAdapter(val activity: Activity): RecyclerView.Adapter<AchievementAdapter.ViewHolder>() {
    var list = mutableListOf<Achievement>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: ItemAchievementBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: Achievement){
            binding.data = data
            binding.constAchievement.setOnClickListener {
                onClickListener.onClick(it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemAchievementBinding =
            ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemAchievementBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        val displaymetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val deviceWidth = displaymetrics.widthPixels / 3
        val deviceHeight = displaymetrics.heightPixels / 3
        holder.binding.apply {
            ivAchieve.layoutParams.width = deviceWidth
        }
    }
    override fun getItemCount(): Int = list.size

    lateinit var onClickListener: OnClickListener
    interface OnClickListener{
        fun onClick(view: View, position: Int)
    }
}