package com.d205.sdutyplus.view.join

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.common.JobHashtag
import com.d205.sdutyplus.databinding.ItemTagBinding

class TagAdapter(): RecyclerView.Adapter<TagAdapter.ViewHolder>() {
    var jobList = mutableListOf<JobHashtag>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(var binding: ItemTagBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(name: String){
            binding.btnTag.text = name
            binding.btnTag.setOnClickListener {
                onClickTagItem.onClick(it, absoluteAdapterPosition, name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemTagBinding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemTagBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(jobList[position].name)
    }

    override fun getItemCount(): Int = jobList.size

    interface OnClickTagListener{
        fun onClick(view: View, position: Int, tagName: String)
    }
    lateinit var onClickTagItem: OnClickTagListener
}