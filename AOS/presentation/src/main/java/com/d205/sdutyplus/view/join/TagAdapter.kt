package com.d205.sdutyplus.view.join

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.common.JobHashtag
import com.d205.sdutyplus.R
import com.d205.sdutyplus.databinding.ItemTagBinding

class TagAdapter(): RecyclerView.Adapter<TagAdapter.ViewHolder>() {
    private var curPosition = -1
    var jobList = mutableListOf<JobHashtag>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(var binding: ItemTagBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(jobHashtag: JobHashtag){
            binding.btnTag.text = jobHashtag.name
            if(jobHashtag.selected) {
                binding.btnTag.setBackgroundResource(R.drawable.bg_tag_selected)
                binding.btnTag.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else {
                binding.btnTag.setBackgroundResource(R.drawable.bg_tag_unselected)
                binding.btnTag.setTextColor(Color.parseColor("#0a0a0a"))
            }

            binding.btnTag.setOnClickListener {
                if(curPosition != -1) {
                    jobList[curPosition].selected = false
                    notifyItemChanged(curPosition)
                }
                jobList[absoluteAdapterPosition].selected = true
                curPosition = absoluteAdapterPosition
                notifyItemChanged(absoluteAdapterPosition)
                onClickTagItem.onClick(jobHashtag)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemTagBinding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemTagBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(jobList[position])
    }

    override fun getItemCount(): Int = jobList.size

    fun setCurPosition(position: Int) {
        curPosition = position
    }

    interface OnClickTagListener{
        fun onClick(jobHashtag: JobHashtag)
    }
    lateinit var onClickTagItem: OnClickTagListener
}